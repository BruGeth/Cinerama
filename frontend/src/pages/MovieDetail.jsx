import { useParams } from 'react-router-dom'; // Hook to retrieve URL parameters
import { movies } from '../components/DataMovie'; // Import movie data from external file
import '../styles/MovieDetail.css'; // Import CSS styles for layout and design

const MovieDetail = () => {
  const { id } = useParams(); // Extract movie ID from URL parameters
  const movie = movies.find((m) => m.id === Number(id)); // Find movie by ID

  // If movie is not found, display an error message
  if (!movie) return <h2 style={{ color: 'white' }}>Película no encontrada</h2>;

  // Generate an array of the next 7 days for date selection
  const days = Array.from({ length: 7 }, (_, i) => {
    const date = new Date();
    date.setDate(date.getDate() + i);
    return {
      short: date.toLocaleDateString('es-PE', {
        weekday: 'short',
        day: '2-digit',
        month: 'short'
      }), // Short formatted date for display
      full: date.toLocaleDateString('es-PE') // Full formatted date
    };
  });

  return (
    <div className="movie-detail-container">
      <div className="left-column"> {/* Left section containing movie poster and synopsis */}
        <img src={movie.image} alt={movie.title} className="movie-image" /> {/* Display movie poster */}
        <h3 className="sinopsis-title">Sinopsis</h3>
        <p className="sinopsis-text">{movie.description}</p> {/* Display movie description */}
      </div>

      <div className="right-column"> {/* Right section for movie details and showtimes */}
        <h1 className="movie-title">{movie.title}</h1> {/* Movie title */}
        <p><strong>Duración:</strong> {movie.duration} min</p> {/* Movie duration */}
        <p><strong>Género:</strong> {movie.genre}</p> {/* Movie genre */}

        <div className="date-selector"> {/* Display upcoming dates for selection */}
          {days.map((day, index) => (
            <div key={index} className="date-box">
              <p>{day.short}</p>
            </div>
          ))}
        </div>

        <div className="cinema-info"> {/* Cinema location details */}
          <p className="cinema-name">Cinerama Miraflores</p>
          <p>Edificio El Pacífico, Av. José Pardo 121, Miraflores Lima 18</p>
        </div>

        <div className="showtimes"> {/* Display available showtimes as buttons */}
          {movie.showtimes.map((time, idx) => (
            <button key={idx} className="showtime-button">{time}</button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default MovieDetail; // Export component for use in other parts of the application
