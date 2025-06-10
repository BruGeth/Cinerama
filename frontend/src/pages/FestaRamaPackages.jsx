import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/FestaRamaPackages.css';
import confetti from 'canvas-confetti';

const FestaRamaPackages = () => {
  const navigate = useNavigate();
  const [fadeIn, setFadeIn] = useState(false);
  const [selectedPackage, setSelectedPackage] = useState(null);
  const [showModal, setShowModal] = useState(false);
  
  useEffect(() => {
    setTimeout(() => setFadeIn(true), 100); // delay animation
  }, []);

  const handleBack = () => {
    navigate('/festarama'); 
  };

  const handleSelectPackage = (packageName) => {
    setSelectedPackage(packageName);
    setShowModal(true);
    
    // Trigger confetti effect when package is selected
  const duration = 3 * 1000;
  const animationEnd = Date.now() + duration;

  const randomInRange = (min, max) => {
    return Math.random() * (max - min) + min;
  }

  const interval = setInterval(function() {
    const timeLeft = animationEnd - Date.now();

    if (timeLeft <= 0) {
      return clearInterval(interval);
    }

    const particleCount = 50 * (timeLeft / duration);
    
    // Fire from the left
    confetti({
      particleCount,
      startVelocity: 30,
      spread: 80,
      origin: {
        x: randomInRange(0.1, 0.3),
        y: Math.random() - 0.2
      }
    });
    
    // Fire from the right
    confetti({
      particleCount,
      startVelocity: 30,
      spread: 80,
      origin: {
        x: randomInRange(0.7, 0.9),
        y: Math.random() - 0.2
      }
    });
  }, 250);
};

  const closeModal = () => {
    setShowModal(false);
  };

  const confirmPackage = () => {
    alert(`¡Has reservado el ${selectedPackage}! Te contactaremos pronto para confirmar los detalles.`);
    setShowModal(false);
  };

  return (
    <div className={`festarama-packages-container ${fadeIn ? 'fade-in' : ''}`}>
      <div className="packages-header">
        <button onClick={handleBack} className="packages-back-button">
          ← Volver
        </button>
        <h1 className="packages-title">Nuestros Paquetes de Fiesta</h1>
        <p className="packages-subtitle">Elige el paquete perfecto para la celebración de tus pequeños</p>
      </div>

      <div className="packages-grid">
        {/* Paquete Básico */}
        <div className="package-card basic">
          <div className="package-header">
            <h2>Paquete Básico</h2>
            <div className="price-tag">S/. 500</div>
          </div>
          <div className="package-body">
            <ul className="package-features">
              <li><span className="check-icon">✓</span> Sala privada por 3 horas</li>
              <li><span className="check-icon">✓</span> Película a elección</li>
              <li><span className="check-icon">✓</span> Palomitas y refrescos para 15 niños</li>
              <li><span className="check-icon">✓</span> Invitaciones digitales</li>
              <li><span className="check-icon">✓</span> Decoración básica</li>
            </ul>
          </div>
          <div className="package-footer">
            <button 
              className="select-button basic-button"
              onClick={() => handleSelectPackage("Paquete Básico")}
            >
              Seleccionar
            </button>
          </div>
        </div>

        {/* Paquete Premium */}
        <div className="package-card premium">
          <div className="package-header">
            <div className="ribbon-popular">
            <span className="ribbon-text">¡MÁS VENDIDO!</span>
            <div className="ribbon-shine"></div>
            </div>
            <h2>Paquete Premium</h2>
            <div className="price-tag">S/. 1000</div>
          </div>
          <div className="package-body">
            <ul className="package-features">
              <li><span className="check-icon">✓</span> Sala privada por 4 horas</li>
              <li><span className="check-icon">✓</span> Película a elección</li>
              <li><span className="check-icon">✓</span> Palomitas y refrescos para 30 niños</li>
              <li><span className="check-icon">✓</span> Pastel temático</li>
              <li><span className="check-icon">✓</span> Decoración temática completa</li>
              <li><span className="check-icon">✓</span> Animador por 2 horas</li>
              <li><span className="check-icon">✓</span> Invitaciones físicas y digitales</li>
            </ul>
          </div>
          <div className="package-footer">
            <button 
              className="select-button premium-button"
              onClick={() => handleSelectPackage("Paquete Premium")}
            >
              Seleccionar
            </button>
          </div>
        </div>

        {/* Paquete Deluxe */}
        <div className="package-card deluxe">
          <div className="package-header">
            <h2>Paquete Deluxe</h2>
            <div className="price-tag">S/. 1500</div>
          </div>
          <div className="package-body">
            <ul className="package-features">
              <li><span className="check-icon">✓</span> Sala VIP privada por 5 horas</li>
              <li><span className="check-icon">✓</span> Película a elección</li>
              <li><span className="check-icon">✓</span> Menú completo para 50 niños</li>
              <li><span className="check-icon">✓</span> Pastel temático personalizado</li>
              <li><span className="check-icon">✓</span> Decoración premium</li>
              <li><span className="check-icon">✓</span> 2 Animadores por 3 horas</li>
              <li><span className="check-icon">✓</span> Sesión de fotos profesional</li>
              <li><span className="check-icon">✓</span> Recuerdos para invitados</li>
            </ul>
          </div>
          <div className="package-footer">
            <button 
              className="select-button deluxe-button"
              onClick={() => handleSelectPackage("Paquete Deluxe")}
            >
              Seleccionar
            </button>
          </div>
        </div>
      </div>

      {/* FAQ Section */}
      <div className="faq-section">
        <h2>Preguntas Frecuentes</h2>
        <div className="faq-grid">
          <div className="faq-item">
            <h3>¿Cuánto tiempo antes debo reservar?</h3>
            <p>Recomendamos reservar con al menos 2 semanas de anticipación para garantizar disponibilidad.</p>
          </div>
          <div className="faq-item">
            <h3>¿Puedo traer mi propia decoración?</h3>
            <p>¡Claro! Puedes complementar nuestra decoración con elementos personales.</p>
          </div>
          <div className="faq-item">
            <h3>¿Qué películas están disponibles?</h3>
            <p>Tenemos un amplio catálogo de películas infantiles y familiares. Confirmaremos disponibilidad al momento de tu reserva.</p>
          </div>
          <div className="faq-item">
            <h3>¿Puedo modificar el paquete?</h3>
            <p>Sí, podemos personalizar cualquier paquete según tus necesidades. Consulta por opciones adicionales.</p>
          </div>
        </div>
      </div>

      {/* Confirmation Modal */}
      {showModal && (
        <div className="modal-overlay-improved">
          <div className="modal-content">
            <span className="close-modal" onClick={closeModal}>&times;</span>
            <h2>¡Excelente elección!</h2>
            <p>Has seleccionado el <strong>{selectedPackage}</strong></p>
            <p>Para continuar con la reserva, necesitaremos algunos datos adicionales.</p>
            
            <div className="modal-buttons">
              <button className="modal-cancel" onClick={closeModal}>Cancelar</button>
              <button className="modal-confirm" onClick={confirmPackage}>Confirmar Reserva</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default FestaRamaPackages;