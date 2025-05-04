import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/VerifyEmail.css";

const VerifyEmail = () => {
  const [code, setCode] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Verifying code:", code);

    navigate("/");
  };

  return (
    <div className="verify-container">
      <h2>Verifica tu correo electrónico</h2>
      <p>Hemos enviado un código de verificación a tu correo.</p>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Ingresa tu código de verificación"
          value={code}
          onChange={(e) => setCode(e.target.value)}
          required
        />
        <button type="submit">Verificar</button>
      </form>
    </div>
  );
};

export default VerifyEmail;
