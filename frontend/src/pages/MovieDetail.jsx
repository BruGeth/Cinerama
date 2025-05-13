import React from 'react';

import { useParams } from 'react-router-dom';
import { MOVIES } from '../Components/movieData';
import '../styles/MovieDetail.css';

const MovieDetail = () => {
  const { id } = useParams();
  const movie = MOVIES.find((m) => m.id === Number(id));

  if (!movie) return <h2 style={{ color: 'white' }}>Película no encontrada</h2>;

  const days = Array.from({ length: 7 }, (_, i) => {
    const date = new Date();
    date.setDate(date.getDate() + i);
    return {
      short: date.toLocaleDateString('es-PE', {
        weekday: 'short',
        day: '2-digit',
        month: 'short',
      }),
      full: date.toLocaleDateString('es-PE'),
    };
  });

  return (
    <div className="movie-detail-container">
      <div className="left-column">
        <img src={movie.image} alt={movie.title} className="movie-image" />
        <h3 className="sinopsis-title">Sinopsis</h3>
        <p className="sinopsis-text">{movie.description}</p>
      </div>

      <div className="right-column">
        <h1 className="movie-title">{movie.title}</h1>
        <p><strong>Duración:</strong> {movie.duration} min</p>
        <p><strong>Género:</strong> {movie.genre}</p>

        <div className="date-selector">
          {days.map((day, index) => (
            <div key={index} className="date-box">
              <p>{day.short}</p>
            </div>
          ))}
        </div>

        <div className="cinema-info">
          <p className="cinema-name">Cinerama Miraflores</p>
          <p>Edificio El Pacífico, Av. José Pardo 121, Miraflores Lima 18</p>
        </div>

        <div className="showtimes">
          {movie.showtimes.map((time, idx) => (
            <button key={idx} className="showtime-button">{time}</button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default MovieDetail;
