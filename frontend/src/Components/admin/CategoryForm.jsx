import { useState, useEffect } from "react"
import "../../styles/CategoryForm.css"

function CategoryForm({ category, onSave, onClose }) {
  const [formData, setFormData] = useState({
    name: "",
  })

  const [errors, setErrors] = useState({})

  useEffect(() => {
    if (category) {
      setFormData({
        name: category.name || "",
      })
    }
  }, [category])

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
      newErrors.name = "El nombre es requerido"
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

    // Prepare data according to API format
    const categoryData = {
      name: formData.name.trim(),
    }

    onSave(categoryData)
  }

  return (
    <div className="modal-overlay">
      <div className="modal-content category-modal">
        <div className="modal-header">
          <h3>{category ? "Editar Categoría" : "Agregar Nueva Categoría"}</h3>
          <button className="close-btn" onClick={onClose}>
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className="category-form">
          <div className="form-group">
            <label htmlFor="name">Nombre de la Categoría *</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className={errors.name ? "error" : ""}
              placeholder="Ej: Bebidas, Dulces, Snacks..."
            />
            {errors.name && <span className="error-message">{errors.name}</span>}
          </div>

          <div className="form-actions">
            <button type="button" className="cancel-btn" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="save-btn">
              {category ? "Actualizar" : "Guardar"} Categoría
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default CategoryForm
