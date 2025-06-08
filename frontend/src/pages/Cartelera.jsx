import React from 'react';
import { useNavigate } from 'react-router-dom'; // Import navigation function for handling route changes
import { FaTicketAlt } from 'react-icons/fa';// Import ticket icon for UI enhancement
import { MOVIES } from '../Components/movieData';// Import movie data from external file
import '../styles/Cartelera.css'; // Import styles for component styling

const Cartelera = () => {
  const navigate = useNavigate(); // Hook for navigation within the app

  // Function to navigate to the movie purchase page based on selected movie ID
  const handleBuyClick = (id) => {
    navigate(`/cartelera/${id}`);
  };

  return (
    <div className="cartelera-container">
      <h1 className="cartelera-title">Cartelera</h1>
      <div className="cartelera-grid">{/* Grid layout for displaying movies */}
        {MOVIES.map((movie) => (
          <div key={movie.id} className="cartelera-card">{/* Unique movie card */}
            <img src={movie.image} alt={movie.title} className="cartelera-image" />
            <h2 className="cartelera-movie-title">{movie.title}</h2>
            <p className="cartelera-description">{movie.description}</p>
            <button className="buy-button" onClick={() => handleBuyClick(movie.id)}>
              <FaTicketAlt style={{ marginRight: '8px' }} />
              Comprar
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Cartelera;// Export component for use in other parts of the application
