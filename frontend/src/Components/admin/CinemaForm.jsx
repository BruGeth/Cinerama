import { useState, useEffect } from "react";
import "../../styles/CinemaForm.css";

function CinemaForm({ cinema, onSave, onClose }) {
  const [formData, setFormData] = useState({
    name: "",
    address: "",
    phone: "",
    email: "",
    status: "ACTIVE",
    capacity: "",
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (cinema) {
      setFormData({
        name: cinema.name || "",
        address: cinema.address || "",
        phone: cinema.phone || "",
        email: cinema.email || "",
        status: cinema.status || "ACTIVE",
        capacity: cinema.capacity?.toString() || "",
      });
    }
  }, [cinema]);

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

    if (!formData.address.trim()) {
      newErrors.address = "La dirección es requerida";
    }

    if (!formData.phone.trim()) {
      newErrors.phone = "El teléfono es requerido";
    }

    if (!formData.email.trim()) {
      newErrors.email = "El email es requerido";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "El email no es válido";
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

    const cinemaData = {
      name: formData.name.trim(),
      address: formData.address.trim(),
      phone: formData.phone.trim(),
      email: formData.email.trim(),
      status: formData.status,
      capacity: Number.parseInt(formData.capacity),
    };

    onSave(cinemaData);
  };

  return (
    <div className="modal-overlay" onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className="modal-content cinema-modal">
        <div className="modal-header">
          <h3>{cinema ? "Editar Cine" : "Agregar Nuevo Cine"}</h3>
          <button className="close-btn" onClick={onClose}>
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className="cinema-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="name">Nombre del Cine *</label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className={errors.name ? "error" : ""}
                placeholder="Ej: Cinerama Miraflores"
              />
              {errors.name && (
                <span className="error-message">{errors.name}</span>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="capacity">Capacidad Total *</label>
              <input
                type="number"
                id="capacity"
                name="capacity"
                value={formData.capacity}
                onChange={handleChange}
                className={errors.capacity ? "error" : ""}
                placeholder="Ej: 250"
                min="1"
              />
              {errors.capacity && (
                <span className="error-message">{errors.capacity}</span>
              )}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="address">Dirección *</label>
            <input
              type="text"
              id="address"
              name="address"
              value={formData.address}
              onChange={handleChange}
              className={errors.address ? "error" : ""}
              placeholder="Ej: Av. José Larco 1232, Miraflores"
            />
            {errors.address && (
              <span className="error-message">{errors.address}</span>
            )}
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="phone">Teléfono *</label>
              <input
                type="tel"
                id="phone"
                name="phone"
                value={formData.phone}
                onChange={handleChange}
                className={errors.phone ? "error" : ""}
                placeholder="+51 1 234-5678"
              />
              {errors.phone && (
                <span className="error-message">{errors.phone}</span>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="email">Email *</label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                className={errors.email ? "error" : ""}
                placeholder="cine@cinerama.pe"
              />
              {errors.email && (
                <span className="error-message">{errors.email}</span>
              )}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="status">Estado</label>
            <select
              id="status"
              name="status"
              value={formData.status}
              onChange={handleChange}
            >
              <option value="ACTIVE">Activo</option>
              <option value="INACTIVE">Inactivo</option>
            </select>
          </div>

          <div className="form-actions">
            <button type="button" className="cancel-btn" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="save-btn">
              {cinema ? "Actualizar" : "Guardar"} Cine
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default CinemaForm;
