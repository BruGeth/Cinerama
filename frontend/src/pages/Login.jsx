import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Login.css';
import userService from '../services/userService'; 

const Login = () => {
  const [credentials, setCredentials] = useState({
    email: '',
    password: ''
  });
  const [rememberMe, setRememberMe] = useState(false);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCredentials({
      ...credentials,
      [name]: value
    });
  };

  const handleRememberMe = () => {
    setRememberMe(!rememberMe);
  };

  const validateForm = () => {
    if (!credentials.email) {
      setError('Correo electrónico es requerido');
      return false;
    }
    if (!credentials.password) {
      setError('Contraseña es requerida');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    if (!validateForm()) return;
  
    setIsLoading(true);
    setError('');
  
    try {
      const user = await userService.loginUser(credentials);
  
      // Puedes guardar el usuario o token en localStorage si lo deseas
      localStorage.setItem('user', JSON.stringify(user));
  
      if (rememberMe) {
        localStorage.setItem('userEmail', credentials.email);
      } else {
        localStorage.removeItem('userEmail');
      }
  
      navigate('/');
    } catch (err) {
      console.error("Login error:", err.message);
      setError('Credenciales inválidas. Inténtalo nuevamente.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-form-wrapper">
        <h1 className="login-title">Iniciar Sesión</h1>
        
        {error && <div className="login-error">{error}</div>}
        
        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="email">Correo electrónico</label>
            <input
              type="email"
              id="email"
              name="email"
              value={credentials.email}
              onChange={handleChange}
              className="form-control"
              placeholder=""
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="password">Contraseña</label>
            <input
              type="password"
              id="password"
              name="password"
              value={credentials.password}
              onChange={handleChange}
              className="form-control"
              placeholder=""
            />
          </div>
          
          <div className="form-options">
            <div className="remember-me">
              <input
                type="checkbox"
                id="rememberMe"
                checked={rememberMe}
                onChange={handleRememberMe}
              />
              <label htmlFor="rememberMe">Recordarme</label>
            </div>
            <Link to="/recuperar-password" className="forgot-password">
              ¿OLVIDASTE TU CONTRASEÑA?
            </Link>
          </div>
          
          <button 
            type="submit" 
            className="login-button"
            disabled={isLoading}
          >
            {isLoading ? 'CARGANDO...' : 'INICIAR SESIÓN'}
          </button>
          
          <div className="register-link">
            ¿No tienes cuenta? <Link to="/register">Regístrate</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
