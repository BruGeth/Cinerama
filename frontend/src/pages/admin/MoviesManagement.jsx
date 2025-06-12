import { useState, useEffect } from "react"
import AdminLayout from "../../layouts/AdminLayout"
import MovieForm from "../../components/admin/MovieForm"
import "../../styles/MoviesManagement.css"
import {
  fetchMovies,
  createMovie,
  updateMovie,
  deleteMovie as apiDeleteMovie,
} from "../../services/movieService" // Import API service

function MoviesManagement() {
  const [movies, setMovies] = useState([])
  const [showForm, setShowForm] = useState(false)
  const [editingMovie, setEditingMovie] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadMovies()
  }, [])

  // Fetch movies from API
  const loadMovies = async () => {
    setLoading(true)
    try {
      const data = await fetchMovies()
      setMovies(data)
    } catch (err) {
      alert("Error loading movies")
    }
    setLoading(false)
  }

  const handleAddMovie = () => {
    setEditingMovie(null)
    setShowForm(true)
  }

  const handleEditMovie = (movie) => {
    setEditingMovie(movie)
    setShowForm(true)
  }

  const handleDeleteMovie = async (movieId) => {
    if (window.confirm("¿Estás seguro de que quieres eliminar esta película?")) {
      try {
        await apiDeleteMovie(movieId)
        setMovies(movies.filter((movie) => movie.id !== movieId))
      } catch (err) {
        alert("Error deleting movie")
      }
    }
  }

  const handleSaveMovie = async (movieData) => {
    try {
      if (editingMovie) {
        // Update existing movie
        await updateMovie(editingMovie.id, movieData)
      } else {
        // Add new movie
        await createMovie(movieData)
      }
      setShowForm(false)
      setEditingMovie(null)
      loadMovies()
    } catch (err) {
      alert("Error saving movie")
    }
  }

  const handleCloseForm = () => {
    setShowForm(false)
    setEditingMovie(null)
  }

  if (loading) {
    return (
      <AdminLayout>
        <div className="loading">Cargando películas...</div>
      </AdminLayout>
    )
  }

  return (
    <AdminLayout>
      <div className="movies-management">
        <div className="page-header">
          <h2>Gestión de Películas</h2>
          <button className="add-btn" onClick={handleAddMovie}>
            + Agregar Película
          </button>
        </div>

        <div className="movies-grid">
          {movies.map((movie) => (
            <div key={movie.id} className="movie-card">
              <div className="movie-poster-placeholder">
                <span className="movie-icon">🎬</span>
              </div>

              <div className="movie-info">
                <h3>{movie.title}</h3>
                <p className="movie-genre">{movie.genreName || movie.genre}</p>
                <p className="movie-duration">{movie.duration} min</p>
                <p className="movie-rating">Clasificación: {movie.rating}</p>
                <span className={`status-badge ${movie.status}`}>
                  {movie.status === "cartelera" ? "En Cartelera" : "Próximamente"}
                </span>
              </div>

              <div className="movie-actions">
                <button className="edit-btn" onClick={() => handleEditMovie(movie)}>
                  ✏️ Editar
                </button>
                <button className="delete-btn" onClick={() => handleDeleteMovie(movie.id)}>
                  🗑️ Eliminar
                </button>
              </div>
            </div>
          ))}
        </div>

        {showForm && <MovieForm movie={editingMovie} onSave={handleSaveMovie} onClose={handleCloseForm} />}
      </div>
    </AdminLayout>
  )
}

export default MoviesManagement