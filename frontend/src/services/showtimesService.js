const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";
const API_SHOWTIMES = `${API_BASE_URL}/api/showtimes`;

// Helper for auth headers
const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};

// SHOWTIMES API FUNCTIONS
export const fetchShowtimes = async (filters = {}) => {
  try {
    const queryParams = new URLSearchParams();

    if (filters.movieId) queryParams.append("movieId", filters.movieId);
    if (filters.cinemaId) queryParams.append("cinemaId", filters.cinemaId);
    if (filters.date) queryParams.append("date", filters.date);

    const url = queryParams.toString()
      ? `${API_SHOWTIMES}?${queryParams}`
      : API_SHOWTIMES;

    const res = await fetch(url, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error("Error fetching showtimes");
    return await res.json();
  } catch (error) {
    console.error("Error fetching showtimes:", error);
    // Fallback to mock data in development
    if (process.env.NODE_ENV === 'development') {
      return getMockShowtimes();
    }
    throw error;
  }
};

export const fetchShowtimesByMovie = async (movieId) => {
  try {
    const res = await fetch(`${API_SHOWTIMES}/movie/${movieId}`, { 
      headers: getAuthHeaders() 
    });
    if (!res.ok) throw new Error("Error fetching showtimes by movie");
    return await res.json();
  } catch (error) {
    console.error("Error fetching showtimes by movie:", error);
    if (process.env.NODE_ENV === 'development') {
      const mockData = await getMockShowtimes();
      return mockData.filter(showtime => showtime.movie.id === movieId);
    }
    throw error;
  }
};

export const fetchShowtimesByCinema = async (cinemaId) => {
  try {
    const res = await fetch(`${API_SHOWTIMES}/cinema/${cinemaId}`, { 
      headers: getAuthHeaders() 
    });
    if (!res.ok) throw new Error("Error fetching showtimes by cinema");
    return await res.json();
  } catch (error) {
    console.error("Error fetching showtimes by cinema:", error);
    if (process.env.NODE_ENV === 'development') {
      const mockData = await getMockShowtimes();
      return mockData.filter(showtime => showtime.cinema.id === cinemaId);
    }
    throw error;
  }
};

export const fetchShowtimesByDate = async (date) => {
  try {
    const res = await fetch(`${API_SHOWTIMES}/date/${date}`, { 
      headers: getAuthHeaders() 
    });
    if (!res.ok) throw new Error("Error fetching showtimes by date");
    return await res.json();
  } catch (error) {
    console.error("Error fetching showtimes by date:", error);
    if (process.env.NODE_ENV === 'development') {
      const mockData = await getMockShowtimes();
      return mockData.filter(showtime => showtime.date === date);
    }
    throw error;
  }
};

export const fetchShowtimeById = async (id) => {
  try {
    const res = await fetch(`${API_SHOWTIMES}/${id}`, { 
      headers: getAuthHeaders() 
    });
    if (!res.ok) throw new Error("Error fetching showtime by ID");
    return await res.json();
  } catch (error) {
    console.error("Error fetching showtime by ID:", error);
    if (process.env.NODE_ENV === 'development') {
      const mockData = await getMockShowtimes();
      return mockData.find(showtime => showtime.id === id);
    }
    throw error;
  }
};

export const createShowtime = async (showtimeData) => {
  try {
    const res = await fetch(API_SHOWTIMES, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify(showtimeData),
    });
    if (!res.ok) throw new Error("Error creating showtime");
    return await res.json();
  } catch (error) {
    console.error("Error creating showtime:", error);
    if (process.env.NODE_ENV === 'development') {
      // Mock creation
      const newShowtime = {
        id: Date.now(),
        ...showtimeData,
        availableSeats: 150,
        ticketPrices: []
      };
      return newShowtime;
    }
    throw error;
  }
};

