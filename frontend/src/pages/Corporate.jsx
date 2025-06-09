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
                <div className="corporate-image-content">
                  <div className="corporate-floating-icon">🎬</div>
                  <h2 className="corporate-image-title">Eventos</h2>
                  <div className="corporate-shine-effect"></div>
                </div>
              </div>
              <div className="corporate-card-content corporate-yellow">
                <div className="corporate-content-wrapper">
                  <h2>Eventos</h2>
                  <p>Vive la experiencia de alquilar una sala para tener en línea, documentales, conciertos o cualquier otro tipo de streaming desde nuestras áreas a tu disposición.</p>
                  <button className="corporate-product-button" onClick={() => navigate('/events')}>
                    Cotizar
                  </button>
                </div>
              </div>
            </div>
          </div>

          {/* Special Functions Card */}
          <div className="corporate-product-card">
            <div className="corporate-card-overlay">
              <div className="corporate-card-image corporate-functions-img">
                <div className="corporate-image-content">
                  <div className="corporate-floating-icon">🍿</div>
                  <h2 className="corporate-image-title">Funciones Especiales</h2>
                  <div className="corporate-shine-effect"></div>
                </div>
              </div>
              <div className="corporate-card-content corporate-red">
                <div className="corporate-content-wrapper">
                  <h2>Funciones Especiales</h2>
                  <p>Disfruta de salas exclusivas para ti y tus invitados, con películas de estreno, contenido alternativo y mucho más.</p>
                  <button className="corporate-product-button" onClick={() => navigate('/specialfunctions')}>
                    Cotizar
                  </button>
                </div>
              </div>
            </div>
          </div>

          {/* FestaRama Card */}
          <div className="corporate-product-card">
            <div className="corporate-card-overlay">
              <div className="corporate-card-image corporate-festarama-img">
                <div className="corporate-image-content">
                  <div className="corporate-floating-icon">🎉</div>
                  <h2 className="corporate-image-title">FestaRama</h2>
                  <div className="corporate-shine-effect"></div>
                </div>
              </div>
              <div className="corporate-card-content corporate-red">
                <div className="corporate-content-wrapper">
                  <h2>FestaRama</h2>
                  <p>Festeja celebrando momentos de película con tu talento y amigos, nosotros te ofrecemos la experiencia, permitiendo que tu grupo disfrute videojuegos y comer con un toque especial.</p>
                  <button className="corporate-product-button" onClick={() => navigate('/festarama')}>
                    Cotizar
                  </button>
                </div>
              </div>
            </div>
          </div>

          {/* Advertising Card */}
          <div className="corporate-product-card">
            <div className="corporate-card-overlay">
              <div className="corporate-card-image corporate-advertising-img">
                <div className="corporate-image-content">
                  <div className="corporate-floating-icon">📢</div>
                  <h2 className="corporate-image-title">Publicidad</h2>
                  <div className="corporate-shine-effect"></div>
                </div>
              </div>
              <div className="corporate-card-content corporate-yellow">
                <div className="corporate-content-wrapper">
                  <h2>Publicidad</h2>
                  <p>Promociona tus elementos para ampliar tu marca, dependes un mayor impacto desde asociación de marca y difusión.</p>
                  <button className="corporate-product-button" onClick={() => navigate('/advertising')} >
                    Cotizar
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Corporate;