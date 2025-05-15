import { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import '../styles/Profile.css';
import { AuthContext } from '../context/AuthContext';

const Profile = () => {
  const { user, logout, loading } = useContext(AuthContext);
  
  if (loading) {
    return <div className="profile-container">Cargando perfil...</div>;
  }
  
  if (!user) {
    return <Navigate to="/login" />;
  }
  
  const handleLogout = () => {
    logout();
    window.location.href = '/';
  };
  
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