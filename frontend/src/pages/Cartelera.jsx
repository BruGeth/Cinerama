import React from 'react';
import { useNavigate } from 'react-router-dom'; 
import { FaTicketAlt } from 'react-icons/fa';
import { MOVIES } from '../Components/movieData';
import '../styles/Cartelera.css'; 

const Cartelera = () => {
  const navigate = useNavigate(); 

  const handleBuyClick = (id) => {
    navigate(`/billboard/${id}`);
  };

  return (
    <div className="cartelera-container">
      <h1 className="cartelera-title">Cartelera</h1>
      <div className="cartelera-grid">
        {MOVIES.map((movie) => (
          <div key={movie.id} className="cartelera-card">
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

export default Cartelera;
