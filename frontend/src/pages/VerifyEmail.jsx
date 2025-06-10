import { useState } from "react"; 
import { useLocation, useNavigate } from "react-router-dom"; // Hooks for navigation and retrieving email state
import "../styles/VerifyEmail.css"; // Import styles
import userService from "../services/userService"; // Import service for API requests

const VerifyEmail = () => {
  const [code, setCode] = useState(Array(6).fill("")); // State to store the 6-digit verification code
  const [activeIndex, setActiveIndex] = useState(0); // Tracks the current input focus
  const navigate = useNavigate(); // Hook for navigation
  const location = useLocation(); // Hook for retrieving the passed email from previous route

  // Handles input change and moves focus automatically to the next box
  const handleChange = (index, value) => {
    if (value.match(/^[0-9]$/) || value === "") { // Ensures only numeric input
      const newCode = [...code];
      newCode[index] = value;
      setCode(newCode);

      if (value !== "" && index < 5) { // Moves focus to the next input
        setActiveIndex(index + 1);
        document.getElementById(`code-input-${index + 1}`).focus();
      }
    }
  };

  // Handles form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    const finalCode = code.join(""); // Converts array into string

    if (finalCode.length !== 6) { // Validate code length
      alert("Por favor, ingresa un código de 6 números.");
      return;
    }

    try {
      const email = location.state?.email; // Retrieve email from navigation state
      console.log("Email recibido en verify:", email);
      await userService.verifyUser({ email, verificationCode: finalCode }); // Send verification request

      console.log("Verificación exitosa.");
      navigate("/login"); // Redirect user on success
    } catch (error) {
      console.error("Error al verificar:", error.message);
      alert("Código inválido o expirado. Inténtalo de nuevo."); // Display error message
    }
  };

  return (
    <div className="verify-email-page">
      <div className="verify-card">
        <h1>Confirma tu identidad</h1> {/* Title */}
        <p>Acaba de llegar un código a su correo para que verifique su identidad</p> {/* Instructions */}

        <form onSubmit={handleSubmit}>
          <div className="code-inputs"> {/* Numeric inputs for verification code */}
            {code.map((num, index) => (
              <input
                id={`code-input-${index}`}
                key={index}
                type="text"
                maxLength="1"
                value={num}
                onChange={(e) => handleChange(index, e.target.value)}
                className={activeIndex === index ? "active" : ""}
                onFocus={() => setActiveIndex(index)}
                required
              />
            ))}
          </div>

          <div className="verify-buttons"> {/* Submit and resend buttons */}
            <button type="submit" className="send-button">Enviar</button>
            <button 
              type="button" 
              className="resend-button" 
              onClick={() => setCode(Array(6).fill(""))} // Clear input fields when resending
            >
              Reenviar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default VerifyEmail; // Export component for use in other parts of the application
