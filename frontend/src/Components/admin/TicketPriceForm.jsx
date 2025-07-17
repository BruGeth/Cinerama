import { useState, useEffect, useMemo } from "react";
import styles from "../../styles/TicketPriceForm.module.css";

// Opciones estáticas fuera del componente para evitar recreaciones
const ALL_FORMAT_OPTIONS = [
  { value: "TWO_D", label: "2D" },
  { value: "THREE_D", label: "3D" },
  { value: "IMAX", label: "IMAX" },
  { value: "FOUR_D_X", label: "4DX" }
];

function TicketPriceForm({ ticketPrice, showtimes, onSave, onClose }) {
  const [formData, setFormData] = useState({
    showtimeId: "",
    type: "GENERAL",
    format: "",
    price: "",
  });

  const [errors, setErrors] = useState({});

  const ticketTypeOptions = [
    { value: "GENERAL", label: "General/Adulto", icon: "👤" },
    { value: "CHILD", label: "Niños", icon: "🧒" },
    { value: "STUDENT", label: "Estudiantes", icon: "🎓" },
    { value: "SENIOR", label: "Tercera Edad", icon: "👴" },
    { value: "VIP", label: "VIP", icon: "⭐" }
  ];

  // Obtener los formatos disponibles basado en el showtime seleccionado
  const availableFormats = useMemo(() => {
    if (!formData.showtimeId) return [];
    
    const selectedShowtime = showtimes.find(s => s.id === Number.parseInt(formData.showtimeId));
    if (!selectedShowtime) return [];

    console.log("Showtime seleccionado para formatos:", selectedShowtime);

    // Si el showtime tiene una sala con tecnologías específicas, filtrar formatos
    const room = selectedShowtime.room;
    if (room && room.technology && Array.isArray(room.technology)) {
      console.log("Tecnologías de la sala:", room.technology);
      
      // Mapear tecnologías de sala a formatos
      const technologyToFormatMap = {
        "TWO_D": "TWO_D",
        "THREE_D": "THREE_D",
        "IMAX": "IMAX",
        "FOUR_DX": "FOUR_D_X"
      };

      const filteredFormats = ALL_FORMAT_OPTIONS.filter(format => 
        room.technology.some(tech => technologyToFormatMap[tech] === format.value)
      );
      
      console.log("Formatos filtrados por tecnología:", filteredFormats);
      return filteredFormats;
    }

    // Si no hay información de tecnología en la sala, mostrar el formato del showtime
    if (selectedShowtime.format) {
      console.log("Usando formato del showtime:", selectedShowtime.format);
      return ALL_FORMAT_OPTIONS.filter(format => format.value === selectedShowtime.format);
    }

    // Fallback: mostrar todos los formatos
    console.log("Fallback: mostrando todos los formatos");
    return ALL_FORMAT_OPTIONS;
  }, [formData.showtimeId, showtimes]);

  useEffect(() => {
    if (ticketPrice) {
      console.log("Precio recibido para editar:", ticketPrice);
      setFormData({
        showtimeId: ticketPrice.showtimeId?.toString() || "",
        type: ticketPrice.type || "GENERAL",
        format: ticketPrice.format || "",
        price: ticketPrice.price?.toString() || "",
      });
    } else {
      // Reset form para nuevo precio
      setFormData({
        showtimeId: "",
        type: "GENERAL",
        format: "",
        price: "",
      });
    }
  }, [ticketPrice]);

  // Actualizar formato cuando se selecciona un showtime
  useEffect(() => {
    if (formData.showtimeId && !ticketPrice) {
      // Solo para nuevos precios (no al editar)
      const selectedShowtime = showtimes.find(s => s.id === Number.parseInt(formData.showtimeId));
      if (selectedShowtime && availableFormats.length > 0) {
        // Si el showtime tiene un formato específico, usarlo
        if (selectedShowtime.format && availableFormats.some(f => f.value === selectedShowtime.format)) {
          setFormData(prev => ({ ...prev, format: selectedShowtime.format }));
        } else {
          // Usar el primer formato disponible
          setFormData(prev => ({ ...prev, format: availableFormats[0].value }));
        }
      }
    } else if (!formData.showtimeId && !ticketPrice) {
      // Limpiar formato si no hay showtime seleccionado
      setFormData(prev => ({ ...prev, format: "" }));
    }
  }, [formData.showtimeId, availableFormats, showtimes, ticketPrice]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });

    // Clear error when user types
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: "",
      });
    }
  };

  const validate = () => {
    const newErrors = {};

    // CAMPOS REQUERIDOS
    if (!formData.showtimeId) {
      newErrors.showtimeId = "Debe seleccionar un horario";
    }
    if (!formData.type) {
      newErrors.type = "Debe seleccionar un tipo de ticket";
    }
    if (!formData.format) {
      newErrors.format = "Debe seleccionar un formato";
    }
    if (!formData.price) {
      newErrors.price = "El precio es requerido";
    }

    // Validar precio
    if (formData.price) {
      const price = Number.parseFloat(formData.price);
      if (isNaN(price) || price <= 0) {
        newErrors.price = "El precio debe ser mayor a 0";
      } else if (price < 0.01) {
        newErrors.price = "El precio mínimo es S/ 0.01";
      }
    }

    // Validar enum type
    if (formData.type && !["GENERAL", "CHILD", "STUDENT", "SENIOR", "VIP"].includes(formData.type)) {
      newErrors.type = "Tipo de ticket inválido";
    }

    // Validar enum format
    if (formData.format && !["TWO_D", "THREE_D", "IMAX", "FOUR_D_X"].includes(formData.format)) {
      newErrors.format = "Formato inválido";
    }

    return newErrors;
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    const ticketPriceData = {
      showtimeId: Number.parseInt(formData.showtimeId),
      type: formData.type,
      format: formData.format,
      price: Number.parseFloat(formData.price),
    };

    console.log("Datos de precio a enviar:", ticketPriceData);
    onSave(ticketPriceData);
  };

  const getShowtimeDisplay = (showtime) => {
    const movie = showtime.movie || showtime;
    const room = showtime.room || {};
    const date = showtime.showDate || showtime.date || "";
    const time = showtime.showTime || showtime.time || "";
    
    return `${movie.title} - ${date} ${time} (${room.name || 'Sala'})`;
  };

  const getSelectedShowtime = () => {
    return showtimes.find(s => s.id === Number.parseInt(formData.showtimeId));
  };

  return (
    <div className={styles.modalOverlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={`${styles.modalContent} ${styles.ticketPriceModal}`}>
        <div className={styles.modalHeader}>
          <h3>{ticketPrice ? "Editar Precio" : "Agregar Precio de Ticket"}</h3>
          <button className={styles.closeBtn} onClick={onClose}>
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className={styles.ticketPriceForm}>
          <div className={styles.formGroup}>
            <label htmlFor="showtimeId">Horario *</label>
            <select
              id="showtimeId"
              name="showtimeId"
              value={formData.showtimeId}
              onChange={handleChange}
              className={errors.showtimeId ? styles.error : ""}
            >
              <option value="">Seleccionar horario</option>
              {showtimes.map((showtime) => (
                <option key={showtime.id} value={showtime.id}>
                  {getShowtimeDisplay(showtime)}
                </option>
              ))}
            </select>
            {errors.showtimeId && (
              <span className={styles.errorMessage}>{errors.showtimeId}</span>
            )}
          </div>

          <div className={styles.formRow}>
            <div className={styles.formGroup}>
              <label htmlFor="type">Tipo de Ticket *</label>
              <select
                id="type"
                name="type"
                value={formData.type}
                onChange={handleChange}
                className={errors.type ? styles.error : ""}
              >
                {ticketTypeOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.icon} {option.label}
                  </option>
                ))}
              </select>
              {errors.type && (
                <span className={styles.errorMessage}>{errors.type}</span>
              )}
            </div>

            <div className={styles.formGroup}>
              <label htmlFor="format">Formato *</label>
              <select
                id="format"
                name="format"
                value={formData.format}
                onChange={handleChange}
                className={errors.format ? styles.error : ""}
                disabled={!formData.showtimeId || availableFormats.length === 0}
              >
                <option value="">
                  {!formData.showtimeId 
                    ? "Selecciona un horario primero" 
                    : availableFormats.length === 0
                      ? "No hay formatos disponibles"
                      : "Seleccionar formato"
                  }
                </option>
                {availableFormats.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              {errors.format && (
                <span className={styles.errorMessage}>{errors.format}</span>
              )}
            </div>
          </div>

          <div className={styles.formGroup}>
            <label htmlFor="price">Precio (S/) *</label>
            <div className={styles.priceInputWrapper}>
              <span className={styles.currency}>S/</span>
              <input
                type="number"
                id="price"
                name="price"
                value={formData.price}
                onChange={handleChange}
                className={errors.price ? styles.error : ""}
                placeholder="15.50"
                step="0.01"
                min="0.01"
              />
            </div>
            {errors.price && (
              <span className={styles.errorMessage}>{errors.price}</span>
            )}
          </div>

          {/* Preview Section */}
          {formData.showtimeId && formData.type && formData.format && formData.price && (
            <div className={styles.previewSection}>
              <h4>Vista Previa</h4>
              <div className={styles.previewDetails}>
                <div className={styles.previewItem}>
                  <span className={styles.previewLabel}>Horario:</span>
                  <span className={styles.previewValue}>
                    {getSelectedShowtime() ? getShowtimeDisplay(getSelectedShowtime()) : ""}
                  </span>
                </div>
                <div className={styles.previewItem}>
                  <span className={styles.previewLabel}>Tipo:</span>
                  <span className={styles.previewValue}>
                    {ticketTypeOptions.find(t => t.value === formData.type)?.icon} {" "}
                    {ticketTypeOptions.find(t => t.value === formData.type)?.label}
                  </span>
                </div>
                <div className={styles.previewItem}>
                  <span className={styles.previewLabel}>Formato:</span>
                  <span className={styles.previewValue}>
                    {ALL_FORMAT_OPTIONS.find(f => f.value === formData.format)?.label}
                  </span>
                </div>
                <div className={styles.previewItem}>
                  <span className={styles.previewLabel}>Precio:</span>
                  <span className={styles.previewPrice}>
                    S/ {Number.parseFloat(formData.price).toFixed(2)}
                  </span>
                </div>
              </div>
            </div>
          )}

          <div className={styles.formActions}>
            <button type="button" className={styles.cancelBtn} onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className={styles.saveBtn}>
              {ticketPrice ? "Actualizar" : "Guardar"} Precio
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default TicketPriceForm;
