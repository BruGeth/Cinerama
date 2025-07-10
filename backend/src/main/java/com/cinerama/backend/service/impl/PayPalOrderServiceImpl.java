package com.cinerama.backend.service.impl;

import com.cinerama.backend.service.PayPalOrderService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Service responsible for creating PayPal orders through the SDK.
 */
@Service
public class PayPalOrderServiceImpl implements PayPalOrderService {

    @Autowired
    private APIContext apiContext;

    /**
     * Creates a payment order in PayPal.
     *
     * @param total      Total amount for the order.
     * @param currency   Currency code in which the payment is made (e.g. "USD").
     * @param returnUrl  URL to which the user is redirected after completing the payment.
     * @param cancelUrl  URL to which the user is redirected if they cancel the payment.
     * @return A map containing the PayPal payment ID and approval URL.
     * @throws PayPalRESTException If an error occurs during the PayPal order creation process.
     */
    @Timed(value = "payment.ticket.order.creation.duration", description = "Duración al crear orden de boletos")
    @Override
    public Map<String, String> createOrder(Double total, String currency, String returnUrl, String cancelUrl) throws PayPalRESTException {

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Purchase from Cinerama");

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
                .orElseThrow(() -> new RuntimeException("PayPal approval URL not found"));

        Map<String, String> result = new HashMap<>();
        result.put("payment_id", paymentId);
        result.put("approval_url", approvalUrl);
        return result;
    }
}
