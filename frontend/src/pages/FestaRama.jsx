import { useEffect, useState } from 'react';
import '../styles/FestaRama.css';
import { useNavigate } from 'react-router-dom';

const FestaRama = () => {
  const navigate = useNavigate();
  const [fadeIn, setFadeIn] = useState(false);

  // Trigger fade-in animation on mount after a short delay
  useEffect(() => {
    setTimeout(() => setFadeIn(true), 100);
  }, []);

  // Navigate back to the Corporate page
  const handleBack = () => {
    navigate('/corporate');
  };
  
  // Navigate to the packages/reservation page
  const handleReserveClick = () => {
    navigate('/festarama/packages'); 
  };

  return (
    <div className={`festarama-container ${fadeIn ? 'fade-in' : ''}`}>
      {/*  FestaRama Banner Section */}
      <div className="festarama-banner">
        <div className="banner-content">
          <div className="festarama-banner-icon">🎉</div> 
          <h2 className="banner-title">FestaRama</h2>
          <p className="banner-description">
            ¡El lugar perfecto para celebrar cumpleaños y fiestas infantiles con la magia del cine!
          </p>
        </div>
      </div>

      {/* Main Content Section */}
      <div className="festarama-content">
        {/* Back Button */}
        <button onClick={handleBack} className="festarama-back-button">
          ← Volver
        </button>

      {/* Birthday Party Card Section */}
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

          {/* Feature List */}  
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

          {/* Reserve Button */}
            <button className="festarama-reserve-button" onClick={handleReserveClick}>🎈 Reserva tu fiesta</button>
          </div>

          {/* Birthday Image */}
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

export default FestaRama;
