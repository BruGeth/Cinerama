import React, { useEffect, useState } from 'react';
import '../styles/FestaRamaPage.css';
import { useNavigate } from 'react-router-dom';

const FestaRamaPage = () => {
  const navigate = useNavigate();
  const [fadeIn, setFadeIn] = useState(false);

  useEffect(() => {
    setTimeout(() => setFadeIn(true), 100); // delay animation
  }, []);

  const handleBack = () => {
    navigate('/corporate');
  };
  // Nueva función para manejar el clic en el botón de reserva
  const handleReserveClick = () => {
    navigate('/festarama/packages'); // Navega a la página de paquetes
  };

  return (
    <div className={`festarama-container ${fadeIn ? 'fade-in' : ''}`}>
      {/* Banner */}
      <div className="festarama-banner">
        <div className="banner-content">
          <div className="festarama-banner-icon">🎉</div> 
          <h2 className="banner-title">FestaRama</h2>
          <p className="banner-description">
            ¡El lugar perfecto para celebrar cumpleaños y fiestas infantiles con la magia del cine!
          </p>
        </div>
      </div>

      {/* Content */}
      <div className="festarama-content">
        <button onClick={handleBack} className="festarama-back-button">
          ← Volver
        </button>

        <div className="festarama-birthday-card">
          <div className="festarama-birthday-info">
            <h1 className="festarama-birthday-title">La Mejor Fiesta de Cumpleaños</h1>
            <p>
              En FestaRama de CINERAMA creamos experiencias inolvidables para los más pequeños.
              Nuestras fiestas combinan la magia del cine con actividades divertidas, decoración
              temática y deliciosos alimentos.
            </p>
            <p>
              Cada fiesta es personalizada según los gustos del cumpleañero, con temáticas de sus
              películas favoritas y atención especializada.
            </p>

            <div className="festarama-feature-list">
              <div className="festarama-feature-item pastel-tematico">
                <span>🎂 Pastel temático</span> 
              </div>
              <div className="festarama-feature-item hasta-20-ninos">
                <span>👦👧 Hasta 20 niños</span> 
              </div>
              <div className="festarama-feature-item todos-los-dias">
                <span>📆 Todos los días</span> 
              </div>
              <div className="festarama-feature-item sala-privada">
                <span>🎥 Sala privada</span>
              </div>
            </div>

            <button className="festarama-reserve-button" onClick={handleReserveClick}>🎈 Reserva tu fiesta</button>
          </div>

          <div className="festarama-birthday-image">
            <img
              src="/images/FestaRama.jpg"
              alt="Niños celebrando cumpleaños"
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default FestaRamaPage;
