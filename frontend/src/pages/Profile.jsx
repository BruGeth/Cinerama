import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import '../styles/Profile.css'; 

const Profile = () => {
  const [user, setUser] = useState(null);
  
  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);
  
  const handleLogout = () => {
    localStorage.removeItem('user');
    window.location.href = '/';
  };
  
  if (!user) {
    return (
      <div className="profile-container">
        <h2>No has iniciado sesión</h2>
        <p>Por favor, inicia sesión para ver tu perfil.</p>
        <Link to="/login" className="btn-edit-profile">Iniciar Sesión</Link>
      </div>
    );
  }
  
  return (
    <div className="profile-container">
      <h2>Bienvenido, {user.name}</h2>
      
      <div className="profile-info">
        <div className="profile-section">
          <h3>Información Personal</h3>
          <p><strong>Nombre:</strong> {user.name}</p>
          <p><strong>Email:</strong> {user.email}</p>
        </div>
        
        <div className="profile-section">
          <h3>Preferencias</h3>
          <p><strong>Cine favorito:</strong> {user.favoriteCinema || 'No especificado'}</p>
          <p><strong>Género preferido:</strong> {user.favoriteGenre || 'No especificado'}</p>
        </div>
      </div>
      
      <div className="profile-actions">
        <button className="btn-edit-profile">Editar Perfil</button>
        <button onClick={handleLogout}>Cerrar Sesión</button>
      </div>
    </div>
  );
};

export default Profile;