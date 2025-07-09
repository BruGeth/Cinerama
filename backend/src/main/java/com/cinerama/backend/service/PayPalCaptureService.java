package com.cinerama.backend.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PayPalCaptureService {

    /**
     * Executes a PayPal payment using the payment ID and payer ID.
     *
     * @param paymentId The ID of the payment to be captured.
     * @param payerId   The ID of the payer confirming the payment.
     * @return The resulting PayPal Payment object.
     * @throws PayPalRESTException If an error occurs during execution.
     */
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
