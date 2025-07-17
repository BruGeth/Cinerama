const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";
const API_CINEMAS = `${API_BASE_URL}/api/cinemas`;
const API_ROOMS = `${API_BASE_URL}/api/rooms`;

// Helper for auth headers
const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};

// CINEMAS
export const fetchCinemas = async () => {
  try {
    const res = await fetch(API_CINEMAS, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error("Error fetching cinemas");
    return await res.json();
  } catch (error) {
    console.error("Error fetching cinemas:", error);
    throw error;
  }
};

export const createCinema = async (cinemaData) => {
  try {
    const res = await fetch(API_CINEMAS, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify(cinemaData),
    });
    
    if (!res.ok) {
      let errorMessage = `Error creating cinema: ${res.status} ${res.statusText}`;
      try {
        const errorText = await res.text();
        console.error('🔥 Respuesta completa del servidor:', errorText);
        
        // Intentar parsear como JSON para obtener más detalles
        try {
          const errorJson = JSON.parse(errorText);
          if (errorJson.message) {
            errorMessage += ` - ${errorJson.message}`;
          }
          if (errorJson.details) {
            errorMessage += ` - Detalles: ${errorJson.details}`;
          }
        } catch (e) {
          // Si no es JSON válido, usar el texto completo
          if (errorText) {
            errorMessage += ` - ${errorText}`;
          }
        }
      } catch (e) {
        console.error('No se pudo leer el cuerpo del error:', e);
      }
      
      throw new Error(errorMessage);
    }
    
    const result = await res.json();
    return result;
  } catch (error) {
    console.error('Error en createCinema:', error);
    throw error;
  }
};

export const updateCinema = async (id, cinemaData) => {
  try {
    const res = await fetch(`${API_CINEMAS}/${id}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify(cinemaData),
    });
    if (!res.ok) throw new Error("Error updating cinema");
    return await res.json();
  } catch (error) {
    console.error("Error updating cinema:", error);
    throw error;
  }
};

export const deleteCinema = async (id) => {
  try {
    const res = await fetch(`${API_CINEMAS}/${id}`, {
      method: "DELETE",
      headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error("Error deleting cinema");
    return { message: "Cinema deleted successfully" };
  } catch (error) {
    console.error("Error deleting cinema:", error);
    throw error;
  }
};

// ROOMS
export const fetchRooms = async (cinemaId = null) => {
  try {
    const url = cinemaId ? `${API_ROOMS}?cinemaId=${cinemaId}` : API_ROOMS;
    const res = await fetch(url, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error("Error fetching rooms");
    return await res.json();
  } catch (error) {
    console.error("Error fetching rooms:", error);
    throw error;
  }
};

export const createRoom = async (roomData) => {
  try {
    const res = await fetch(API_ROOMS, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify(roomData),
    });
    if (!res.ok) throw new Error("Error creating room");
    return await res.json();
  } catch (error) {
    console.error("Error creating room:", error);
    throw error;
  }
};

export const updateRoom = async (id, roomData) => {
  try {
    const res = await fetch(`${API_ROOMS}/${id}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify(roomData),
    });
    if (!res.ok) throw new Error("Error updating room");
    return await res.json();
  } catch (error) {
    console.error("Error updating room:", error);
    throw error;
  }
};

export const deleteRoom = async (id) => {
  try {
    const res = await fetch(`${API_ROOMS}/${id}`, {
      method: "DELETE",
      headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error("Error deleting room");
    return { message: "Room deleted successfully" };
  } catch (error) {
    console.error("Error deleting room:", error);
    throw error;
  }
};

// Mock data for development
export const getMockCinemas = () => {
  return Promise.resolve([
    {
      id: 1,
      name: "Cinerama Miraflores",
      address: "Av. José Larco 1232, Miraflores",
      city: "Lima",
      phone: "+51 1 234-5678",
      email: "miraflores@cinerama.pe",
      status: "ACTIVE",
    },
    {
      id: 2,
      name: "Cinerama San Miguel",
      address: "Av. La Marina 2355, San Miguel",
      city: "Lima",
      phone: "+51 1 234-5679",
      email: "sanmiguel@cinerama.pe",
      status: "ACTIVE",
    },
    {
      id: 3,
      name: "Cinerama Plaza Norte",
      address: "Av. Túpac Amaru 899, Independencia",
      city: "Lima",
      phone: "+51 1 234-5680",
      email: "plazanorte@cinerama.pe",
      status: "ACTIVE",
    },
  ]);
};

export const getMockRooms = () => {
  return Promise.resolve([
    {
      id: 1,
      name: "Sala 1",
      capacity: 150,
      type: "STANDARD",
      technology: ["TWO_D"],
      audioSystem: "Dolby Digital",
      status: "ACTIVE",
      cinemaId: 1,
      cinema: { id: 1, name: "Cinerama Miraflores" },
      totalSeats: 150,
      availableSeats: 150,
    },
    {
      id: 2,
      name: "Sala 2",
      capacity: 200,
      type: "VIP",
      technology: ["TWO_D", "THREE_D"],
      audioSystem: "Dolby Atmos",
      status: "ACTIVE",
      cinemaId: 1,
      cinema: { id: 1, name: "Cinerama Miraflores" },
      totalSeats: 200,
      availableSeats: 200,
    },
    {
      id: 3,
      name: "Sala IMAX",
      capacity: 300,
      type: "IMAX",
      technology: ["IMAX", "THREE_D"],
      audioSystem: "IMAX Enhanced Sound System",
      status: "ACTIVE",
      cinemaId: 1,
      cinema: { id: 1, name: "Cinerama Miraflores" },
      totalSeats: 300,
      availableSeats: 300,
    },
    {
      id: 4,
      name: "Sala 1",
      capacity: 120,
      type: "STANDARD",
      technology: ["TWO_D", "FOUR_DX"],
      audioSystem: "DTS:X",
      status: "ACTIVE",
      cinemaId: 2,
      cinema: { id: 2, name: "Cinerama San Miguel" },
      totalSeats: 120,
      availableSeats: 120,
    },
  ]);
};
