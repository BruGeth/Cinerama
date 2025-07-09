package com.cinerama.backend.service.impl;

import com.cinerama.backend.service.PayPalCaptureService;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayPalCaptureServiceImpl implements PayPalCaptureService {

    @Autowired
    private APIContext apiContext;

    /**
     * Captures a PayPal payment using the provided payment ID and payer ID.
     *
     * @param paymentId The ID of the payment to capture.
     * @param payerId   The ID of the payer confirming the payment.
     * @return The captured Payment object from PayPal.
     * @throws PayPalRESTException if execution fails.
     */
    @Override
    public Payment ejecutarPago(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);

        return payment.execute(apiContext, execution);
    }
}
