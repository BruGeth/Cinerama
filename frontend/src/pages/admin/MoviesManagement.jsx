import { useState, useEffect } from "react"
import AdminLayout from "../../layouts/AdminLayout"
import MovieForm from "../../components/admin/MovieForm"
import GenreForm from "../../components/admin/GenreForm"
import "../../styles/MoviesManagement.css"
import {
  fetchMovies,
  fetchMovieById,
  createMovie,
  updateMovie,
  deleteMovie as apiDeleteMovie,
} from "../../services/movieService"
import {
  fetchGenres,
  createGenre,
  deleteGenre as apiDeleteGenre,
} from "../../services/genreService"

function MoviesManagement() {
  const [movies, setMovies] = useState([])
  const [genres, setGenres] = useState([])
  const [showMovieForm, setShowMovieForm] = useState(false)
  const [showGenreForm, setShowGenreForm] = useState(false)
  const [editingMovie, setEditingMovie] = useState(null)
  const [loading, setLoading] = useState(true)
  const [activeTab, setActiveTab] = useState("movies")

  useEffect(() => {
    loadMovies()
    loadGenres()
  }, [])

  // Fetch movies from API
  const loadMovies = async () => {
    setLoading(true)
    try {
      const data = await fetchMovies()
      console.log("Películas cargadas:", data) // Para debug
      setMovies(data)
    } catch (err) {
      console.error("Error loading movies:", err)
      alert("Error loading movies")
    }
    setLoading(false)
  }

  // Fetch genres from API
  const loadGenres = async () => {
    try {
      const data = await fetchGenres()
      console.log("Géneros cargados:", data) // Para debug
      setGenres(data)
    } catch (err) {
      console.error("Error loading genres:", err)
    }
  }

  const handleAddMovie = () => {
    setEditingMovie(null)
    setShowMovieForm(true)
  }

  const handleEditMovie = async (movie) => {
    console.log("Editando película básica:", movie) // Para debug
    try {
      // Obtener datos completos de la película desde el backend
      const fullMovieData = await fetchMovieById(movie.id);
      console.log("Datos completos de película:", fullMovieData) // Para debug
      setEditingMovie(fullMovieData);
      setShowMovieForm(true);
    } catch (err) {
      console.error("Error loading full movie data:", err);
      // Fallback: usar los datos disponibles
      setEditingMovie(movie);
      setShowMovieForm(true);
    }
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
      setShowMovieForm(false)
      setEditingMovie(null)
      loadMovies()
    } catch (err) {
      alert("Error saving movie")
    }
  }

  const handleCloseMovieForm = () => {
    setShowMovieForm(false)
    setEditingMovie(null)
  }

  // Genre management functions
  const handleAddGenre = () => {
    setShowGenreForm(true)
  }

  const handleSaveGenre = async (genreData) => {
    try {
      // Verificar si el género ya existe
      if (genres.some(g => g.name.toLowerCase() === genreData.name.toLowerCase())) {
        alert("Este género ya existe")
        return
      }

      await createGenre(genreData)
      setShowGenreForm(false)
      loadGenres()
    } catch (err) {
      alert("Error saving genre")
    }
  }

  const handleCloseGenreForm = () => {
    setShowGenreForm(false)
  }

  const handleDeleteGenre = async (genreId, genreName) => {
    if (window.confirm(`¿Estás seguro de que quieres eliminar el género "${genreName}"?`)) {
      try {
        await apiDeleteGenre(genreId)
        setGenres(genres.filter((genre) => genre.id !== genreId))
      } catch (err) {
        alert("Error eliminando el género. Puede estar en uso por algunas películas.")
      }
    }
  }

  if (loading) {
    return (
      <AdminLayout>
        <div className="loading">Cargando datos...</div>
      </AdminLayout>
    )
  }

  const getStatusLabel = (status) => {
    switch (status) {
      case "NOW_PLAYING": return "En Cartelera"
      case "COMING_SOON": return "Próximamente"
      case "ENDED": return "Finalizada"
      case "UNKNOWN":
      case null:
      case undefined: return "Estado no definido"
      default: return status || "Estado no definido"
    }
  }

  const getGenreName = (movie) => {
    // El backend devuelve genreName directamente
    if (movie.genreName) {
      return movie.genreName;
    }
    
    // Fallback: buscar por genreId si existe
    let genreId = movie.genreId || movie.genre?.id || movie.genre;
    
    console.log("Buscando género para película:", movie.title, "genreId:", genreId, "genreName:", movie.genreName, "géneros disponibles:", genres);
    
    if (!genreId) {
      return "Sin género";
    }
    
    const genre = genres.find(g => g.id === genreId || g.id === Number(genreId));
    return genre ? genre.name : `Sin género (ID: ${genreId})`;
  }

  return (
    <AdminLayout>
      <div className="movies-management">
        <div className="page-header">
          <h2>Gestión de Películas y Géneros</h2>
        </div>

        <div className="tabs">
          <button 
            className={`tab ${activeTab === "movies" ? "active" : ""}`}
            onClick={() => setActiveTab("movies")}
          >
            Películas
          </button>
          <button 
            className={`tab ${activeTab === "genres" ? "active" : ""}`}
            onClick={() => setActiveTab("genres")}
          >
            Géneros
          </button>
        </div>

        {activeTab === "movies" && (
          <div className="movies-section">
            <div className="section-header">
              <h3>Gestión de Películas</h3>
              <button className="add-btn" onClick={handleAddMovie}>
                + Agregar Película
              </button>
            </div>

            <div className="movies-grid">
              {movies.map((movie) => (
                <div key={movie.id} className="movie-card">
                  <div className="movie-poster">
                    {movie.imageUrl ? (
                      <img src={movie.imageUrl} alt={movie.title} />
                    ) : (
                      <div className="movie-poster-placeholder">
                        <span className="movie-icon">🎬</span>
                      </div>
                    )}
                  </div>

                  <div className="movie-info">
                    <h3>{movie.title}</h3>
                    <p className="movie-genre">{getGenreName(movie)}</p>
                    <p className="movie-duration">{movie.duration} min</p>
                    <p className="movie-rating">Clasificación: {movie.rating}</p>
                    {movie.director && <p className="movie-director">Dir: {movie.director}</p>}
                    {movie.releaseDate && <p className="movie-release">Estreno: {movie.releaseDate}</p>}
                    <span className={`status-badge ${(movie.status || 'unknown')?.toLowerCase()}`}>
                      {getStatusLabel(movie.status || 'UNKNOWN')}
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
          </div>
        )}

        {activeTab === "genres" && (
          <div className="genres-section">
            <div className="section-header">
              <h3>Gestión de Géneros</h3>
              <button className="add-btn" onClick={handleAddGenre}>
                + Agregar Género
              </button>
            </div>

            <div className="genres-grid">
              {genres.length > 0 ? (
                genres.map((genre) => (
                  <div key={genre.id} className="genre-card">
                    <div className="genre-info">
                      <h3>{genre.name}</h3>
                      <p className="genre-id">ID: {genre.id}</p>
                    </div>
                    <div className="genre-actions">
                      <button 
                        className="delete-btn" 
                        onClick={() => handleDeleteGenre(genre.id, genre.name)}
                      >
                        🗑️ Eliminar
                      </button>
                    </div>
                  </div>
                ))
              ) : (
                <div className="no-items">
                  <p>No hay géneros registrados</p>
                  <button className="add-btn" onClick={handleAddGenre}>
                    Agregar el primer género
                  </button>
                </div>
              )}
            </div>
          </div>
        )}

        {showMovieForm && (
          <MovieForm 
            movie={editingMovie} 
            onSave={handleSaveMovie} 
            onClose={handleCloseMovieForm} 
          />
        )}

        {showGenreForm && (
          <GenreForm 
            onSave={handleSaveGenre} 
            onClose={handleCloseGenreForm} 
          />
        )}
      </div>
    </AdminLayout>
  )
}

export default MoviesManagement