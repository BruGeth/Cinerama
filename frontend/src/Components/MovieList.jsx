import React from 'react';
import { useMovies } from './useMovies';

const MovieList = () => {
  const { movies, loading, error } = useMovies();

  if (loading) return <p>Cargando películas...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div>
      <h2>Películas Disponibles</h2>
      <ul>
        {movies.map(movie => (
          <li key={movie.id}>
            {movie.title} – {movie.genre}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default MovieList;
