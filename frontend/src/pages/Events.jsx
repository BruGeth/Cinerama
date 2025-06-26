import React, { useState, useEffect, useCallback, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import '../styles/Events.css';

const Events = () => {
  const navigate = useNavigate();
  const [selectedEvent, setSelectedEvent] = useState('');
  const [selectedCinema, setSelectedCinema] = useState('');
  const [currentStep, setCurrentStep] = useState(1);
  const [isAnimating, setIsAnimating] = useState(false);
  const [formData, setFormData] = useState({
    eventType: '',
    cinema: '',
    date: '',
    time: '',
    duration: '',
    attendees: '',
    requirements: '',
    contactName: '',
    contactEmail: '',
    contactPhone: '',
    company: '',
    message: ''
  });

  const eventTypes = useMemo(() => [
    {
      id: 'conferencia',
      name: ' 🎤 Conferencias',
      description: 'Espacios ideales para presentaciones corporativas y académicas con tecnología de vanguardia',
      image: '/images/conference.jpg', 
      backgroundClass: 'conference-bg',
      gradient: 'linear-gradient(135deg, #dc2626 0%, #fbbf24 100%)',
      accentColor: '#dc2626'
    },
    {
      id: 'capacitacion',
      name: ' 📚 Capacitación',
      description: 'Ambientes perfectos para sesiones de entrenamiento y formación profesional inmersiva',
      image: '/images/training.jpg', 
      backgroundClass: 'training-bg',
      gradient: 'linear-gradient(135deg, #ef4444 0%, #fcd34d 100%)',
      accentColor: '#ef4444'
    },
    {
      id: 'lanzamiento',
      name: ' 🚀 Lanzamiento',
      description: 'Eventos espectaculares para presentar productos y servicios con máximo impacto visual',
      image: '/images/launch.jpg', 
      backgroundClass: 'launch-bg',
      gradient: 'linear-gradient(135deg, #b91c1c 0%, #f59e0b 100%)',
      accentColor: '#b91c1c'
    },
    {
      id: 'presentaciones',
      name: ' 📊 Presentaciones',
      description: 'Salas equipadas para demostraciones y exhibiciones profesionales de alto nivel',
      image: '/images/presentation.jpg', 
      backgroundClass: 'presentation-bg',
      gradient: 'linear-gradient(135deg, #dc2626 0%, #fbbf24 100%)',
      accentColor: '#dc2626'
    },
    {
      id: 'club-fans',
      name: ' ⭐ Club de Fans',
      description: 'Experiencias exclusivas para comunidades y seguidores con contenido premium',
      image: '/images/fanclub.jpg', 
      backgroundClass: 'fanclub-bg',
      gradient: 'linear-gradient(135deg, #991b1b 0%, #d97706 100%)',
      accentColor: '#991b1b'
    },
    {
      id: 'zona-gamer',
      name: ' 🎮 Zona Gamer',
      description: 'Experiencias gaming épicas con pantallas gigantes y torneos competitivos de última generación',
      image: '/images/gaming.jpg', 
      backgroundClass: 'gaming-bg',
      gradient: 'linear-gradient(135deg, #dc2626 0%, #fbbf24 50%, #ef4444 100%)',
      accentColor: '#dc2626',
      isNew: true
    }
  ], []);

  // Cinema options - Updated with specific images and locations
  const cinemaOptions = useMemo(() => [
    {
      id: 'cinema-miraflores',
      name: 'Cinerama Miraflores',
      description: 'Cinema de lujo en el corazón de Miraflores con tecnología de vanguardia y servicios premium',
      capacity: '180 personas',
      features: ['4K Projection', '2D ', '3D ', 'XD', 'IMAX'],
      image: 'https://www.cinerama.com.pe/_admin/assets/images/cines/pacifico.jpg',
      gradient: 'linear-gradient(135deg, #dc2626 0%, #fbbf24 100%)',
      location: 'Miraflores'
    },
    {
      id: 'cinema-minka',
      name: 'Cinerama Minka',
      description: 'Moderno complejo cinematográfico en Minka con amplias instalaciones y tecnología avanzada',
      capacity: '220 personas',
      features: ['HD Projection', '3D', 'XD', 'IMAX'],
      image: 'https://www.cinerama.com.pe/_admin/assets/images/cines/minka.jpg',
      gradient: 'linear-gradient(135deg, #ef4444 0%, #fcd34d 100%)',
      location: 'Callao'
    }
  ], []);

  const steps = [
    { number: 1, title: 'Evento', active: currentStep >= 1 },
    { number: 2, title: 'Cines', active: currentStep >= 2 },
    { number: 3, title: 'Detalles', active: currentStep >= 3 },
    { number: 4, title: 'Contacto', active: currentStep >= 4 },
    { number: 5, title: 'Resumen', active: currentStep >= 5 }
  ];

  // Handle event selection
  const handleEventSelect = useCallback((eventId) => {
    if (isAnimating) return;
    setIsAnimating(true);
    setSelectedEvent(eventId);
    setFormData(prev => ({ ...prev, eventType: eventId }));
    if (navigator.vibrate) navigator.vibrate(50);
    setTimeout(() => setIsAnimating(false), 300);
  }, [isAnimating]);

  // Handle cinema selection
  const handleCinemaSelect = useCallback((cinemaId) => {
    if (isAnimating) return;
    setIsAnimating(true);
    setSelectedCinema(cinemaId);
    setFormData(prev => ({ ...prev, cinema: cinemaId }));
    if (navigator.vibrate) navigator.vibrate(50);
    setTimeout(() => setIsAnimating(false), 300);
  }, [isAnimating]);

  // Handle form input changes
  const handleInputChange = useCallback((field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  }, []);

  // Handle continue to next step
  const handleContinue = useCallback(() => {
    if (isAnimating) return;
    const canContinue = (
      (currentStep === 1 && selectedEvent) ||
      (currentStep === 2 && selectedCinema) ||
      (currentStep === 3 && formData.date && formData.time && formData.attendees) ||
      (currentStep === 4 && formData.contactName && formData.contactEmail && formData.contactPhone) ||
      currentStep === 5
    );

    if (canContinue) {
      setIsAnimating(true);
      setTimeout(() => {
        setCurrentStep(prev => Math.min(prev + 1, 5));
        setIsAnimating(false);
      }, 300);
    }
  }, [selectedEvent, selectedCinema, formData, currentStep, isAnimating]);

  // Handle back navigation
  const handleBack = useCallback(() => {
    if (isAnimating) return;
    setIsAnimating(true);
    
    if (currentStep > 1) {
      setTimeout(() => {
        setCurrentStep(prev => prev - 1);
        setIsAnimating(false);
      }, 300);
    } else {
      navigate('/corporate');
    }
  }, [isAnimating, currentStep, navigate]);

  // Handle final submission
  const handleSubmit = useCallback(async () => {
  try {
  const response = await fetch("http://localhost:8080/api/events", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(formData),
  });

  const result = await response.json();
  if (response.ok) {
    toast.success("🎬 ¡Tu solicitud fue enviada exitosamente!");
    setTimeout(() => navigate("/corporate"), 3000); // redirecciona luego del toast
  } else {
    toast.error("❌ Error: " + result.message);
  }
  } catch (error) {
  toast.error("❌ No se pudo conectar con el servidor. Intenta nuevamente.");
  console.error(error);
  }
  }, [formData, navigate]);

  // Image error handling
  const handleImageError = (e, backgroundClass) => {
    e.target.style.display = 'none';
    if (e.target.parentElement) {
      e.target.parentElement.classList.add(backgroundClass);
    }
  };

  // Keyboard navigation
  useEffect(() => {
    const handleKeyDown = (e) => {
      if (currentStep === 1 && selectedEvent) {
        const currentIndex = eventTypes.findIndex(event => event.id === selectedEvent);
        switch (e.key) {
          case 'ArrowUp':
          case 'ArrowLeft':
            e.preventDefault();
            if (currentIndex > 0) handleEventSelect(eventTypes[currentIndex - 1].id);
            break;
          case 'ArrowDown':
          case 'ArrowRight':
            e.preventDefault();
            if (currentIndex < eventTypes.length - 1) handleEventSelect(eventTypes[currentIndex + 1].id);
            break;
          case 'Enter':
            e.preventDefault();
            if (selectedEvent) handleContinue();
            break;
          case 'Escape':
            e.preventDefault();
            handleBack();
            break;
          default:
            break;
        }
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [currentStep, selectedEvent, handleEventSelect, handleContinue, handleBack, eventTypes]);

  useEffect(() => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }, [currentStep]);

  return (
    <div className="events-container">
      {/* Header Section */}
      <div className="events-header">
        <div className="events-header-content">
          <div className="events-icon">🎬</div>
          <h1 className="events-title">Eventos</h1>
          <p className="events-subtitle">
            Convierte tus ideas en experiencias cinematográficas únicas 
          </p>
        </div>
        <div className="events-header-overlay"></div>
      </div>

      {/* Progress Steps */}
      <div className="events-progress">
        <div className="events-progress-container">
          {steps.map((step, index) => (
            <div 
              key={step.number} 
              className={`events-step ${step.active ? 'active' : ''} ${currentStep === step.number ? 'current' : ''}`}
            >
              <div className="events-step-number">{step.number}</div>
              <span className="events-step-title">{step.title}</span>
              {index < steps.length - 1 && (
                <div className={`events-step-line ${step.active ? 'active' : ''}`}></div>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* Main Content */}
      <div className="events-content">
        
        {/* STEP 1: Event Selection */}
        {currentStep === 1 && (
          <div className="events-selection-section">
            <div className="events-selection-header">
              <h2>Selecciona el tipo de evento que deseas realizar</h2>
              <p>Elige la opción que mejor se adapte a tu visión y objetivos empresariales</p>
            </div>

            <div className="events-grid">
              {eventTypes.map((event) => (
                <div
                  key={event.id}
                  className={`events-card ${selectedEvent === event.id ? 'selected' : ''} ${isAnimating ? 'animating' : ''}`}
                  onClick={() => handleEventSelect(event.id)}
                  style={{ '--card-gradient': event.gradient, '--accent-color': event.accentColor }}
                >
                  <div className={`events-card-image ${event.backgroundClass}`}>
                    <img 
                      src={event.image} 
                      alt={`${event.name} - Evento corporativo`}
                      className="events-card-img"
                      loading="lazy"
                      onError={(e) => handleImageError(e, event.backgroundClass)}
                    />
                    <div className="events-card-icon">{event.icon}</div>
                    <div className="events-card-overlay">
                      <h3>{event.name}</h3>
                    </div>
                  </div>
                  
                  <div className="events-card-content">
                    <div className="events-card-header">
                      <div className="events-card-icon-small">{event.icon}</div>
                      <h4>{event.name}</h4>
                    </div>
                    <p>{event.description}</p>
                    <div className={`events-radio ${selectedEvent === event.id ? 'checked' : ''}`}>
                      <div className="events-radio-inner"></div>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>← Regresar</button>
              <button 
                className={`events-btn-primary ${!selectedEvent ? 'disabled' : ''}`}
                onClick={handleContinue}
                disabled={!selectedEvent}
              >
                Continuar →
              </button>
            </div>
          </div>
        )}

        {/* STEP 2: Cinema Selection */}
        {currentStep === 2 && (
          <div className="events-selection-section">
            <div className="events-selection-header">
              <h2>Selecciona el cine para tu evento</h2>
              <p>Elige la sala que mejor se adapte a tus necesidades y número de asistentes</p>
            </div>

            <div className="events-grid">
              {cinemaOptions.map((cinema) => (
                <div
                  key={cinema.id}
                  className={`events-card cinema-card ${selectedCinema === cinema.id ? 'selected' : ''}`}
                  onClick={() => handleCinemaSelect(cinema.id)}
                  style={{ '--card-gradient': cinema.gradient }}
                >
                  <div className="events-card-image cinema-bg">
                    <img 
                      src={cinema.image} 
                      alt={`${cinema.name} - Cinema`}
                      className="events-card-img"
                      loading="lazy"
                      onError={(e) => handleImageError(e, 'cinema-bg')}
                    />
                    <div className="events-card-overlay">
                      <h3>{cinema.name}</h3>
                      <p className="cinema-location">{cinema.location}</p>
                    </div>
                  </div>
                  
                  <div className="events-card-content">
                    <div className="events-card-header">
                      <div className="events-card-icon-small">{cinema.icon}</div>
                      <h4>{cinema.name}</h4>
                    </div>
                    <p>{cinema.description}</p>
                    <div className="cinema-details">
                      <div className="cinema-capacity">
                        <span className="capacity-icon">👥</span>
                        {cinema.capacity}
                      </div>
                      <div className="cinema-features">
                        {cinema.features.map((feature, index) => (
                          <span key={index} className="feature-tag">
                            {feature}
                          </span>
                        ))}
                      </div>
                    </div>
                    <div className={`events-radio ${selectedCinema === cinema.id ? 'checked' : ''}`}>
                      <div className="events-radio-inner"></div>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>← Regresar</button>
              <button 
                className={`events-btn-primary ${!selectedCinema ? 'disabled' : ''}`}
                onClick={handleContinue}
                disabled={!selectedCinema}
              >
                Continuar →
              </button>
            </div>
          </div>
        )}

        {/* STEP 3: Event Details */}
        {currentStep === 3 && (
          <div className="events-form-section">
            <div className="events-selection-header">
              <h2>Detalles del Evento</h2>
              <p>Proporciona la información específica para organizar tu evento perfectamente</p>
            </div>

            <div className="events-form">
              <div className="form-row">
                <div className="form-group">
                  <label>Fecha del Evento *</label>
                  <input 
                    type="date" 
                    value={formData.date}
                    onChange={(e) => handleInputChange('date', e.target.value)}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Hora de Inicio *</label>
                  <input 
                    type="time" 
                    value={formData.time}
                    onChange={(e) => handleInputChange('time', e.target.value)}
                    required
                  />
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Duración (horas)</label>
                  <select 
                    value={formData.duration}
                    onChange={(e) => handleInputChange('duration', e.target.value)}
                  >
                    <option value="">Seleccionar duración</option>
                    <option value="1">1 hora</option>
                    <option value="2">2 horas</option>
                    <option value="3">3 horas</option>
                    <option value="4">4 horas</option>
                    <option value="5+">5+ horas</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Número de Asistentes *</label>
                  <input 
                    type="number" 
                    placeholder="Ej: 50"
                    value={formData.attendees}
                    onChange={(e) => handleInputChange('attendees', e.target.value)}
                    required
                  />
                </div>
              </div>

              <div className="form-group">
                <label>Requerimientos Especiales</label>
                <textarea 
                  placeholder="Describe cualquier requerimiento especial para tu evento..."
                  value={formData.requirements}
                  onChange={(e) => handleInputChange('requirements', e.target.value)}
                  rows="4"
                ></textarea>
              </div>
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>← Regresar</button>
              <button 
                className={`events-btn-primary ${!formData.date || !formData.time || !formData.attendees ? 'disabled' : ''}`}
                onClick={handleContinue}
                disabled={!formData.date || !formData.time || !formData.attendees}
              >
                Continuar →
              </button>
            </div>
          </div>
        )}

        {/* STEP 4: Contact Information */}
        {currentStep === 4 && (
          <div className="events-form-section">
            <div className="events-selection-header">
              <h2>Información de Contacto</h2>
              <p>Completa tus datos para que podamos contactarte y confirmar tu evento</p>
            </div>

            <div className="events-form">
              <div className="form-row">
                <div className="form-group">
                  <label>Nombre Completo *</label>
                  <input 
                    type="text" 
                    placeholder="Tu nombre completo"
                    value={formData.contactName}
                    onChange={(e) => handleInputChange('contactName', e.target.value)}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Empresa/Organización</label>
                  <input 
                    type="text" 
                    placeholder="Nombre de tu empresa"
                    value={formData.company}
                    onChange={(e) => handleInputChange('company', e.target.value)}
                  />
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Correo Electrónico *</label>
                  <input 
                    type="email" 
                    placeholder="tu@email.com"
                    value={formData.contactEmail}
                    onChange={(e) => handleInputChange('contactEmail', e.target.value)}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Teléfono *</label>
                  <input 
                    type="tel" 
                    placeholder="+51 999 999 999"
                    value={formData.contactPhone}
                    onChange={(e) => handleInputChange('contactPhone', e.target.value)}
                    required
                  />
                </div>
              </div>

              <div className="form-group">
                <label>Mensaje Adicional</label>
                <textarea 
                  placeholder="Cuéntanos más sobre tu evento o cualquier pregunta específica..."
                  value={formData.message}
                  onChange={(e) => handleInputChange('message', e.target.value)}
                  rows="4"
                ></textarea>
              </div>
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>← Regresar</button>
              <button 
                className={`events-btn-primary ${!formData.contactName || !formData.contactEmail || !formData.contactPhone ? 'disabled' : ''}`}
                onClick={handleContinue}
                disabled={!formData.contactName || !formData.contactEmail || !formData.contactPhone}
              >
                Continuar →
              </button>
            </div>
          </div>
        )}

        {/* STEP 5: Summary */}
        {currentStep === 5 && (
          <div className="events-summary-section">
            <div className="events-selection-header">
              <h2>Resumen del Evento</h2>
              <p>Revisa todos los detalles antes de enviar tu solicitud</p>
            </div>

            <div className="summary-card">
              <div className="summary-section">
                <h3>🎬 Tipo de Evento</h3>
                <p>{eventTypes.find(e => e.id === selectedEvent)?.name}</p>
                <span className="summary-description">
                  {eventTypes.find(e => e.id === selectedEvent)?.description}
                </span>
              </div>

              <div className="summary-section">
                <h3>🎞️ Cine Seleccionado</h3>
                <p>{cinemaOptions.find(c => c.id === selectedCinema)?.name}</p>
                <span className="summary-description">
                  {cinemaOptions.find(c => c.id === selectedCinema)?.description}
                </span>
              </div>

              <div className="summary-section">
                <h3>📅 Detalles del Evento</h3>
                <div className="summary-details">
                  <p><strong>Fecha:</strong> {formData.date}</p>
                  <p><strong>Hora:</strong> {formData.time}</p>
                  <p><strong>Duración:</strong> {formData.duration || 'No especificada'}</p>
                  <p><strong>Asistentes:</strong> {formData.attendees} personas</p>
                  {formData.requirements && (
                    <p><strong>Requerimientos:</strong> {formData.requirements}</p>
                  )}
                </div>
              </div>

              <div className="summary-section">
                <h3>👤 Información de Contacto</h3>
                <div className="summary-details">
                  <p><strong>Nombre:</strong> {formData.contactName}</p>
                  {formData.company && <p><strong>Empresa:</strong> {formData.company}</p>}
                  <p><strong>Email:</strong> {formData.contactEmail}</p>
                  <p><strong>Teléfono:</strong> {formData.contactPhone}</p>
                  {formData.message && (
                    <p><strong>Mensaje:</strong> {formData.message}</p>
                  )}
                </div>
              </div>
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>← Regresar</button>
              <button className="events-btn-primary" onClick={handleSubmit}>
                Enviar Solicitud ✨
              </button>
            </div>
          </div>
        )}
      </div>
      <ToastContainer
      position="top-center"
      autoClose={3000}
      hideProgressBar={false}
      newestOnTop
      closeOnClick
      pauseOnHover
      theme="colored"
    />
    </div>
  );
};

export default Events;