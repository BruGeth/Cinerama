import { useState, useEffect } from "react";

export function useMovies(movieId = null) {
  const [movies, setMovies] = useState(movieId ? null : []);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let url = "/api/movies";
    if (movieId) url += `/${movieId}`;
    fetch(url)
      .then(res => {
        console.log("Respuesta de la API:", res);
        return res.json();
      })
      .then(data => {
        console.log("Datos recibidos:", data);
        setMovies(data);
        setLoading(false);
      })
      .catch(error => {
        console.error("Error al obtener películas:", error);
        setLoading(false);
      });
  }, [movieId]);

  return { movies, loading };
}