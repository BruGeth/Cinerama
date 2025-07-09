package com.cinerama.backend.service;

import com.paypal.base.rest.PayPalRESTException;

import java.util.Map;

public interface PayPalOrderService {

    /**
     * Creates a PayPal order with the given parameters.
     *
     * @param total     The total payment amount.
     * @param moneda  The currency code (e.g., "USD").
     * @param returnUrl URL to redirect the user after payment approval.
     * @param cancelUrl URL to redirect the user if they cancel the payment.
     * @return A map containing the PayPal payment ID and the approval URL.
     * @throws PayPalRESTException If an error occurs while creating the PayPal order.
     */
    Map<String, String> crearOrden(Double total, String moneda, String returnUrl, String cancelUrl) throws PayPalRESTException;
}
