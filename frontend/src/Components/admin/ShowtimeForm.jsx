import { useState, useEffect } from "react";
import { fetchRooms } from "../../services/cinemasService";
import styles from "../../styles/ShowtimeForm.module.css";

function ShowtimeForm({ showtime, movies, cinemas, onSave, onClose }) {
  const [formData, setFormData] = useState({
    movieId: "",
    cinemaId: "",
    roomId: "",
    showDate: "",
    showTime: "",
    format: "TWO_D",
    language: "SPANISH",
    status: "ACTIVE",
  });

  const [rooms, setRooms] = useState([]);
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const formatOptions = [
    { value: "TWO_D", label: "2D" },
    { value: "THREE_D", label: "3D" },
    { value: "IMAX", label: "IMAX" },
    { value: "FOUR_D_X", label: "4DX" }
  ];

  const languageOptions = [
    { value: "SPANISH", label: "Español" },
    { value: "SUBTITLED", label: "Subtitulada" },
    { value: "DUBBED", label: "Doblada" }
  ];

  const statusOptions = [
    { value: "ACTIVE", label: "Activo" },
    { value: "CANCELLED", label: "Cancelado" },
    { value: "SOLD_OUT", label: "Agotado" }
  ];

  useEffect(() => {
    if (showtime) {
      console.log("Horario recibido para editar:", showtime);
      setFormData({
        movieId: showtime.movieId?.toString() || showtime.movie?.id?.toString() || "",
        cinemaId: showtime.cinemaId?.toString() || showtime.cinema?.id?.toString() || "",
        roomId: showtime.roomId?.toString() || showtime.room?.id?.toString() || "",
        showDate: showtime.showDate || showtime.date || "",
        showTime: showtime.showTime || showtime.time || "",
        format: showtime.format || "TWO_D",
        language: showtime.language || "SPANISH",
        status: showtime.status || "ACTIVE",
      });
    }
  }, [showtime]);

  // Cargar salas cuando se selecciona un cine
  useEffect(() => {
    if (formData.cinemaId) {
      fetchRoomsData(formData.cinemaId);
    } else {
      setRooms([]);
      setFormData(prev => ({ ...prev, roomId: "" }));
    }
  }, [formData.cinemaId]);

  const fetchRoomsData = async (cinemaId) => {
    setLoading(true);
    try {
      const roomsData = await fetchRooms(Number.parseInt(cinemaId));
      setRooms(roomsData || []);
    } catch (error) {
      console.error("Error loading rooms:", error);
      setRooms([]);
      setErrors(prev => ({ ...prev, roomId: "Error cargando las salas" }));
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });

    // Reset room selection when cinema changes
    if (name === "cinemaId") {
      setFormData((prev) => ({
        ...prev,
        cinemaId: value,
        roomId: "",
      }));
    }

    // Clear error when user types
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: "",
      });
    }
  };

  // Validar compatibilidad de formato con la sala
  const validateFormatCompatibility = (format, roomId) => {
    if (!format || !roomId) return true;
    
    const selectedRoom = rooms.find(room => room.id === Number.parseInt(roomId));
    if (!selectedRoom || !selectedRoom.technology) return true;
    
    // Mapear formatos a tecnologías de sala
    const formatTechnologyMap = {
      TWO_D: "TWO_D",
      THREE_D: "THREE_D", 
      IMAX: "IMAX",
      FOUR_D_X: "FOUR_DX"
    };
    
    const requiredTechnology = formatTechnologyMap[format];
    return selectedRoom.technology.includes(requiredTechnology);
  };

  const validate = () => {
    const newErrors = {};

    // CAMPOS REQUERIDOS
    if (!formData.movieId) {
      newErrors.movieId = "Debe seleccionar una película";
    }
    if (!formData.cinemaId) {
      newErrors.cinemaId = "Debe seleccionar un cine";
    }
    if (!formData.roomId) {
      newErrors.roomId = "Debe seleccionar una sala";
    }
    if (!formData.showDate) {
      newErrors.showDate = "La fecha es requerida";
    }
    if (!formData.showTime) {
      newErrors.showTime = "La hora es requerida";
    }

    // Validar que la fecha no sea en el pasado
    if (formData.showDate) {
      const today = new Date().toISOString().split("T")[0];
      if (formData.showDate < today) {
        newErrors.showDate = "No se pueden crear horarios en fechas pasadas";
      }
    }

    // Validar compatibilidad de formato con sala
    if (formData.format && formData.roomId) {
      if (!validateFormatCompatibility(formData.format, formData.roomId)) {
        newErrors.format = "El formato seleccionado no está disponible en esta sala";
      }
    }

    // Validar enums
    if (formData.format && !["TWO_D", "THREE_D", "IMAX", "FOUR_D_X"].includes(formData.format)) {
      newErrors.format = "Formato inválido";
    }
    if (formData.language && !["SPANISH", "SUBTITLED", "DUBBED"].includes(formData.language)) {
      newErrors.language = "Idioma inválido";
    }
    if (formData.status && !["ACTIVE", "CANCELLED", "SOLD_OUT"].includes(formData.status)) {
      newErrors.status = "Estado inválido";
    }

    return newErrors;
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    const showtimeData = {
      movieId: Number.parseInt(formData.movieId),
      cinemaId: Number.parseInt(formData.cinemaId),
      roomId: Number.parseInt(formData.roomId),
      showDate: formData.showDate,
      showTime: formData.showTime,
      format: formData.format,
      language: formData.language,
      status: formData.status,
    };

    console.log("Datos de horario a enviar:", showtimeData);
    onSave(showtimeData);
  };

  const getSelectedMovie = () => {
    return movies.find(movie => movie.id === Number.parseInt(formData.movieId));
  };

  const getSelectedRoom = () => {
    return rooms.find(room => room.id === Number.parseInt(formData.roomId));
  };

  return (
    <div className={styles.modalOverlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={`${styles.modalContent} ${styles.showtimeModal}`}>
        <div className={styles.modalHeader}>
          <h3>{showtime ? "Editar Horario" : "Agregar Nuevo Horario"}</h3>
          <button className={styles.closeBtn} onClick={onClose}>
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className={styles.showtimeForm}>
          <div className={styles.formRow}>
            <div className={styles.formGroup}>
              <label htmlFor="movieId">Película *</label>
              <select
                id="movieId"
                name="movieId"
                value={formData.movieId}
                onChange={handleChange}
                className={errors.movieId ? styles.error : ""}
              >
                <option value="">Seleccionar película</option>
                {movies.map((movie) => (
                  <option key={movie.id} value={movie.id}>
                    {movie.title} ({movie.duration} min)
                  </option>
                ))}
              </select>
              {errors.movieId && (
                <span className={styles.errorMessage}>{errors.movieId}</span>
              )}
            </div>

            <div className={styles.formGroup}>
              <label htmlFor="cinemaId">Cine *</label>
              <select
                id="cinemaId"
                name="cinemaId"
                value={formData.cinemaId}
                onChange={handleChange}
                className={errors.cinemaId ? styles.error : ""}
              >
                <option value="">Seleccionar cine</option>
                {cinemas.map((cinema) => (
                  <option key={cinema.id} value={cinema.id}>
                    {cinema.name} - {cinema.city}
                  </option>
                ))}
              </select>
              {errors.cinemaId && (
                <span className={styles.errorMessage}>{errors.cinemaId}</span>
              )}
            </div>
          </div>

          <div className={styles.formRow}>
            <div className={styles.formGroup}>
              <label htmlFor="roomId">Sala *</label>
              <select
                id="roomId"
                name="roomId"
                value={formData.roomId}
                onChange={handleChange}
                className={errors.roomId ? styles.error : ""}
                disabled={!formData.cinemaId || loading}
              >
                <option value="">
                  {loading ? "Cargando salas..." : "Seleccionar sala"}
                </option>
                {rooms.map((room) => (
                  <option key={room.id} value={room.id}>
                    {room.name} ({room.type} - {room.capacity} asientos)
                    {room.technology && room.technology.length > 0 &&
                      ` - ${room.technology.join(", ")}`
                    }
                  </option>
                ))}
              </select>
              {errors.roomId && (
                <span className={styles.errorMessage}>{errors.roomId}</span>
              )}
            </div>

            <div className={styles.formGroup}>
              <label htmlFor="showDate">Fecha *</label>
              <input
                type="date"
                id="showDate"
                name="showDate"
                value={formData.showDate}
                onChange={handleChange}
                className={errors.showDate ? styles.error : ""}
                min={new Date().toISOString().split("T")[0]}
              />
              {errors.showDate && (
                <span className={styles.errorMessage}>{errors.showDate}</span>
              )}
            </div>
          </div>

          <div className={styles.formRow}>
            <div className={styles.formGroup}>
              <label htmlFor="showTime">Hora *</label>
              <input
                type="time"
                id="showTime"
                name="showTime"
                value={formData.showTime}
                onChange={handleChange}
                className={errors.showTime ? styles.error : ""}
              />
              {errors.showTime && (
                <span className={styles.errorMessage}>{errors.showTime}</span>
              )}
            </div>

            <div className={styles.formGroup}>
              <label htmlFor="format">Formato</label>
              <select
                id="format"
                name="format"
                value={formData.format}
                onChange={handleChange}
                className={errors.format ? styles.error : ""}
              >
                {formatOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              {errors.format && (
                <span className={styles.errorMessage}>{errors.format}</span>
              )}
            </div>
          </div>

          <div className={styles.formRow}>
            <div className={styles.formGroup}>
              <label htmlFor="language">Idioma</label>
              <select
                id="language"
                name="language"
                value={formData.language}
                onChange={handleChange}
                className={errors.language ? styles.error : ""}
              >
                {languageOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              {errors.language && (
                <span className={styles.errorMessage}>{errors.language}</span>
              )}
            </div>

            <div className={styles.formGroup}>
              <label htmlFor="status">Estado</label>
              <select
                id="status"
                name="status"
                value={formData.status}
                onChange={handleChange}
                className={errors.status ? styles.error : ""}
              >
                {statusOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              {errors.status && (
                <span className={styles.errorMessage}>{errors.status}</span>
              )}
            </div>
          </div>

          {/* Summary Section */}
          {formData.movieId && formData.showDate && formData.showTime && (
            <div className={styles.summarySection}>
              <h4>Resumen del Horario</h4>
              <div className={styles.summaryDetails}>
                <p><strong>Película:</strong> {getSelectedMovie()?.title}</p>
                <p><strong>Duración:</strong> {getSelectedMovie()?.duration} minutos</p>
                <p><strong>Fecha y Hora:</strong> {formData.showDate} a las {formData.showTime}</p>
                {getSelectedRoom() && (
                  <p><strong>Sala:</strong> {getSelectedRoom()?.name} ({getSelectedRoom()?.capacity} asientos)</p>
                )}
                <p><strong>Formato:</strong> {formatOptions.find(f => f.value === formData.format)?.label}</p>
                <p><strong>Idioma:</strong> {languageOptions.find(l => l.value === formData.language)?.label}</p>
              </div>
            </div>
          )}

          <div className={styles.formActions}>
            <button type="button" className={styles.cancelBtn} onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className={styles.saveBtn}>
              {showtime ? "Actualizar" : "Guardar"} Horario
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ShowtimeForm;