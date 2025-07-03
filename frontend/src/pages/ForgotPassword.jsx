import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../styles/ForgotPassword.css"; // Import styles
import passwordResetService from "../services/passwordResetService"; // Import service for API requests
import errorMessages from "../utils/errorMessages";

const ForgotPassword = () => {
  // States for the different steps
  const [currentStep, setCurrentStep] = useState(1); // 1: email, 2: code, 3: password
  const [email, setEmail] = useState("");
  const [code, setCode] = useState(Array(6).fill(""));
  const [activeIndex, setActiveIndex] = useState(0);
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [resendCount, setResendCount] = useState(0);
  const [resendMessage, setResendMessage] = useState("");
  const [emailError, setEmailError] = useState("");
  const [loading, setLoading] = useState(false);
  const [tokenError, setTokenError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const navigate = useNavigate();

  // Handle code input change and auto-advance
  const handleChange = (index, value) => {
    if (value.match(/^[0-9]$/) || value === "") {
      const newCode = [...code];
      newCode[index] = value;
      setCode(newCode);
      if (value !== "" && index < 5) {
        setActiveIndex(index + 1);
        document.getElementById(`code-input-${index + 1}`)?.focus();
      }
    }
  };

  // Handle keyboard navigation and backspace for code inputs
  const handleKeyDown = (index, e) => {
    if (e.key === "Backspace") {
      if (code[index] === "" && index > 0) {
        setActiveIndex(index - 1);
        document.getElementById(`code-input-${index - 1}`)?.focus();
        const newCode = [...code];
        newCode[index - 1] = "";
        setCode(newCode);
        e.preventDefault();
      }
    } else if (e.key === "ArrowLeft") {
      if (index > 0) {
        setActiveIndex(index - 1);
        document.getElementById(`code-input-${index - 1}`)?.focus();
      }
      e.preventDefault();
    } else if (e.key === "ArrowRight" && index < 5) {
      setActiveIndex(index + 1);
      document.getElementById(`code-input-${index + 1}`)?.focus();
      e.preventDefault();
    }
  };

  // Handle paste event for code inputs
  const handlePaste = (e) => {
    const paste = e.clipboardData.getData("text").replace(/\D/g, "");
    if (paste.length === 6) {
      const pasteArr = paste.split("");
      setCode(pasteArr);
      setActiveIndex(5);
      setTimeout(() => {
        document.getElementById("code-input-5")?.focus();
      }, 0);
      e.preventDefault();
    }
  };

  // Step 1: Send email to receive code
  const handleEmailSubmit = async (e) => {
    e.preventDefault();
    if (!email) {
      setEmailError("Por favor ingresa tu correo electrónico.");
      return;
    }
    setEmailError(""); // Clear previous error

    setLoading(true);
    try {
      await passwordResetService.sendResetEmail(email);
      setCurrentStep(2);
      setResendMessage(
        "Codigo enviado correctamente. Puedes reenviarlo hasta 3 veces."
      );
    } catch (error) {
      let code = error.code || error.message;
      try {
        // if error is an Axios error, extract code from response
        if (error.response && error.response.data) {
          code = error.response.data.code || error.response.data.error || code;
        } else if (typeof error === "string") {
          const parsed = JSON.parse(error);
          code = parsed.code || parsed.error || code;
        } else if (error.message) {
          const parsed = JSON.parse(error.message);
          code = parsed.code || parsed.error || code;
        }
      } catch {}
      setEmailError(errorMessages[code] || errorMessages.UNKNOWN_ERROR);
    } finally {
      setLoading(false);
    }
  };

  // Step 2: Verify code
  const handleCodeSubmit = async (e) => {
    e.preventDefault();
    const finalCode = code.join("");
    if (finalCode.length !== 6) {
      setTokenError("Por favor ingresa el código de 6 dígitos.");
      return;
    }

    setTokenError("");
    setLoading(true);
    try {
      await passwordResetService.validateResetToken(email, finalCode);
      setCurrentStep(3);
    } catch (error) {
      let code = error.code || error.message;
      try {
        if (error.response && error.response.data) {
          code = error.response.data.code || error.response.data.error || code;
        } else if (typeof error === "string") {
          const parsed = JSON.parse(error);
          code = parsed.code || parsed.error || code;
        } else if (error.message) {
          const parsed = JSON.parse(error.message);
          code = parsed.code || parsed.error || code;
        }
      } catch {}
      setTokenError(errorMessages[code] || errorMessages.UNKNOWN_ERROR);
    } finally {
      setLoading(false);
    }
  };

  // Step 3: Change password
  const handlePasswordSubmit = async (e) => {
    e.preventDefault();
    if (!newPassword || !confirmPassword) {
      setPasswordError(errorMessages.EMPTY_FIELDS || "Por favor completa todos los campos.");
      return;
    }
    if (newPassword.length < 8) {
      setPasswordError(errorMessages.PASSWORD_TOO_SHORT || "La contraseña debe tener al menos 8 caracteres.");
      return;
    }
    if (newPassword !== confirmPassword) {
      setPasswordError(errorMessages.PASSWORDS_DO_NOT_MATCH || "Las contraseñas no coinciden.");
      return;
    }

    setPasswordError("");
    setLoading(true);
    try {
      await passwordResetService.changePassword(
        email,
        newPassword,
        confirmPassword
      );
      // Puedes personalizar el mensaje de éxito si quieres
      navigate("/login");
    } catch (error) {
      let code = error.code || error.message;
      try {
        if (error.response && error.response.data) {
          code = error.response.data.code || error.response.data.error || code;
        } else if (typeof error === "string") {
          const parsed = JSON.parse(error);
          code = parsed.code || parsed.error || code;
        } else if (error.message) {
          const parsed = JSON.parse(error.message);
          code = parsed.code || parsed.error || code;
        }
      } catch {}
      setPasswordError(errorMessages[code] || errorMessages.UNKNOWN_ERROR);
    } finally {
      setLoading(false);
    }
  };

  // Resend code (use the same as sendResetEmail)
  const handleResendCode = async () => {
    setCode(Array(6).fill(""));
    setActiveIndex(0);
    setTokenError(""); // Limpiar error de token al reenviar

    if (resendCount >= 3) {
      setResendMessage("Limite de reenvíos alcanzado.");
      return;
    }

    setLoading("resend"); // Usar string para distinguir loading de reenviar
    try {
      await passwordResetService.sendResetEmail(email);
      const newCount = resendCount + 1;
      setResendCount(newCount);

      if (newCount >= 3) {
        setResendMessage("Limite de reenvíos alcanzado.");
      } else {
        setResendMessage(
          `Codigo reenviado. Intentos restantes: ${3 - newCount}`
        );
      }
    } catch (error) {
      setResendMessage("Error al enviar el codigo. Intenta mas tarde.");
    } finally {
      setLoading(false);
    }
  };

  // Go back to previous step
  const goBack = () => {
    if (currentStep === 2) {
      setCurrentStep(1);
      setCode(Array(6).fill(""));
      setActiveIndex(0);
      setResendCount(0);
      setResendMessage("");
    } else if (currentStep === 3) {
      setCurrentStep(2);
      setNewPassword("");
      setConfirmPassword("");
    }
  };

  return (
    <div className="forgot-password-page">
      <div className="forgot-password-card">
        {/* Back button */}
        {currentStep > 1 && (
          <button className="back-button" onClick={goBack} type="button">
            {/* Unicode left arrow */}←
          </button>
        )}

        {/* Step 1: Request email */}
        {currentStep === 1 && (
          <>
            <h1>Recuperar Contraseña</h1>
            <p>
              Ingresa tu correo electrónico y te enviaremos un código para
              restablecer tu contraseña.
            </p>

            <form onSubmit={handleEmailSubmit}>
              <div className="email-input-container">
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="example@email.com"
                  className="email-input"
                  required
                />
                {emailError && (
                  <div className="email-error-message">{emailError}</div>
                )}
              </div>

              <div className="forgot-password-buttons-group">
                <div className="forgot-password-buttons">
                  <button
                    type="submit"
                    className="send-button"
                    disabled={loading}
                  >
                    {loading ? "Enviando..." : "Enviar Código"}
                  </button>
                  <Link to="/login" className="back-to-login-button">
                    Volver al Login
                  </Link>
                </div>
              </div>
            </form>
          </>
        )}

        {/* Step 2: Verify code */}
        {currentStep === 2 && (
          <>
            <h1>Verificar Código</h1>
            <p>
              Hemos enviado un código de 6 dígitos a <strong>{email}</strong>
            </p>

            <form onSubmit={handleCodeSubmit}>
              <div className="code-inputs">
                {code.map((num, index) => (
                  <input
                    id={`code-input-${index}`}
                    key={index}
                    type="text"
                    maxLength={1}
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
              {tokenError && (
                <div className="token-error-message">{tokenError}</div>
              )}
              <div className="forgot-password-buttons-group">
                {resendMessage && (
                  <div className="resend-message">{resendMessage}</div>
                )}
                <div className="forgot-password-buttons">
                  <button
                    type="submit"
                    className="send-button"
                    disabled={loading === true || loading === "resend"}
                  >
                    {loading === true ? "Verificando..." : "Verificar"}
                  </button>
                  <button
                    type="button"
                    className="resend-button"
                    onClick={handleResendCode}
                    disabled={resendCount >= 3 || loading === true || loading === "resend"}
                  >
                    {loading === "resend" ? "Reenviando..." : "Reenviar"}
                  </button>
                </div>
              </div>
            </form>
          </>
        )}

        {/* Step 3: New password */}
        {currentStep === 3 && (
          <>
            <h1>Nueva Contraseña</h1>
            <p>
              Ingresa tu nueva contraseña. Debe tener al menos 8 caracteres.
            </p>

            <form onSubmit={handlePasswordSubmit}>

              <div className="password-inputs">
                <div className="password-input-container">
                  <input
                    type="password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    placeholder="Nueva contraseña"
                    className="password-input"
                    required
                  />
                </div>

                <div className="password-input-container">
                  <input
                    type="password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    placeholder="Confirmar contraseña"
                    className="password-input"
                    required
                  />
                </div>
                {passwordError && (
                  <div className="token-error-message">{passwordError}</div>
                )}
              </div>

              <div className="forgot-password-buttons-group">
                <div className="forgot-password-buttons">
                  <button
                    type="submit"
                    className="send-button"
                    disabled={loading}
                  >
                    {loading ? "Cambiando..." : "Cambiar Contraseña"}
                  </button>
                </div>
              </div>
            </form>
          </>
        )}
      </div>
    </div>
  );
};

export default ForgotPassword;
