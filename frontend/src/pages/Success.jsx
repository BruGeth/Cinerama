import React, { useEffect, useState, useRef } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import "../styles/Succes.css";

const Success = () => {
  // Get query parameters from the URL
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  // Ref to prevent multiple executions of the payment capture logic
  const hasRun = useRef(false);
  // State for payment message and status
  const [message, setMessage] = useState("Procesando el pago...");
  const [status, setStatus] = useState("loading");

  useEffect(() => {
    // Function to capture the payment after PayPal redirect
    const capturarPago = async () => {
      // Get paymentId and payerId from URL query params
      const paymentId = searchParams.get("paymentId");
      const payerId = searchParams.get("PayerID");

      // If required params are missing, show error
      if (!paymentId || !payerId) {
        setMessage("Faltan datos en la confirmación del pago.");
        setStatus("error");
        return;
      }
      if (hasRun.current) return;
      hasRun.current = true;
      try {
        // Get user token from localStorage (if needed for authentication)
        const token = localStorage.getItem("token");

        // Call backend endpoint to capture the payment
        const response = await fetch(
          `/api/payment/capture?paymentId=${paymentId}&payerId=${payerId}`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        // Parse backend response
        const result = await response.json();
        console.log(" Respuesta de backend:", result);

        // If payment was successful, update message and status
        if (response.ok) {
          setMessage("¡Pago realizado con éxito! Tu orden ha sido registrada.");
          setStatus("success");
        } else {
          // If backend returned an error, show error message
          setMessage(`Error al capturar el pago: ${result.error || "Desconocido"}`);
          setStatus("error");
        }
      } catch (error) {
        // Handle network or unexpected errors
        console.error(" Error al finalizar el pago:", error);
        setMessage("Ocurrió un error al procesar el pago.");
        setStatus("error");
      }
    };

    // Run the payment capture function on component mount
    capturarPago();
  }, [searchParams]);

  return (
    <div className="payment-success-container">
      {/* Show title based on payment status */}
      <h1>{status === "success" ? "¡Gracias por tu compra!" : "Estado del pago"}</h1>
      {/* Show payment message */}
      <p>{message}</p>

      {/* Show button to return home if payment was successful */}
      {status === "success" && (
        <button className="success-button" onClick={() => navigate("/")}>
          Volver al inicio
        </button>
      )}
    </div>
  );
};

export default Success;