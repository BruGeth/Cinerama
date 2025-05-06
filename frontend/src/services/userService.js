const API_BASE_URL = 'http://localhost:8080/api/auth';

const userService = {
  registerUser: async (data) => {
    try {
      const response = await fetch(`${API_BASE_URL}/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          name: data.fullName,
          email: data.email,
          password: data.password,
          confirmPassword: data.confirmPassword
        })
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error during registration');
      }

      return await response.json(); 
    } catch (error) {
      throw error;
    }
  },

  loginUser: async (credentials) => {
    const response = await fetch(`${API_BASE_URL}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(credentials),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || "Login failed.");
    }

    console.log("Login successful:", credentials.email);
    return response.json();
  },

  
  verifyUser: async (verificationData) => {
    const response = await fetch(`${API_BASE_URL}/verify`, {
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
  }
};


export default userService;
