import React, { createContext, useContext, useState } from "react";

// Create the context to store and share payment-related state
export const PaymentContext = createContext();

// Custom hook to access the context safely
export const usePayment = () => {
    const context = useContext(PaymentContext);
    if (!context) throw new Error("usePayment debe usarse dentro de un PaymentProvider");
    return context;
};

// Provider component that supplies payment state and functions to its children
export const PaymentProvider = ({ children }) => {
    const [orderId, setOrderId] = useState(null); // Stores the generated PayPal order ID
    const [paymentStatus, setPaymentStatus] = useState("idle"); // Tracks the payment status
    const [message, setMessage] = useState(""); // Optional message for user feedback

    // Function to create a PayPal order through the backend
    const createOrder = async (amount) => {
        try {
            const token = localStorage.getItem("token"); // Retrieve JWT token from local storage

            const response = await fetch("http://localhost:8080/api/orders", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({
                    amount: amount.toFixed(2) // Send the amount with 2 decimals
                }),
            });

            if (!response.ok) {
                const errorText = await response.text();
                console.error("Error creando la orden:", response.status, errorText);
                throw new Error(`Error ${response.status}: ${errorText}`);
            }

            const data = await response.json();
            if (!data || !data.id) throw new Error("Order no valida ID no recibida.");

            setOrderId(data.id); // Save the PayPal order ID
            setPaymentStatus("procesando"); // Update status to indicate processing has started
            return data.id; // Return the order ID to be used by the PayPal SDK
        } catch (error) {
            console.error("Error creando la orden:", error);
            setPaymentStatus("fallo"); // Update status to failed
            setMessage("Error mientras se creaba la orden."); // Optional message for UI
            throw error;
        }
    };

    // Function to capture (complete) the PayPal order via the backend
    const captureOrder = async (paypalOrderId) => {
        try {
            const token = localStorage.getItem("token");

            const response = await fetch(
                `http://localhost:8080/api/orders/${paypalOrderId}/capture`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            const data = await response.json();
            setPaymentStatus("completed"); // Update status to indicate success
            return data; // Return result for UI or further logic
        } catch (error) {
            console.error("Error capturing order:", error);
            setPaymentStatus("failed"); // Mark as failed
            setMessage("Error capturing the payment."); // Set error message
            throw error;
        }
    };

    // Make payment data and functions available to all children
    return (
        <PaymentContext.Provider
            value={{ orderId, paymentStatus, message, createOrder, captureOrder }}
        >
            {children}
        </PaymentContext.Provider>
    );
};
