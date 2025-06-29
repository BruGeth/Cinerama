import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../styles/ForgotPassword.css"; // Import styles
import passwordResetService from "../services/passwordResetService"; // Import service for API requests

const ForgotPassword = () => {
  // States for the different steps
  const [currentStep, setCurrentStep] = useState(1); // 1: email, 2: code, 3: password
  const [email, setEmail] = useState("");
  const [code, setCode] = useState(Array(6).fill(""));
  const [activeIndex, setActiveIndex] = useState(0);
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [resendCount, setResendCount] = useState(0);
  const [resendMessage, setResendMessage] = useState("");
  const [loading, setLoading] = useState(false);
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
      alert("Please enter your email.");
      return;
    }

    setLoading(true);
    try {
      await passwordResetService.sendResetEmail(email);
      setCurrentStep(2);
      setResendMessage("Code sent successfully to your email.");
    } catch (error) {
      alert(error.message);
    } finally {
      setLoading(false);
    }
  };

  // Step 2: Verify code
  const handleCodeSubmit = async (e) => {
    e.preventDefault();
    const finalCode = code.join("");
    if (finalCode.length !== 6) {
      alert("Please enter a 6-digit code.");
      return;
    }

    setLoading(true);
    try {
      await passwordResetService.validateResetToken(email, finalCode);
      setCurrentStep(3);
    } catch (error) {
      alert(error.message);
    } finally {
      setLoading(false);
    }
  };

  // Step 3: Change password
  const handlePasswordSubmit = async (e) => {
    e.preventDefault();
    if (!newPassword || !confirmPassword) {
      alert("Please complete all fields.");
      return;
    }
    if (newPassword.length < 8) {
      alert("Password must be at least 8 characters.");
      return;
    }
    if (newPassword !== confirmPassword) {
      alert("Passwords do not match.");
      return;
    }

    setLoading(true);
    try {
      await passwordResetService.changePassword(
        email,
        newPassword,
        confirmPassword
      );
      alert("Password changed successfully!");
      navigate("/login");
    } catch (error) {
      alert(error.message);
    } finally {
      setLoading(false);
    }
  };

  // Resend code (use the same as sendResetEmail)
  const handleResendCode = async () => {
    setCode(Array(6).fill(""));
    setActiveIndex(0);

    if (resendCount >= 3) {
      setResendMessage("Resend limit reached.");
      return;
    }

    setLoading(true);
    try {
      await passwordResetService.sendResetEmail(email);
      const newCount = resendCount + 1;
      setResendCount(newCount);

      if (newCount >= 3) {
        setResendMessage("Resend limit reached.");
      } else {
        setResendMessage(
          `Code resent successfully. Attempts left: ${3 - newCount}`
        );
      }
    } catch (error) {
      setResendMessage("Error resending code. Try again later.");
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
                  placeholder="tu@email.com"
                  className="email-input"
                  required
                />
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

              <div className="forgot-password-buttons-group">
                {resendMessage && (
                  <div className="resend-message">{resendMessage}</div>
                )}
                <div className="forgot-password-buttons">
                  <button
                    type="submit"
                    className="send-button"
                    disabled={loading}
                  >
                    {loading ? "Verificando..." : "Verificar"}
                  </button>
                  <button
                    type="button"
                    className="resend-button"
                    onClick={handleResendCode}
                    disabled={resendCount >= 3 || loading}
                  >
                    Reenviar
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
                    type={showPassword ? "text" : "password"}
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    placeholder="Nueva contraseña"
                    className="password-input"
                    required
                  />
                  <button
                    type="button"
                    className="password-toggle"
                    onClick={() => setShowPassword(!showPassword)}
                  >
                    {showPassword ? "🚫" : "👁️"}
                  </button>
                </div>

                <div className="password-input-container">
                  <input
                    type={showConfirmPassword ? "text" : "password"}
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    placeholder="Confirmar contraseña"
                    className="password-input"
                    required
                  />
                  <button
                    type="button"
                    className="password-toggle"
                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  >
                    {showConfirmPassword ? "🚫" : "👁️"}
                  </button>
                </div>
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
