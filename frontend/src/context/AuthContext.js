import {
  createContext,
  useState,
  useEffect,
  useContext,
  useCallback,
} from "react";
import userService from "../services/userService";
import { jwtDecode } from "jwt-decode";

export const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within an AuthProvider");
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Decodify the JWT token to extract user information
  const getUserFromToken = (token) => {
    try {
      const decoded = jwtDecode(token);
      return {
        email: decoded.sub,
        role: decoded.roles === "ROLE_ADMIN" ? "admin" : "user",
      };
    } catch {
      return null;
    }
  };

  const loadUser = useCallback(async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token");
      if (token) {
        const userFromToken = getUserFromToken(token);
        setUser(userFromToken);
        localStorage.setItem("user", JSON.stringify(userFromToken));
      } else {
        setUser(null);
      }
    } catch (error) {
      console.error("Error loading user:", error);
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadUser();
  }, [loadUser]);

  const login = async (credentials) => {
    try {
      // Call the userService to log in the user
      const response = await userService.loginUser(credentials);
      const { name, token } = response;
      localStorage.setItem("token", token);

      const decoded = jwtDecode(token);
      const userFromToken = {
        name, // <-- Add name from the response
        email: decoded.sub,
        role: decoded.roles === "ROLE_ADMIN" ? "admin" : "user",
      };

      setUser(userFromToken);
      localStorage.setItem("token", token);
      localStorage.setItem("user", JSON.stringify(userFromToken));
      return token;
    } catch (error) {
      // if the error is due to unverified account, resend verification code
      if (error.message === "Account is not verified") {
        try {
          await userService.sendVerificationCode(credentials.email);
        } catch (sendError) {
          console.error("Error al reenviar el código de verificación:", sendError);
        }
      }
      console.error("Login error:", error);
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setUser(null);
  };

  const isAdmin = () => user?.role === "admin";
  const hasRole = (role) => user?.role === role;

  return (
    <AuthContext.Provider
      value={{ user, login, logout, loading, isAdmin, hasRole }}
    >
      {children}
    </AuthContext.Provider>
  );
};
