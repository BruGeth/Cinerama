import React, { useState } from "react";
import { useSearchParams } from "react-router-dom";
import { PayPalScriptProvider, PayPalButtons } from "@paypal/react-paypal-js";
import "../styles/PaymentConfirmation.css";
import { usePayment } from "../context/PaymentContext"; 

const PaymentConfirmation = () => {
  const [searchParams] = useSearchParams();
  const [showPayPal, setShowPayPal] = useState(false);

  // Access payment-related actions and state from context
  const {
    createOrder,
    paymentStatus,
    message,
    setMessage
  } = usePayment();

  // Extract booking details from the URL query parameters
  const seats = searchParams.get("seats")?.split(",") || [];
  const movie = searchParams.get("movie");
  const showtime = searchParams.get("showtime");
  const format = searchParams.get("format");
  const ticketsGeneral = parseInt(searchParams.get("ticketsGeneral") || "0");
  const ticketsChild = parseInt(searchParams.get("ticketsChild") || "0");
  const totalAmount = ticketsGeneral * 15 + ticketsChild * 10;// Calculate total based on ticket types

  // Check if PayPal Client ID is defined in environment variables
  if (!process.env.REACT_APP_PAYPAL_CLIENT_ID) {
    return <p>Error: PayPal Client ID no está definido</p>;
  }
 
  // Create a PayPal order using the backend API
  const handleCreateOrder = async () => {
  try {
    const id = await createOrder(totalAmount);
    console.log("ID de orden enviado a PayPal:", id);
    return id; 
  } catch (error) {
    console.error("Error creando orden:", error);
    throw error;
  }
};

// Handle PayPal approval and trigger backend capture
  const handleOnApprove = async (data) => {
  try {
    console.log("Orden aprobada por PayPal:", data.orderID);

    // Capture the order through the backend API
    const token = localStorage.getItem("token"); // use JWT if required
    const response = await fetch(`/api/orders/${data.orderID}/capture`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`, // include authorization if required
      },
    });

    const result = await response.json();
    console.log("Respuesta de captura:", result);

    if (response.ok) {
      if (setMessage) setMessage("¡Pago realizado con éxito!");
      // Optionally redirect or reset booking flow here
    } else {
      if (setMessage) setMessage(`Error al capturar la orden: ${result.error}`);
    }

  } catch (error) {
    console.error("Error al capturar la orden:", error);
    if (setMessage) setMessage(`Hubo un problema al finalizar el pago: ${error.message}`);
  }
};
  return (
    <div className="payment-container">
      <h1 className="payment-title">CONFIRMACIÓN Y PAGO</h1>
      <p className="payment-instruction">Revisa los detalles antes de proceder</p>

      <div className="payment-summary">
        <h2>Detalle de la compra</h2>
        <p>Película: <strong>{movie}</strong></p>
        <p>Fecha y hora: <strong>{showtime}</strong></p>
        <p>Asientos seleccionados: <strong>{seats.join(", ")}</strong></p>
        <p>Formato: <strong>{format}</strong></p>
        <p>Entradas: <strong>{ticketsGeneral} General, {ticketsChild} Niño</strong></p>
        <h3 className="total-price">Total: S/{totalAmount}</h3>
      </div>

      <div className="payment-method">
        <h2>Selecciona el método de pago</h2>

        {!showPayPal && (
          <button className="paypal-button" onClick={() => setShowPayPal(true)}>
            Pagar con PayPal
          </button>
        )}

        {showPayPal && (
          <PayPalScriptProvider
            options={{
              "client-id": process.env.REACT_APP_PAYPAL_CLIENT_ID,
              currency: "USD",
            }}
          >
            <PayPalButtons
              style={{ shape: "rect", layout: "vertical", color: "gold", label: "paypal" }}
              createOrder={() => handleCreateOrder()}
              onApprove={(data) => handleOnApprove(data)}
              onError={(err) => {
                console.error("Error con PayPal:", err);
                if (setMessage) setMessage(`Error en el proceso de pago: ${err.message || err}`);
              }}
            />
          </PayPalScriptProvider>
        )}
      </div>

      {message && <p className="payment-message">{message}</p>}
      {paymentStatus === "completed" && <p className="success-message">¡Pago realizado con éxito!</p>}
    </div>
  );
};

export default PaymentConfirmation;
