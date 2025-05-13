const API_BASE_URL = "http://localhost:8080/api";

const userService = {
  registerUser: async (data) => {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name: data.fullName,
          email: data.email,
          password: data.password,
          confirmPassword: data.confirmPassword,
        }),
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Error during registration");
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  },

  loginUser: async (credentials) => {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(credentials),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || "Login failed.");
    }

    const data = await response.json(); // Contiene el token
    localStorage.setItem("token", data.token); // 👈 Guardar token
    return data.token;
  },

  getCurrentUser: async () => {
    const token = localStorage.getItem("token");

    console.log(token); // Verifica si el token está presente
    // Si no hay token, no se puede obtener el usuario
    if (!token) return null;

    const response = await fetch(`${API_BASE_URL}/user/me`, {
      method: "GET",
      headers: {
        'Authorization': `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      credentials: 'include' 
    });

    if (!response.ok) {
      localStorage.removeItem("token"); // Token inválido
      return null;
    }

    return await response.json(); // Retorna el usuario
  },

  verifyUser: async (verificationData) => {
    const response = await fetch(`${API_BASE_URL}/auth/verify`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(verificationData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || "Verification failed.");
    }

    const message = await response.text();
    console.log("Server response:", message);
  },
};

export default userService;
