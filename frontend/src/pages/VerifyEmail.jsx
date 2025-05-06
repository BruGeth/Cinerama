import { useState } from "react"
import { useLocation, useNavigate } from "react-router-dom"
import "../styles/VerifyEmail.css"
import userService from "../services/userService"

const VerifyEmail = () => {
  const [code, setCode] = useState(Array(6).fill(""))
  const [activeIndex, setActiveIndex] = useState(0)
  const navigate = useNavigate()
  const location = useLocation()

  const handleChange = (index, value) => {
    if (value.match(/^[0-9]$/) || value === "") {
      const newCode = [...code]
      newCode[index] = value
      setCode(newCode)

      if (value !== "" && index < 5) {
        setActiveIndex(index + 1)
        document.getElementById(`code-input-${index + 1}`).focus()
      }
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    const finalCode = code.join("");
  
    if (finalCode.length !== 6) {
      alert("Por favor, ingresa un código de 6 números.");
      return;
    }
  
    try {
      const email = location.state?.email; 
      console.log("Email recibido en verify:", email);
      await userService.verifyUser({ email, verificationCode: finalCode });
  
      console.log("Verificación exitosa.");
      navigate("/login"); // pagina inicio con el inicio de sesión
    } catch (error) {
      console.error("Error al verificar:", error.message);
      alert("Código inválido o expirado. Inténtalo de nuevo.");
    }
  }
  

  return (
    <div className="verify-email-page">
      <div className="verify-card">
        <h1>Confirma tu identidad</h1>
        <p>Acaba de llegar un código a su correo para que verifique su identidad</p>

        <form onSubmit={handleSubmit}>
          <div className="code-inputs">
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

          <div className="verify-buttons">
            <button type="submit" className="send-button">
              Enviar
            </button>
            <button type="button" className="resend-button" onClick={() => setCode(Array(6).fill(""))}>
              Reenviar
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default VerifyEmail
