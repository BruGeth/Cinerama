"use client";

import { useState, useEffect } from "react";
import "../../styles/ProductForm.css";

function ProductForm({ product, categories, onSave, onClose }) {
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    price: "",
    image: "",
    categoryId: "",
    stock: "",
    stockUnit: "unidades",
  })

  const [errors, setErrors] = useState({})

  useEffect(() => {
    if (product) {
      setFormData({
        name: product.name || "",
        description: product.description || "",
        price: product.price || "",
        image: product.image || "",
        categoryId: product.category?.id?.toString() || "",
        stock: product.stock || "",
        stockUnit: product.stockUnit || "unidades",
      })
    }
  }, [product])

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

    if (!formData.price || Number.parseFloat(formData.price) <= 0) {
      newErrors.price = "El precio debe ser mayor a 0"
    }

    if (!formData.categoryId) {
      newErrors.categoryId = "Debe seleccionar una categoría"
    }

    if (formData.stock && Number.parseInt(formData.stock) < 0) {
      newErrors.stock = "El stock debe ser mayor o igual a 0"
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

    // Convert numeric fields and prepare data according to API format
    const productData = {
      name: formData.name.trim(),
      description: formData.description.trim() || null,
      price: Number.parseFloat(formData.price),
      image: formData.image.trim() || null,
      categoryId: Number.parseInt(formData.categoryId),
      stock: formData.stock ? Number.parseInt(formData.stock) : 0,
      stockUnit: formData.stockUnit || "unidades",
    }

    onSave(productData)
  }

  return (
    <div className="modal-overlay">
      <div className="modal-content product-modal">
        <div className="modal-header">
          <h3>{product ? "Editar Producto" : "Agregar Nuevo Producto"}</h3>
          <button className="close-btn" onClick={onClose}>
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className="product-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="name">Nombre del Producto *</label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className={errors.name ? "error" : ""}
                placeholder="Ej: Palomitas Grandes"
              />
              {errors.name && <span className="error-message">{errors.name}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="categoryId">Categoría *</label>
              <select
                id="categoryId"
                name="categoryId"
                value={formData.categoryId}
                onChange={handleChange}
                className={errors.categoryId ? "error" : ""}
              >
                <option value="">Seleccionar categoría</option>
                {categories.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </select>
              {errors.categoryId && <span className="error-message">{errors.categoryId}</span>}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="description">Descripción</label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              rows="3"
              placeholder="Describe el producto..."
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="price">Precio (S/) *</label>
              <input
                type="number"
                id="price"
                name="price"
                value={formData.price}
                onChange={handleChange}
                step="0.01"
                min="0"
                className={errors.price ? "error" : ""}
                placeholder="0.00"
              />
              {errors.price && <span className="error-message">{errors.price}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="image">URL de Imagen</label>
              <input
                type="text"
                id="image"
                name="image"
                value={formData.image}
                onChange={handleChange}
                placeholder="https://ejemplo.com/imagen.jpg"
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="stock">Stock</label>
              <input
                type="number"
                id="stock"
                name="stock"
                value={formData.stock}
                onChange={handleChange}
                min="0"
                className={errors.stock ? "error" : ""}
                placeholder="0"
              />
              {errors.stock && <span className="error-message">{errors.stock}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="stockUnit">Unidad de Stock</label>
              <select id="stockUnit" name="stockUnit" value={formData.stockUnit} onChange={handleChange}>
                <option value="unidades">Unidades</option>
                <option value="kg">Kilogramos</option>
                <option value="litros">Litros</option>
                <option value="paquetes">Paquetes</option>
                <option value="cajas">Cajas</option>
              </select>
            </div>
          </div>

          <div className="form-actions">
            <button type="button" className="cancel-btn" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="save-btn">
              {product ? "Actualizar" : "Guardar"} Producto
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default ProductForm