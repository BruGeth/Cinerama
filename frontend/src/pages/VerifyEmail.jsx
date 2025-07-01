import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom"; // Hooks for navigation and retrieving email state
import "../styles/VerifyEmail.css"; // Import styles
import userService from "../services/userService"; // Import service for API requests

const VerifyEmail = () => {
  const [code, setCode] = useState(Array(6).fill("")); // State to store the 6-digit verification code
  const [activeIndex, setActiveIndex] = useState(0); // Tracks the current input focus
  const [resendCount, setResendCount] = useState(0); // Counter for resend attempts
  const [resendMessage, setResendMessage] = useState(""); // Message to display after resending code
  const [codeError, setCodeError] = useState(""); // Error message for code
  const [loading, setLoading] = useState(false); // false | 'verify' | 'resend'
  const navigate = useNavigate(); // Hook for navigation
  const location = useLocation(); // Hook for retrieving the passed email from previous route

  // Handles input change and moves focus automatically to the next box
  const handleChange = (index, value) => {
    if (value.match(/^[0-9]$/) || value === "") {
      const newCode = [...code];
      newCode[index] = value;
      setCode(newCode);
      setCodeError("");
      if (value !== "" && index < 5) {
        setActiveIndex(index + 1);
        document.getElementById(`code-input-${index + 1}`).focus();
      }
    }
  };

  // Handles backspace key to move focus back to the previous box if current box is empty
  const handleKeyDown = (index, e) => {
    if (e.key === "Backspace") {
      if (code[index] === "" && index > 0) {
        setActiveIndex(index - 1);
        document.getElementById(`code-input-${index - 1}`).focus();
        const newCode = [...code];
        newCode[index - 1] = "";
        setCode(newCode);
        e.preventDefault();
      }
    } else if (e.key === "ArrowLeft") {
      if (index > 0) {
        setActiveIndex(index - 1);
        document.getElementById(`code-input-${index - 1}`).focus();
      }
      e.preventDefault();
    } else if (e.key === "ArrowRight" && index < 5) {
      setActiveIndex(index + 1);
      document.getElementById(`code-input-${index + 1}`).focus();
      e.preventDefault();
    }
  };

  const handlePaste = (e) => {
    const paste = e.clipboardData.getData("text").replace(/\D/g, "");
    if (paste.length === 6) {
      const pasteArr = paste.split("");
      setCode(pasteArr);
      setActiveIndex(5);
      setCodeError("");
      setTimeout(() => {
        document.getElementById("code-input-5").focus();
      }, 0);
      e.preventDefault();
    }
  };

  // Handles form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    const finalCode = code.join(""); // Converts array into string
    // Validate code length
    if (finalCode.length !== 6) {
      setCodeError("Por favor, ingresa un código de 6 números.");
      return;
    }

    setCodeError("");
    setLoading("verify");
    try {
      const email = location.state?.email; // Retrieve email from navigation state
      await userService.verifyUser({ email, verificationCode: finalCode }); // Send verification request
      navigate("/login"); // Redirect user on success
    } catch (error) {
      setCodeError("Código inválido o expirado. Inténtalo de nuevo.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="verify-email-page">
      <div className="verify-card">
        <h1>Confirma tu identidad</h1>
        <p>
          Acaba de llegar un código a su correo para que verifique su identidad
        </p>
        <form onSubmit={handleSubmit}>
          <div className="code-inputs">
            {code.map((num, index) => (
              <input
                id={`code-input-${index}`}
                key={index}
                type="text"
                maxLength="1"
                inputMode="numeric"
                pattern="[0-9]*"
                aria-label={`Verification code digit ${index + 1}`}
                value={num}
                onChange={(e) => handleChange(index, e.target.value)}
                onKeyDown={(e) => handleKeyDown(index, e)}
                onPaste={handlePaste}
                className={activeIndex === index ? "active" : ""}
                onFocus={() => setActiveIndex(index)}
                required
              />
            ))}
          </div>
          {codeError && (
            <div className="token-error-message">{codeError}</div>
          )}
          <div className="verify-buttons-group">
            {resendMessage && (
              <div className="resend-message">{resendMessage}</div>
            )}
            <div className="verify-buttons">
              <button
                type="submit"
                className="send-button"
                disabled={loading === 'verify' || loading === 'resend'}
              >
                {loading === 'verify' ? 'Verificando...' : 'Verificar'}
              </button>
              <button
                type="button"
                className="resend-button"
                onClick={async () => {
                  setCode(Array(6).fill(""));
                  setActiveIndex(0);
                  setCodeError("");
                  if (resendCount >= 3) {
                    setResendMessage("Has alcanzado el límite de reenvíos.");
                    return;
                  }
                  setLoading("resend");
                  const email = location.state?.email;
                  try {
                    await userService.sendVerificationCode(email);
                    const newCount = resendCount + 1;
                    setResendCount(newCount);
                    if (newCount >= 3) {
                      setResendMessage("Límite de reenvíos alcanzado.");
                    } else {
                      setResendMessage(
                        `Código reenviado correctamente. Intentos restantes: ${3 - newCount}`
                      );
                    }
                  } catch (error) {
                    setResendMessage(
                      "Error al reenviar el código. Intenta más tarde."
                    );
                  } finally {
                    setLoading(false);
                  }
                }}
                disabled={resendCount >= 3 || loading === 'verify' || loading === 'resend'}
              >
                {loading === 'resend' ? 'Reenviando...' : 'Reenviar'}
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default VerifyEmail; // Export component for use in other parts of the application
