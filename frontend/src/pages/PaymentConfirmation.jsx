import React from "react";
import { useSearchParams } from "react-router-dom";

import "../styles/PaymentConfirmation.css";

const PaymentConfirmation = () => {
  const [searchParams] = useSearchParams();
  const seats = searchParams.get("seats")?.split(",") || []; 
  const movie = searchParams.get("movie");
  const showtime = searchParams.get("showtime");
  const format = searchParams.get("format");
  const ticketsGeneral = searchParams.get("ticketsGeneral");
  const ticketsChild = searchParams.get("ticketsChild");

  return (
    <div className="payment-container">
      <h1 className="payment-title">CONFIRMACIÓN Y PAGO</h1>
      <p className="payment-instruction">Revisa los detalles antes de proceder</p>

      {/* Payment Summary */}
      <div className="payment-summary">
        <h2>Detalle de la compra</h2>
        <p>Película: <strong>{movie}</strong></p>
        <p>Fecha y hora: <strong>{showtime}</strong></p>
        <p>Asientos seleccionados: <strong>{seats.join(", ")}</strong></p>
        <p>Formato: <strong>{format}</strong></p>
        <p>Entradas: <strong>{ticketsGeneral} General, {ticketsChild} Niño</strong></p>
        <h3 className="total-price">Total: S/{(ticketsGeneral * 15) + (ticketsChild * 10)}</h3>
      </div>

      {/* Payment Method Selection */}
      <div className="payment-method">
        <h2>Selecciona el método de pago</h2>
        <button className="paypal-button">Pagar con PayPal</button>
      </div>
    </div>
  );
};

export default PaymentConfirmation;
