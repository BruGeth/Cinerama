import { useState, useEffect } from "react";
import styles from "../../styles/TicketPriceForm.module.css";

function TicketPriceForm({ ticketPrice, showtimes, onSave, onClose }) {
  const [formData, setFormData] = useState({
    showtimeId: "",
    type: "GENERAL",
    format: "TWO_D",
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

  const formatOptions = [
    { value: "TWO_D", label: "2D" },
    { value: "THREE_D", label: "3D" },
    { value: "IMAX", label: "IMAX" },
    { value: "FOUR_D_X", label: "4DX" }
  ];

  useEffect(() => {
    if (ticketPrice) {
      console.log("Precio recibido para editar:", ticketPrice);
      setFormData({
        showtimeId: ticketPrice.showtimeId?.toString() || "",
        type: ticketPrice.type || "GENERAL",
        format: ticketPrice.format || "TWO_D",
        price: ticketPrice.price?.toString() || "",
      });
    }
  }, [ticketPrice]);

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
              >
                {formatOptions.map((option) => (
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
                    {formatOptions.find(f => f.value === formData.format)?.label}
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
