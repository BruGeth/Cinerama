import { createContext, useState, useEffect } from "react";
import userService from "../services/userService";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadUser = async () => {
    try {
      setLoading(true);
      const currentUser = await userService.getCurrentUser();
      
      if (currentUser) {
        setUser(currentUser);
        // Guardar el usuario en localStorage
        localStorage.setItem("user", JSON.stringify(currentUser));
      }
    } catch (error) {
      console.error("Error loading user:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUser(); // Al iniciar, cargamos usuario si hay token
  }, []);

  const login = async (credentials) => {
    try {
      const token = await userService.loginUser(credentials);
      await loadUser(); // cargar datos del usuario después del login
      return token;
    } catch (error) {
      console.error("Login error:", error);
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user"); // También eliminar el usuario
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};