import { Link } from "react-router-dom"
import "../styles/Navbar.css"

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        <div className="navbar-logo">
          <Link to="/">CINERAMA</Link>
        </div>
        <div className="navbar-links">
          <Link to="/cartelera" className="nav-link">
            CARTELERA
          </Link>
          <Link to="/cines" className="nav-link">
            CINES
          </Link>
          <Link to="/promociones" className="nav-link">
            PROMOCIONES
          </Link>
          <Link to="/confiteria" className="nav-link">
            CONFITERÍA
          </Link>
          <Link to="/corporativo" className="nav-link">
            CORPORATIVO
          </Link>
        </div>
        <div className="navbar-buttons">
          <Link to="/login" className="btn btn-login">
            INICIAR SESIÓN
          </Link>
          <Link to="/register" className="btn btn-register">
            REGISTRARSE
          </Link>
        </div>
      </div>
    </nav>
  )
}

export default Navbar