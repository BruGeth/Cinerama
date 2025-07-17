// Genre Service para el frontend
const API_BASE_URL = "/api/genres";

// Helper function to get authorization headers
const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};

// Obtener todos los géneros
export const fetchGenres = async () => {
  const res = await fetch(API_BASE_URL);
  if (!res.ok) throw new Error("Error fetching genres");
  return await res.json();
};

// Crear un nuevo género
export const createGenre = async (genreData) => {
  const res = await fetch(API_BASE_URL, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(genreData),
  });
  if (!res.ok) throw new Error("Error creating genre");
  return await res.json();
};

// Eliminar un género
export const deleteGenre = async (id) => {
  const res = await fetch(`${API_BASE_URL}/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders(),
  });
  if (!res.ok) throw new Error("Error deleting genre");
};
