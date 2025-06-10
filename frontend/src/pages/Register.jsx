import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Register.css';
import userService from '../services/userService';

const Register = () => {
  const navigate = useNavigate();

  // State to store form input values
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    password: '',
    confirmPassword: '',
    acceptTerms: false
  });

  // State to hold validation errors for each input field
  const [errors, setErrors] = useState({});

  // Indicates whether the form is being submitted
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Handle input field changes (text and checkbox)
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    // Update the corresponding field in formData
    setFormData(prevData => ({
      ...prevData,
      [name]: type === 'checkbox' ? checked : value
    }));

    // Clear specific error message once the user modifies that field
    if (errors[name]) {
      setErrors(prevErrors => ({
        ...prevErrors,
        [name]: ''
      }));
    }
  };

  // Validate form inputs and return an object with error messages
  const validate = () => {
    const newErrors = {};

    if (!formData.fullName.trim()) {
      newErrors.fullName = 'El nombre completo es requerido';
    }

    if (!formData.email) {
      newErrors.email = 'El correo electrónico es requerido';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Formato de correo electrónico inválido';
    }

    if (!formData.password) {
      newErrors.password = 'La contraseña es requerida';
    } else if (formData.password.length < 6) {
      newErrors.password = 'La contraseña debe tener al menos 6 caracteres';
    }

    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Las contraseñas no coinciden';
    }

    if (!formData.acceptTerms) {
      newErrors.acceptTerms = 'Debes aceptar los términos y condiciones';
    }

    return newErrors;
  };

  // Handles form submission
  const handleSubmit = async (e) => {
    e.preventDefault(); // Prevent default page reload on form submit

    const validationErrors = validate();

    // If there are validation errors, display them and stop submission
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setIsSubmitting(true); // Disable button and show loading state

    try {
      // Send user data to backend service
      const user = await userService.registerUser(formData);
      console.log('Usuario registrado:', user);

      // Redirect to verification page and pass the email as state
      navigate('/verify', { state: { email: formData.email } });

    } catch (error) {
      // Handle API or network errors
      console.error('Error al registrar:', error);
      setErrors({ submit: error.message || 'Error al procesar el registro. Inténtalo nuevamente.' });
    } finally {
      setIsSubmitting(false); // Re-enable the form
    }
  };

  return (
    <div className="register-container">
      <div className="register-form-container">
        <h1>Crear cuenta</h1>

        {/* Form with input fields */}
        <form onSubmit={handleSubmit}>

          {/* Full name input */}
          <div className="form-group">
            <label htmlFor="fullName">Nombre completo</label>
            <input
              type="text"
              id="fullName"
              name="fullName"
              value={formData.fullName}
              onChange={handleChange}
              className={errors.fullName ? 'input-error' : ''}
            />
            {errors.fullName && <span className="error-message">{errors.fullName}</span>}
          </div>

          {/* Email input */}
          <div className="form-group">
            <label htmlFor="email">Correo electrónico</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className={errors.email ? 'input-error' : ''}
            />
            {errors.email && <span className="error-message">{errors.email}</span>}
          </div>

          {/* Password input */}
          <div className="form-group">
            <label htmlFor="password">Contraseña</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className={errors.password ? 'input-error' : ''}
            />
            {errors.password && <span className="error-message">{errors.password}</span>}
          </div>

          {/* Confirm password input */}
          <div className="form-group">
            <label htmlFor="confirmPassword">Confirmar contraseña</label>
            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              className={errors.confirmPassword ? 'input-error' : ''}
            />
            {errors.confirmPassword && <span className="error-message">{errors.confirmPassword}</span>}
          </div>

          {/* Terms and conditions checkbox */}
          <div className="form-group checkbox-group">
            <input
              type="checkbox"
              id="acceptTerms"
              name="acceptTerms"
              checked={formData.acceptTerms}
              onChange={handleChange}
            />
            <label htmlFor="acceptTerms">Acepto los términos y condiciones</label>
            {errors.acceptTerms && <span className="error-message">{errors.acceptTerms}</span>}
          </div>

          {/* General submission error */}
          {errors.submit && <div className="error-message general-error">{errors.submit}</div>}

          {/* Submit button */}
          <button
            type="submit"
            className="register-button"
            disabled={isSubmitting}
          >
            {isSubmitting ? 'PROCESANDO...' : 'REGISTRARSE'}
          </button>
        </form>

        {/* Link to login page if user already has an account */}
        <div className="login-link">
          <p>¿Ya tienes una cuenta? <Link to="/login">Iniciar sesión</Link></p>
        </div>
      </div>
    </div>
  );
};

export default Register;
