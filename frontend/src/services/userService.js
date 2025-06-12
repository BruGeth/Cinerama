const API_BASE_URL = "/api";

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
      console.error("Error en loginUser:", error);
      throw new Error(error.message || "Login failed.");
    }

    const data = await response.json(); // Contains the token and user data
    console.log("Respuesta del backend (login):", data);
    localStorage.setItem("token", data.token); // save token in localStorage
    return data;
  },

  getCurrentUser: async () => {
    const token = localStorage.getItem("token");

    console.log(token); // Verify if the token is being retrieved correctly
    // If the token is not present, return null
    if (!token) return null;

    try {
      const response = await fetch(`${API_BASE_URL}/user/me`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        credentials: "include",
      });

      if (!response.ok) {
        localStorage.removeItem("token"); // Invalid token, remove it
        localStorage.removeItem("user"); // Also remove user data
        return null;
      }

      const userData = await response.json();
      // Log the user data to verify it
      localStorage.setItem("user", JSON.stringify(userData));
      return userData; // Return the user data
    } catch (error) {
      console.error("Error getting current user:", error);
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      return null;
    }
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
