import React, { useEffect, useState, useRef } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import "../styles/Succes.css";

const Success = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const hasRun = useRef(false);

  const [message, setMessage] = useState("Procesando el pago...");
  const [status, setStatus] = useState("loading");
  const [pdfBlob, setPdfBlob] = useState(null); 

  useEffect(() => {
    const capturarPago = async () => {
      const paymentId = searchParams.get("paymentId");
      const payerId = searchParams.get("PayerID");

      if (!paymentId || !payerId) {
        setMessage("Faltan datos en la confirmación del pago.");
        setStatus("error");
        console.error("Missing paymentId or payerId in query params");
        return;
      }

      if (hasRun.current) return;
      hasRun.current = true;

      try {
        const token = localStorage.getItem("token");

        const response = await fetch(
          `/api/payment/capture?paymentId=${paymentId}&payerId=${payerId}`,
          {
            method: "POST",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (response.ok) {
          const blob = await response.blob();
          setPdfBlob(blob); // ✅ Guardamos el recibo para que el usuario lo descargue cuando quiera

          setMessage("¡Pago realizado con éxito! Tu orden ha sido registrada.");
          setStatus("success");
        } else {
          const errorData = await response.json();
          console.error("Payment capture failed:", errorData);
          setMessage(`Error al capturar el pago: ${errorData.message || "Desconocido"}`);
          setStatus("error");
        }
      } catch (error) {
        console.error("Unexpected error during payment capture:", error);
        setMessage("Ocurrió un error al procesar el pago.");
        setStatus("error");
      }
    };

    capturarPago();
  }, [searchParams]);

  const descargarRecibo = () => {
    if (!pdfBlob) return;
    const url = window.URL.createObjectURL(pdfBlob);
    const link = document.createElement("a");
    link.href = url;
    link.download = "resumen_compra.pdf";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  };

  return (
    <div className="payment-success-container">
      <h1>{status === "success" ? "¡Gracias por tu compra!" : "Estado del pago"}</h1>
      <p>{message}</p>

      {status === "success" && (
        <div className="success-actions">
          {pdfBlob && (
            <button className="success-button" onClick={descargarRecibo}>
              Descargar recibo
            </button>
          )}
          <button className="success-button" onClick={() => navigate("/")}>
            Volver al inicio
          </button>
        </div>
      )}
    </div>
  );
};

export default Success;
