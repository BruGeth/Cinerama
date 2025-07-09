import React, { useState } from "react";
import { useSearchParams } from "react-router-dom";
import "../styles/PaymentConfirmation.css";
import { usePayment } from "../context/PaymentContext";

const PaymentConfirmation = () => {
  // Get URL query parameters
  const [searchParams] = useSearchParams();
  const [isProcessing, setIsProcessing] = useState(false);

  // Access payment context
  const {
    createOrder,
    paymentStatus,
    message,
    setMessage
  } = usePayment();

  // Extract purchase details from query params
  const seats = searchParams.get("seats")?.split(",") || [];
  const movie = searchParams.get("movie");
  const showtime = searchParams.get("showtime");
  const format = searchParams.get("format");
  const ticketsGeneral = parseInt(searchParams.get("ticketsGeneral") || "0");
  const ticketsChild = parseInt(searchParams.get("ticketsChild") || "0");
  const totalAmount = ticketsGeneral * 15 + ticketsChild * 10;
  const tipoCambio = 0.2818;
  const amountUSD = Math.round(totalAmount * tipoCambio * 100) / 100;

  // Handle PayPal payment button click
  const handlePayWithPayPal = async () => {
    setIsProcessing(true);
    try {
      // Create PayPal order and redirect to approval URL
      const { approvalUrl } = await createOrder(amountUSD);
      console.log("Redirigiendo a PayPal:", approvalUrl);
      window.location.href = approvalUrl;
    } catch (error) {
      // Handle payment initiation error
      console.error("Error iniciando pago:", error);
      setMessage("Ocurrió un error al iniciar el pago con PayPal.");
      setIsProcessing(false);
    }
  };

  return (
    <div className="payment-container">
      <h1 className="payment-title">CONFIRMACIÓN Y PAGO</h1>
      <p className="payment-instruction">Revisa los detalles antes de proceder</p>

      {/* Purchase summary */}
      <div className="payment-summary">
        <h2>Detalle de la compra</h2>
        <p>Película: <strong>{movie}</strong></p>
        <p>Fecha y hora: <strong>{showtime}</strong></p>
        <p>Asientos seleccionados: <strong>{seats.join(", ")}</strong></p>
        <p>Formato: <strong>{format}</strong></p>
        <p>Entradas: <strong>{ticketsGeneral} General, {ticketsChild} Niño</strong></p>
        <h3 className="total-price">
          Total: S/{totalAmount} ≈ ${amountUSD} USD
        </h3>
      </div>

      {/* Payment method section */}
      <div className="payment-method">
        <h2>Selecciona el método de pago</h2>
        <button
          className="paypal-button"
          onClick={handlePayWithPayPal}
          disabled={isProcessing}
        >
          {isProcessing ? "Redirigiendo a PayPal..." : "Pagar con PayPal"}
        </button>
      </div>

      {/* Display payment messages */}
      {message && <p className="payment-message">{message}</p>}
      {paymentStatus === "completed" && (
        <p className="success-message">¡Pago realizado con éxito!</p>
      )}
    </div>
  );
};

export default PaymentConfirmation;
