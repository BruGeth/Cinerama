import { useNavigate } from 'react-router-dom'; // Import navigation function for handling route changes
import { FaTicketAlt } from 'react-icons/fa'; // Import ticket icon for UI enhancement
import { useMovies } from '../hooks/useMovies'; // Import custom hook for fetching movie data from API
import '../styles/Cartelera.css'; // Import styles for component styling

const Cartelera = () => {
  const navigate = useNavigate(); // Hook for navigation within the app
  const { movies, loading } = useMovies(); // Fetch all movies from the API

  // Function to navigate to the movie purchase page based on selected movie ID
  const handleBuyClick = (id) => {
    navigate(`/billboard/${id}`); // Navigates to a specific movie's detail page
  };

  // Show loading message while fetching movies
  if (loading) return <h2 style={{ color: "white" }}>Loading...</h2>;

  return (
    <div className="cartelera-container">
      <h1 className="cartelera-title">Cartelera</h1>
      <div className="cartelera-grid">{/* Grid layout for displaying movies */}
        {movies.map((movie) => (
          <div key={movie.id} className="cartelera-card">{/* Unique movie card */}
            <img src={movie.imageUrl} alt={movie.title} className="cartelera-image" />{/* Movie poster */}
            <h2 className="cartelera-movie-title">{movie.title}</h2>
            <p className="cartelera-description">{movie.descriptionShowtimes}</p>
            <button className="buy-button" onClick={() => handleBuyClick(movie.id)}>{/* Purchase button */}
              <FaTicketAlt style={{ marginRight: '8px' }} />
              Comprar
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Cartelera; // Export component for use in other parts of the application