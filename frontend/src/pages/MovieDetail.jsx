import React, { useState } from 'react';
import { MOVIES } from '../Components/movieData';
import '../styles/MovieDetail.css';
import { useParams, useNavigate } from 'react-router-dom';


const MovieDetail = () => {
  const { id } = useParams();
  const movie = MOVIES.find((m) => m.id === Number(id));

  const navigate = useNavigate();

  const [selectedDate, setSelectedDate] = useState(0);

  if (!movie) return <h2 style={{ color: 'white' }}>Película no encontrada</h2>;

  const days = Array.from({ length: 7 }, (_, i) => {
    const date = new Date();
    date.setDate(date.getDate() + i);
    return {
      id: i,
      dayName: date.toLocaleDateString('es-PE', { weekday: 'short' }).slice(0, 3).toUpperCase(),
      formattedDate: `${date.toLocaleDateString('es-PE', { day: '2-digit' })} ${date.toLocaleDateString('es-PE', { month: 'short' }).replace('.', '')}. ${date.getFullYear()}`,
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
          {days.map((day) => (
            <button
              key={day.id}
              className={`date-box ${selectedDate === day.id ? 'active' : ''}`}
              onClick={() => setSelectedDate(day.id)}
            >
              <p style={{ fontWeight: 'bold', fontSize: '16px' }}>{day.dayName}</p>
              <p style={{ fontSize: '14px', color: '#ccc' }}>{day.formattedDate}</p>
            </button>
          ))}
        </div>

        <div className={`showtimes ${selectedDate !== null ? 'active' : ''}`}>
          {Array.isArray(movie.showtimes[selectedDate]) ?
            movie.showtimes[selectedDate].map((time, idx) => (
              <button
                key={idx}
                className="showtime-button"
                onClick={() => navigate(`/purchase/${movie.id}/${time}`)}
              >
                {time}
              </button>
            ))
            : <p style={{ color: "#ccc" }}>No hay horarios disponibles</p>} 
        </div>
      </div>
    </div>
  );
};

export default MovieDetail;