export const updateShowtime = async (id, showtimeData) => {
  try {
    const res = await fetch(`${API_SHOWTIMES}/${id}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify(showtimeData),
    });
    if (!res.ok) throw new Error("Error updating showtime");
    return await res.json();
  } catch (error) {
    console.error("Error updating showtime:", error);
    if (process.env.NODE_ENV === 'development') {
      // Mock update
      return { id, ...showtimeData };
    }
    throw error;
  }
};

export const deleteShowtime = async (id) => {
  try {
    const res = await fetch(`${API_SHOWTIMES}/${id}`, {
      method: "DELETE",
      headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error("Error deleting showtime");
    return { message: "Showtime deleted successfully" };
  } catch (error) {
    console.error("Error deleting showtime:", error);
    if (process.env.NODE_ENV === 'development') {
      return { message: "Showtime deleted successfully (mock)" };
    }
    throw error;
  }
};

// Tipos de tickets predefinidos
export const getTicketTypes = () => {
  return [
    {
      id: "general",
      name: "General/Adulto",
      description: "Precio estándar para adultos",
      icon: "👤",
      isDefault: true,
    },
    {
      id: "child",
      name: "Niños",
      description: "Menores de 12 años",
      icon: "🧒",
      isDefault: true,
    },
    {
      id: "student",
      name: "Estudiantes",
      description: "Con carnet universitario",
      icon: "🎓",
      isDefault: false,
    },
    {
      id: "senior",
      name: "Tercera Edad",
      description: "Mayores de 65 años",
      icon: "👴",
      isDefault: false,
    },
    {
      id: "vip",
      name: "VIP",
      description: "Asientos premium",
      icon: "⭐",
      isDefault: false,
    },
  ];
};

// Updated Mock data with new API structure
export const getMockShowtimes = () => {
  return Promise.resolve([
    {
      id: 1,
      movie: { id: 1, title: "Avatar: El Camino del Agua", duration: 192 },
      cinema: { id: 1, name: "Cinerama Miraflores" },
      room: { id: 1, name: "Sala 1", capacity: 150, type: "STANDARD" },
      showDate: "2025-07-17",
      showTime: "14:30:00",
      format: "THREE_D",
      language: "SPANISH",
      availableSeats: 120,
      status: "ACTIVE",
      ticketPrices: [
        { id: 1, type: "GENERAL", format: "THREE_D", price: 18.50 },
        { id: 2, type: "CHILD", format: "THREE_D", price: 12.00 },
        { id: 3, type: "STUDENT", format: "THREE_D", price: 15.00 },
        { id: 4, type: "SENIOR", format: "THREE_D", price: 14.00 },
      ],
    },
    {
      id: 2,
      movie: { id: 1, title: "Avatar: El Camino del Agua", duration: 192 },
      cinema: { id: 1, name: "Cinerama Miraflores" },
      room: { id: 2, name: "Sala VIP", capacity: 80, type: "VIP" },
      showDate: "2025-07-17",
      showTime: "17:00:00",
      format: "IMAX",
      language: "SUBTITLED",
      availableSeats: 65,
      status: "ACTIVE",
      ticketPrices: [
        { id: 5, type: "GENERAL", format: "IMAX", price: 35.00 },
        { id: 6, type: "CHILD", format: "IMAX", price: 28.00 },
        { id: 7, type: "VIP", format: "IMAX", price: 45.00 },
      ],
    },
    {
      id: 3,
      movie: { id: 2, title: "Spider-Man: No Way Home", duration: 148 },
      cinema: { id: 2, name: "Cinerama San Miguel" },
      room: { id: 3, name: "Sala 1", capacity: 120, type: "STANDARD" },
      showDate: "2025-07-18",
      showTime: "19:30:00",
      format: "TWO_D",
      language: "SPANISH",
      availableSeats: 95,
      status: "ACTIVE",
      ticketPrices: [
        { id: 8, type: "GENERAL", format: "TWO_D", price: 16.00 },
        { id: 9, type: "CHILD", format: "TWO_D", price: 10.00 },
        { id: 10, type: "STUDENT", format: "TWO_D", price: 13.00 },
      ],
    },
    {
      id: 4,
      movie: { id: 3, title: "Top Gun: Maverick", duration: 130 },
      cinema: { id: 1, name: "Cinerama Miraflores" },
      room: { id: 4, name: "Sala 4DX", capacity: 60, type: "PREMIUM" },
      showDate: "2025-07-17",
      showTime: "21:00:00",
      format: "FOUR_D_X",
      language: "DUBBED",
      availableSeats: 45,
      status: "ACTIVE",
      ticketPrices: [
        { id: 11, type: "GENERAL", format: "FOUR_D_X", price: 42.00 },
        { id: 12, type: "CHILD", format: "FOUR_D_X", price: 35.00 },
        { id: 13, type: "VIP", format: "FOUR_D_X", price: 55.00 },
      ],
    },
  ]);
};
