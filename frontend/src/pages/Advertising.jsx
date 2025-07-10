import { useState, useEffect, useCallback, useMemo } from "react"
import { useNavigate } from "react-router-dom"
import "../styles/SpecialFunctions.css"
import "../styles/Advertising.css"
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const Advertising = () => {
  const navigate = useNavigate()
  const [selectedCinema, setSelectedCinema] = useState("")
  const [selectedAdvertising, setSelectedAdvertising] = useState("")
  const [currentStep, setCurrentStep] = useState(1)
  const [isAnimating, setIsAnimating] = useState(false)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)
  const [formData, setFormData] = useState({
    cinema: "",
    advertisingType: "",
    category: "",
    duration: "5",
    startDate: "",
    endDate: "",
    budget: "",
    requirements: "",
    contactName: "",
    contactEmail: "",
    contactPhone: "",
    company: "",
    message: "",
  })

  // Cinema options - same as original
  const cinemaOptions = useMemo(
    () => [
      {
        id: "Cinema-Miraflores",
        name: "Cinerama Miraflores",
        description: "Cinema de lujo en el corazón de Miraflores con tecnología de vanguardia y servicios premium",
        capacity: "180 personas",
        features: ["4K Projection", "2D", "3D", "XD", "IMAX"],
        image: "https://www.cinerama.com.pe/_admin/assets/images/cines/pacifico.jpg",
        gradient: "linear-gradient(135deg, #dc2626 0%, #fbbf24 100%)",
        location: "Miraflores",
      },
      {
        id: "Cinema-Minka",
        name: "Cinerama Minka",
        description: "Moderno complejo cinematográfico en Minka con amplias instalaciones y tecnología avanzada",
        capacity: "220 personas",
        features: ["HD Projection", "3D", "XD", "IMAX"],
        image: "https://www.cinerama.com.pe/_admin/assets/images/cines/minka.jpg",
        gradient: "linear-gradient(135deg, #ef4444 0%, #fcd34d 100%)",
        location: "Callao",
      },

    ],
    [],
  )

  // Advertising options based on the images
  const advertisingOptions = useMemo(
    () => [
      {
        id: "Pantalla",
        title: " 🎬 Publicidad en Pantalla",
        description: "Anuncios proyectados en pantalla grande antes de las funciones",
        image: 'images/PublicidadPantalla.jpg',
        gradient: "linear-gradient(135deg, #dc2626 0%, #fbbf24 100%)",
        availableCinemas: ["Cinema-Miraflores", "Cinema-Minka"],
      },
      {
        id: "Lobby",
        title: " 🏢 Publicidad en Lobby",
        description: "Espacios publicitarios en las áreas comunes del cinema",
        image: 'images/PublicidadLobby.jpg',
        gradient: "linear-gradient(135deg, #1e40af 0%, #3b82f6 100%)",
        availableCinemas: ["Cinema-Miraflores", "Cinema-Minka"],
      },
      {
        id: "Digital",
        title: " 💻 Publicidad Digital",
        description: "Campañas digitales en plataformas online y redes sociales",
        image: 'images/PublicidadDigital.png',
        gradient: "linear-gradient(135deg, #7c3aed 0%, #a855f7 100%)",
        availableCinemas: ["Cinema-Miraflores", "Cinema-Minka"],
      },
    ],
    [],
  )

  // Category options for advertising
  const categoryOptions = [
    { value: "Pagina Web", label: "Página Web" },
    { value: "Blog", label: "Blog" },
    { value: "Boleteria Express", label: "Boletería Express" },
    { value: "Aplicativo Movil", label: "Aplicativo Móvil" },
  ]

  // Budget ranges
  const budgetOptions = [
    { value: "1000-5000", label: "S/ 1,000 - S/ 5,000" },
    { value: "5000-10000", label: "S/ 5,000 - S/ 10,000" },
    { value: "10000-20000", label: "S/ 10,000 - S/ 20,000" },
    { value: "20000-50000", label: "S/ 20,000 - S/ 50,000" },
    { value: "50000+", label: "Más de S/ 50,000" },
  ]

  const steps = [
    { number: 1, title: "Cines", active: currentStep >= 1 },
    { number: 2, title: "Publicidad", active: currentStep >= 2 },
    { number: 3, title: "Detalles", active: currentStep >= 3 },
    { number: 4, title: "Contacto", active: currentStep >= 4 },
    { number: 5, title: "Resumen", active: currentStep >= 5 },
  ]

  // Get available advertising options based on selected cinema
  const availableAdvertising = useMemo(() => {
    try {
      if (!selectedCinema) return advertisingOptions
      return advertisingOptions.filter((ad) => {
        return ad.availableCinemas && ad.availableCinemas.includes(selectedCinema)
      })
    } catch (err) {
      console.error("Error filtering advertising:", err)
      return advertisingOptions
    }
  }, [selectedCinema, advertisingOptions])

  // Email validation helper
  const isValidEmail = useCallback((email) => {
    try {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      return emailRegex.test(email)
    } catch (err) {
      console.error("Error validating email:", err)
      return false
    }
  }, [])

  // Phone validation helper
  const isValidPhone = useCallback((phone) => {
    try {
      const phoneRegex = /^[+]?[0-9\s\-()]{9,}$/
      return phoneRegex.test(phone)
    } catch (err) {
      console.error("Error validating phone:", err)
      return false
    }
  }, [])

  // Handle cinema selection
  const handleCinemaSelect = useCallback(
    (cinemaId) => {
      try {
        if (isAnimating) return

        const cinema = cinemaOptions.find((c) => c.id === cinemaId)
        if (!cinema) {
          throw new Error("Cine no válido seleccionado")
        }

        setIsAnimating(true)
        setError(null)
        setSelectedCinema(cinemaId)
        setSelectedAdvertising("")
        setFormData((prev) => ({ ...prev, cinema: cinemaId, advertisingType: "" }))

        if (navigator.vibrate) {
          navigator.vibrate(50)
        }

        setTimeout(() => setIsAnimating(false), 300)
      } catch (err) {
        console.error("Error selecting cinema:", err)
        setError(`Error al seleccionar el cine: ${err.message}`)
        setIsAnimating(false)
      }
    },
    [isAnimating, cinemaOptions],
  )

  // Handle advertising selection
  const handleAdvertisingSelect = useCallback(
    (advertisingId) => {
      try {
        const advertising = advertisingOptions.find((a) => a.id === advertisingId)
        if (!advertising) {
          throw new Error("Tipo de publicidad no válido")
        }

        setError(null)
        setSelectedAdvertising(advertisingId)
        setFormData((prev) => ({ ...prev, advertisingType: advertisingId }))

        if (navigator.vibrate) {
          navigator.vibrate(50)
        }
      } catch (err) {
        console.error("Error selecting advertising:", err)
        setError(`Error al seleccionar el tipo de publicidad: ${err.message}`)
      }
    },
    [advertisingOptions],
  )

  // Handle form input changes
  const handleInputChange = useCallback(
    (field, value) => {
      try {
        setError(null)

        if (!field || typeof field !== "string") {
          throw new Error("Campo inválido")
        }

        if (field === "contactEmail" && value) {
          if (!isValidEmail(value)) {
            setError("Por favor ingresa un email válido")
          }
        }

        if (field === "contactPhone" && value) {
          if (!isValidPhone(value)) {
            setError("Por favor ingresa un teléfono válido (mínimo 9 dígitos)")
          }
        }

        const characterLimits = {
          contactName: 100,
          company: 100,
          requirements: 500,
          message: 500,
        }

        if (characterLimits[field] && value.length > characterLimits[field]) {
          setError(`El campo no puede exceder ${characterLimits[field]} caracteres`)
          return
        }

        setFormData((prev) => ({ ...prev, [field]: value }))
      } catch (err) {
        console.error("Error updating form data:", err)
        setError(`Error al actualizar la información: ${err.message}`)
      }
    },
    [isValidEmail, isValidPhone],
  )

  // Handle continue to next step
  const handleContinue = useCallback(() => {
    try {
      if (isAnimating || loading) return

      setError(null)

      let canContinue = false
      let errorMessage = ""

      switch (currentStep) {
        case 1:
          canContinue = !!selectedCinema
          errorMessage = "Por favor selecciona un cine"
          break
        case 2:
          canContinue = !!selectedAdvertising
          errorMessage = "Por favor selecciona un tipo de publicidad"
          break
        case 3:
          canContinue = !!(formData.category && formData.duration)
          errorMessage = "Por favor completa la categoría y duración"
          break
        case 4:
          const hasRequiredFields = !!(formData.contactName && formData.contactEmail && formData.contactPhone)
          const hasValidEmail = isValidEmail(formData.contactEmail)
          const hasValidPhone = isValidPhone(formData.contactPhone)

          canContinue = hasRequiredFields && hasValidEmail && hasValidPhone

          if (!hasRequiredFields) {
            errorMessage = "Por favor completa todos los campos requeridos"
          } else if (!hasValidEmail) {
            errorMessage = "Por favor ingresa un email válido"
          } else if (!hasValidPhone) {
            errorMessage = "Por favor ingresa un teléfono válido"
          }
          break
        case 5:
          canContinue = true
          break
        default:
          canContinue = false
          errorMessage = "Paso inválido"
      }

      if (!canContinue) {
        setError(errorMessage)
        return
      }

      setIsAnimating(true)
      setTimeout(() => {
        setCurrentStep((prev) => Math.min(prev + 1, 5))
        setIsAnimating(false)
      }, 300)
    } catch (err) {
      console.error("Error continuing to next step:", err)
      setError(`Error al continuar: ${err.message}`)
      setIsAnimating(false)
    }
  }, [selectedCinema, selectedAdvertising, formData, currentStep, isAnimating, loading, isValidEmail, isValidPhone])

  // Handle back navigation
  const handleBack = useCallback(() => {
    try {
      if (isAnimating) return

      setIsAnimating(true)
      setError(null)

      if (currentStep > 1) {
        setTimeout(() => {
          setCurrentStep((prev) => prev - 1)
          setIsAnimating(false)
        }, 300)
      } else {
        navigate("/corporate")
      }
    } catch (err) {
      console.error("Error navigating back:", err)
      setError(`Error al regresar: ${err.message}`)
      setIsAnimating(false)
    }
  }, [isAnimating, currentStep, navigate])

  // Handle final submission
  const handleSubmit = useCallback(async () => {
    try {
      setLoading(true)
      setError(null)

      const requiredFields = ["contactName", "contactEmail", "contactPhone", "category", "duration"];
      const missingFields = requiredFields.filter((field) => !formData[field]);

      if (missingFields.length > 0) {
        throw new Error(`Campos requeridos faltantes: ${missingFields.join(", ")}`);
      }

      if (!isValidEmail(formData.contactEmail)) {
        throw new Error("Email inválido");
      }

      if (!isValidPhone(formData.contactPhone)) {
        throw new Error("Teléfono inválido");
      }

      if (!selectedAdvertising || !selectedCinema) {
        throw new Error("Selección de publicidad o cine incompleta");
      }

      const selectedAdvertisingData = advertisingOptions.find((a) => a.id === selectedAdvertising);
      const selectedCinemaData = cinemaOptions.find((c) => c.id === selectedCinema);

      if (!selectedAdvertisingData || !selectedCinemaData) {
        throw new Error("Error al obtener detalles de la selección");
      }

      // Payload that matches the backend
      const payload = {
        advertisingType: selectedAdvertisingData.id,
        cinema: selectedCinemaData.id,
        category: formData.category,
        duration: formData.duration,
        startDate: formData.startDate || null,
        endDate: formData.endDate || null,
        budget: formData.budget || null,
        requirements: formData.requirements || null,
        contactName: formData.contactName,
        contactEmail: formData.contactEmail,
        contactPhone: formData.contactPhone,
        company: formData.company || null,
        message: formData.message || null
      };

      const response = await fetch("http://localhost:8080/api/advertising", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      const result = await response.json();

      if (response.ok) {
        toast.success(result.message || "🎉 ¡Solicitud de publicidad enviada correctamente!");
        setTimeout(() => {
          navigate("/corporate");
        }, 3000);
      } else {
        toast.error(result.message || "❌ Error al enviar solicitud.");
      }
    } catch (err) {
      console.error("Error submitting form:", err);
      toast.error(err.message || "❌ Error inesperado al enviar la solicitud.");
    } finally {
      setLoading(false);
    }
  }, [formData, selectedAdvertising, selectedCinema, advertisingOptions, cinemaOptions, navigate,
    isValidEmail, isValidPhone,])

  // Image error handling
  const handleImageError = useCallback((e, backgroundClass = "cinema-bg") => {
    try {
      console.warn("Image failed to load:", e.target.src)
      e.target.style.display = "none"
      if (e.target.parentElement) {
        e.target.parentElement.classList.add(backgroundClass)
      }
    } catch (err) {
      console.error("Error handling image error:", err)
    }
  }, [])

  // Keyboard navigation
  useEffect(() => {
    const handleKeyDown = (e) => {
      try {
        if (currentStep === 1 && selectedCinema) {
          const currentIndex = cinemaOptions.findIndex((cinema) => cinema.id === selectedCinema)
          switch (e.key) {
            case "ArrowUp":
            case "ArrowLeft":
              e.preventDefault()
              if (currentIndex > 0) handleCinemaSelect(cinemaOptions[currentIndex - 1].id)
              break
            case "ArrowDown":
            case "ArrowRight":
              e.preventDefault()
              if (currentIndex < cinemaOptions.length - 1) handleCinemaSelect(cinemaOptions[currentIndex + 1].id)
              break
            case "Enter":
              e.preventDefault()
              if (selectedCinema) handleContinue()
              break
            case "Escape":
              e.preventDefault()
              handleBack()
              break
            default:
              break
          }
        }
      } catch (err) {
        console.error("Error in keyboard navigation:", err)
      }
    }

    window.addEventListener("keydown", handleKeyDown)
    return () => window.removeEventListener("keydown", handleKeyDown)
  }, [currentStep, selectedCinema, handleCinemaSelect, handleContinue, handleBack, cinemaOptions])

  // Scroll to top on step change
  useEffect(() => {
    try {
      window.scrollTo({ top: 0, behavior: "smooth" })
    } catch (err) {
      console.error("Error scrolling to top:", err)
      window.scrollTo(0, 0)
    }
  }, [currentStep])

  // Error boundary effect
  useEffect(() => {
    const handleError = (event) => {
      console.error("Global error caught:", event.error)
      setError("Ha ocurrido un error inesperado. Por favor recarga la página.")
    }

    const handleUnhandledRejection = (event) => {
      console.error("Unhandled promise rejection:", event.reason)
      setError("Error de conexión. Por favor verifica tu conexión a internet.")
    }

    window.addEventListener("error", handleError)
    window.addEventListener("unhandledrejection", handleUnhandledRejection)

    return () => {
      window.removeEventListener("error", handleError)
      window.removeEventListener("unhandledrejection", handleUnhandledRejection)
    }
  }, [])

  return (
    <div className="events-container">
      {/* Header Section */}
      <div className="events-header">
        <div className="events-header-content">
          <div className="events-icon">📢</div>
          <h1 className="events-title">Publicidad</h1>
          <p className="events-subtitle">
            Promociona tu marca en nuestros cines con publicitarias innovadoras
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
          <p>Enviando Publicidad...</p>
        </div>
      )}

      {/* Progress Steps */}
      <div className="events-progress">
        <div className="events-progress-container">
          {steps.map((step, index) => (
            <div
              key={step.number}
              className={`events-step ${step.active ? "active" : ""} ${currentStep === step.number ? "current" : ""}`}
            >
              <div className="events-step-number">{step.number}</div>
              <span className="events-step-title">{step.title}</span>
              {index < steps.length - 1 && <div className={`events-step-line ${step.active ? "active" : ""}`}></div>}
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
              <h2>Selecciona el cine para tu campaña publicitaria</h2>
              <p>Elige la ubicación que mejor se adapte a tu público objetivo</p>
            </div>

            <div className="events-grid">
              {cinemaOptions.map((cinema) => (
                <div
                  key={cinema.id}
                  className={`events-card cinema-card ${selectedCinema === cinema.id ? "selected" : ""}`}
                  onClick={() => handleCinemaSelect(cinema.id)}
                  style={{ "--card-gradient": cinema.gradient }}
                >
                  <div className="events-card-image cinema-bg">
                    <img
                      src={cinema.image || "/placeholder.svg?height=200&width=300"}
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
                    <div className={`events-radio ${selectedCinema === cinema.id ? "checked" : ""}`}>
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
                className={`events-btn-primary ${!selectedCinema ? "disabled" : ""}`}
                onClick={handleContinue}
                disabled={!selectedCinema}
              >
                Continuar →
              </button>
            </div>
          </div>
        )}

        {/* STEP 2: Advertising Type Selection */}
        {currentStep === 2 && (
          <div className="events-selection-section">
            <div className="events-selection-header">
              <h2>Elige el tipo de publicidad que deseas cotizar</h2>
              <p>Selecciona la opción que mejor se adapte a tus objetivos de marketing</p>
            </div>

            <div className="advertising-grid-horizontal">
              {availableAdvertising.map((advertising) => (
                <div
                  key={advertising.id}
                  className={`events-card advertising-card ${selectedAdvertising === advertising.id ? "selected" : ""}`}
                  onClick={() => handleAdvertisingSelect(advertising.id)}
                  style={{ "--card-gradient": advertising.gradient }}
                >
                  <div className="events-card-image advertising-bg">
                    <img
                      src={advertising.image || "/placeholder.svg?height=200&width=300"}
                      alt={`${advertising.title} - Publicidad`}
                      className="events-card-img"
                      loading="lazy"
                      onError={(e) => handleImageError(e, "advertising-bg")}
                    />
                    <div className="advertising-icon-large">{advertising.title.split(' ')[0]}</div>
                    <div className="events-card-overlay">
                      <h3>{advertising.title}</h3>
                    </div>
                  </div>

                  <div className="events-card-content">
                    <div className="events-card-header">
                      <div className="events-card-icon-small">{advertising.icon}</div>
                      <h4>{advertising.title}</h4>
                    </div>
                    <p>{advertising.description}</p>
                    <div className={`events-radio ${selectedAdvertising === advertising.id ? "checked" : ""}`}>
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
                className={`events-btn-primary ${!selectedAdvertising ? "disabled" : ""}`}
                onClick={handleContinue}
                disabled={!selectedAdvertising}
              >
                Continuar →
              </button>
            </div>
          </div>
        )}

        {/* STEP 3: Details Form */}
        {currentStep === 3 && (
          <div className="events-form-section">
            <div className="events-selection-header">
              <h2>Detalles</h2>
              <p>Completa la información específica para tu campaña publicitaria</p>
            </div>

            <div className="details-summary">
              <div className="selected-items">
                <div className="selected-item">
                  <h4>Tipo de Publicidad</h4>
                  <p>{advertisingOptions.find((a) => a.id === selectedAdvertising)?.title}</p>
                </div>
                <div className="selected-item">
                  <h4>Cines</h4>
                  <p>{cinemaOptions.find((c) => c.id === selectedCinema)?.name.replace("Cinerama ", "")}</p>
                </div>
              </div>
            </div>

            <div className="events-form">
              <div className="form-row">
                <div className="form-group">
                  <label>Tiempo de Permanencia: *</label>
                  <div className="duration-selector">
                    <button
                      type="button"
                      onClick={() =>
                        handleInputChange("duration", Math.max(1, Number.parseInt(formData.duration) - 1).toString())
                      }
                      className="duration-btn"
                    >
                      -
                    </button>
                    <span className="duration-value">{formData.duration}</span>
                    <button
                      type="button"
                      onClick={() => handleInputChange("duration", (Number.parseInt(formData.duration) + 1).toString())}
                      className="duration-btn"
                    >
                      +
                    </button>
                    <span className="duration-label">semanas</span>
                  </div>
                </div>
              </div>

              <div className="form-group">
                <label>Categoría: *</label>
                <div className="category-grid">
                  {categoryOptions.map((category) => (
                    <div
                      key={category.value}
                      className={`category-option ${formData.category === category.value ? "selected" : ""}`}
                      onClick={() => handleInputChange("category", category.value)}
                    >
                      <div className={`category-checkbox ${formData.category === category.value ? "checked" : ""}`}>
                        {formData.category === category.value && "✓"}
                      </div>
                      <span>{category.label}</span>
                    </div>
                  ))}
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Fecha de Inicio</label>
                  <input
                    type="date"
                    value={formData.startDate}
                    onChange={(e) => handleInputChange("startDate", e.target.value)}
                    min={new Date().toISOString().split("T")[0]}
                  />
                </div>
                <div className="form-group">
                  <label>Presupuesto Estimado</label>
                  <select value={formData.budget} onChange={(e) => handleInputChange("budget", e.target.value)}>
                    <option value="">Selecciona un rango</option>
                    {budgetOptions.map((budget) => (
                      <option key={budget.value} value={budget.value}>
                        {budget.label}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="form-group">
                <label>Requerimientos Especiales</label>
                <textarea
                  placeholder="Describe cualquier requerimiento especial para tu campaña..."
                  value={formData.requirements}
                  onChange={(e) => handleInputChange("requirements", e.target.value)}
                  rows="4"
                  maxLength="500"
                ></textarea>
                <small className="char-count">{formData.requirements.length}/500 caracteres</small>
              </div>
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>
                ← Regresar
              </button>
              <button
                className={`events-btn-primary ${!formData.category || !formData.duration ? "disabled" : ""}`}
                onClick={handleContinue}
                disabled={!formData.category || !formData.duration}
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
              <p>Completa tus datos para que podamos contactarte y enviar tu cotización</p>
            </div>

            <div className="events-form">
              <div className="form-row">
                <div className="form-group">
                  <label>Nombre Completo *</label>
                  <input
                    type="text"
                    placeholder="Tu nombre completo"
                    value={formData.contactName}
                    onChange={(e) => handleInputChange("contactName", e.target.value)}
                    required
                    maxLength="100"
                  />
                </div>
                <div className="form-group">
                  <label>Empresa/Institución</label>
                  <input
                    type="text"
                    placeholder="Nombre de tu empresa"
                    value={formData.company}
                    onChange={(e) => handleInputChange("company", e.target.value)}
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
                    onChange={(e) => handleInputChange("contactEmail", e.target.value)}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Teléfono *</label>
                  <input
                    type="tel"
                    placeholder="+51 999 999 999"
                    value={formData.contactPhone}
                    onChange={(e) => handleInputChange("contactPhone", e.target.value)}
                    required
                  />
                </div>
              </div>

              <div className="form-group">
                <label>Mensaje Adicional</label>
                <textarea
                  placeholder="Cuéntanos más sobre tu campaña publicitaria o cualquier pregunta específica..."
                  value={formData.message}
                  onChange={(e) => handleInputChange("message", e.target.value)}
                  rows="4"
                  maxLength="500"
                ></textarea>
                <small className="char-count">{formData.message.length}/500 caracteres</small>
              </div>
            </div>

            <div className="events-actions">
              <button className="events-btn-secondary" onClick={handleBack}>
                ← Regresar
              </button>
              <button
                className={`events-btn-primary ${!formData.contactName || !formData.contactEmail || !formData.contactPhone ? "disabled" : ""}`}
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
              <h2>Resumen de la Campaña Publicitaria</h2>
              <p>Revisa todos los detalles antes de enviar tu solicitud de cotización</p>
            </div>

            <div className="summary-card">
              <div className="summary-section">
                <h3>🎬 Cine Seleccionado</h3>
                <p>{cinemaOptions.find((c) => c.id === selectedCinema)?.name}</p>
                <span className="summary-description">
                  {cinemaOptions.find((c) => c.id === selectedCinema)?.description}
                </span>
              </div>

              <div className="summary-section">
                <h3>📢 Tipo de Publicidad</h3>
                <p>{advertisingOptions.find((a) => a.id === selectedAdvertising)?.title}</p>
                <span className="summary-description">
                  {advertisingOptions.find((a) => a.id === selectedAdvertising)?.description}
                </span>
              </div>

              <div className="summary-section">
                <h3>📋 Detalles de la Campaña</h3>
                <div className="summary-details">
                  <p>
                    <strong>Categoría:</strong> {categoryOptions.find((c) => c.value === formData.category)?.label}
                  </p>
                  <p>
                    <strong>Duración:</strong> {formData.duration} semanas
                  </p>
                  {formData.startDate && (
                    <p>
                      <strong>Fecha de Inicio:</strong> {new Date(formData.startDate).toLocaleDateString("es-ES")}
                    </p>
                  )}
                  {formData.budget && (
                    <p>
                      <strong>Presupuesto:</strong> {budgetOptions.find((b) => b.value === formData.budget)?.label}
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
                      <strong>Empresa:</strong> {formData.company}
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
              <button className="events-btn-primary" onClick={handleSubmit} disabled={loading}>
                {loading ? "Enviando ..." : "Enviar Solicitud"}
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
  )
}

export default Advertising