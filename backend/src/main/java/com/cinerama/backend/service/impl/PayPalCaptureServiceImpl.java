package com.cinerama.backend.service.impl;

import com.cinerama.backend.service.PayPalCaptureService;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service responsible for executing PayPal payments.
 * This service uses the payment ID and payer ID to confirm and complete the transaction via PayPal API.
 */
@Service
public class PayPalCaptureServiceImpl implements PayPalCaptureService {
    @Autowired
    private APIContext apiContext;

    /**
     * Executes the PayPal payment using the provided payment ID and payer ID.
     *
     * @param paymentId The unique identifier of the PayPal payment to execute.
     * @param payerId   The PayPal-assigned ID of the user authorizing the payment.
     * @return The resulting Payment object returned by the PayPal API.
     * @throws PayPalRESTException If an error occurs during execution.
     * This method is timed to monitor the duration of payment capture operations.
     */
    @Timed(value = "payment.ticket.capture.duration", description = "Duración al capturar pago de boletos")
    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);

        return payment.execute(apiContext, execution);
    }
}
