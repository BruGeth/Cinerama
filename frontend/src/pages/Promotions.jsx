import React, { useState } from 'react';
import '../styles/Promotions.css';
import PromoSlider from '../Components/PromoSlider';

// Main component that displays all cinema promotions with filtering and modal functionality
const Promotions = () => {
  // State to track which promotion category tab is currently active (all, tickets, combos, etc.)
  const [activeTab, setActiveTab] = useState('all');

  // Static data structure containing all promotion categories and their details
  // Each promotion has: id, title, description, icon type, conditions array, and FAQs array
  const promotions = {
    // Ticket-related promotions (2x1 deals, student discounts)
    tickets: [
      {
        id: 1,
        title: 'Entradas 2x1', // "Buy one get one free tickets"
        description: '¡Compra una entrada y llévate otra gratis! Válido solo los miércoles cuando compres a través de nuestra app o web.',
        icon: 'ticket', // Used to determine which FontAwesome icon to display
        conditions: [
          'Válido solo los miércoles',
          'Debe realizar la compra a través de app o web',
          'No válido con otras promociones',
          'Limitado a una promoción por usuario por día'
        ],
        faqs: [
          { question: '¿Cómo canjeo esta promoción?', answer: 'Simplemente realiza tu compra a través de nuestra app o sitio web los miércoles, y el descuento se aplicará automáticamente al finalizar la compra.' },
          { question: '¿Puedo usar esto para cualquier película?', answer: 'Sí, esta promoción es válida para todas las películas, sujeto a disponibilidad.' },
          { question: '¿Ambas entradas tienen que ser para la misma película?', answer: 'Sí, ambas entradas deben ser para la misma función y película.' }
        ]
      },
      {
        id: 2,
        title: 'Descuento Estudiantil', // "Student discount"
        description: '20% de descuento en todas las entradas regulares con identificación estudiantil válida.',
        icon: 'ticket',
        conditions: [
          'Debe presentar una identificación estudiantil válida en el cine',
          'Válido para una entrada por identificación',
          'No se puede combinar con otras ofertas'
        ],
        faqs: [
          { question: '¿Necesito mostrar mi identificación estudiantil?', answer: 'Sí, debes presentar una identificación estudiantil válida en la taquilla.' },
          { question: '¿Puedo reservar en línea con el descuento estudiantil?', answer: 'Sí, pero deberás verificar tu estatus de estudiante en el cine.' }
        ]
      }
    ],
    // Food combo promotions (family packs, date night specials)
    combos: [
      {
        id: 3,
        title: 'Combo Familiar', // "Family combo"
        description: 'Obtén unas palomitas grandes, 4 bebidas medianas y 2 cajas de dulces a un precio especial.',
        icon: 'popcorn',
        conditions: [
          'Disponible todos los días de la semana',
          'No se puede combinar con otras promociones de alimentos',
          'Sujeto a disponibilidad'
        ],
        faqs: [
          { question: '¿Puedo sustituir elementos en el combo?', answer: 'Las sustituciones están disponibles por un cargo adicional.' },
          { question: '¿Este combo está disponible para pedidos en línea?', answer: 'Sí, puedes pre-ordenar este combo al comprar tus entradas en línea.' }
        ]
      },
      {
        id: 4,
        title: 'Combo Noche de Cita', // "Date night combo"
        description: 'Palomitas medianas, 2 bebidas y un postre para compartir a un precio especial.',
        icon: 'popcorn',
        conditions: [
          'Disponible todos los días',
          'Hasta agotar existencias',
          'No se puede combinar con otras ofertas de alimentos'
        ],
        faqs: [
          { question: '¿Qué opciones de postre hay disponibles?', answer: 'Puedes elegir entre pastel de chocolate, helado o galletas.' },
          { question: '¿Podemos actualizar a palomitas grandes?', answer: 'Sí, las actualizaciones están disponibles por un cargo adicional.' }
        ]
      }
    ],
    // Special gift promotions (birthday rewards, loyalty programs)
    gifts: [
      {
        id: 5,
        title: 'Regalo de Cumpleaños', // "Birthday gift"
        description: 'Entrada gratis durante tu semana de cumpleaños. Requiere registro.',
        icon: 'gift',
        conditions: [
          'Debe estar registrado en nuestro programa de fidelidad',
          'Válido 3 días antes y después de tu cumpleaños',
          'Una entrada gratis por cumpleaños',
          'Sujeto a disponibilidad'
        ],
        faqs: [
          { question: '¿Cómo me registro para el regalo de cumpleaños?', answer: 'Necesitas crear una cuenta en nuestro programa de fidelidad y verificar tu fecha de nacimiento.' },
          { question: '¿Necesito mostrar identificación?', answer: 'Sí, podemos solicitar identificación para verificar tu fecha de nacimiento.' }
        ]
      }
    ],
    // Credit card partnership promotions
    cards: [
      {
        id: 6,
        title: 'Promoción Tarjetas de Crédito', // "Credit card promotion"
        description: '15% de descuento al pagar con tarjetas bancarias participantes.',
        icon: 'card',
        conditions: [
          'Válido solo con tarjetas bancarias participantes',
          'El descuento se aplica solo a entradas',
          'No válido con otros descuentos',
          'El horario de promoción varía según el banco'
        ],
        faqs: [
          { question: '¿Qué bancos participan en esta promoción?', answer: 'Actualmente tenemos alianzas con BCP, BBVA e Interbank.' },
          { question: '¿Puedo usar esta promoción en línea?', answer: 'Sí, el descuento se aplicará automáticamente cuando pagues con una tarjeta elegible.' }
        ]
      }
    ]
  };

  // Function that filters and returns promotions based on the currently selected tab
  // If 'all' is selected, it combines all promotion categories into a single array
  const getFilteredPromotions = () => {
    if (activeTab === 'all') {
      return [
        ...promotions.tickets,   // Spread operator to merge all arrays
        ...promotions.combos,
        ...promotions.gifts,
        ...promotions.cards
      ];
    }
    // Return specific category array, or empty array if category doesn't exist  
    return promotions[activeTab] || [];
  };

  // Maps promotion icon types to corresponding FontAwesome CSS classes
  // Used to display appropriate icons for each promotion category
  const getIconClass = (iconType) => {
    switch (iconType) {
      case 'ticket':
        return 'fa fa-ticket';      // Ticket icon for ticket promotions
      case 'popcorn':
        return 'fa fa-film';        // Film icon for combo promotions
      case 'gift':
        return 'fa fa-gift';        // Gift icon for birthday/loyalty promotions
      case 'card':
        return 'fa fa-credit-card'; // Credit card icon for payment promotions
      default:
        return 'fa fa-star';        // Default star icon as fallback
    }
  };

  // State to track which promotion is currently selected for detailed view in modal
  // null means no modal is open, object means modal shows that promotion's details
  const [selectedPromotion, setSelectedPromotion] = useState(null);

  // Function to open modal with detailed view of selected promotion
  const openPromotionDetails = (promotion) => {
    setSelectedPromotion(promotion);
  };

  // Function to close the promotion details modal
  const closePromotionDetails = () => {
    setSelectedPromotion(null);
  };

  return (
    <div className="promotions-page">
      {/* Hero section with promotional slider/carousel */}
      <div className="promo-slider-container">
        <PromoSlider />
      </div>

      {/* Main promotions section with filtering tabs and grid display */}
      <div className="promotions-section">
        <h2>Promociones Disponibles</h2>
        
        {/* Tab navigation for filtering promotions by category */}
        <div className="promo-tabs">
          <button 
            className={`tab-button ${activeTab === 'all' ? 'active' : ''}`}
            onClick={() => setActiveTab('all')}
          >
            Todas
          </button>
          <button 
            className={`tab-button ${activeTab === 'tickets' ? 'active' : ''}`}
            onClick={() => setActiveTab('tickets')}
          >
            Boletos
          </button>
          <button 
            className={`tab-button ${activeTab === 'combos' ? 'active' : ''}`}
            onClick={() => setActiveTab('combos')}
          >
            Combos
          </button>
          {/* Note: This tab references 'specials' but that category doesn't exist in data */}
          <button 
            className={`tab-button ${activeTab === 'specials' ? 'active' : ''}`}
            onClick={() => setActiveTab('specials')}
          >
            Especiales
          </button>
        </div>

        {/* Grid layout displaying filtered promotion cards */}
        <div className="promotions-grid">
          {getFilteredPromotions().map((promo) => (
            <div key={promo.id} className="promo-card">
              {/* Icon section using FontAwesome classes */}
              <div className="promo-icon">
                <i className={getIconClass(promo.icon)}></i>
              </div>
              {/* Promotion content: title and description */}
              <div className="promo-content">
                <h3>{promo.title}</h3>
                <p>{promo.description}</p>
              </div>
              {/* Button to open detailed modal view */}
              <button 
                className="view-details-btn" 
                onClick={() => openPromotionDetails(promo)}
              >
                Ver detalles
              </button>
            </div>
          ))}
        </div>
      </div>

      {/* Modal overlay and content - only rendered when a promotion is selected */}
      {selectedPromotion && (
        <div className="promo-modal-overlay" onClick={closePromotionDetails}>
          {/* Modal content - stops propagation to prevent closing when clicking inside */}
          <div className="promo-modal" onClick={(e) => e.stopPropagation()}>
            {/* Close button using × symbol */}
            <button className="close-modal" onClick={closePromotionDetails}>&times;</button>
            <div className="modal-content">
              {/* Modal header with large icon and promotion title */}
              <div className="modal-header">
                <div className="promo-icon large">
                  <i className={getIconClass(selectedPromotion.icon)}></i>
                </div>
                <h2>{selectedPromotion.title}</h2>
              </div>
              
              {/* Modal body containing description, terms, and FAQs */}
              <div className="modal-body">
                {/* Promotion description */}
                <div className="promo-description-container">
                  <p className="promo-description">{selectedPromotion.description}</p>
                </div>
                
                {/* Terms and conditions list */}
                <div className="promo-conditions">
                  <h3>Términos y Condiciones</h3>
                  <ul>
                    {selectedPromotion.conditions.map((condition, index) => (
                      <li key={index}>{condition}</li>
                    ))}
                  </ul>
                </div>
                
                {/* FAQ section with questions and answers */}
                <div className="promo-faqs">
                  <h3>Preguntas Frecuentes</h3>
                  <div className="modal-faqs-grid">
                    {selectedPromotion.faqs.map((faq, index) => (
                      <div key={index} className="modal-faq-item">
                        <h4>{faq.question}</h4>
                        <p>{faq.answer}</p>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
              
              {/* Modal footer with call-to-action button */}
              <div className="modal-footer">
                <button className="redeem-btn">Canjear Oferta</button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* General FAQ section displayed at bottom of page */}
      {/* Contains common questions about promotions in horizontal layout */}
      <div className="faq-section">
        <h2>Preguntas Frecuentes</h2>
        <div className="faq-container horizontal">
          <div className="faq-item">
            <h3>¿Cómo canjeo las promociones?</h3>
            <p>Las promociones pueden ser canjeadas a través de nuestra app o sitio web. Simplemente selecciona la promoción que deseas usar durante el proceso de compra.</p>
          </div>
          <div className="faq-item">
            <h3>¿Puedo combinar múltiples promociones?</h3>
            <p>La mayoría de las promociones no se pueden combinar con otras ofertas. Por favor, consulta los términos y condiciones de cada promoción para más detalles.</p>
          </div>
          <div className="faq-item">
            <h3>¿Las promociones están disponibles en todas las sucursales?</h3>
            <p>Las promociones pueden variar según la ubicación. Por favor, consulta con tu cine local para conocer la disponibilidad específica.</p>
          </div>
          <div className="faq-item">
            <h3>¿Necesito una cuenta para acceder a las promociones?</h3>
            <p>Algunas promociones requieren que inicies sesión en una cuenta. Crear una cuenta es gratis y te da acceso a ofertas exclusivas.</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Promotions;