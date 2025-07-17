// Servicio para gestión de precios de tickets - Conectado con backend
const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";
const API_TICKET_PRICES = `${API_BASE_URL}/api/ticket-prices`;

// Helper for auth headers
const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};

export const ticketPricesService = {
  // Obtener todos los precios de tickets
  async fetchTicketPrices() {
    try {
      const res = await fetch(API_TICKET_PRICES, { headers: getAuthHeaders() });
      if (!res.ok) throw new Error("Error fetching ticket prices");
      return await res.json();
    } catch (error) {
      console.error("Error al obtener precios de tickets:", error);
      throw error;
    }
  },

  // Obtener precios por showtime ID - ENDPOINT PRINCIPAL
  async fetchTicketPricesByShowtime(showtimeId) {
    try {
      const res = await fetch(`${API_TICKET_PRICES}/showtime/${showtimeId}`, { 
        headers: getAuthHeaders() 
      });
      if (!res.ok) throw new Error("Error fetching ticket prices by showtime");
      return await res.json();
    } catch (error) {
      console.error("Error al obtener precios por showtime:", error);
      throw error;
    }
  },

  // Obtener precios por tipo de ticket
  async fetchTicketPricesByType(type) {
    try {
      const res = await fetch(`${API_TICKET_PRICES}/type/${type}`, { 
        headers: getAuthHeaders() 
      });
      if (!res.ok) throw new Error("Error fetching ticket prices by type");
      return await res.json();
    } catch (error) {
      console.error("Error al obtener precios por tipo:", error);
      throw error;
    }
  },

  // Crear nuevo precio de ticket
  async createTicketPrice(ticketPriceData) {
    try {
      const res = await fetch(API_TICKET_PRICES, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(ticketPriceData),
      });
      if (!res.ok) throw new Error("Error creating ticket price");
      return await res.json();
    } catch (error) {
      console.error("Error al crear precio de ticket:", error);
      throw error;
    }
  },

  // Crear múltiples precios en batch
  async createTicketPricesBatch(ticketPricesData) {
    try {
      const res = await fetch(`${API_TICKET_PRICES}/batch`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(ticketPricesData),
      });
      if (!res.ok) throw new Error("Error creating ticket prices batch");
      return await res.json();
    } catch (error) {
      console.error("Error al crear precios en lote:", error);
      throw error;
    }
  },

  // Actualizar precio de ticket
  async updateTicketPrice(id, ticketPriceData) {
    try {
      const res = await fetch(`${API_TICKET_PRICES}/${id}`, {
        method: "PUT",
        headers: getAuthHeaders(),
        body: JSON.stringify(ticketPriceData),
      });
      if (!res.ok) throw new Error("Error updating ticket price");
      return await res.json();
    } catch (error) {
      console.error("Error al actualizar precio de ticket:", error);
      throw error;
    }
  },

  // Eliminar precio de ticket
  async deleteTicketPrice(id) {
    try {
      const res = await fetch(`${API_TICKET_PRICES}/${id}`, {
        method: "DELETE",
        headers: getAuthHeaders(),
      });
      if (!res.ok) throw new Error("Error deleting ticket price");
      return { message: "Precio eliminado exitosamente" };
    } catch (error) {
      console.error("Error al eliminar precio de ticket:", error);
      throw error;
    }
  },

  // Obtener precio por ID
  async fetchTicketPriceById(id) {
    try {
      const res = await fetch(`${API_TICKET_PRICES}/${id}`, { 
        headers: getAuthHeaders() 
      });
      if (!res.ok) throw new Error("Error fetching ticket price by ID");
      return await res.json();
    } catch (error) {
      console.error("Error al obtener precio por ID:", error);
      throw error;
    }
  },

  // Verificar si existe precio para showtime, tipo y formato
  async checkPriceExists(showtimeId, type, format, excludeId = null) {
    try {
      const prices = await this.fetchTicketPricesByShowtime(showtimeId);
      return prices.some(price => 
        price.type === type && 
        price.format === format &&
        (excludeId ? price.id !== excludeId : true)
      );
    } catch (error) {
      console.error("Error al verificar precio existente:", error);
      return false;
    }
  },

  // Obtener estadísticas de precios
  async getTicketPriceStats() {
    try {
      // En producción habría un endpoint específico para stats
      const prices = await this.fetchTicketPrices();
      
      if (prices.length === 0) {
        return {
          total: 0,
          minPrice: 0,
          maxPrice: 0,
          avgPrice: 0,
          byType: {},
          byFormat: {}
        };
      }

      const priceValues = prices.map(p => p.price);
      return {
        total: prices.length,
        minPrice: Math.min(...priceValues),
        maxPrice: Math.max(...priceValues),
        avgPrice: priceValues.reduce((a, b) => a + b, 0) / priceValues.length,
        byType: {
          GENERAL: prices.filter(p => p.type === 'GENERAL').length,
          CHILD: prices.filter(p => p.type === 'CHILD').length,
          STUDENT: prices.filter(p => p.type === 'STUDENT').length,
          SENIOR: prices.filter(p => p.type === 'SENIOR').length,
          VIP: prices.filter(p => p.type === 'VIP').length
        },
        byFormat: {
          TWO_D: prices.filter(p => p.format === 'TWO_D').length,
          THREE_D: prices.filter(p => p.format === 'THREE_D').length,
          IMAX: prices.filter(p => p.format === 'IMAX').length,
          FOUR_D_X: prices.filter(p => p.format === 'FOUR_D_X').length
        }
      };
    } catch (error) {
      console.error("Error al obtener estadísticas:", error);
      throw error;
    }
  }
};

// Tipos de tickets y formatos disponibles
export const getTicketTypes = () => {
  return [
    { value: "GENERAL", label: "General/Adulto", icon: "👤", description: "Precio estándar para adultos" },
    { value: "CHILD", label: "Niños", icon: "🧒", description: "Menores de 12 años" },
    { value: "STUDENT", label: "Estudiantes", icon: "🎓", description: "Con carnet universitario válido" },
    { value: "SENIOR", label: "Tercera Edad", icon: "👴", description: "Mayores de 65 años" },
    { value: "VIP", label: "VIP", icon: "⭐", description: "Asientos premium con servicios exclusivos" }
  ];
};

export const getFormatTypes = () => {
  return [
    { value: "TWO_D", label: "2D", description: "Formato estándar bidimensional" },
    { value: "THREE_D", label: "3D", description: "Formato tridimensional con gafas especiales" },
    { value: "IMAX", label: "IMAX", description: "Pantalla gigante con sonido inmersivo" },
    { value: "FOUR_D_X", label: "4DX", description: "Experiencia multisensorial con efectos físicos" }
  ];
};

export default ticketPricesService;
