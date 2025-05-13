import { createContext, useState, useEffect } from "react";
import userService from "../services/userService";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  const loadUser = async () => {
    const currentUser = await userService.getCurrentUser();
    setUser(currentUser);
  };

  useEffect(() => {
    loadUser(); // Al iniciar, cargamos usuario si hay token
  }, []);

  const login = async (credentials) => {
    const token = await userService.loginUser(credentials);
    await loadUser(); // cargar datos del usuario después del login
    return token;
  };

  const logout = () => {
    localStorage.removeItem("token");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
