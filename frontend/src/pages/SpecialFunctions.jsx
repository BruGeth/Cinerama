import { useState, useEffect, useCallback, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import { useMovies } from "../hooks/useMovies"; // Import custom hook to fetch movies from API
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import "../styles/SpecialFunctions.css";

const SpecialFunctions = () => {
  const navigate = useNavigate();
  const [selectedCinema, setSelectedCinema] = useState("");
  const [selectedMovie, setSelectedMovie] = useState("");
  const [currentStep, setCurrentStep] = useState(1);
  const [isAnimating, setIsAnimating] = useState(false);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [movieList, setMovieList] = useState([]);
  const [formData, setFormData] = useState({

    cinema: "",
    movie: "",
    institutionType: "",
    capacity: "",
    date: "",
    time: "",
    duration: "",
    attendees: "",
    requirements: "",
    contactName: "",
    contactEmail: "",
    contactPhone: "",
    company: "",
    message: "",
  });

  // Fetch movies from API using custom hook
  const { movies, loading: moviesLoading } = useMovies();
  useEffect(() => {
    if (Array.isArray(movies)) {
      setMovieList(movies);
    }
  }, [movies]);
  
  // Cinema options (static)
  const cinemaOptions = useMemo(
    () => [
      {
        id: "Cinerama Miraflores",
        name: "Cinerama Miraflores",
        description:
          "Cinema de lujo en el corazón de Miraflores con tecnología de vanguardia y servicios premium",
        capacity: "180 personas",
        features: ["4K Projection", "2D", "3D", "XD", "IMAX"],
        image:
          "https://www.cinerama.com.pe/_admin/assets/images/cines/pacifico.jpg",
        gradient: "linear-gradient(135deg, #dc2626 0%, #fbbf24 100%)",
        location: "Miraflores",
      },
      {
        id: "Cinerama Minka",
        name: "Cinerama Minka",
        description:
          "Moderno complejo cinematográfico en Minka con amplias instalaciones y tecnología avanzada",
        capacity: "220 personas",
        features: ["HD Projection", "3D", "XD", "IMAX"],
        image:
          "https://www.cinerama.com.pe/_admin/assets/images/cines/minka.jpg",
        gradient: "linear-gradient(135deg, #ef4444 0%, #fcd34d 100%)",
        location: "Callao",
      },
    ],
    []
  );

  // Helper function to assign gradients based on genre
  const getMovieGradient = useCallback((genre) => {
    const gradients = {
      Acción: "linear-gradient(135deg, #dc2626 0%, #fbbf24 100%)",
      Terror: "linear-gradient(135deg, #7c2d12 0%, #dc2626 100%)",
      "Ciencia Ficción": "linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%)",
      Drama: "linear-gradient(135deg, #7c3aed 0%, #a855f7 100%)",
      Thriller: "linear-gradient(135deg, #374151 0%, #6b7280 100%)",
      Suspenso: "linear-gradient(135deg, #581c87 0%, #7c3aed 100%)",
    };
    return (
      gradients[genre] || "linear-gradient(135deg, #6b7280 0%, #9ca3af 100%)"
    );
  }, []);

  // Filter and transform movies from API - Only the 3 specific movies
  const movieOptions = useMemo(() => {
    if (!Array.isArray(movieList)) return [];

    return movieList
      .filter((movie) =>
        [
          "Thunderbolts",
          "Destino Final: Lazos de Sangre",
          "Star Wars: Episodio III - La venganza de los Sith",
        ].includes(movie.title)
      )
      .map((movie) => ({
        id: movie.id.toString(),
        title: movie.title,
        description: movie.descriptionMovie,
        image: movie.imageUrl,
        duration: `${movie.duration} min`,
        rating: movie.rating,
        genre: movie.genre,
        availableCinemas: movie.availableCinemas,
        gradient: getMovieGradient(movie.genre),
        originalData: movie,
      }));
  }, [movieList]);

  // Institution types for the details form
  const institutionTypes = [
    { value: "empresa", label: "Empresa" },
    { value: "colegio", label: "Colegio" },
    { value: "asociaciones", label: "Asociaciones" },
    { value: "otros", label: "Otros" },
  ];

  // Capacity options
  const capacityOptions = [
    { value: "50", label: "Hasta 50 personas" },
    { value: "100", label: "Hasta 100 personas" },
    { value: "150", label: "Hasta 150 personas" },
    { value: "200", label: "Hasta 200 personas" },
    { value: "250", label: "Hasta 250 personas" },
    { value: "300", label: "Hasta 300 personas" },
    { value: "400", label: "Hasta 400 personas" },
    { value: "500", label: "Hasta 500 personas" },
  ];


  // Steps for the progress bar
  const steps = [
    { number: 1, title: "Cines", active: currentStep >= 1 },
    { number: 2, title: "Película", active: currentStep >= 2 },
    { number: 3, title: "Detalles", active: currentStep >= 3 },
    { number: 4, title: "Contacto", active: currentStep >= 4 },
    { number: 5, title: "Resumen", active: currentStep >= 5 },
  ];

  // Get available movies based on selected cinema
  const availableMovies = useMemo(() => {
    return movieOptions;
  }, [movieOptions]);

  // Email validation helper
  const isValidEmail = useCallback((email) => {
    try {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailRegex.test(email);
    } catch (err) {
      console.error("Error validating email:", err);
      return false;
    }
  }, []);

  // Phone validation helper
  const isValidPhone = useCallback((phone) => {
    try {
      const phoneRegex = /^[+]?[0-9\s\-()]{9,}$/;
      return phoneRegex.test(phone);
    } catch (err) {
      console.error("Error validating phone:", err);
      return false;
    }
  }, []);

  // Handle cinema selection with error handling
  const handleCinemaSelect = useCallback(
    (cinemaId) => {
      try {
        if (isAnimating) return;

        // Validate cinema exists
        const cinema = cinemaOptions.find((c) => c.id === cinemaId);
        if (!cinema) {
          throw new Error("Cine no válido seleccionado");
        }

        setIsAnimating(true);
        setError(null);
        setSelectedCinema(cinemaId);
        setSelectedMovie(""); // Reset movie selection when cinema changes
        setFormData((prev) => ({ ...prev, cinema: cinemaId, movie: "" }));

        // Haptic feedback if available
        if (navigator.vibrate) {
          navigator.vibrate(50);
        }

        setTimeout(() => setIsAnimating(false), 300);
      } catch (err) {
        console.error("Error selecting cinema:", err);
        setError(`Error al seleccionar el cine: ${err.message}`);
        setIsAnimating(false);
      }
    },
    [isAnimating, cinemaOptions]
  );

  // Handle movie selection
  const handleMovieSelect = useCallback(
    (movieId) => {
      try {
        const movie = movieOptions.find((m) => m.id === movieId);
        if (!movie) {
          throw new Error("Película no válida seleccionada");
        }

        setError(null);
        setSelectedMovie(movieId);
        setFormData((prev) => ({ ...prev, movie: movieId }));
      } catch (err) {
        console.error("Error selecting movie:", err);
        setError(`Error al seleccionar la película: ${err.message}`);
      }
    },
    [movieOptions]
  );

  // Handle form input changes with validation
  const handleInputChange = useCallback(
    (field, value) => {
      try {
        setError(null);

        // Validate field exists
        if (!field || typeof field !== "string") {
          throw new Error("Campo inválido");
        }

        // Real-time validation for specific fields
        if (field === "contactEmail" && value) {
          if (!isValidEmail(value)) {
            setError("Por favor ingresa un email válido");
          }
        }

        if (field === "contactPhone" && value) {
          if (!isValidPhone(value)) {
            setError("Por favor ingresa un teléfono válido (mínimo 9 dígitos)");
          }
        }

        // Character limits
        const characterLimits = {
          contactName: 100,
          company: 100,
          requirements: 500,
          message: 500,
        };

        if (characterLimits[field] && value.length > characterLimits[field]) {
          setError(
            `El campo no puede exceder ${characterLimits[field]} caracteres`
          );
          return;
        }

        setFormData((prev) => ({ ...prev, [field]: value }));
      } catch (err) {
        console.error("Error updating form data:", err);
        setError(`Error al actualizar la información: ${err.message}`);
      }
    },
    [isValidEmail, isValidPhone]
  );

  // Handle continue to next step with validation
  const handleContinue = useCallback(() => {
    try {
      if (isAnimating || loading) return;

      setError(null);

      // Step-specific validation
      let canContinue = false;
      let errorMessage = "";

      switch (currentStep) {
        case 1:
          canContinue = !!selectedCinema;
          errorMessage = "Por favor selecciona un cine";
          break;
        case 2:
          canContinue = !!selectedMovie;
          errorMessage = "Por favor selecciona una película";
          break;
        case 3:
          canContinue = !!(formData.institutionType && formData.capacity);
          errorMessage =
            "Por favor completa el tipo de institución y capacidad";
          break;
        case 4:
          const hasRequiredFields = !!(
            formData.contactName &&
            formData.contactEmail &&
            formData.contactPhone
          );
          const hasValidEmail = isValidEmail(formData.contactEmail);
          const hasValidPhone = isValidPhone(formData.contactPhone);

          canContinue = hasRequiredFields && hasValidEmail && hasValidPhone;

          if (!hasRequiredFields) {
            errorMessage = "Por favor completa todos los campos requeridos";
          } else if (!hasValidEmail) {
            errorMessage = "Por favor ingresa un email válido";
          } else if (!hasValidPhone) {
            errorMessage = "Por favor ingresa un teléfono válido";
          }
          break;
        case 5:
          canContinue = true;
          break;
        default:
          canContinue = false;
          errorMessage = "Paso inválido";
      }

      if (!canContinue) {
        setError(errorMessage);
        return;
      }

      setIsAnimating(true);
      setTimeout(() => {
        setCurrentStep((prev) => Math.min(prev + 1, 5));
        setIsAnimating(false);
      }, 300);
    } catch (err) {
      console.error("Error continuing to next step:", err);
      setError(`Error al continuar: ${err.message}`);
      setIsAnimating(false);
    }
  }, [
    selectedCinema,
    selectedMovie,
    formData,
    currentStep,
    isAnimating,
    loading,
    isValidEmail,
    isValidPhone,
  ]);

  // Handle back navigation with error handling
  const handleBack = useCallback(() => {
    try {
      if (isAnimating) return;

      setIsAnimating(true);
      setError(null);

      if (currentStep > 1) {
        setTimeout(() => {
          setCurrentStep((prev) => prev - 1);
          setIsAnimating(false);
        }, 300);
      } else {
        navigate("/corporate");
      }
    } catch (err) {
      console.error("Error navigating back:", err);
      setError(`Error al regresar: ${err.message}`);
      setIsAnimating(false);
    }
  }, [isAnimating, currentStep, navigate]);

  // Handle final submission with comprehensive error handling
  const handleSubmit = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      const requiredFields = [
        "contactName",
        "contactEmail",
        "contactPhone",
        "institutionType",
        "capacity",
      ];
      const missingFields = requiredFields.filter((field) => !formData[field]);

      if (missingFields.length > 0) {
        throw new Error(
          `Campos requeridos faltantes: ${missingFields.join(", ")}`
        );
      }

      if (!isValidEmail(formData.contactEmail)) {
        throw new Error("Email inválido");
      }

      if (!isValidPhone(formData.contactPhone)) {
        throw new Error("Teléfono inválido");
      }

      if (!selectedMovie || !selectedCinema) {
        throw new Error("Selección de película o cine incompleta");
      }

      // Prepara payload
      const payload = {
        cinema: selectedCinema,
        movie: selectedMovie,
        institutionType: formData.institutionType,
        capacity: formData.capacity,
        date: formData.date,
        time: formData.time,
        duration: formData.duration,
        attendees: formData.attendees,
        requirements: formData.requirements,
        contactName: formData.contactName,
        contactEmail: formData.contactEmail,
        contactPhone: formData.contactPhone,
        company: formData.company,
        message: formData.message,
      };

      // Actual call to the backend
      const response = await fetch("http://localhost:8080/api/specialfunctions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      const result = await response.json();

      if (response.ok) {
        toast.success(result.message || "🎉 ¡Función especial enviada correctamente!");
        setTimeout(() => {
          navigate("/corporate");
        }, 3000);
      } else {
        throw new Error(result.message || "Error al enviar la solicitud");
      }
    } catch (err) {
      console.error("Error al enviar la función especial:", err);
      toast.error(err.message || "❌ Error inesperado al enviar la función especial.");
    } finally {
      setLoading(false);
    }
  }, [formData, selectedMovie, selectedCinema, isValidEmail, isValidPhone, navigate]);

  // Image error handling with fallback
  const handleImageError = useCallback((e, backgroundClass = "cinema-bg") => {
    try {
      console.warn("Image failed to load:", e.target.src);
      e.target.style.display = "none";
      if (e.target.parentElement) {
        e.target.parentElement.classList.add(backgroundClass);
      }
    } catch (err) {
      console.error("Error handling image error:", err);
    }
  }, []);

  // Keyboard navigation with error handling
  useEffect(() => {
    const handleKeyDown = (e) => {
      try {
        if (currentStep === 1 && selectedCinema) {
          const currentIndex = cinemaOptions.findIndex(
            (cinema) => cinema.id === selectedCinema
          );
          switch (e.key) {
            case "ArrowUp":
            case "ArrowLeft":
              e.preventDefault();
              if (currentIndex > 0)
                handleCinemaSelect(cinemaOptions[currentIndex - 1].id);
              break;
            case "ArrowDown":
            case "ArrowRight":
              e.preventDefault();
              if (currentIndex < cinemaOptions.length - 1)
                handleCinemaSelect(cinemaOptions[currentIndex + 1].id);
              break;
            case "Enter":
              e.preventDefault();
              if (selectedCinema) handleContinue();
              break;
            case "Escape":
              e.preventDefault();
              handleBack();
              break;
            default:
              break;
          }
        }
      } catch (err) {
        console.error("Error in keyboard navigation:", err);
      }
    };

    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [
    currentStep,
    selectedCinema,
    handleCinemaSelect,
    handleContinue,
    handleBack,
    cinemaOptions,
  ]);

  // Scroll to top on step change
  useEffect(() => {
    try {
      window.scrollTo({ top: 0, behavior: "smooth" });
    } catch (err) {
      console.error("Error scrolling to top:", err);
      // Fallback for older browsers
      window.scrollTo(0, 0);
    }
  }, [currentStep]);

  // Error boundary effect
  useEffect(() => {
    const handleError = (event) => {
      console.error("Global error caught:", event.error);
      setError("Ha ocurrido un error inesperado. Por favor recarga la página.");
    };

    const handleUnhandledRejection = (event) => {
      console.error("Unhandled promise rejection:", event.reason);
      setError("Error de conexión. Por favor verifica tu conexión a internet.");
    };

    window.addEventListener("error", handleError);
    window.addEventListener("unhandledrejection", handleUnhandledRejection);

    return () => {
      window.removeEventListener("error", handleError);
      window.removeEventListener(
        "unhandledrejection",
        handleUnhandledRejection
      );
    };
  }, []);

  // Show error state if no movies could be loaded
  if (error && movieOptions.length === 0) {
    return (
      <div className="events-container">
        <div className="events-header">
          <div className="events-header-content">
            <div className="events-icon">⚠️</div>
            <h1 className="events-title">Error</h1>
            <p className="events-subtitle">{error}</p>
          </div>
        </div>
        <div className="events-content">
          <div className="events-actions">
            <button
              className="events-btn-primary"
              onClick={() => window.location.reload()}
            >
              Recargar Página
            </button>
            <button
              className="events-btn-secondary"
              onClick={() => navigate("/corporate")}
            >
              Regresar
            </button>
          </div>
        </div>
      </div>
    );
  }

  if (moviesLoading) {
    return (
      <div className="loading-overlay">
        <div className="loading-spinner"></div>
        <p>Loading movies...</p>
      </div>
    );
  }

  return (
    <div className="events-container">
      {/* Header Section */}
      <div className="events-header">
        <div className="events-header-content">
          <div className="events-icon">🎭</div>
          <h1 className="events-title">Funciones Especiales</h1>
          <p className="events-subtitle">
            Disfruta de experiencias cinematográficas únicas y exclusivas
          </p>
        </div>
        <div className="events-header-overlay"></div>
      </div>

      {/* Error Display */}
      {error && (
        <div
          className="error-banner"
          style={{
            backgroundColor: "#fee2e2",
            border: "1px solid #fecaca",
            borderRadius: "8px",
            padding: "12px 16px",
            margin: "16px",
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
            color: "#dc2626",
          }}
        >
          <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
            <span className="error-icon">⚠️</span>
            <span className="error-message">{error}</span>
          </div>
          <button
            className="error-close"
            onClick={() => setError(null)}
            style={{
              background: "none",
              border: "none",
              fontSize: "18px",
              cursor: "pointer",
              color: "#dc2626",
            }}
          >
            ×
          </button>
        </div>
      )}

      {/* Loading Overlay */}
      {loading && (
        <div
          className="loading-overlay"
          style={{
            position: "fixed",
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: "rgba(0, 0, 0, 0.5)",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            zIndex: 9999,
            color: "white",
          }}
        >
          <div
            className="loading-spinner"
            style={{
              width: "40px",
              height: "40px",
              border: "4px solid #f3f3f3",
              borderTop: "4px solid #dc2626",
              borderRadius: "50%",
              animation: "spin 1s linear infinite",
              marginBottom: "16px",
            }}
          ></div>
          <p>Enviando solicitud...</p>
        </div>
      )}

      {/* Progress Steps */}
      <div className="events-progress">
        <div className="events-progress-container">
          {steps.map((step, index) => (
            <div
              key={step.number}
              className={`events-step ${step.active ? "active" : ""} ${currentStep === step.number ? "current" : ""
                }`}
            >
              <div className="events-step-number">{step.number}</div>
              <span className="events-step-title">{step.title}</span>
              {index < steps.length - 1 && (
                <div
                  className={`events-step-line ${step.active ? "active" : ""}`}
                ></div>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* Main Content */}
      <div className="events-content">
        {/* STEP 1: Cinema Selection */}
        {currentStep === 1 && (
          <div className="events-selection-section">
            <div className="events-selection-header">
              <h2>Selecciona el cine para tu función especial</h2>
              <p>
                Elige la sala que mejor se adapte a tus necesidades y ubicación
                preferida
              </p>
            </div>

            <div className="events-grid">
              {cinemaOptions.map((cinema) => (
                <div
                  key={cinema.id}
                  className={`events-card cinema-card ${selectedCinema === cinema.id ? "selected" : ""
                    }`}
                  onClick={() => handleCinemaSelect(cinema.id)}
                  style={{ "--card-gradient": cinema.gradient }}
                >
                  <div className="events-card-image cinema-bg">
                    <img
                      src={
                        cinema.image || "/placeholder.svg?height=200&width=300"
                      }
                      alt={`${cinema.name} - Cinema`}
                      className="events-card-img"
                      loading="lazy"
                      onError={(e) => handleImageError(e, "cinema-bg")}
                    />
                    <div className="events-card-overlay">
                      <h3>{cinema.name}</h3>
                      <p className="cinema-location">{cinema.location}</p>
                    </div>
                  </div>

                  <div className="events-card-content">
                    <div className="events-card-header">
                      <div className="events-card-icon-small">🎬</div>
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
                    <div
                      className={`events-radio ${selectedCinema === cinema.id ? "checked" : ""
                        }`}
                    >
                      <div className="events-radio-inner"></div>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>
                ← Regresar
              </button>
              <button
                className={`events-btn-primary ${!selectedCinema ? "disabled" : ""
                  }`}
                onClick={handleContinue}
                disabled={!selectedCinema}
              >
                Continuar →
              </button>
            </div>
          </div>
        )}

        {/* STEP 2: Movie Selection */}
        {currentStep === 2 && (
          <div className="events-selection-section">
            <div className="events-selection-header">
              <h2>
                Elige el contenido disponible en base a tu selección de cine
              </h2>
              <p>
                *La fecha de estreno de la película no debe exceder los 3 meses.
              </p>
              <p>
                **Validar con un ejecutivo si los Próximos Estrenos se
                proyectarán en el cine elegido.
              </p>
              <div className="cinema-info">
                <h3>
                  Cines:{" "}
                  {cinemaOptions
                    .find((c) => c.id === selectedCinema)
                    ?.name.replace("Cinerama ", "")}
                </h3>
              </div>
            </div>

            <div className="movies-carousel">
              <div className="movies-grid">
                {availableMovies.length > 0 ? (
                  availableMovies.map((movie) => (
                    <div
                      key={movie.id}
                      className={`movie-card ${selectedMovie === movie.id ? "selected" : ""
                        }`}
                      onClick={() => handleMovieSelect(movie.id)}
                      style={{ "--card-gradient": movie.gradient }}
                    >
                      <div className="movie-poster">
                        <img
                          src={
                            movie.image ||
                            "/placeholder.svg?height=400&width=300"
                          }
                          alt={`${movie.title} - Poster`}
                          className="movie-img"
                          loading="lazy"
                          onError={(e) => handleImageError(e, "movie-bg")}
                        />
                        {movie.isFanEvent && (
                          <div className="movie-badge fan-event">FAN EVENT</div>
                        )}
                        {movie.isComingSoon && (
                          <div className="movie-badge coming-soon">
                            PRÓXIMAMENTE
                          </div>
                        )}
                        <div className="movie-overlay">
                          <h4>{movie.title}</h4>
                          <p>{movie.description}</p>
                          <div className="movie-details-info">
                            <span>{movie.genre}</span>
                            <span>{movie.duration}</span>
                            <span>{movie.rating}</span>
                          </div>
                        </div>
                      </div>
                    </div>
                  ))
                ) : (
                  <div className="no-movies-message">
                    <p>
                      No hay películas disponibles para este cine en este
                      momento.
                    </p>
                  </div>
                )}
              </div>
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>
                Regresar
              </button>
              <button
                className={`events-btn-primary ${!selectedMovie ? "disabled" : ""
                  }`}
                onClick={handleContinue}
                disabled={!selectedMovie}
              >
                Continuar
              </button>
            </div>
          </div>
        )}

        {/* STEP 3: Details Form */}
        {currentStep === 3 && (
          <div className="events-form-section">
            <div className="events-selection-header">
              <h2>Detalles de la Función</h2>
              <p>Completa la información específica para tu función especial</p>
            </div>

            <div className="details-summary">
              <div className="selected-items">
                <div className="selected-item">
                  <h4>Película</h4>
                  <p>
                    {movieOptions.find((m) => m.id === selectedMovie)?.title}
                  </p>
                </div>
                <div className="selected-item">
                  <h4>Cines</h4>
                  <p>
                    {cinemaOptions
                      .find((c) => c.id === selectedCinema)
                      ?.name.replace("Cinerama ", "")}
                  </p>
                </div>
              </div>
            </div>

            <div className="events-form">
              <div className="form-row">
                <div className="form-group">
                  <label>Tipo de Institución: *</label>
                  <select
                    value={formData.institutionType}
                    onChange={(e) =>
                      handleInputChange("institutionType", e.target.value)
                    }
                    required
                  >
                    <option value="">Selecciona una opción</option>
                    {institutionTypes.map((type) => (
                      <option key={type.value} value={type.value}>
                        {type.label}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label>Capacidad: *</label>
                  <select
                    value={formData.capacity}
                    onChange={(e) =>
                      handleInputChange("capacity", e.target.value)
                    }
                    required
                  >
                    <option value="">Selecciona capacidad</option>
                    {capacityOptions.map((capacity) => (
                      <option key={capacity.value} value={capacity.value}>
                        {capacity.label}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Fecha Preferida</label>
                  <input
                    type="date"
                    value={formData.date}
                    onChange={(e) => handleInputChange("date", e.target.value)}
                    min={new Date().toISOString().split("T")[0]}
                  />
                </div>
                <div className="form-group">
                  <label>Hora Preferida</label>
                  <input
                    type="time"
                    value={formData.time}
                    onChange={(e) => handleInputChange("time", e.target.value)}
                  />
                </div>
              </div>

              <div className="form-group">
                <label>Requerimientos Especiales</label>
                <textarea
                  placeholder="Describe cualquier requerimiento especial para tu función..."
                  value={formData.requirements}
                  onChange={(e) =>
                    handleInputChange("requirements", e.target.value)
                  }
                  rows="4"
                  maxLength="500"
                ></textarea>
                <small className="char-count">
                  {formData.requirements.length}/500 caracteres
                </small>
              </div>
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>
                ← Regresar
              </button>
              <button
                className={`events-btn-primary ${!formData.institutionType || !formData.capacity
                  ? "disabled"
                  : ""
                  }`}
                onClick={handleContinue}
                disabled={!formData.institutionType || !formData.capacity}
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
              <p>
                Completa tus datos para que podamos contactarte y confirmar tu
                función especial
              </p>
            </div>

            <div className="events-form">
              <div className="form-row">
                <div className="form-group">
                  <label>Nombre Completo *</label>
                  <input
                    type="text"
                    placeholder="Tu nombre completo"
                    value={formData.contactName}
                    onChange={(e) =>
                      handleInputChange("contactName", e.target.value)
                    }
                    required
                    maxLength="100"
                  />
                </div>
                <div className="form-group">
                  <label>Empresa/Institución</label>
                  <input
                    type="text"
                    placeholder="Nombre de tu empresa o institución"
                    value={formData.company}
                    onChange={(e) =>
                      handleInputChange("company", e.target.value)
                    }
                    maxLength="100"
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
                    onChange={(e) =>
                      handleInputChange("contactEmail", e.target.value)
                    }
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Teléfono *</label>
                  <input
                    type="tel"
                    placeholder="+51 999 999 999"
                    value={formData.contactPhone}
                    onChange={(e) =>
                      handleInputChange("contactPhone", e.target.value)
                    }
                    required
                  />
                </div>
              </div>

              <div className="form-group">
                <label>Mensaje Adicional</label>
                <textarea
                  placeholder="Cuéntanos más sobre tu función especial o cualquier pregunta específica..."
                  value={formData.message}
                  onChange={(e) => handleInputChange("message", e.target.value)}
                  rows="4"
                  maxLength="500"
                ></textarea>
                <small className="char-count">
                  {formData.message.length}/500 caracteres
                </small>
              </div>
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>
                ← Regresar
              </button>
              <button
                className={`events-btn-primary ${!formData.contactName ||
                  !formData.contactEmail ||
                  !formData.contactPhone
                  ? "disabled"
                  : ""
                  }`}
                onClick={handleContinue}
                disabled={
                  !formData.contactName ||
                  !formData.contactEmail ||
                  !formData.contactPhone
                }
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
              <h2>Resumen de la Función Especial</h2>
              <p>Revisa todos los detalles antes de enviar tu solicitud</p>
            </div>

            <div className="summary-card">
              <div className="summary-section">
                <h3>🎬 Cine Seleccionado</h3>
                <p>
                  {cinemaOptions.find((c) => c.id === selectedCinema)?.name}
                </p>
                <span className="summary-description">
                  {
                    cinemaOptions.find((c) => c.id === selectedCinema)
                      ?.description
                  }
                </span>
              </div>

              <div className="summary-section">
                <h3>🎭 Película Seleccionada</h3>
                <p>{movieOptions.find((m) => m.id === selectedMovie)?.title}</p>
                <span className="summary-description">
                  {
                    movieOptions.find((m) => m.id === selectedMovie)
                      ?.description
                  }
                </span>
                <div className="movie-summary-details">
                  <span>
                    <strong>Género:</strong>{" "}
                    {movieOptions.find((m) => m.id === selectedMovie)?.genre}
                  </span>
                  <span>
                    <strong>Duración:</strong>{" "}
                    {movieOptions.find((m) => m.id === selectedMovie)?.duration}
                  </span>
                </div>
              </div>

              <div className="summary-section">
                <h3>📋 Detalles de la Función</h3>
                <div className="summary-details">
                  <p>
                    <strong>Tipo de Institución:</strong>{" "}
                    {
                      institutionTypes.find(
                        (t) => t.value === formData.institutionType
                      )?.label
                    }
                  </p>
                  <p>
                    <strong>Capacidad:</strong>{" "}
                    {
                      capacityOptions.find((c) => c.value === formData.capacity)
                        ?.label
                    }
                  </p>
                  {formData.date && (
                    <p>
                      <strong>Fecha:</strong>{" "}
                      {new Date(formData.date).toLocaleDateString("es-ES")}
                    </p>
                  )}
                  {formData.time && (
                    <p>
                      <strong>Hora:</strong> {formData.time}
                    </p>
                  )}
                  {formData.requirements && (
                    <p>
                      <strong>Requerimientos:</strong> {formData.requirements}
                    </p>
                  )}
                </div>
              </div>

              <div className="summary-section">
                <h3>👤 Información de Contacto</h3>
                <div className="summary-details">
                  <p>
                    <strong>Nombre:</strong> {formData.contactName}
                  </p>
                  {formData.company && (
                    <p>
                      <strong>Empresa/Institución:</strong> {formData.company}
                    </p>
                  )}
                  <p>
                    <strong>Email:</strong> {formData.contactEmail}
                  </p>
                  <p>
                    <strong>Teléfono:</strong> {formData.contactPhone}
                  </p>
                  {formData.message && (
                    <p>
                      <strong>Mensaje:</strong> {formData.message}
                    </p>
                  )}
                </div>
              </div>
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>
                ← Regresar
              </button>
              <button
                className="events-btn-primary"
                onClick={handleSubmit}
                disabled={loading}
              >
                {loading ? "Enviando..." : "Enviar Solicitud ✨"}
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

export default SpecialFunctions;
