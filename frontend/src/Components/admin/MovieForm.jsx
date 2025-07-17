import { useState, useEffect } from "react"
import { fetchGenres } from "../../services/genreService"
import "../../styles/MovieForm.css"

function MovieForm({ movie, onSave, onClose }) {
  const [formData, setFormData] = useState({
    title: "",
    descriptionShowtimes: "",
    descriptionMovie: "",
    duration: "",
    rating: "PG-13",
    genreId: "",
    imageUrl: "",
    trailerUrl: "",
    director: "",
    cast: [],
    releaseDate: "",
    status: "NOW_PLAYING",
  })

  const [errors, setErrors] = useState({})
  const [genres, setGenres] = useState([])
  const [loadingGenres, setLoadingGenres] = useState(true)
  const [castInput, setCastInput] = useState("")

  const ratingOptions = [
    { value: "G", label: "G - General Audiences" },
    { value: "PG", label: "PG - Parental Guidance" },
    { value: "PG-13", label: "PG-13 - Parents Strongly Cautioned" },
    { value: "R", label: "R - Restricted" },
    { value: "NC-17", label: "NC-17 - Adults Only" }
  ]

  const statusOptions = [
    { value: "NOW_PLAYING", label: "En Cartelera" },
    { value: "COMING_SOON", label: "Próximamente" },
    { value: "ENDED", label: "Finalizada" }
  ]

  // Cargar géneros al montar el componente
  useEffect(() => {
    const loadGenres = async () => {
      try {
        const genreList = await fetchGenres()
        setGenres(genreList)
      } catch (error) {
        console.error("Error loading genres:", error)
        setErrors({ general: "Error cargando los géneros" })
      } finally {
        setLoadingGenres(false)
      }
    }
    loadGenres()
  }, [])

  useEffect(() => {
    if (movie) {
      console.log("Película recibida para editar:", movie) // Para debug
      
      // Manejar diferentes formatos de genreId
      let genreId = movie.genreId || movie.genre?.id || "";
      
      // Si no tenemos genreId pero tenemos genreName, buscar el ID
      if (!genreId && movie.genreName && genres.length > 0) {
        const foundGenre = genres.find(g => g.name.toLowerCase() === movie.genreName.toLowerCase());
        if (foundGenre) {
          genreId = foundGenre.id;
        }
      }
      
      setFormData({
        title: movie.title || "",
        descriptionShowtimes: movie.descriptionShowtimes || "",
        descriptionMovie: movie.descriptionMovie || "",
        duration: movie.duration?.toString() || "",
        rating: movie.rating || "PG-13",
        genreId: genreId ? String(genreId) : "",
        imageUrl: movie.imageUrl || "",
        trailerUrl: movie.trailerUrl || "",
        director: movie.director || "",
        cast: movie.cast || [],
        releaseDate: movie.releaseDate || "",
        status: movie.status || "NOW_PLAYING",
      })
      setCastInput(movie.cast ? movie.cast.join(", ") : "")
    }
  }, [movie, genres])

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

  const handleCastChange = (e) => {
    const value = e.target.value
    setCastInput(value)
    
    // Convertir string separado por comas a array
    const castArray = value.split(",").map(actor => actor.trim()).filter(actor => actor.length > 0)
    setFormData({
      ...formData,
      cast: castArray
    })
  }

  const validate = () => {
    const newErrors = {}

    // CAMPOS REQUERIDOS
    if (!formData.title.trim()) {
      newErrors.title = "El título es requerido"
    }
    if (!formData.descriptionShowtimes.trim()) {
      newErrors.descriptionShowtimes = "La descripción de cartelera es requerida"
    }
    if (!formData.descriptionMovie.trim()) {
      newErrors.descriptionMovie = "La sinopsis es requerida"
    }
    if (!formData.duration || Number.parseInt(formData.duration) <= 0) {
      newErrors.duration = "La duración debe ser mayor a 0"
    }
    if (!formData.rating.trim()) {
      newErrors.rating = "La clasificación es requerida"
    }
    if (!formData.genreId) {
      newErrors.genreId = "El género es requerido"
    }
    if (!formData.imageUrl.trim()) {
      newErrors.imageUrl = "La URL de la imagen es requerida"
    }

    // Validar que rating esté en los valores permitidos
    if (formData.rating && !["G", "PG", "PG-13", "R", "NC-17"].includes(formData.rating)) {
      newErrors.rating = "Clasificación inválida"
    }

    // Validar que status esté en los valores permitidos
    if (formData.status && !["NOW_PLAYING", "COMING_SOON", "ENDED"].includes(formData.status)) {
      newErrors.status = "Estado inválido"
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

    const movieData = {
      title: formData.title.trim(),
      descriptionShowtimes: formData.descriptionShowtimes.trim(),
      descriptionMovie: formData.descriptionMovie.trim(),
      duration: Number.parseInt(formData.duration),
      rating: formData.rating,
      genreId: Number.parseInt(formData.genreId),
      imageUrl: formData.imageUrl.trim(),
      status: formData.status,
    }

    // Solo agregar campos opcionales si tienen valor
    if (formData.trailerUrl.trim()) {
      movieData.trailerUrl = formData.trailerUrl.trim()
    }
    
    if (formData.director.trim()) {
      movieData.director = formData.director.trim()
    }
    
    if (formData.releaseDate) {
      movieData.releaseDate = formData.releaseDate
    }
    
    if (formData.cast.length > 0) {
      movieData.cast = formData.cast
    }

    console.log("Datos a enviar:", movieData) // Para debug
    onSave(movieData)
  }

  return (
    <div className="modal-overlay" onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className="modal-content movie-modal">
        <div className="modal-header">
          <h3>{movie ? "Editar Película" : "Agregar Nueva Película"}</h3>
          <button className="close-btn" onClick={onClose}>
            ×
          </button>
        </div>

        {errors.general && (
          <div className="error-banner">{errors.general}</div>
        )}

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
                placeholder="Ej: Avatar: El Camino del Agua"
              />
              {errors.title && <span className="error-message">{errors.title}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="genreId">Género *</label>
              {loadingGenres ? (
                <select disabled>
                  <option>Cargando géneros...</option>
                </select>
              ) : (
                <select
                  id="genreId"
                  name="genreId"
                  value={formData.genreId}
                  onChange={handleChange}
                  className={errors.genreId ? "error" : ""}
                >
                  <option value="">Seleccionar género</option>
                  {genres.map((genre) => (
                    <option key={genre.id} value={genre.id}>
                      {genre.name}
                    </option>
                  ))}
                </select>
              )}
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
                placeholder="192"
              />
              {errors.duration && <span className="error-message">{errors.duration}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="rating">Clasificación *</label>
              <select 
                id="rating" 
                name="rating" 
                value={formData.rating} 
                onChange={handleChange}
                className={errors.rating ? "error" : ""}
              >
                {ratingOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              {errors.rating && <span className="error-message">{errors.rating}</span>}
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="director">Director</label>
              <input
                type="text"
                id="director"
                name="director"
                value={formData.director}
                onChange={handleChange}
                placeholder="Ej: James Cameron"
              />
            </div>

            <div className="form-group">
              <label htmlFor="releaseDate">Fecha de Estreno</label>
              <input
                type="date"
                id="releaseDate"
                name="releaseDate"
                value={formData.releaseDate}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="cast">Reparto (separado por comas)</label>
            <input
              type="text"
              id="cast"
              name="cast"
              value={castInput}
              onChange={handleCastChange}
              placeholder="Ej: Sam Worthington, Zoe Saldana, Sigourney Weaver"
            />
          </div>

          <div className="form-group">
            <label htmlFor="imageUrl">URL de la Imagen *</label>
            <input
              type="url"
              id="imageUrl"
              name="imageUrl"
              value={formData.imageUrl}
              onChange={handleChange}
              className={errors.imageUrl ? "error" : ""}
              placeholder="https://ejemplo.com/poster.jpg"
            />
            {errors.imageUrl && <span className="error-message">{errors.imageUrl}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="trailerUrl">URL del Trailer</label>
            <input
              type="url"
              id="trailerUrl"
              name="trailerUrl"
              value={formData.trailerUrl}
              onChange={handleChange}
              placeholder="https://youtube.com/watch?v=..."
            />
          </div>

          <div className="form-group">
            <label htmlFor="descriptionShowtimes">Descripción para Cartelera *</label>
            <textarea
              id="descriptionShowtimes"
              name="descriptionShowtimes"
              value={formData.descriptionShowtimes}
              onChange={handleChange}
              rows="2"
              placeholder="Descripción corta para mostrar en la cartelera..."
              className={errors.descriptionShowtimes ? "error" : ""}
            />
            {errors.descriptionShowtimes && (
              <span className="error-message">{errors.descriptionShowtimes}</span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="descriptionMovie">Sinopsis Completa *</label>
            <textarea
              id="descriptionMovie"
              name="descriptionMovie"
              value={formData.descriptionMovie}
              onChange={handleChange}
              rows="4"
              placeholder="Sinopsis completa de la película..."
              className={errors.descriptionMovie ? "error" : ""}
            />
            {errors.descriptionMovie && (
              <span className="error-message">{errors.descriptionMovie}</span>
            )}
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
            {errors.status && <span className="error-message">{errors.status}</span>}
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
