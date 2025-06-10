import { useState, useEffect, useMemo, useCallback } from "react"
import "../styles/Promotions.css"


// Main component that displays all cinema promotions with filtering and modal functionality
const Promotions = () => {
  const [activeTab, setActiveTab] = useState("all")
  const [selectedPromotion, setSelectedPromotion] = useState(null)
  const [currentDate, setCurrentDate] = useState(new Date())
  const [showRedemptionModal, setShowRedemptionModal] = useState(false)
  const [redeemedPromotion, setRedeemedPromotion] = useState(null)
  const [isRedeeming, setIsRedeeming] = useState(false)

  // Update current date every minute to ensure real-time accuracy
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentDate(new Date())
    }, 60000) // Update every minute

    return () => clearInterval(interval)
  }, [])

  // Enhanced promotion data with date conditions and coupon codes
  const promotions = useMemo(() => ({
    tickets: [
      {
        id: 1,
        title: "Entradas 2x1 - Miércoles",
        description:
          "¡Compra una entrada y llévate otra gratis! Válido solo los miércoles cuando compres a través de nuestra app o web.",
        icon: "ticket",
        startDate: new Date("2024-01-01"),
        endDate: new Date("2025-12-31"),
        dayConditions: [3], // Wednesday = 3 (0=Sunday, 6=Saturday)
        timeConditions: null,
        requiresApp: true,
        maxUsesPerUser: 1,
        maxUsesPerDay: null,
        couponCode: "WED2X1",
        discount: "50%",
        conditions: [
          "Válido solo los miércoles",
          "Debe realizar la compra a través de app o web",
          "No válido con otras promociones",
          "Limitado a una promoción por usuario por día",
        ],
        faqs: [
          {
            question: "¿Cómo canjeo esta promoción?",
            answer:
              "Simplemente realiza tu compra a través de nuestra app o sitio web los miércoles, y el descuento se aplicará automáticamente al finalizar la compra.",
          },
          {
            question: "¿Puedo usar esto para cualquier película?",
            answer: "Sí, esta promoción es válida para todas las películas, sujeto a disponibilidad.",
          },
          {
            question: "¿Ambas entradas tienen que ser para la misma película?",
            answer: "Sí, ambas entradas deben ser para la misma función y película.",
          },
        ],
      },
      {
        id: 2,
        title: "Descuento Estudiantil",
        description: "20% de descuento en todas las entradas regulares con identificación estudiantil válida.",
        icon: "ticket",
        startDate: new Date("2024-01-01"),
        endDate: new Date("2025-12-31"),
        dayConditions: null, // Available all days
        timeConditions: null,
        requiresApp: false,
        maxUsesPerUser: null,
        maxUsesPerDay: null,
        couponCode: "STUDENT20",
        discount: "20%",
        conditions: [
          "Debe presentar una identificación estudiantil válida en el cine",
          "Válido para una entrada por identificación",
          "No se puede combinar con otras ofertas",
        ],
        faqs: [
          {
            question: "¿Necesito mostrar mi identificación estudiantil?",
            answer: "Sí, debes presentar una identificación estudiantil válida en la taquilla.",
          },
          {
            question: "¿Puedo reservar en línea con el descuento estudiantil?",
            answer: "Sí, pero deberás verificar tu estatus de estudiante en el cine.",
          },
        ],
      },
    ],
    // Food combo promotions (family packs, date night specials)
    combos: [
      {
        id: 3,
        title: "Combo Familiar - Fines de Semana",
        description:
          "Obtén unas palomitas grandes, 4 bebidas medianas y 2 cajas de dulces a un precio especial. Solo fines de semana.",
        icon: "popcorn",
        startDate: new Date("2024-01-01"),
        endDate: new Date("2025-12-31"),
        dayConditions: [5, 6, 0], // Friday, Saturday, Sunday
        timeConditions: null,
        requiresApp: false,
        maxUsesPerUser: null,
        maxUsesPerDay: null,
        couponCode: "FAMILY30",
        discount: "30%",
        conditions: [
          "Disponible solo los fines de semana (viernes a domingo)",
          "No se puede combinar con otras promociones de alimentos",
          "Sujeto a disponibilidad",
        ],
        faqs: [
          {
            question: "¿Puedo sustituir elementos en el combo?",
            answer: "Las sustituciones están disponibles por un cargo adicional.",
          },
          {
            question: "¿Este combo está disponible para pedidos en línea?",
            answer: "Sí, puedes pre-ordenar este combo al comprar tus entradas en línea.",
          },
        ],
      },
      {
        id: 4,
        title: "Happy Hour Combo",
        description:
          "Palomitas medianas, 2 bebidas y un postre para compartir a precio especial. Solo de 2:00 PM a 5:00 PM.",
        icon: "popcorn",
        startDate: new Date("2024-01-01"),
        endDate: new Date("2025-12-31"),
        dayConditions: [1, 2, 3, 4, 5], // Monday to Friday
        timeConditions: { start: "14:00", end: "17:00" },
        requiresApp: false,
        maxUsesPerUser: null,
        maxUsesPerDay: null,
        couponCode: "HAPPY25",
        discount: "25%",
        conditions: [
          "Disponible de lunes a viernes de 2:00 PM a 5:00 PM",
          "Hasta agotar existencias",
          "No se puede combinar con otras ofertas de alimentos",
        ],
        faqs: [
          {
            question: "¿Qué opciones de postre hay disponibles?",
            answer: "Puedes elegir entre pastel de chocolate, helado o galletas.",
          },
          {
            question: "¿Podemos actualizar a palomitas grandes?",
            answer: "Sí, las actualizaciones están disponibles por un cargo adicional.",
          },
        ],
      },
    ],
    // Special gift promotions (birthday rewards, loyalty programs)
    gifts: [
      {
        id: 5,
        title: "Regalo de Cumpleaños",
        description: "Entrada gratis durante tu semana de cumpleaños. Requiere registro.",
        icon: "gift",
        startDate: new Date("2024-01-01"),
        endDate: new Date("2025-12-31"),
        dayConditions: null,
        timeConditions: null,
        requiresApp: true,
        maxUsesPerUser: 1,
        maxUsesPerDay: null,
        specialCondition: "birthday", // Special condition for birthday validation
        couponCode: "BIRTHDAY",
        discount: "100%",
        conditions: [
          "Debe estar registrado en nuestro programa de fidelidad",
          "Válido 3 días antes y después de tu cumpleaños",
          "Una entrada gratis por cumpleaños",
          "Sujeto a disponibilidad",
        ],
        faqs: [
          {
            question: "¿Cómo me registro para el regalo de cumpleaños?",
            answer: "Necesitas crear una cuenta en nuestro programa de fidelidad y verificar tu fecha de nacimiento.",
          },
          {
            question: "¿Necesito mostrar identificación?",
            answer: "Sí, podemos solicitar identificación para verificar tu fecha de nacimiento.",
          },
        ],
      },
    ],
    // Credit card partnership promotions
    cards: [
      {
        id: 6,
        title: "Promoción Tarjetas de Crédito - Martes",
        description: "15% de descuento al pagar con tarjetas bancarias participantes. Solo los martes.",
        icon: "card",
        startDate: new Date("2024-01-01"),
        endDate: new Date("2025-12-31"),
        dayConditions: [2], // Tuesday
        timeConditions: null,
        requiresApp: false,
        maxUsesPerUser: null,
        maxUsesPerDay: null,
        couponCode: "CARD15",
        discount: "15%",
        conditions: [
          "Válido solo los martes",
          "Válido solo con tarjetas bancarias participantes",
          "El descuento se aplica solo a entradas",
          "No válido con otros descuentos",
        ],
        faqs: [
          {
            question: "¿Qué bancos participan en esta promoción?",
            answer: "Actualmente tenemos alianzas con BCP, BBVA e Interbank.",
          },
          {
            question: "¿Puedo usar esta promoción en línea?",
            answer: "Sí, el descuento se aplicará automáticamente cuando pagues con una tarjeta elegible.",
          },
        ],
      },
    ],
  }),[])

  // Utility functions for date/time validation
  const isDateInRange = (date, startDate, endDate) => {
    const current = new Date(date)
    current.setHours(0, 0, 0, 0)
    const start = new Date(startDate)
    start.setHours(0, 0, 0, 0)
    const end = new Date(endDate)
    end.setHours(23, 59, 59, 999)

    return current >= start && current <= end
  }

  const isDayValid = (date, dayConditions) => {
    if (!dayConditions || dayConditions.length === 0) return true
    const dayOfWeek = date.getDay()
    return dayConditions.includes(dayOfWeek)
  }

  const isTimeValid = (date, timeConditions) => {
    if (!timeConditions) return true

    const currentTime = date.toTimeString().slice(0, 5) // HH:MM format
    return currentTime >= timeConditions.start && currentTime <= timeConditions.end
  }

  const isBirthdayValid = (userBirthday) => {
    if (!userBirthday) return false

    const today = new Date()
    const birthday = new Date(userBirthday)
    const thisYearBirthday = new Date(today.getFullYear(), birthday.getMonth(), birthday.getDate())

    // Check if within 3 days before or after birthday
    const timeDiff = Math.abs(today - thisYearBirthday)
    const daysDiff = Math.ceil(timeDiff / (1000 * 60 * 60 * 24))

    return daysDiff <= 3
  }

  // Main function to check if promotion is active
  const isPromotionActive = useCallback((promotion, userData = {}) => {
    const now = currentDate

    if (!isDateInRange(now, promotion.startDate, promotion.endDate)) return false
    if (!isDayValid(now, promotion.dayConditions)) return false
    if (!isTimeValid(now, promotion.timeConditions)) return false
    if (promotion.specialCondition === "birthday") return isBirthdayValid(userData.birthday)
    return true
  }, [currentDate])

  // Get promotion status for display
  const getPromotionStatus = useCallback((promotion, userData = {}) => {
    const now = currentDate

    if (isPromotionActive(promotion, userData)) {
      return { status: "active", message: "Promoción Activa" }
    }
    if (now < promotion.startDate) {
      const daysUntil = Math.ceil((promotion.startDate - now) / (1000 * 60 * 60 * 24))
      return { status: "upcoming", message: `Disponible en ${daysUntil} días` }
    }
    if (now > promotion.endDate) {
      return { status: "expired", message: "Promoción Expirada" }
    }
    if (promotion.dayConditions && !isDayValid(now, promotion.dayConditions)) {
      const dayNames = ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sabado"]
      const validDays = promotion.dayConditions.map((day) => dayNames[day]).join(", ")
      return { status: "inactive", message: `Disponible: ${validDays}` }
    }
    if (promotion.timeConditions && !isTimeValid(now, promotion.timeConditions)) {
      return {
        status: "inactive",
        message: `Disponible: ${promotion.timeConditions.start} - ${promotion.timeConditions.end}`,
      }
    }
    return { status: "inactive", message: "No Disponible" }
  }, [currentDate, isPromotionActive])

  // Filter promotions based on active tab and show only active ones by default
  const getFilteredPromotions = useMemo(() => {
    const allPromotions = [
      ...promotions.tickets,
      ...promotions.combos,
      ...promotions.gifts,
      ...promotions.cards,
    ]
    const filtered = activeTab === "all" ? allPromotions : promotions[activeTab] || []
    return filtered.map((promo) => ({
      ...promo,
      statusInfo: getPromotionStatus(promo, { birthday: "1990-05-31" }), // Mock user data
    }))
  }, [activeTab, promotions, getPromotionStatus])
  // Get active promotions only
  const activePromotions = useMemo(() => {
    return getFilteredPromotions.filter((promo) => promo.statusInfo.status === "active")
  }, [getFilteredPromotions]

  // Maps promotion icon types to corresponding FontAwesome CSS classes
  // Used to display appropriate icons for each promotion category
  const getIconClass = (iconType) => {
    switch (iconType) {
      case "ticket":
        return "fa fa-ticket"
      case "popcorn":
        return "fa fa-film"
      case "gift":
        return "fa fa-gift"
      case "card":
        return "fa fa-credit-card"
      default:
        return "fa fa-star"
    }
  }

  // Function to open modal with detailed view of selected promotion
  const openPromotionDetails = (promotion) => {
    setSelectedPromotion(promotion)
  }

  // Function to close the promotion details modal
  const closePromotionDetails = () => {
    setSelectedPromotion(null)
  }

  // Handle promotion redemption
  const handleRedeemPromotion = async (promotion) => {
    setIsRedeeming(true)

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1500))

    setRedeemedPromotion(promotion)
    setSelectedPromotion(null)
    setShowRedemptionModal(true)
    setIsRedeeming(false)
  }

  return (
    <div className="min-h-screen bg-white text-gray-900 p-6">
      {/* Stats Section - Horizontal Layout */}
      <div className="mb-8">
        <h2 className="text-3xl font-bold text-center mb-6 text-white">Estado de Promociones</h2>
        <div className="flex flex-wrap justify-center gap-6 max-w-6xl mx-auto">
          <div className="bg-gradient-to-r from-green-500 to-green-600 rounded-xl p-6 text-center min-w-48 shadow-lg">
            <div className="text-4xl font-bold text-white mb-2">{activePromotions.length}</div>
            <div className="text-green-100 font-medium">Activas Ahora</div>
          </div>
          <div className="bg-gradient-to-r from-blue-500 to-blue-600 rounded-xl p-6 text-center min-w-48 shadow-lg">
            <div className="text-4xl font-bold text-white mb-2">{getFilteredPromotions.length}</div>
            <div className="text-blue-100 font-medium">Total Disponibles</div>
          </div>
          <div className="bg-gradient-to-r from-yellow-500 to-yellow-600 rounded-xl p-6 text-center min-w-48 shadow-lg">
            <div className="text-4xl font-bold text-white mb-2">
              {getFilteredPromotions.filter((p) => p.statusInfo.status === "upcoming").length}
            </div>
            <div className="text-yellow-100 font-medium">Próximamente</div>
          </div>
          <div className="bg-gradient-to-r from-purple-500 to-purple-600 rounded-xl p-6 text-center min-w-48 shadow-lg">
            <div className="text-2xl font-bold text-white mb-2 capitalize">
              {currentDate.toLocaleDateString("es-ES", { weekday: "long" })}
            </div>
            <div className="text-purple-100 font-medium">Hoy es</div>
          </div>
        </div>
      </div>

      {/* Promotions Section */}
      <div className="max-w-7xl mx-auto">
        <h2 className="text-3xl font-bold text-center mb-8 text-white">Promociones Disponibles</h2>

        {/* Tabs */}
        <div className="flex flex-wrap justify-center gap-4 mb-8">
          <button
            className={`px-6 py-3 rounded-lg font-medium transition-all duration-300 ${
              activeTab === "all" ? "bg-red-600 text-white shadow-lg" : "bg-gray-700 text-gray-300 hover:bg-gray-600"
            }`}
            onClick={() => setActiveTab("all")}
          >
            Todas ({getFilteredPromotions.length})
          </button>
          <button
            className={`px-6 py-3 rounded-lg font-medium transition-all duration-300 ${
              activeTab === "tickets"
                ? "bg-red-600 text-white shadow-lg"
                : "bg-gray-700 text-gray-300 hover:bg-gray-600"
            }`}
            onClick={() => setActiveTab("tickets")}
          >
            Boletos
          </button>
          <button
            className={`px-6 py-3 rounded-lg font-medium transition-all duration-300 ${
              activeTab === "combos" ? "bg-red-600 text-white shadow-lg" : "bg-gray-700 text-gray-300 hover:bg-gray-600"
            }`}
            onClick={() => setActiveTab("combos")}
          >
            Combos
          </button>
          <button
            className={`px-6 py-3 rounded-lg font-medium transition-all duration-300 ${
              activeTab === "gifts" ? "bg-red-600 text-white shadow-lg" : "bg-gray-700 text-gray-300 hover:bg-gray-600"
            }`}
            onClick={() => setActiveTab("gifts")}
          >
            Regalos
          </button>
          <button
            className={`px-6 py-3 rounded-lg font-medium transition-all duration-300 ${
              activeTab === "cards" ? "bg-red-600 text-white shadow-lg" : "bg-gray-700 text-gray-300 hover:bg-gray-600"
            }`}
            onClick={() => setActiveTab("cards")}
          >
            Tarjetas
          </button>
        </div>

        {/* Promotions Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-12">
          {getFilteredPromotions.map((promo) => (
            <div
              key={promo.id}
              className={`promo-card bg-white rounded-xl overflow-hidden shadow-lg transition-all duration-300 hover:shadow-xl hover:scale-105 relative border ${
                promo.statusInfo.status === "active"
                  ? "ring-2 ring-green-500 border-green-200"
                  : promo.statusInfo.status === "expired"
                    ? "opacity-60 border-gray-200"
                    : "border-gray-200"
              }`}
            >
              {/* Status Badge - Positioned to not overlap title */}
              <div
                className={`status-badge absolute top-2 right-2 px-2 py-1 rounded-full text-xs font-semibold z-10 shadow-sm ${
                  promo.statusInfo.status === "active"
                    ? "bg-green-500 text-white"
                    : promo.statusInfo.status === "upcoming"
                      ? "bg-yellow-500 text-white"
                      : promo.statusInfo.status === "expired"
                        ? "bg-red-500 text-white"
                        : "bg-gray-500 text-white"
                }`}
              >
                {promo.statusInfo.message}
              </div>

              {/* Card Content */}
              <div className="card-content p-6 flex flex-col h-full">
                {/* Header with Icon and Title */}
                <div className="card-header flex items-start mb-4 pt-2">
                  <div
                    className={`w-14 h-14 rounded-full flex items-center justify-center text-2xl flex-shrink-0 ${
                      promo.statusInfo.status === "active" ? "bg-green-100 text-green-600" : "bg-gray-100 text-gray-600"
                    }`}
                  >
                    <i className={getIconClass(promo.icon)}></i>
                  </div>
                  <div className="ml-4 flex-1 pr-16">
                    <h3 className="card-title text-xl font-bold text-gray-800 leading-tight">{promo.title}</h3>
                  </div>
                </div>

                {/* Description */}
                <div className="flex-1 mb-4">
                  <p className="card-description text-gray-600 leading-relaxed text-sm">{promo.description}</p>
                </div>

                {/* Discount Badge */}
                <div className="discount-section flex items-center justify-between mb-6 bg-gray-50 p-3 rounded-lg">
                  <span className="text-sm font-medium text-gray-700">Descuento:</span>
                  <div className="bg-gradient-to-r from-red-500 to-orange-500 text-white px-4 py-2 rounded-full text-sm font-bold shadow-sm">
                    {promo.discount}
                  </div>
                </div>

                {/* Action Button */}
                <div className="card-footer mt-auto">
                  <button
                    className={`w-full py-3 px-4 rounded-lg font-medium transition-all duration-300 ${
                      promo.statusInfo.status === "active"
                        ? "bg-green-600 hover:bg-green-700 text-white"
                        : promo.statusInfo.status === "expired"
                          ? "bg-gray-600 text-gray-400 cursor-not-allowed"
                          : "bg-blue-600 hover:bg-blue-700 text-white"
                    }`}
                    onClick={() => openPromotionDetails(promo)}
                    disabled={promo.statusInfo.status === "expired"}
                  >
                    {promo.statusInfo.status === "active" ? "Canjear Ahora" : "Ver Detalles"}
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>

        {getFilteredPromotions.length === 0 && (
          <div className="text-center py-16">
            <i className="fa fa-exclamation-circle text-6xl text-gray-400 mb-6"></i>
            <p className="text-xl text-gray-400">No hay promociones disponibles en esta categoría.</p>
          </div>
        )}

        {/* FAQ Section */}
        <div className="bg-gray-800 rounded-xl p-8">
          <h2 className="text-3xl font-bold text-center text-white mb-8">Preguntas Frecuentes</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            <div className="bg-gray-700 p-6 rounded-lg">
              <h3 className="text-lg font-bold text-white mb-3">¿Cómo canjeo las promociones?</h3>
              <p className="text-gray-300 leading-relaxed">
                Las promociones pueden ser canjeadas a través de nuestra app o sitio web. Simplemente selecciona la
                promoción que deseas usar durante el proceso de compra.
              </p>
            </div>
            <div className="bg-gray-700 p-6 rounded-lg">
              <h3 className="text-lg font-bold text-white mb-3">¿Puedo combinar múltiples promociones?</h3>
              <p className="text-gray-300 leading-relaxed">
                La mayoría de las promociones no se pueden combinar con otras ofertas. Por favor, consulta los términos
                y condiciones de cada promoción para más detalles.
              </p>
            </div>
            <div className="bg-gray-700 p-6 rounded-lg">
              <h3 className="text-lg font-bold text-white mb-3">
                ¿Las promociones están disponibles en todas las sucursales?
              </h3>
              <p className="text-gray-300 leading-relaxed">
                Las promociones pueden variar según la ubicación. Por favor, consulta con tu cine local para conocer la
                disponibilidad específica.
              </p>
            </div>
            <div className="bg-gray-700 p-6 rounded-lg">
              <h3 className="text-lg font-bold text-white mb-3">
                ¿Necesito una cuenta para acceder a las promociones?
              </h3>
              <p className="text-gray-300 leading-relaxed">
                Algunas promociones requieren que inicies sesión en una cuenta. Crear una cuenta es gratis y te da
                acceso a ofertas exclusivas.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Promotion Details Modal - FIXED RESPONSIVE DESIGN */}
      {selectedPromotion && (
        <div className="modal-overlay-fixed fixed inset-0 bg-black bg-opacity-60 flex items-center justify-center p-4 z-50">
          <div className="modal-content-fixed bg-white rounded-xl w-full max-w-md max-h-[80vh] flex flex-col shadow-2xl">
            {/* Modal Header - Fixed */}
            <div className="modal-header-fixed p-4 border-b border-gray-200 flex-shrink-0 relative">
              <button
                className="close-button-fixed absolute right-3 top-3 text-gray-400 hover:text-gray-600 text-2xl font-bold w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 transition-colors"
                onClick={closePromotionDetails}
              >
                ×
              </button>
              <div className="flex items-center justify-center w-full">
                <div
                  className={`w-12 h-12 rounded-full flex items-center justify-center text-2xl mr-3 ${
                    selectedPromotion.statusInfo.status === "active"
                      ? "bg-green-100 text-green-600"
                      : "bg-gray-100 text-gray-600"
                  }`}
                >
                  <i className={getIconClass(selectedPromotion.icon)}></i>
                </div>
                <div className="text-center">
                  <h2 className="text-xl font-bold text-gray-800">{selectedPromotion.title}</h2>
                  <div
                    className={`inline-block px-3 py-1 rounded-full text-xs font-medium mt-1 ${
                      selectedPromotion.statusInfo.status === "active"
                        ? "bg-green-500 text-white"
                        : selectedPromotion.statusInfo.status === "upcoming"
                          ? "bg-yellow-500 text-white"
                          : selectedPromotion.statusInfo.status === "expired"
                            ? "bg-red-500 text-white"
                            : "bg-gray-500 text-white"
                    }`}
                  >
                    {selectedPromotion.statusInfo.message}
                  </div>
                </div>
              </div>
            </div>

            {/* Modal Body - Scrollable */}
            <div className="modal-body-fixed flex-1 overflow-y-auto p-6">
              <div className="mb-6">
                <p className="text-gray-600 text-base leading-relaxed">{selectedPromotion.description}</p>
              </div>

              {/* Discount Info */}
              <div className="mb-6 text-center">
                <div className="bg-gradient-to-r from-red-500 to-orange-500 text-white px-6 py-3 rounded-lg inline-block shadow-sm">
                  <span className="text-sm font-medium">Descuento: </span>
                  <span className="text-xl font-bold">{selectedPromotion.discount}</span>
                </div>
              </div>

              <div className="mb-6">
                <h3 className="text-lg font-bold text-gray-800 mb-3">Términos y Condiciones</h3>
                <ul className="space-y-2">
                  {selectedPromotion.conditions.map((condition, index) => (
                    <li key={index} className="text-gray-600 flex items-start text-sm">
                      <span className="text-red-500 mr-2 flex-shrink-0">•</span>
                      <span>{condition}</span>
                    </li>
                  ))}
                </ul>
              </div>

              <div className="mb-6">
                <h3 className="text-lg font-bold text-gray-800 mb-3">Preguntas Frecuentes</h3>
                <div className="space-y-3">
                  {selectedPromotion.faqs.map((faq, index) => (
                    <div key={index} className="bg-gray-50 p-4 rounded-lg">
                      <h4 className="font-bold text-gray-800 mb-2 text-sm">{faq.question}</h4>
                      <p className="text-gray-600 text-sm leading-relaxed">{faq.answer}</p>
                    </div>
                  ))}
                </div>
              </div>
            </div>

            {/* Modal Footer - Fixed */}
            <div className="modal-footer-fixed p-4 border-t border-gray-200 flex-shrink-0">
              <div className="text-center">
                <button
                  className={`px-8 py-3 rounded-lg font-medium text-base transition-all duration-300 ${
                    selectedPromotion.statusInfo.status === "active"
                      ? "bg-green-600 hover:bg-green-700 text-white"
                      : "bg-gray-600 text-gray-400 cursor-not-allowed"
                  } ${isRedeeming ? "opacity-50 cursor-not-allowed" : ""}`}
                  disabled={selectedPromotion.statusInfo.status !== "active" || isRedeeming}
                  onClick={() => handleRedeemPromotion(selectedPromotion)}
                >
                  {isRedeeming ? (
                    <>
                      <i className="fa fa-spinner fa-spin mr-2"></i>
                      Canjeando...
                    </>
                  ) : selectedPromotion.statusInfo.status === "active" ? (
                    "Canjear Oferta"
                  ) : (
                    "No Disponible"
                  )}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Redemption Success Modal */}
      {showRedemptionModal && redeemedPromotion && (
        <div className="modal-overlay-fixed fixed inset-0 bg-black bg-opacity-60 flex items-center justify-center p-4 z-50">
          <div className="success-modal-fixed bg-white rounded-xl max-w-md w-full shadow-2xl">
            <div className="p-6">
              <div className="text-center">
                <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <i className="fa fa-check text-3xl text-green-600"></i>
                </div>
                <h2 className="text-2xl font-bold text-green-600 mb-2">¡Promoción Canjeada!</h2>
                <p className="text-gray-600 mb-6">Tu cupón ha sido generado exitosamente</p>
              </div>

              <div className="bg-green-50 border border-green-200 rounded-lg p-4 mb-6">
                <div className="text-center">
                  <div className="font-bold text-lg text-gray-800 mb-2">{redeemedPromotion.title}</div>
                  <div className="text-sm text-gray-600 mb-2">Código de cupón:</div>
                  <div className="font-mono text-xl font-bold text-green-700 bg-white p-3 rounded border-2 border-dashed border-green-300">
                    {redeemedPromotion.couponCode}
                  </div>
                  <div className="text-sm text-gray-600 mt-2">
                    Descuento: <span className="font-bold text-green-700">{redeemedPromotion.discount}</span>
                  </div>
                </div>
              </div>

              <div className="text-center">
                <p className="text-gray-600 mb-4">¡Ahora puedes elegir tu película y usar tu cupón!</p>

                <button
                  className="px-8 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
                  onClick={() => setShowRedemptionModal(false)}
                >
                  Cerrar
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default Promotions