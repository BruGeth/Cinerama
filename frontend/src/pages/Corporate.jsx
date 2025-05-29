import React from 'react';
import '../styles/Corporate.css';
import { useNavigate } from 'react-router-dom';

const Corporate = () => {
  const navigate = useNavigate();

  return (
    <div className="corporate-container">
      <div className="corporate-content">
        <h1 className="corporate-title">Conoce nuestros productos</h1>

        <div className="corporate-products-grid">
          {/* Events Card */}
          <div className="corporate-product-card">
            <div className="corporate-card-overlay">
              <div className="corporate-card-image corporate-events-img">
                <h2 className="corporate-image-title">Eventos</h2>
              </div>
              <div className="corporate-card-content corporate-yellow">
                <h2>Eventos</h2>
                <p>Vive la experiencia de alquilar una sala para tener en línea, documentales, conciertos o cualquier otro tipo de streaming desde nuestras áreas a tu disposición.</p>
                <button className="corporate-product-button">Cotizar</button>
              </div>
            </div>
          </div>

          {/* Special Functions Card */}
          <div className="corporate-product-card">
            <div className="corporate-card-overlay">
              <div className="corporate-card-image corporate-functions-img">
                <h2 className="corporate-image-title">Funciones Especiales</h2>
              </div>
              <div className="corporate-card-content corporate-red">
                <h2>Funciones Especiales</h2>
                <p>Disfruta de salas exclusivas para ti y tus invitados, con películas de estreno, contenido alternativo y mucho más.</p>
                <button className="corporate-product-button">Cotizar</button>
              </div>
            </div>
          </div>

          {/* Tickets and Combos Card */}
          <div className="corporate-product-card">
            <div className="corporate-card-overlay">
              <div className="corporate-card-image corporate-tickets-img">
                <h2 className="corporate-image-title">Entradas y Combos</h2>
              </div>
              <div className="corporate-card-content corporate-yellow">
                <h2>Entradas y Combos</h2>
                <p>Adquiere nuestros productos con precios especiales y propuestas personalizadas para empresas y clientes.</p>
                <button className="corporate-product-button">Cotizar</button>
              </div>
            </div>
          </div>

          {/* FestaRama Card */}
          <div className="corporate-product-card">
            <div className="corporate-card-overlay">
              <div className="corporate-card-image corporate-festarama-img">
                <h2 className="corporate-image-title">FestaRama</h2>
              </div>
              <div className="corporate-card-content corporate-red">
                <h2>FestaRama</h2>
                <p>Festeja celebrando momentos de película con tu talento y amigos, nosotros te ofrecemos la experiencia, permitiendo que tu grupo disfrute videojuegos y comer con un toque especial.</p>
              <button className="corporate-product-button"onClick={() => navigate('/festarama')}>Cotizar</button>
              </div>
            </div>
          </div>

          {/* Advertising Card */}
          <div className="corporate-product-card">
            <div className="corporate-card-overlay">
              <div className="corporate-card-image corporate-advertising-img">
                <h2 className="corporate-image-title">Publicidad</h2>
              </div>
              <div className="corporate-card-content corporate-yellow">
                <h2>Publicidad</h2>
                <p>Promociona tus elementos para ampliar tu marca, dependes un mayor impacto desde asociación de marca y difusión.</p>
                <button className="corporate-product-button">Cotizar</button>
              </div>
            </div>
          </div>

          {/* At Work Card */}
          <div className="corporate-product-card">
            <div className="corporate-card-overlay">
              <div className="corporate-card-image corporate-atwork-img">
                <h2 className="corporate-image-title">At Work</h2>
              </div>
              <div className="corporate-card-content corporate-red">
                <h2>At Work</h2>
                <p>Conoce sobre este programa dedicado para tus colaboradores, con el fin de integrarlos permitiendo reconocer su desempeño y economía.</p>
                <button className="corporate-product-button">Cotizar</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Corporate;
