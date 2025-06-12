const API_BASE_URL = "/api/movies";

// Helper function to get authorization headers
const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};

// Obtain all movies
export const fetchMovies = async () => {
  const res = await fetch(API_BASE_URL);
  if (!res.ok) throw new Error("Error fetching movies");
  return await res.json();
};

// Create a new movie
export const createMovie = async (movieData) => {
  const res = await fetch(API_BASE_URL, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(movieData),
  });
  if (!res.ok) throw new Error("Error creating movie");
  return await res.json();
};

// Update an existing movie
export const updateMovie = async (id, movieData) => {
  const res = await fetch(`${API_BASE_URL}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(movieData),
  });
  if (!res.ok) throw new Error("Error updating movie");
  return await res.json();
};

// Delete a movie
export const deleteMovie = async (id) => {
  const res = await fetch(`${API_BASE_URL}/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders(),
  });
  if (!res.ok) throw new Error("Error deleting movie");
};