import { Link } from "react-router-dom"
import "../styles/Footer.css"

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-logo">
          <Link to="/">CINERAMA</Link>
        </div>

        <div className="footer-columns">
          <div className="footer-column">
            <h3>Nosotros</h3>
            <ul>
              <li>
                <Link to="/quienes-somos">Quienes somos</Link>
              </li>
              <li>
                <Link to="/terminos">Términos y Condiciones</Link>
              </li>
              <li>
                <Link to="/privacidad">Política de Privacidad</Link>
              </li>
            </ul>
          </div>

          <div className="footer-column">
            <h3>Atención al Cliente</h3>
            <ul>
              <li>
                <Link to="/contacto">Contacto</Link>
              </li>
              <li>
                <Link to="/preguntas-frecuentes">Preguntas Frecuentes</Link>
              </li>
              <li>
                <Link to="/ayuda">Ayuda</Link>
              </li>
            </ul>
          </div>

          <div className="footer-column">
            <h3>Síguenos</h3>
            <div className="social-links">
              <Link to="https://facebook.com" className="social-link">
                Facebook
              </Link>
              <Link to="https://instagram.com" className="social-link">
                Instagram
              </Link>
              <Link to="https://twitter.com" className="social-link">
                Twitter
              </Link>
            </div>
          </div>
        </div>
      </div>
      <div className="footer-copyright">© 2025 CINERAMA. Todos los derechos reservados</div>
    </footer>
  )
}

export default Footer
