import { useState } from 'react'; // Import React and useState for managing component state
import { movies } from '../Components/DataMovie'; // Import movie data
import '../styles/MovieDetail.css'; // Import CSS for styling
import { useParams, useNavigate } from 'react-router-dom'; // Import hooks for routing functionality

const MovieDetail = () => {
  const { id } = useParams(); // Extract movie ID from the URL
  const navigate = useNavigate(); // Hook for handling navigation

  // Find the movie matching the extracted ID
  const movie = movies.find((m) => m.id === Number(id));

  // Handles navigation to the purchase page when selecting a showtime
  const handleSelectShowtime = (format, time) => {
    navigate(`/purchase/${movie.id}/${time}/${format}`);
  };

  // State to track the selected date for showtimes
  const [selectedDate, setSelectedDate] = useState(0);

  // Display an error message if the movie is not found
  if (!movie) return <h2 style={{ color: 'white' }}>Película no encontrada</h2>;

  // Generate an array of the next 7 days for showtime selection
  const days = Array.from({ length: 7 }, (_, i) => {
    const date = new Date();
    date.setDate(date.getDate() + i);
    return {
      id: i, // Unique identifier for each date
      dayName: date.toLocaleDateString('es-PE', { weekday: 'short' }).slice(0, 3).toUpperCase(), // Extract weekday abbreviation
      formattedDate: `${date.toLocaleDateString('es-PE', { day: '2-digit' })} ${date.toLocaleDateString('es-PE', { month: 'short' }).replace('.', '')}. ${date.getFullYear()}`, // Full formatted date
    };
  });

  return (
    <div className="movie-detail-container"> {/* Main container for movie details */}
      <div className="left-column"> {/* Left section for movie poster and synopsis */}
        <img src={movie.image} alt={movie.title} className="movie-image" /> {/* Display movie poster */}
        <h3 className="sinopsis-title">Sinopsis</h3>
        <p className="sinopsis-text">{movie.description_movie}</p> {/* Display movie description */}
      </div>

      <div className="right-column"> {/* Right section containing movie information and showtimes */}
        <h1 className="movie-title">{movie.title}</h1> {/* Display movie title */}
        <p><strong>Duración:</strong> {movie.duration} min</p> {/* Display movie duration */}
        <p><strong>Género:</strong> {movie.genre}</p> {/* Display movie genre */}

        <div className="date-selector"> {/* Date selection buttons */}
          {days.map((day) => (
            <button
              key={day.id}
              className={`date-box ${selectedDate === day.id ? 'active' : ''}`} // Apply active styling for selected date
              onClick={() => setSelectedDate(day.id)} // Set selected date on click
            >
              <p style={{ fontWeight: 'bold', fontSize: '16px' }}>{day.dayName}</p> {/* Display short weekday name */}
              <p style={{ fontSize: '14px', color: '#ccc' }}>{day.formattedDate}</p> {/* Display formatted date */}
            </button>
          ))}
        </div>

        {/* Showtime selection grid */}
        <div className={`showtime-grid ${selectedDate !== null ? 'active' : ''}`}>
          {["2D", "3D", "XD"].map((format) => ( // Iterate over different formats
            <div key={format} className="format-container"> {/* Format section */}
              <h3>{format}</h3> {/* Display format title */}
              <div className="showtime-buttons"> {/* Container for showtime buttons */}
                {Array.isArray(movie.showtimes[format]) && movie.showtimes[format].length > 0 ? (
                  movie.showtimes[format].map((time, idx) => ( // Iterate through available showtimes
                    <button
                      key={idx}
                      className="showtime-button"
                      onClick={() => handleSelectShowtime(format, time)} // Handle showtime selection
                    >
                      {time}
                    </button>
                  ))
                ) : (
                  <p className="no-showtime">No disponible</p> // Display message if no showtimes are available
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default MovieDetail; // Export component for use in other parts of the application
