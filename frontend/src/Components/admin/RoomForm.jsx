import { useState, useEffect } from "react";
import "../../styles/RoomForm.css";

function RoomForm({ room, cinemas, onSave, onClose }) {
  const [formData, setFormData] = useState({
    name: "",
    cinemaId: "",
    capacity: "",
    type: "STANDARD",
    format: "TWO_D",
    status: "ACTIVE",
  });

  const [errors, setErrors] = useState({});

  const formatOptions = [
    { value: "TWO_D", label: "2D" },
    { value: "THREE_D", label: "3D" },
    { value: "FOUR_D_X", label: "4DX" }
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
        format: room.format || "TWO_D",
        status: room.status || "ACTIVE",
      });
    }
  }, [room]);

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

    if (!formData.name.trim()) {
      newErrors.name = "El nombre es requerido";
    }

    if (!formData.cinemaId) {
      newErrors.cinemaId = "Debe seleccionar un cine";
    }

    if (!formData.capacity || Number.parseInt(formData.capacity) <= 0) {
      newErrors.capacity = "La capacidad debe ser mayor a 0";
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
      format: formData.format,
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
              <label htmlFor="type">Tipo de Sala</label>
              <select
                id="type"
                name="type"
                value={formData.type}
                onChange={handleChange}
              >
                {roomTypeOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="format">Formato</label>
              <select
                id="format"
                name="format"
                value={formData.format}
                onChange={handleChange}
              >
                {formatOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="status">Estado</label>
              <select
                id="status"
                name="status"
                value={formData.status}
                onChange={handleChange}
              >
                {statusOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </div>
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
