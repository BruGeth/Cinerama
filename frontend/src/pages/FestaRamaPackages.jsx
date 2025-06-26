import { useState, useEffect, useCallback, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import { useMovies } from "../hooks/useMovies"; // Use custom hook to fetch movies from API
import "../styles/FestaRamaPackages.css";
import confetti from "canvas-confetti";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";


const FestaRamaPackages = () => {
  const navigate = useNavigate();
  const [fadeIn, setFadeIn] = useState(false);
  const [selectedPackage, setSelectedPackage] = useState(null);
  const [selectedCinema, setSelectedCinema] = useState("");
  const [selectedMovie, setSelectedMovie] = useState("");
  const [currentStep, setCurrentStep] = useState(1);
  const [isAnimating, setIsAnimating] = useState(false);
  const [error, setError] = useState(null);
  const [loading] = useState(false);
  const [formData, setFormData] = useState({
    package: "",
    cinema: "",
    movie: "",
    eventDate: "",
    eventTime: "",
    numberOfKids: "",
    specialRequests: "",
    parentName: "",
    parentEmail: "",
    parentPhone: "",
    childName: "",
    childAge: "",
    message: "",
  });

  // Fetch movies from API using custom hook
  const { movies, loading: moviesLoading } = useMovies();

  // Packages data
  const packages = [
    {
      id: "basic",
      name: "Paquete Básico",
      price: 500,
      features: [
        "Sala privada por 3 horas",
        "Película a elección",
        "Palomitas y refrescos para 15 niños",
        "Invitaciones digitales",
        "Decoración básica",
      ],
      maxKids: 15,
      gradient: "linear-gradient(135deg, #6c757d, #495057)",
    },
    {
      id: "premium",
      name: "Paquete Premium",
      price: 1000,
      features: [
        "Sala privada por 4 horas",
        "Película a elección",
        "Palomitas y refrescos para 30 niños",
        "Pastel temático",
        "Decoración temática completa",
        "Animador por 2 horas",
        "Invitaciones físicas y digitales",
      ],
      maxKids: 30,
      gradient: "linear-gradient(135deg, #dc3545, #b02a37)",
      popular: true,
    },
    {
      id: "deluxe",
      name: "Paquete Deluxe",
      price: 1500,
      features: [
        "Sala VIP privada por 5 horas",
        "Película a elección",
        "Menú completo para 50 niños",
        "Pastel temático personalizado",
        "Decoración premium",
        "2 Animadores por 3 horas",
        "Sesión de fotos profesional",
        "Recuerdos para invitados",
      ],
      maxKids: 50,
      gradient: "linear-gradient(135deg, #ffc107, #cc9a06)",
    },
  ];

  // Cinema options
  const cinemaOptions = useMemo(
    () => [
      {
        id: "cinema-miraflores",
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
        id: "cinema-minka",
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
      Infantil: "linear-gradient(135deg, #f59e0b 0%, #ef4444 100%)",
      Familiar: "linear-gradient(135deg, #10b981 0%, #3b82f6 100%)",
    };
    return (
      gradients[genre] || "linear-gradient(135deg, #6b7280 0%, #9ca3af 100%)"
    );
  }, []);

  // Filter and transform movies from API - Only the 3 specific movies
  const movieOptions = useMemo(() => {
    try {
      if (!movies || !Array.isArray(movies)) {
        throw new Error("No se pudieron cargar los datos de películas");
      }
      // Filter only the 3 specific movies requested
      const targetMovies = [
        "Destino Final: Lazos de Sangre",
        "Star Wars: Episodio III - La venganza de los Sith",
        "Thunderbolts",
      ];
      const filteredMovies = movies
        .filter((movie) => {
          if (!movie || !movie.title) return false;
          return targetMovies.includes(movie.title);
        })
        .map((movie) => ({
          id: movie.id ? movie.id.toString() : Math.random().toString(),
          title: movie.title || "Título no disponible",
          description: movie.descriptionMovie || "Descripción no disponible",
          image: movie.imageUrl || "/placeholder.svg?height=400&width=300",
          genre: movie.genreName || "Sin género",
          duration: `${movie.duration || 120} min`,
          rating: movie.rating || "PG-13",
          availableCinemas: ["cinema-miraflores", "cinema-minka"],
          gradient: getMovieGradient(movie.genreName || "Drama"),
          showtimes: movie.showtimes || [],
          originalData: movie,
        }))
        .filter(Boolean);

      if (filteredMovies.length === 0) {
        throw new Error(
          "No se encontraron las películas especificadas en la cartelera"
        );
      }
      return filteredMovies;
    } catch (err) {
      setError(`Error al cargar las películas: ${err.message}`);
      return [];
    }
  }, [movies, getMovieGradient]);

  const steps = [
    { number: 1, title: "Paquete", active: currentStep >= 1 },
    { number: 2, title: "Cines", active: currentStep >= 2 },
    { number: 3, title: "Película", active: currentStep >= 3 },
    { number: 4, title: "Contacto", active: currentStep >= 4 },
    { number: 5, title: "Resumen", active: currentStep >= 5 },
  ];

  // Get available movies based on selected cinema
  const availableMovies = useMemo(() => {
    try {
      if (!selectedCinema) return movieOptions;
      return movieOptions.filter((movie) => {
        return (
          movie.availableCinemas &&
          movie.availableCinemas.includes(selectedCinema)
        );
      });
    } catch (err) {
      console.error("Error filtering movies:", err);
      return movieOptions;
    }
  }, [selectedCinema, movieOptions]);

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

  useEffect(() => {
    setTimeout(() => setFadeIn(true), 100);
  }, []);

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
        navigate("/festarama");
      }
    } catch (err) {
      console.error("Error navigating back:", err);
      setError(`Error al regresar: ${err.message}`);
      setIsAnimating(false);
    }
  }, [isAnimating, currentStep, navigate]);

  const handlePackageSelect = useCallback(
    (packageData) => {
      try {
        if (isAnimating) return;

        setIsAnimating(true);
        setError(null);
        setSelectedPackage(packageData);
        setFormData((prev) => ({ ...prev, package: packageData.id }));

        // Trigger confetti effect
        const duration = 3 * 1000;
        const animationEnd = Date.now() + duration;

        const randomInRange = (min, max) => {
          return Math.random() * (max - min) + min;
        };

        const interval = setInterval(() => {
          const timeLeft = animationEnd - Date.now();

          if (timeLeft <= 0) {
            return clearInterval(interval);
          }

          const particleCount = 50 * (timeLeft / duration);

          // Fire from the left
          if (typeof confetti !== "undefined") {
            confetti({
              particleCount,
              startVelocity: 30,
              spread: 80,
              origin: {
                x: randomInRange(0.1, 0.3),
                y: Math.random() - 0.2,
              },
            });

            // Fire from the right
            confetti({
              particleCount,
              startVelocity: 30,
              spread: 80,
              origin: {
                x: randomInRange(0.7, 0.9),
                y: Math.random() - 0.2,
              },
            });
          }
        }, 250);

        setTimeout(() => {
          setCurrentStep(2);
          setIsAnimating(false);
        }, 1000);
      } catch (err) {
        console.error("Error selecting package:", err);
        setError(`Error al seleccionar el paquete: ${err.message}`);
        setIsAnimating(false);
      }
    },
    [isAnimating]
  );

  const handleCinemaSelect = useCallback(
    (cinemaId) => {
      try {
        if (isAnimating) return;

        const cinema = cinemaOptions.find((c) => c.id === cinemaId);
        if (!cinema) {
          throw new Error("Cine no válido seleccionado");
        }

        setIsAnimating(true);
        setError(null);
        setSelectedCinema(cinemaId);
        setSelectedMovie("");
        setFormData((prev) => ({ ...prev, cinema: cinemaId, movie: "" }));

        setTimeout(() => setIsAnimating(false), 300);
      } catch (err) {
        console.error("Error selecting cinema:", err);
        setError(`Error al seleccionar el cine: ${err.message}`);
        setIsAnimating(false);
      }
    },
    [isAnimating, cinemaOptions]
  );

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

  const handleInputChange = useCallback(
    (field, value) => {
      try {
        setError(null);

        if (!field || typeof field !== "string") {
          throw new Error("Campo inválido");
        }

        // Real-time validation for specific fields
        if (field === "parentEmail" && value) {
          if (!isValidEmail(value)) {
            setError("Por favor ingresa un email válido");
          }
        }

        if (field === "parentPhone" && value) {
          if (!isValidPhone(value)) {
            setError("Por favor ingresa un teléfono válido (mínimo 9 dígitos)");
          }
        }

        // Character limits
        const characterLimits = {
          parentName: 100,
          childName: 100,
          specialRequests: 500,
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

  const handleContinue = useCallback(() => {
    try {
      if (isAnimating || loading) return;

      setError(null);

      let canContinue = false;
      let errorMessage = "";

      switch (currentStep) {
        case 1:
          canContinue = !!selectedPackage;
          errorMessage = "Por favor selecciona un paquete";
          break;
        case 2:
          canContinue = !!selectedCinema;
          errorMessage = "Por favor selecciona un cine";
          break;
        case 3:
          canContinue = !!selectedMovie;
          errorMessage = "Por favor selecciona una película";
          break;
        case 4:
          const hasRequiredFields = !!(
            formData.parentName &&
            formData.parentEmail &&
            formData.parentPhone &&
            formData.childName
          );
          const hasValidEmail = isValidEmail(formData.parentEmail);
          const hasValidPhone = isValidPhone(formData.parentPhone);

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
    selectedPackage,
    selectedCinema,
    selectedMovie,
    formData,
    currentStep,
    isAnimating,
    loading,
    isValidEmail,
    isValidPhone,
  ]);

  const handleSubmit = useCallback(async () => {
    try {
    const payload = {
      packageType: selectedPackage?.id,
      cinema: selectedCinema,
      movie: selectedMovie,
      date: formData.eventDate,
      time: formData.eventTime,
      attendees: formData.numberOfKids,
      birthdayChildName: formData.childName,
      birthdayAge: formData.childAge,
      contactName: formData.parentName,
      contactEmail: formData.parentEmail,
      contactPhone: formData.parentPhone,
      message: formData.message || "", // optional
    };

    const response = await fetch("http://localhost:8080/api/festarama", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    const result = await response.json();
    toast.success(result.message);

    if (response.ok) {
      toast.success(result.message || "🎉 ¡Solicitud enviada exitosamente!");
      navigate("/corporate");
    } else {
      toast.error(result.message || "❌ Ocurrió un error en el servidor.");
    }
  } catch (error) {
    console.error(error);
    toast.error("❌ No se pudo conectar con el servidor.");
  }
}, [formData, selectedPackage, selectedCinema, selectedMovie, navigate]);

  // Image error handling
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

  // Scroll to top on step change
  useEffect(() => {
    try {
      window.scrollTo({ top: 0, behavior: "smooth" });
    } catch (err) {
      console.error("Error scrolling to top:", err);
      window.scrollTo(0, 0);
    }
  }, [currentStep]);

  if (moviesLoading) {
    return (
      <div className="loading-overlay">
        <div className="loading-spinner"></div>
        <p>Loading movies...</p>
      </div>
    );
  }

  return (
    <div className={`events-container ${fadeIn ? "fade-in" : ""}`}>
      {/* Header Section */}
      <div className="events-header">
        <div className="events-header-content">
          <div className="events-icon">🎉</div>
          <h1 className="events-title">FestaRama - Fiestas Infantiles</h1>
          <p className="events-subtitle">
            Crea la fiesta perfecta para tu hijos con nuestros paquetes
            especiales
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
          <p>Enviando reserva...</p>
        </div>
      )}

      {/* Progress Steps */}
      <div className="events-progress">
        <div className="events-progress-container">
          {steps.map((step, index) => (
            <div
              key={step.number}
              className={`events-step ${step.active ? "active" : ""} ${
                currentStep === step.number ? "current" : ""
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
        {/* STEP 1: Package Selection */}
        {currentStep === 1 && (
          <div className="events-selection-section">
            <div className="events-selection-header">
              <h2>Elige el paquete perfecto para la celebración</h2>
              <p>
                Selecciona el paquete que mejor se adapte a tus necesidades y
                presupuesto
              </p>
            </div>

            <div className="events-grid">
              {packages.map((pkg) => (
                <div
                  key={pkg.id}
                  className={`package-card ${pkg.id} ${
                    selectedPackage?.id === pkg.id ? "selected" : ""
                  }`}
                  onClick={() => handlePackageSelect(pkg)}
                  style={{ "--card-gradient": pkg.gradient }}
                >
                  <div className="package-header">
                    {pkg.popular && (
                      <div className="ribbon-popular">
                        <span className="ribbon-text">¡MÁS VENDIDO!</span>
                        <div className="ribbon-shine"></div>
                      </div>
                    )}
                    <h2>{pkg.name}</h2>
                    <div className="price-tag">S/. {pkg.price}</div>
                  </div>
                  <div className="package-body">
                    <ul className="package-features">
                      {pkg.features.map((feature, index) => (
                        <li key={index}>
                          <span className="check-icon">✓</span> {feature}
                        </li>
                      ))}
                    </ul>
                  </div>
                  <div className="package-footer">
                    <div className="package-capacity">
                      <span>👥 Hasta {pkg.maxKids} niños</span>
                    </div>
                    {selectedPackage?.id === pkg.id && (
                      <div className="package-selected-indicator">
                        <span>✓ Seleccionado</span>
                      </div>
                    )}
                  </div>
                </div>
              ))}
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>
                ← Regresar
              </button>
              <button
                className={`events-btn-primary ${
                  !selectedPackage ? "disabled" : ""
                }`}
                onClick={handleContinue}
                disabled={!selectedPackage}
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
              <h2>Selecciona el cine para tu FestaRama</h2>
              <p>Elige la ubicación que mejor te convenga para la fiesta</p>
              <div className="selected-package-info">
                <h3>Paquete seleccionado: {selectedPackage?.name}</h3>
                <p>Precio: S/. {selectedPackage?.price}</p>
              </div>
            </div>

            <div className="events-grid">
              {cinemaOptions.map((cinema) => (
                <div
                  key={cinema.id}
                  className={`events-card cinema-card ${
                    selectedCinema === cinema.id ? "selected" : ""
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
                      className="packages-card-img"
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
                      <div className="packages-card-icon-small">🎬</div>
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
                      className={`packages-radio ${
                        selectedCinema === cinema.id ? "checked" : ""
                      }`}
                    >
                      <div className="packages-radio-inner"></div>
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
                className={`events-btn-primary ${
                  !selectedCinema ? "disabled" : ""
                }`}
                onClick={handleContinue}
                disabled={!selectedCinema}
              >
                Continuar →
              </button>
            </div>
          </div>
        )}

        {/* STEP 3: Movie Selection */}
        {currentStep === 3 && (
          <div className="events-selection-section">
            <div className="events-selection-header">
              <h2>Elige la película para la fiesta</h2>
              <p>
                Selecciona una película apropiada para niños que todos puedan
                disfrutar
              </p>
              <div className="cinema-info">
                <h3>
                  Cine:{" "}
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
                      className={`movie-card ${
                        selectedMovie === movie.id ? "selected" : ""
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
                      No hay películas familiares disponibles para este cine en
                      este momento.
                    </p>
                  </div>
                )}
              </div>
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>
                ← Regresar
              </button>
              <button
                className={`events-btn-primary ${
                  !selectedMovie ? "disabled" : ""
                }`}
                onClick={handleContinue}
                disabled={!selectedMovie}
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
                Completa los datos para confirmar la reserva de la FestaRama
              </p>
            </div>

            <div className="events-form">
              <div className="form-row">
                <div className="form-group">
                  <label>Nombre del Padre/Madre *</label>
                  <input
                    type="text"
                    placeholder="Tu nombre completo"
                    value={formData.parentName}
                    onChange={(e) =>
                      handleInputChange("parentName", e.target.value)
                    }
                    required
                    maxLength="100"
                  />
                </div>
                <div className="form-group">
                  <label>Nombre del Niño/a *</label>
                  <input
                    type="text"
                    placeholder="Nombre del cumpleañero/a"
                    value={formData.childName}
                    onChange={(e) =>
                      handleInputChange("childName", e.target.value)
                    }
                    required
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
                    value={formData.parentEmail}
                    onChange={(e) =>
                      handleInputChange("parentEmail", e.target.value)
                    }
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Teléfono *</label>
                  <input
                    type="tel"
                    placeholder="+51 999 999 999"
                    value={formData.parentPhone}
                    onChange={(e) =>
                      handleInputChange("parentPhone", e.target.value)
                    }
                    required
                  />
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Edad del Niño/a</label>
                  <select
                    value={formData.childAge}
                    onChange={(e) =>
                      handleInputChange("childAge", e.target.value)
                    }
                  >
                    <option value="">Selecciona la edad</option>
                    {Array.from({ length: 13 }, (_, i) => i + 3).map((age) => (
                      <option key={age} value={age}>
                        {age} años
                      </option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label>Número de Niños</label>
                  <select
                    value={formData.numberOfKids}
                    onChange={(e) =>
                      handleInputChange("numberOfKids", e.target.value)
                    }
                  >
                    <option value="">Selecciona cantidad</option>
                    {Array.from(
                      { length: selectedPackage?.maxKids || 15 },
                      (_, i) => i + 1
                    ).map((num) => (
                      <option key={num} value={num}>
                        {num} niños
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
                    value={formData.eventDate}
                    onChange={(e) =>
                      handleInputChange("eventDate", e.target.value)
                    }
                    min={new Date().toISOString().split("T")[0]}
                  />
                </div>
                <div className="form-group">
                  <label>Hora Preferida</label>
                  <input
                    type="time"
                    value={formData.eventTime}
                    onChange={(e) =>
                      handleInputChange("eventTime", e.target.value)
                    }
                  />
                </div>
              </div>

              <div className="form-group">
                <label>Solicitudes Especiales</label>
                <textarea
                  placeholder="Describe cualquier solicitud especial para la fiesta (decoración temática, alergias alimentarias, etc.)"
                  value={formData.specialRequests}
                  onChange={(e) =>
                    handleInputChange("specialRequests", e.target.value)
                  }
                  rows="4"
                  maxLength="500"
                ></textarea>
                <small className="char-count">
                  {formData.specialRequests.length}/500 caracteres
                </small>
              </div>
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>
                ← Regresar
              </button>
              <button
                className={`events-btn-primary ${
                  !formData.parentName ||
                  !formData.parentEmail ||
                  !formData.parentPhone ||
                  !formData.childName
                    ? "disabled"
                    : ""
                }`}
                onClick={handleContinue}
                disabled={
                  !formData.parentName ||
                  !formData.parentEmail ||
                  !formData.parentPhone ||
                  !formData.childName
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
              <h2>Resumen de tu FestaRama</h2>
              <p>Revisa todos los detalles antes de confirmar tu reserva</p>
            </div>

            <div className="summary-card">
              <div className="summary-section">
                <h3>🎉 Paquete Seleccionado</h3>
                <p>{selectedPackage?.name}</p>
                <span className="summary-price">
                  S/. {selectedPackage?.price}
                </span>
                <div className="package-summary-features">
                  {selectedPackage?.features.map((feature, index) => (
                    <span key={index} className="feature-item">
                      ✓ {feature}
                    </span>
                  ))}
                </div>
              </div>

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
                <h3>👤 Información de Contacto</h3>
                <div className="summary-details">
                  <p>
                    <strong>Padre/Madre:</strong> {formData.parentName}
                  </p>
                  <p>
                    <strong>Niño/a:</strong> {formData.childName}
                    {formData.childAge && ` (${formData.childAge} años)`}
                  </p>
                  <p>
                    <strong>Email:</strong> {formData.parentEmail}
                  </p>
                  <p>
                    <strong>Teléfono:</strong> {formData.parentPhone}
                  </p>
                  {formData.numberOfKids && (
                    <p>
                      <strong>Número de niños:</strong> {formData.numberOfKids}
                    </p>
                  )}
                  {formData.eventDate && (
                    <p>
                      <strong>Fecha:</strong>{" "}
                      {new Date(formData.eventDate).toLocaleDateString("es-ES")}
                    </p>
                  )}
                  {formData.eventTime && (
                    <p>
                      <strong>Hora:</strong> {formData.eventTime}
                    </p>
                  )}
                  {formData.specialRequests && (
                    <p>
                      <strong>Solicitudes especiales:</strong>{" "}
                      {formData.specialRequests}
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
                {loading ? "Enviando..." : "Confirmar Reserva 🎉"}
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

      {/* Add CSS for loading animation */}
      <style jsx>{`
        @keyframes spin {
          0% {
            transform: rotate(0deg);
          }
          100% {
            transform: rotate(360deg);
          }
        }
      `}</style>
    </div>
    
  );
};

export default FestaRamaPackages;
