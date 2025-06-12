import { useState, useEffect } from "react"
import "../../styles/MovieForm.css"

function MovieForm({ movie, onSave, onClose }) {
  const [formData, setFormData] = useState({
    title: "",
    descriptionShowtimes: "",
    descriptionMovie: "",
    rating: "PG",
    genreId: "",
    imageUrl: "",
    duration: "",
    trailerUrl: "",
  })

  const [errors, setErrors] = useState({})

  useEffect(() => {
    if (movie) {
      setFormData({
        title: movie.title || "",
        descriptionShowtimes: movie.descriptionShowtimes || "",
        descriptionMovie: movie.descriptionMovie || "",
        rating: movie.rating || "PG",
        genreId: movie.genreId ? String(movie.genreId) : "",
        imageUrl: movie.imageUrl || "",
        duration: movie.duration || "",
        trailerUrl: movie.trailerUrl || "",
      })
    }
  }, [movie])

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

    if (!formData.title.trim()) {
      newErrors.title = "El título es requerido"
    }
    if (!formData.descriptionShowtimes.trim()) {
      newErrors.descriptionShowtimes = "La descripción de cartelera es requerida"
    }
    if (!formData.descriptionMovie.trim()) {
      newErrors.descriptionMovie = "La sinopsis es requerida"
    }
    if (!formData.rating.trim()) {
      newErrors.rating = "La clasificación es requerida"
    }
    if (!formData.genreId) {
      newErrors.genreId = "El género es requerido"
    }
    if (!formData.duration || formData.duration <= 0) {
      newErrors.duration = "La duración debe ser mayor a 0"
    }
    if (!formData.imageUrl.trim()) {
      newErrors.imageUrl = "La URL de la imagen es requerida"
    }
    // trailerUrl is optional

    return newErrors
  }

  const handleSubmit = (e) => {
    e.preventDefault()

    const validationErrors = validate()
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors)
      return
    }

    // genreId should be sent as a number
    onSave({ ...formData, genreId: Number(formData.genreId) })
  }

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header">
          <h3>{movie ? "Editar Película" : "Agregar Nueva Película"}</h3>
          <button className="close-btn" onClick={onClose}>
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className="movie-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="title">Título *</label>
              <input
                type="text"
                id="title"
                name="title"
                value={formData.title}
                onChange={handleChange}
                className={errors.title ? "error" : ""}
              />
              {errors.title && <span className="error-message">{errors.title}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="genreId">Género (ID) *</label>
              <input
                type="number"
                id="genreId"
                name="genreId"
                value={formData.genreId}
                onChange={handleChange}
                className={errors.genreId ? "error" : ""}
                min="1"
                placeholder="ID del género"
              />
              {errors.genreId && <span className="error-message">{errors.genreId}</span>}
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="duration">Duración (minutos) *</label>
              <input
                type="number"
                id="duration"
                name="duration"
                value={formData.duration}
                onChange={handleChange}
                min="1"
                className={errors.duration ? "error" : ""}
              />
              {errors.duration && <span className="error-message">{errors.duration}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="rating">Clasificación *</label>
              <select id="rating" name="rating" value={formData.rating} onChange={handleChange}>
                <option value="G">G - Apta para todo público</option>
                <option value="PG">PG - Se sugiere compañía de adultos</option>
                <option value="PG-13">PG-13 - Mayores de 13 años</option>
                <option value="R">R - Mayores de 17 años</option>
              </select>
              {errors.rating && <span className="error-message">{errors.rating}</span>}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="imageUrl">URL de la Imagen *</label>
            <input
              type="text"
              id="imageUrl"
              name="imageUrl"
              value={formData.imageUrl}
              onChange={handleChange}
              className={errors.imageUrl ? "error" : ""}
              placeholder="https://..."
            />
            {errors.imageUrl && <span className="error-message">{errors.imageUrl}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="descriptionShowtimes">Descripción para Cartelera *</label>
            <textarea
              id="descriptionShowtimes"
              name="descriptionShowtimes"
              value={formData.descriptionShowtimes}
              onChange={handleChange}
              rows="2"
              placeholder="Descripción corta para la cartelera..."
              className={errors.descriptionShowtimes ? "error" : ""}
            />
            {errors.descriptionShowtimes && (
              <span className="error-message">{errors.descriptionShowtimes}</span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="descriptionMovie">Sinopsis *</label>
            <textarea
              id="descriptionMovie"
              name="descriptionMovie"
              value={formData.descriptionMovie}
              onChange={handleChange}
              rows="4"
              placeholder="Describe brevemente la trama de la película..."
              className={errors.descriptionMovie ? "error" : ""}
            />
            {errors.descriptionMovie && (
              <span className="error-message">{errors.descriptionMovie}</span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="trailerUrl">URL del Trailer</label>
            <input
              type="text"
              id="trailerUrl"
              name="trailerUrl"
              value={formData.trailerUrl}
              onChange={handleChange}
              placeholder="https://..."
            />
          </div>

          <div className="form-actions">
            <button type="button" className="cancel-btn" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="save-btn">
              {movie ? "Actualizar" : "Guardar"} Película
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default MovieForm