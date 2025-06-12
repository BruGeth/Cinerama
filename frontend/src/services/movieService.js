const API_BASE_URL = "/api/movies";

// Get all movies
export const fetchMovies = async () => {
  const res = await fetch(API_BASE_URL);
  if (!res.ok) throw new Error("Error fetching movies");
  return await res.json();
};

// Create a new movie
export const createMovie = async (movieData) => {
  const res = await fetch(API_BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(movieData),
  });
  if (!res.ok) throw new Error("Error creating movie");
  return await res.json();
};

// Update a movie (if your backend supports PUT/PATCH)
export const updateMovie = async (id, movieData) => {
  const res = await fetch(`${API_BASE_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(movieData),
  });
  if (!res.ok) throw new Error("Error updating movie");
  return await res.json();
};

// Delete a movie
export const deleteMovie = async (id) => {
  const res = await fetch(`${API_BASE_URL}/${id}`, {
    method: "DELETE",
  });
  if (!res.ok) throw new Error("Error deleting movie");
};