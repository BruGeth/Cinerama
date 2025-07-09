package com.cinerama.backend.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class PayPalOrderService {

    @Autowired
    private APIContext apiContext;
    /**
     * Create a payment order in PayPal.
     *
     * @param total Total amount of the order.
     * @param moneda Currency in which the payment is made (e.g. “USD”).
     * @param returnUrl URL to which the user is redirected after the payment is complete.
     * URL to which the user will be redirected if he/she cancels the payment.
     * @return A map with the payment ID and approval URL.
     * @throws PayPalRESTException If an error occurs when creating the order in PayPal.
     */
    public Map<String, String> crearOrden(Double total, String moneda, String returnUrl, String cancelUrl) throws PayPalRESTException {

        Amount amount = new Amount();
        amount.setCurrency(moneda);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Compra en Cinerama ");

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(returnUrl);
        redirectUrls.setCancelUrl(cancelUrl);

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setTransactions(List.of(transaction));

        Payment createdPayment = payment.create(apiContext);
        String paymentId = createdPayment.getId();

        String approvalUrl = createdPayment.getLinks().stream()
                .filter(link -> "approval_url".equals(link.getRel()))
                .map(Links::getHref)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró el enlace de aprobación de PayPal"));

        Map<String, String> resultado = new HashMap<>();
        resultado.put("payment_id", paymentId);
        resultado.put("approval_url", approvalUrl);
        return resultado;
    }
}
