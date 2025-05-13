import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const PrivateRoute = ({ children }) => {
  const { user, loading } = useContext(AuthContext);
  
  // Mostrar un indicador de carga mientras se verifica la autenticación
  if (loading) {
    return <div className="loading-container">Cargando...</div>;
  }
  
  // Redirigir al login si no hay usuario autenticado
  if (!user) {
    return <Navigate to="/login" />;
  }
  
  // Renderizar el componente hijo si el usuario está autenticado
  return children;
};

export default PrivateRoute;