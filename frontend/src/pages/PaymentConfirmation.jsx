import React, { useState } from "react";
import { useSearchParams } from "react-router-dom";
import { PayPalScriptProvider, PayPalButtons } from "@paypal/react-paypal-js";
import "../styles/PaymentConfirmation.css";

const PaymentConfirmation = () => {
  // Obtener parámetros de la URL
  const [searchParams] = useSearchParams();
  const [message, setMessage] = useState("");          // Mensajes de error/éxito
  const [showPayPal, setShowPayPal] = useState(false);   // Controla la visibilidad del flujo de PayPal

  // Extraer datos de la compra desde la URL
  const seats = searchParams.get("seats")?.split(",") || [];
  const movie = searchParams.get("movie");
  const showtime = searchParams.get("showtime");
  const format = searchParams.get("format");
  const ticketsGeneral = parseInt(searchParams.get("ticketsGeneral") || "0");
  const ticketsChild = parseInt(searchParams.get("ticketsChild") || "0");
  const totalAmount = ticketsGeneral * 15 + ticketsChild * 10;

  console.log("Paypal Client ID:", process.env.REACT_APP_PAYPAL_CLIENT_ID);

  // Función para crear la orden en el backend utilizando el endpoint OrderController
  const createOrder = async () => {
    // Validar que se tengan los datos mínimos necesarios
    if (!movie || !showtime || seats.length === 0) {
      alert("Faltan datos para completar la compra.");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/api/orders", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          movie: movie,
          showtime: showtime,
          format: format,
          ticketsGeneral: ticketsGeneral,
          ticketsChild: ticketsChild,
          seats: seats,
          totalAmount: totalAmount,
          currency: "USD",
        }),
      });

      const orderData = await response.json();
      console.log("Order Data:", orderData);

      // Verificar que se haya recibido un id de orden válido
      if (!orderData || !orderData.id) {
        throw new Error("No se recibió un id de orden válido.");
      }
      // Retornar el id para que el SDK de PayPal lo use
      return orderData.id;
    } catch (error) {
      console.error("Error en createOrder:", error);
      setMessage(`No se pudo iniciar el proceso de PayPal: ${error.message}`);
      throw error;
    }
  };

  // Función que se invoca al aprobar el pago, para llamar al endpoint de captura en el backend
  const onApprove = async (data) => {
    try {
      // Se utiliza el orderID retornado por PayPal, el cual debe coincidir con el id creado en nuestro backend
      await fetch(`http://localhost:8080/api/orders/${data.orderID}/capture`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
      });
      setMessage("Pago realizado con éxito");
    } catch (error) {
      console.error("Error en onApprove:", error);
      setMessage("Error al aprobar el pago.");
    }
  };

  return (
    <div className="payment-container">
      <h1 className="payment-title">CONFIRMACIÓN Y PAGO</h1>
      <p className="payment-instruction">Revisa los detalles antes de proceder</p>

      {/* Resumen de la compra */}
      <div className="payment-summary">
        <h2>Detalle de la compra</h2>
        <p>
          Película: <strong>{movie}</strong>
        </p>
        <p>
          Fecha y hora: <strong>{showtime}</strong>
        </p>
        <p>
          Asientos seleccionados: <strong>{seats.join(", ")}</strong>
        </p>
        <p>
          Formato: <strong>{format}</strong>
        </p>
        <p>
          Entradas: <strong>{ticketsGeneral} General, {ticketsChild} Niño</strong>
        </p>
        <h3 className="total-price">Total: S/{totalAmount}</h3>
      </div>

      {/* Sección para la integración con PayPal */}
      <div className="payment-method">
        <h2>Selecciona el método de pago</h2>
        {/* Botón para mostrar el flujo de PayPal */}
        {!showPayPal && (
          <button className="paypal-button" onClick={() => setShowPayPal(true)}>
            Pagar con PayPal
          </button>
        )}

        {/* Una vez activado, se muestra el flujo real de PayPal */}
        {showPayPal && (
          <PayPalScriptProvider
            options={{
              "client-id": process.env.REACT_APP_PAYPAL_CLIENT_ID,
              currency: "USD",
            }}
          >
            <PayPalButtons
              style={{
                shape: "rect",
                layout: "vertical",
                color: "gold",
                label: "paypal",
              }}
              // Se usa la función createOrder para llamar a nuestro endpoint y obtener el id de orden
              createOrder={(data, actions) => createOrder()}
              // Una vez aprobado el pago, se invoca onApprove para capturar el pago
              onApprove={(data, actions) => onApprove(data)}
              onError={(err) => {
                console.error("Error con PayPal:", err);
                setMessage(`Error en el proceso de pago: ${err.message || err}`);
              }}
            />
          </PayPalScriptProvider>
        )}
      </div>

      {/* Mostrar mensajes de error o confirmación */}
      {message && <p className="payment-message">{message}</p>}
    </div>
  );
};

export default PaymentConfirmation;
