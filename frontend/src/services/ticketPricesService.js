// Servicio para gestión de precios de tickets - Actualizado para nueva API
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

// Mock data actualizado con formato incluido
const mockTicketPrices = [
  {
    id: 1,
    showtimeId: 1,
    type: "GENERAL",
    format: "THREE_D",
    price: 18.50
  },
  {
    id: 2,
    showtimeId: 1,
    type: "CHILD",
    format: "THREE_D",
    price: 12.00
  },
  {
    id: 3,
    showtimeId: 1,
    type: "STUDENT",
    format: "THREE_D",
    price: 15.00
  },
  {
    id: 4,
    showtimeId: 1,
    type: "SENIOR",
    format: "THREE_D",
    price: 14.00
  },
  {
    id: 5,
    showtimeId: 2,
    type: "GENERAL",
    format: "IMAX",
    price: 35.00
  },
  {
    id: 6,
    showtimeId: 2,
    type: "CHILD",
    format: "IMAX",
    price: 28.00
  },
  {
    id: 7,
    showtimeId: 2,
    type: "VIP",
    format: "IMAX",
    price: 45.00
  },
  {
    id: 8,
    showtimeId: 3,
    type: "GENERAL",
    format: "TWO_D",
    price: 16.00
  },
  {
    id: 9,
    showtimeId: 3,
    type: "CHILD",
    format: "TWO_D",
    price: 10.00
  },
  {
    id: 10,
    showtimeId: 3,
    type: "STUDENT",
    format: "TWO_D",
    price: 13.00
  },
  {
    id: 11,
    showtimeId: 4,
    type: "GENERAL",
    format: "FOUR_D_X",
    price: 42.00
  },
  {
    id: 12,
    showtimeId: 4,
    type: "CHILD",
    format: "FOUR_D_X",
    price: 35.00
  },
  {
    id: 13,
    showtimeId: 4,
    type: "VIP",
    format: "FOUR_D_X",
    price: 55.00
  }
];

let nextId = 14;

// Función para simular delay de red
const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

export const ticketPricesService = {
  // Obtener todos los precios de tickets
  async fetchTicketPrices() {
    try {
      const res = await fetch(API_TICKET_PRICES, { headers: getAuthHeaders() });
      if (!res.ok) throw new Error("Error fetching ticket prices");
      return await res.json();
    } catch (error) {
      console.error("Error al obtener precios de tickets:", error);
      
      // En desarrollo, usar mock data
      if (process.env.NODE_ENV === 'development') {
        await delay(300);
        return [...mockTicketPrices];
      }
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
      
      if (process.env.NODE_ENV === 'development') {
        await delay(200);
        return mockTicketPrices.filter(price => price.showtimeId === showtimeId);
      }
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
      
      if (process.env.NODE_ENV === 'development') {
        await delay(200);
        return mockTicketPrices.filter(price => price.type === type);
      }
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
      
      if (process.env.NODE_ENV === 'development') {
        await delay(500);
        
        // Mock: verificar duplicados
        const exists = mockTicketPrices.find(
          price => price.showtimeId === ticketPriceData.showtimeId && 
                   price.type === ticketPriceData.type &&
                   price.format === ticketPriceData.format
        );

        if (exists) {
          throw new Error(`Ya existe un precio para el tipo ${ticketPriceData.type} y formato ${ticketPriceData.format} en este horario`);
        }

        // Crear nuevo precio
        const newTicketPrice = {
          id: nextId++,
          ...ticketPriceData,
          createdAt: new Date().toISOString()
        };

        mockTicketPrices.push(newTicketPrice);
        return newTicketPrice;
      }
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
      
      if (process.env.NODE_ENV === 'development') {
        await delay(700);
        
        const newPrices = [];
        for (const priceData of ticketPricesData) {
          const newPrice = {
            id: nextId++,
            ...priceData,
            createdAt: new Date().toISOString()
          };
          mockTicketPrices.push(newPrice);
          newPrices.push(newPrice);
        }
        return newPrices;
      }
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
      
      if (process.env.NODE_ENV === 'development') {
        await delay(500);
        
        // Mock: encontrar y actualizar
        const index = mockTicketPrices.findIndex(price => price.id === id);
        if (index === -1) {
          throw new Error("Precio de ticket no encontrado");
        }

        // Verificar duplicados (excluyendo el actual)
        const exists = mockTicketPrices.find(
          price => price.id !== id &&
                   price.showtimeId === ticketPriceData.showtimeId && 
                   price.type === ticketPriceData.type &&
                   price.format === ticketPriceData.format
        );

        if (exists) {
          throw new Error(`Ya existe un precio para el tipo ${ticketPriceData.type} y formato ${ticketPriceData.format} en este horario`);
        }

        const updatedTicketPrice = {
          ...mockTicketPrices[index],
          ...ticketPriceData,
          updatedAt: new Date().toISOString()
        };

        mockTicketPrices[index] = updatedTicketPrice;
        return updatedTicketPrice;
      }
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
      
      if (process.env.NODE_ENV === 'development') {
        await delay(300);
        
        // Mock: eliminar del array
        const index = mockTicketPrices.findIndex(price => price.id === id);
        if (index === -1) {
          throw new Error("Precio de ticket no encontrado");
        }

        mockTicketPrices.splice(index, 1);
        return { message: "Precio eliminado exitosamente" };
      }
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
      
      if (process.env.NODE_ENV === 'development') {
        await delay(200);
        
        // Mock: buscar por ID
        const ticketPrice = mockTicketPrices.find(price => price.id === id);
        if (!ticketPrice) {
          throw new Error("Precio de ticket no encontrado");
        }

        return ticketPrice;
      }
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
