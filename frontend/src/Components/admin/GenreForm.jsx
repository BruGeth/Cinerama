import { useState } from "react"
import "../../styles/GenreForm.css"

function GenreForm({ onSave, onClose }) {
  const [formData, setFormData] = useState({
    name: "",
  })

  const [errors, setErrors] = useState({})

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData({
      ...formData,
      [name]: value,
    })

    // Clear error when user types
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: "",
      })
    }
  }

  const validate = () => {
    const newErrors = {}

    if (!formData.name.trim()) {
      newErrors.name = "El nombre del género es requerido"
    }

    if (formData.name.trim().length < 2) {
      newErrors.name = "El nombre debe tener al menos 2 caracteres"
    }

    return newErrors
  }

  const handleSubmit = (e) => {
    e.preventDefault()

    const validationErrors = validate()
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors)
      return
    }

    const genreData = {
      name: formData.name.trim(),
    }

    onSave(genreData)
  }

  return (
    <div className="modal-overlay" onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className="modal-content genre-modal">
        <div className="modal-header">
          <h3>Agregar Nuevo Género</h3>
          <button className="close-btn" onClick={onClose}>
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className="genre-form">
          <div className="form-group">
            <label htmlFor="name">Nombre del Género *</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className={errors.name ? "error" : ""}
              placeholder="Ej: Ciencia Ficción, Acción, Drama"
              maxLength="50"
              autoFocus
            />
            {errors.name && <span className="error-message">{errors.name}</span>}
          </div>

          <div className="form-actions">
            <button type="button" className="cancel-btn" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="save-btn">
              Guardar Género
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default GenreForm
