import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { Link, useNavigate } from "react-router-dom";
import "../styles/Navbar.css";

const Navbar = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <div className="navbar-logo">
          <Link to="/">
            <span className="logo-ciner">CINER</span>
            <span className="logo-ama">AMA</span>
          </Link>
        </div>
        <div className="navbar-links">
          <Link to="/cartelera" className="nav-link">
            CARTELERA
          </Link>
          <Link to="/cines" className="nav-link">
            CINES
          </Link>
          <Link to="/promotions" className="nav-link">
            PROMOCIONES
          </Link>
          <Link to="/confiteria" className="nav-link">
            CONFITERÍA
          </Link>
          <Link to="/corporate" className="nav-link">
            CORPORATIVO
          </Link>
        </div>

        <div className="navbar-buttons">
          {user ? (
            <>
              <Link to="/profile" className="btn btn-login">
                <span className="user-greeting">Hola, {user.name.split(" ")[0]}</span>
              </Link>
              <button
                className="btn btn-register"
                onClick={handleLogout}
              >
                Cerrar Sesion
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn btn-login">Iniciar Sesion</Link>
              <Link to="/register" className="btn btn-register">Registrarse</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
