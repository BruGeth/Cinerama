import { useState, useEffect } from "react";
import "../../styles/RoomForm.css";

function RoomForm({ room, cinemas, onSave, onClose }) {
  const [formData, setFormData] = useState({
    name: "",
    cinemaId: "",
    capacity: "",
    type: "STANDARD",
    technology: [],
    audioSystem: "",
    status: "ACTIVE",
  });

  const [errors, setErrors] = useState({});

  const technologyOptions = [
    { value: "TWO_D", label: "2D" },
    { value: "THREE_D", label: "3D" },
    { value: "IMAX", label: "IMAX" },
    { value: "FOUR_DX", label: "4DX" }
  ];

  const roomTypeOptions = [
    { value: "STANDARD", label: "Standard" },
    { value: "VIP", label: "VIP" },
    { value: "IMAX", label: "IMAX" }
  ];

  const statusOptions = [
    { value: "ACTIVE", label: "Activa" },
    { value: "INACTIVE", label: "Inactiva" },
    { value: "MAINTENANCE", label: "En Mantenimiento" }
  ];

  useEffect(() => {
    if (room) {
      setFormData({
        name: room.name || "",
        cinemaId: room.cinema?.id?.toString() || room.cinemaId?.toString() || "",
        capacity: room.capacity?.toString() || "",
        type: room.type || "STANDARD",
        technology: room.technology || [],
        audioSystem: room.audioSystem || "",
        status: room.status || "ACTIVE",
      });
    }
  }, [room]);

  const handleChange = (e) => {
    const { name, value, checked } = e.target;
    
    if (name === 'technology') {
      // Manejar checkboxes para technology
      setFormData(prev => {
        const newTechnology = checked
          ? [...prev.technology, value]
          : prev.technology.filter(tech => tech !== value);
        return {
          ...prev,
          technology: newTechnology
        };
      });
    } else {
      setFormData({
        ...formData,
        [name]: value,
      });
    }

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
    if (!formData.name.trim()) {
      newErrors.name = "El nombre es requerido";
    }

    if (!formData.cinemaId) {
      newErrors.cinemaId = "Debe seleccionar un cine";
    }

    if (!formData.capacity || Number.parseInt(formData.capacity) <= 0) {
      newErrors.capacity = "La capacidad debe ser mayor a 0";
    }

    if (!formData.type) {
      newErrors.type = "Debe seleccionar un tipo de sala";
    }

    if (!formData.status) {
      newErrors.status = "Debe seleccionar un estado";
    }

    // Validar que type esté en los valores permitidos
    if (formData.type && !["STANDARD", "VIP", "IMAX"].includes(formData.type)) {
      newErrors.type = "Tipo de sala inválido";
    }

    // Validar que status esté en los valores permitidos
    if (formData.status && !["ACTIVE", "INACTIVE", "MAINTENANCE"].includes(formData.status)) {
      newErrors.status = "Estado inválido";
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

    const roomData = {
      name: formData.name.trim(),
      capacity: Number.parseInt(formData.capacity),
      type: formData.type,
      technology: formData.technology,
      audioSystem: formData.audioSystem.trim() || null,
      status: formData.status,
      cinemaId: Number.parseInt(formData.cinemaId),
      // Datos calculados automáticamente:
      totalSeats: Number.parseInt(formData.capacity),
      availableSeats: Number.parseInt(formData.capacity),
    };

    onSave(roomData);
  };

  return (
    <div className="modal-overlay" onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className="modal-content room-modal">
        <div className="modal-header">
          <h3>{room ? "Editar Sala" : "Agregar Nueva Sala"}</h3>
          <button className="close-btn" onClick={onClose}>
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className="room-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="name">Nombre de la Sala *</label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className={errors.name ? "error" : ""}
                placeholder="Ej: Sala 1, Sala VIP"
              />
              {errors.name && (
                <span className="error-message">{errors.name}</span>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="cinemaId">Cine *</label>
              <select
                id="cinemaId"
                name="cinemaId"
                value={formData.cinemaId}
                onChange={handleChange}
                className={errors.cinemaId ? "error" : ""}
              >
                <option value="">Seleccionar cine</option>
                {cinemas.map((cinema) => (
                  <option key={cinema.id} value={cinema.id}>
                    {cinema.name}
                  </option>
                ))}
              </select>
              {errors.cinemaId && (
                <span className="error-message">{errors.cinemaId}</span>
              )}
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="capacity">Capacidad (asientos) *</label>
              <input
                type="number"
                id="capacity"
                name="capacity"
                value={formData.capacity}
                onChange={handleChange}
                min="1"
                className={errors.capacity ? "error" : ""}
                placeholder="150"
              />
              {errors.capacity && (
                <span className="error-message">{errors.capacity}</span>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="type">Tipo de Sala *</label>
              <select
                id="type"
                name="type"
                value={formData.type}
                onChange={handleChange}
                className={errors.type ? "error" : ""}
              >
                {roomTypeOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              {errors.type && (
                <span className="error-message">{errors.type}</span>
              )}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="audioSystem">Sistema de Audio</label>
            <input
              type="text"
              id="audioSystem"
              name="audioSystem"
              value={formData.audioSystem}
              onChange={handleChange}
              placeholder="Ej: Dolby Atmos, DTS:X"
            />
          </div>

          <div className="form-group">
            <label>Tecnologías Disponibles</label>
            <div className="checkbox-group">
              {technologyOptions.map((option) => (
                <label key={option.value} className="checkbox-label">
                  <input
                    type="checkbox"
                    name="technology"
                    value={option.value}
                    checked={formData.technology.includes(option.value)}
                    onChange={handleChange}
                  />
                  <span className="checkbox-text">{option.label}</span>
                </label>
              ))}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="status">Estado *</label>
            <select
              id="status"
              name="status"
              value={formData.status}
              onChange={handleChange}
              className={errors.status ? "error" : ""}
            >
              {statusOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
            {errors.status && (
              <span className="error-message">{errors.status}</span>
            )}
          </div>

          <div className="form-actions">
            <button type="button" className="cancel-btn" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="save-btn">
              {room ? "Actualizar" : "Guardar"} Sala
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default RoomForm;
