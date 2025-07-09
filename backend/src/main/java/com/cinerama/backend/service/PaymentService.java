package com.cinerama.backend.service;

import com.cinerama.backend.entity.Order;
import com.cinerama.backend.repository.OrderRepository;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.paypal.api.payments.Error;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Service responsible for handling the payment process using PayPal.
 * It delegates order creation and capture logic to the appropriate PayPal services.
 */
@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PayPalOrderService orderService;
    private final PayPalCaptureService captureService;
    private final OrderRepository orderRepository; // Added for updating and saving orders after capture

    private Double convertirSolesADolares(Double montoEnSoles) {
        double tipoCambio = 0.2818;
        return Math.round(montoEnSoles * tipoCambio * 100.0) / 100.0;
    }

    /**
     * Constructs the PaymentService with dependencies for order and capture operations.
     */
    public PaymentService(PayPalOrderService orderService, PayPalCaptureService captureService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.captureService = captureService;
        this.orderRepository = orderRepository;
    }

    /**
     * Creates a PayPal order based on the provided amount and currency.
     *
     * @param amount   the total payment amount
     * @param currency the currency code (e.g., "USD")
     * @return HTTP response with PayPal order ID or error message
     */
    public ResponseEntity<?> createPayment(Double amount, String currency, String returnUrl, String cancelUrl) {
        try {
            logger.info("Creando orden de pago: {} {}", amount, currency);

            Double montoUSD;
            if ("USD".equalsIgnoreCase(currency)) {
                montoUSD = amount;
                logger.info("Monto recibido en USD: {}", montoUSD);
            } else {
                montoUSD = convertirSolesADolares(amount);
                logger.info("Monto convertido a USD: {} (original: {} {})", montoUSD, amount, currency);
            }


            Map<String, String> resultado = orderService.crearOrden(montoUSD, "USD", returnUrl, cancelUrl);

            if (resultado == null || !resultado.containsKey("approval_url") || !resultado.containsKey("payment_id")) {
                logger.error("No se recibió approval_url o payment_id desde PayPalOrderService");
                return ResponseEntity.status(500).body(Map.of("error", "No se pudo crear el enlace de aprobación de PayPal."));
            }

            logger.info("✅ Orden generada. paymentId={}, approvalUrl={}", resultado.get("payment_id"), resultado.get("approval_url"));

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            logger.error("❌ Error creando el pago con PayPal: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                    "code", "PAYPAL_ERROR",
                    "message", e.getMessage()
            ));
        }
    }


    /**
     * Captures a PayPal payment using the provided payment ID and payer ID.
     * Updates the order status and saves it to the database.
     *
     * @param paymentId the PayPal payment ID to capture
     * @param payerId   the PayPal payer ID to execute the payment
     * @return HTTP response with capture status and order details or error message
     */
    public ResponseEntity<?> capturePayment(String paymentId, String payerId) {
        try {
            logger.info("Capturando pago con paymentId={}, payerId={}", paymentId, payerId);

            Payment payment = captureService.ejecutarPago(paymentId, payerId);

            Order order = Order.builder()
                    .paypalOrderId(payment.getId())
                    .amount(new BigDecimal(payment.getTransactions().get(0).getAmount().getTotal()))
                    .currency(payment.getTransactions().get(0).getAmount().getCurrency())
                    .status(payment.getState())
                    .payerEmail(payment.getPayer().getPayerInfo().getEmail())
                    .timestamp(LocalDateTime.now())
                    .build();

            orderRepository.save(order);
            logger.info("✅ Order guardado con éxito: {}", order.getId());

            Map<String, String> response = new HashMap<>();
            response.put("status", payment.getState());
            response.put("paypalOrderId", payment.getId());
            response.put("payer", payment.getPayer().getPayerInfo().getEmail());

            return ResponseEntity.ok(response);

        } catch (PayPalRESTException ex) {
            Error paypalError = ex.getDetails();
            String code = paypalError != null ? paypalError.getName() : "UNKNOWN_ERROR";
            String message = paypalError != null ? paypalError.getMessage() : ex.getMessage();
            String info = paypalError != null ? paypalError.getInformationLink() : "https://developer.paypal.com/";

            logger.error("PayPal error - code: {}, message: {}", code, message, ex);

            return ResponseEntity.status(400).body(Map.of(
                    "code", code,
                    "message", message,
                    "info", info
            ));

        } catch (Exception e) {
            logger.error("Error inesperado al capturar el pago:", e);
            return ResponseEntity.status(500).body(Map.of(
                    "code", "CAPTURE_ERROR",
                    "message", e.getMessage()
            ));
        }
    }
}
