import { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../styles/Login.css";
import { AuthContext } from "../context/AuthContext";

const Login = () => {
  // Accessing the login function from the authentication context
  const { login, isAdmin } = useContext(AuthContext); // 👈 Using the login function from context
  // State to hold the email and password input values
  const [credentials, setCredentials] = useState({
    email: "",
    password: "",
  });
  // State to manage the "Remember me" checkbox
  const [rememberMe, setRememberMe] = useState(false);
   // State to display any login error messages
  const [error, setError] = useState("");
  // State to indicate if login is in progress
  const [isLoading, setIsLoading] = useState(false);
  // Hook for programmatic navigation after login
  const navigate = useNavigate();

  // Handle input changes and update the credentials state
  const handleChange = (e) => {
    const { name, value } = e.target;
    setCredentials((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // Validate form inputs before submitting
  const validateForm = () => {
    if (!credentials.email) {
      setError("Email is required");
      return false;
    }
    if (!credentials.password) {
      setError("Password is required");
      return false;
    }
    return true;
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    setIsLoading(true);
    setError("");

    try {
    // Attempt login using the provided credentials
    console.log("Intentando login con:", credentials);
    await login(credentials);
    console.log("Login exitoso, isAdmin:", isAdmin());
    // Store email in local storage if "Remember me" is checked
    if (rememberMe) {
      localStorage.setItem("userEmail", credentials.email);
    } else {
      localStorage.removeItem("userEmail");
    }
    setTimeout(() => {
      if (isAdmin()) {
        console.log("Redirigiendo a dashboard");
        navigate("/admin/dashboard");
      } else {
        console.log("Redirigiendo a home");
        // Redirect to homepage after successful login
        navigate("/");
      }
    }, 100);
  } catch (err) {
    console.error("Login error:", err.message);
    setError("Invalid credentials. Please try again.");
  } finally {
    setIsLoading(false);
  }
};

  return (
    <div className="login-container">
      <div className="login-form-wrapper">
        <h1 className="login-title">Iniciar sesión</h1>

        {error && <div className="login-error">{error}</div>}

        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="email">Correo electrónico</label>
            <input
              type="email"
              id="email"
              name="email"
              value={credentials.email}
              onChange={handleChange}
              className="form-control"
              placeholder="Introduce tu correo electrónico"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Contraseña</label>
            <input
              type="password"
              id="password"
              name="password"
              value={credentials.password}
              onChange={handleChange}
              className="form-control"
              placeholder="Introduce tu contraseña"
            />
          </div>

          <div className="form-options">
            <div className="remember-me">
              <input
                type="checkbox"
                id="rememberMe"
                checked={rememberMe}
                onChange={() => setRememberMe(!rememberMe)}
              />
              <label htmlFor="rememberMe">Recordarme</label>
            </div>
            <Link to="/recover-password" className="forgot-password">
              ¿Olvidaste tu contraseña?
            </Link>
          </div>

          <button type="submit" className="login-button" disabled={isLoading}>
            {isLoading ? "Cargando..." : "Iniciar sesión"}
          </button>

          <div className="register-link">
            ¿No tienes una cuenta? <Link to="/register">Regístrate</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
