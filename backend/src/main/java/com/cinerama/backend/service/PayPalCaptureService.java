package com.cinerama.backend.service;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayPalCaptureService {

    /**
     * Service to capture a PayPal payment.
     * Uses the payment ID and the payer ID to complete the transaction.
     *
     * @param paymentId The ID of the payment to capture.
     * @param payerId The ID of the payer who made the payment.
     * @return The Payment object resulting from the capture.
     * @throws PayPalRESTException If an error occurs when executing the payment.
     */
    @Autowired
    private APIContext apiContext;

    public Payment ejecutarPago(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);

        return payment.execute(apiContext, execution);
    }
}
