import React, { createContext, useContext, useState } from "react";

// Create the context to store and share payment-related state
export const PaymentContext = createContext();

// Custom hook to access the context safely
export const usePayment = () => {
  const context = useContext(PaymentContext);
  if (!context) throw new Error("usePayment must be used within a PaymentProvider");
  return context;
};

// Provider component that supplies payment state and functions to its children
export const PaymentProvider = ({ children }) => {
  // State to store order ID, payment status, and messages
  const [orderId, setOrderId] = useState(null);
  const [paymentStatus, setPaymentStatus] = useState("idle");
  const [message, setMessage] = useState("");

  // Function to create a PayPal order via backend
  const createOrder = async (amountUSD) => {
    try {
      // Get user token from localStorage (if needed for authentication)
      const token = localStorage.getItem("token");

      // Prepare parameters for the backend request
      const params = new URLSearchParams({
        amount: amountUSD.toFixed(2),
        currency: "USD",
        returnUrl: "http://localhost:3000/success",
        cancelUrl: "http://localhost:3000/cancel"
      });

      // Call backend endpoint to create the payment order
      const response = await fetch(`http://localhost:8080/api/payment/create?${params.toString()}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
      });

      // Parse backend response
      const data = await response.json();
      console.log("Backend response:", data);

      // Handle errors from backend
      if (!response.ok) {
        const errorText = typeof data === "string" ? data : JSON.stringify(data);
        throw new Error(`Error ${response.status}: ${errorText}`);
      }

      // Extract approval URL from backend response
      const rawApproval = data.approval_url;
      const approvalUrl = typeof rawApproval === "string" ? rawApproval.trim() : String(rawApproval);
      console.log("Raw approval URL:", approvalUrl);

      // Validate approval URL
      if (!approvalUrl.startsWith("http")) {
        console.error("Invalid approval URL:", approvalUrl);
        throw new Error("A valid PayPal approval URL was not received.");
      }

      // Extract paymentId from approval URL
      const paymentId = new URL(approvalUrl).searchParams.get("token");
      if (!paymentId) throw new Error("Unable to extract paymentId from approval URL");

      // Update state with order ID and status
      setOrderId(paymentId);
      setPaymentStatus("procesando");

      // Return approval URL and paymentId for further use
      return { approvalUrl, paymentId };
    } catch (error) {
      // Handle errors during order creation
      console.error("Error creating order:", error);
      setPaymentStatus("fallo");
      setMessage("Error creando la orden.");
      throw error;
    }
  };

  // Function to capture the PayPal order after user approval
  const captureOrder = async (paymentId, payerId) => {
    try {
      // Get user token from localStorage (if needed for authentication)
      const token = localStorage.getItem("token");

      // Call backend endpoint to capture the payment
      const response = await fetch(
        `http://localhost:8080/api/payment/capture?paymentId=${paymentId}&payerId=${payerId}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // Parse backend response
      const data = await response.json();
      setPaymentStatus("completed");
      return data;
    } catch (error) {
      // Handle errors during payment capture
      console.error("Error capturing order:", error);
      setPaymentStatus("failed");
      setMessage("Error capturando el pago.");
      throw error;
    }
  };

  // Provide payment state and functions to children components
  return (
    <PaymentContext.Provider
      value={{ orderId, paymentStatus, message, createOrder, captureOrder, setMessage }}
    >
      {children}
    </PaymentContext.Provider>
  );
};
