import React, { useState } from 'react';
import '../styles/Promotions.css';
import PromoSlider from '../Components/PromoSlider';

const Promotions = () => {
  const [activeTab, setActiveTab] = useState('all');

  // Sample promotion data
  const promotions = {
    tickets: [
      {
        id: 1,
        title: 'Entradas 2x1',
        description: '¡Compra una entrada y llévate otra gratis! Válido solo los miércoles cuando compres a través de nuestra app o web.',
        icon: 'ticket',
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
        title: 'Descuento Estudiantil',
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
    combos: [
      {
        id: 3,
        title: 'Combo Familiar',
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
        title: 'Combo Noche de Cita',
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
    gifts: [
      {
        id: 5,
        title: 'Regalo de Cumpleaños',
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
    cards: [
      {
        id: 6,
        title: 'Promoción Tarjetas de Crédito',
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

  // Filter promotions based on active tab
  const getFilteredPromotions = () => {
    if (activeTab === 'all') {
      return [
        ...promotions.tickets,
        ...promotions.combos,
        ...promotions.gifts,
        ...promotions.cards
      ];
    }
    return promotions[activeTab] || [];
  };

  // Get icon class based on promotion type
  const getIconClass = (iconType) => {
    switch (iconType) {
      case 'ticket':
        return 'fa fa-ticket';
      case 'popcorn':
        return 'fa fa-film';
      case 'gift':
        return 'fa fa-gift';
      case 'card':
        return 'fa fa-credit-card';
      default:
        return 'fa fa-star';
    }
  };

  const [selectedPromotion, setSelectedPromotion] = useState(null);

  const openPromotionDetails = (promotion) => {
    setSelectedPromotion(promotion);
  };

  const closePromotionDetails = () => {
    setSelectedPromotion(null);
  };

  return (
    <div className="promotions-page">
      {/* Promo Slider */}
      <div className="promo-slider-container">
        <PromoSlider />
      </div>

      {/* Promotions Section */}
      <div className="promotions-section">
        <h2>Promociones Disponibles</h2>
        
        {/* Tabs */}
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
          <button 
            className={`tab-button ${activeTab === 'specials' ? 'active' : ''}`}
            onClick={() => setActiveTab('specials')}
          >
            Especiales
          </button>
        </div>

        {/* Promotions Grid */}
        <div className="promotions-grid">
          {getFilteredPromotions().map((promo) => (
            <div key={promo.id} className="promo-card">
              <div className="promo-icon">
                <i className={getIconClass(promo.icon)}></i>
              </div>
              <div className="promo-content">
                <h3>{promo.title}</h3>
                <p>{promo.description}</p>
              </div>
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

      {/* Promotion Details Modal */}
      {selectedPromotion && (
        <div className="promo-modal-overlay" onClick={closePromotionDetails}>
          <div className="promo-modal" onClick={(e) => e.stopPropagation()}>
            <button className="close-modal" onClick={closePromotionDetails}>&times;</button>
            <div className="modal-content">
              <div className="modal-header">
                <div className="promo-icon large">
                  <i className={getIconClass(selectedPromotion.icon)}></i>
                </div>
                <h2>{selectedPromotion.title}</h2>
              </div>
              
              <div className="modal-body">
                <div className="promo-description-container">
                  <p className="promo-description">{selectedPromotion.description}</p>
                </div>
                
                <div className="promo-conditions">
                  <h3>Términos y Condiciones</h3>
                  <ul>
                    {selectedPromotion.conditions.map((condition, index) => (
                      <li key={index}>{condition}</li>
                    ))}
                  </ul>
                </div>
                
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
              
              <div className="modal-footer">
                <button className="redeem-btn">Canjear Oferta</button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* FAQ Section - Reorganizada horizontalmente */}
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