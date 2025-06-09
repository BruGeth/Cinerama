import { useContext, useState } from "react";
import { AuthContext } from "../context/AuthContext";
import { Link, useNavigate } from "react-router-dom";
import "../styles/Navbar.css";

const Navbar = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/");
    setMenuOpen(false);
  };

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

  const closeMenu = () => {
    setMenuOpen(false);
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <div className="navbar-mobile-top">
          <div className="navbar-logo">
            <Link to="/" onClick={closeMenu}>
              <span className="logo-ciner">CINER</span>
              <span className="logo-ama">AMA</span>
            </Link>
          </div>

          <button
            className="menu-toggle"
            onClick={toggleMenu}
            aria-label="Toggle menu"
          >
            <span className={`menu-bar ${menuOpen ? "open" : ""}`}></span>
            <span className={`menu-bar ${menuOpen ? "open" : ""}`}></span>
            <span className={`menu-bar ${menuOpen ? "open" : ""}`}></span>
          </button>
        </div>

        <div className={`navbar-mobile-menu ${menuOpen ? "open" : ""}`}>
          <div className="navbar-links">
            <Link to="/movies" className="nav-link" onClick={closeMenu}>
              CARTELERA
            </Link>
            <Link to="/cinemas" className="nav-link" onClick={closeMenu}>
              CINES
            </Link>
            <Link to="/promotions" className="nav-link" onClick={closeMenu}>
              PROMOCIONES
            </Link>
            <Link to="/confectionery" className="nav-link" onClick={closeMenu}>
              CONFITERÍA
            </Link>
            <Link to="/corporate" className="nav-link" onClick={closeMenu}>
              CORPORATIVO
            </Link>
          </div>

          <div className="navbar-buttons">
            {user ? (
              <>
                <Link
                  to="/profile"
                  className="btn btn-login"
                  onClick={closeMenu}
                >
                  <span className="user-greeting">
                    Hola, {user.name ? user.name.split(" ")[0] : user.email}
                  </span>
                </Link>
                {user.role === "admin" && (
                  <button
                    className="btn btn-dashboard"
                    onClick={() => {
                      navigate("/admin/dashboard");
                      setMenuOpen(false);
                    }}
                  >
                    Dashboard
                  </button>
                )}
                <button className="btn btn-register" onClick={handleLogout}>
                  Cerrar Sesion
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="btn btn-login" onClick={closeMenu}>
                  Iniciar Sesion
                </Link>
                <Link
                  to="/register"
                  className="btn btn-register"
                  onClick={closeMenu}
                >
                  Registrarse
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
