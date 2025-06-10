import React from 'react';
import { useNavigate } from 'react-router-dom'; // Hook to enable navigation within the app
import { FaTicketAlt } from 'react-icons/fa'; // Import ticket icon for button styling
import { MOVIES } from '../Components/movieData'; // Import movie data array
import '../styles/Cartelera.css'; // Import styles for layout and design

const Cartelera = () => {
  const navigate = useNavigate(); // Initialize navigation function

  // Function to handle ticket purchase click, redirecting to the movie detail page
  const handleBuyClick = (id) => {
    navigate(`/billboard/${id}`); // Navigates to a specific movie's detail page
  };

  return (
    <div className="cartelera-container"> {/* Main container for the movie listing page */}
      <h1 className="cartelera-title">Cartelera</h1> {/* Page title */}
      <div className="cartelera-grid"> {/* Grid layout for dynamically displaying movie cards */}
        {MOVIES.map((movie) => ( 
          <div key={movie.id} className="cartelera-card"> {/* Unique card for each movie */}
            <img src={movie.image} alt={movie.title} className="cartelera-image" /> {/* Movie poster */}
            <h2 className="cartelera-movie-title">{movie.title}</h2> {/* Movie title */}
            <p className="cartelera-description">{movie.description}</p> {/* Movie description */}
            <button className="buy-button" onClick={() => handleBuyClick(movie.id)}> {/* Purchase button */}
              <FaTicketAlt style={{ marginRight: '8px' }} /> {/* Icon for better UX */}
              Comprar
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Cartelera; // Export component for use in other parts of the application
