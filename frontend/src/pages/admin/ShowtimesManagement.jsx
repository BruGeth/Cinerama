import { useState, useEffect } from "react";
import AdminLayout from "../../layouts/AdminLayout";
import ShowtimeForm from "../../components/admin/ShowtimeForm";
import TicketPriceForm from "../../components/admin/TicketPriceForm";
import {
  fetchShowtimes,
  createShowtime,
  updateShowtime,
  deleteShowtime,
} from "../../services/showtimesService";
import { ticketPricesService, getTicketTypes } from "../../services/ticketPricesService";
import { fetchMovies } from "../../services/movieService";
import { fetchCinemas } from "../../services/cinemasService";
import styles from "../../styles/ShowtimesManagement.module.css";

function ShowtimesManagement() {
  const [showtimes, setShowtimes] = useState([]);
  const [movies, setMovies] = useState([]);
  const [cinemas, setCinemas] = useState([]);
  const [ticketTypes, setTicketTypes] = useState([]);
  const [ticketPrices, setTicketPrices] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [showPriceForm, setShowPriceForm] = useState(false);
  const [editingShowtime, setEditingShowtime] = useState(null);
  const [editingTicketPrice, setEditingTicketPrice] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState("showtimes"); // "showtimes" | "prices"
  const [filters, setFilters] = useState({
    movieId: "",
    cinemaId: "",
    date: "",
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [showtimesData, moviesData, cinemasData, ticketPricesData] = await Promise.all([
        fetchShowtimes(),
        fetchMovies(),
        fetchCinemas(),
        ticketPricesService.fetchTicketPrices(),
      ]);
      setShowtimes(showtimesData);
      setMovies(moviesData);
      setCinemas(cinemasData);
      setTicketPrices(ticketPricesData);
      setTicketTypes(getTicketTypes());
    } catch (error) {
      console.error("Error fetching data:", error);
      setError("Error al cargar los datos. Verifique que el servidor backend esté ejecutándose.");
    } finally {
      setLoading(false);
    }
  };

  // Función para formatear los nombres de formatos de manera más legible
  const formatDisplayName = (format) => {
    const formatMap = {
      'TWO_D': '2D',
      'THREE_D': '3D',
      'IMAX': 'IMAX',
      'FOUR_D_X': '4DX'
    };
    return formatMap[format] || format;
  };

  const handleAddShowtime = () => {
    setEditingShowtime(null);
    setShowForm(true);
  };

  const handleEditShowtime = (showtime) => {
    setEditingShowtime(showtime);
    setShowForm(true);
  };

  const handleDeleteShowtime = async (showtimeId) => {
    if (window.confirm("¿Estás seguro de que quieres eliminar este horario?")) {
      try {
        await deleteShowtime(showtimeId);
        setShowtimes(
          showtimes.filter((showtime) => showtime.id !== showtimeId)
        );
      } catch (error) {
        alert("Error al eliminar el horario: " + error.message);
      }
    }
  };

  const handleSaveShowtime = async (showtimeData) => {
    try {
      if (editingShowtime) {
        const updatedShowtime = await updateShowtime(editingShowtime.id, showtimeData);
        setShowtimes(
          showtimes.map((showtime) =>
            showtime.id === editingShowtime.id ? updatedShowtime : showtime
          )
        );
      } else {
        const newShowtime = await createShowtime(showtimeData);
        setShowtimes([...showtimes, newShowtime]);
      }
      setShowForm(false);
      setEditingShowtime(null);
    } catch (error) {
      alert("Error al guardar el horario: " + error.message);
    }
  };

  const handleCloseForm = () => {
    setShowForm(false);
    setEditingShowtime(null);
  };

  // Funciones para gestión de precios de tickets
  const handleAddTicketPrice = () => {
    setEditingTicketPrice(null);
    setShowPriceForm(true);
  };

  const handleEditTicketPrice = (ticketPrice) => {
    setEditingTicketPrice(ticketPrice);
    setShowPriceForm(true);
  };

  const handleDeleteTicketPrice = async (ticketPriceId) => {
    if (window.confirm("¿Estás seguro de que quieres eliminar este precio?")) {
      try {
        await ticketPricesService.deleteTicketPrice(ticketPriceId);
        setTicketPrices(
          ticketPrices.filter((price) => price.id !== ticketPriceId)
        );
      } catch (error) {
        alert("Error al eliminar el precio: " + error.message);
      }
    }
  };

  const handleSaveTicketPrice = async (ticketPriceData) => {
    try {
      if (editingTicketPrice) {
        const updatedPrice = await ticketPricesService.updateTicketPrice(
          editingTicketPrice.id,
          ticketPriceData
        );
        setTicketPrices(
          ticketPrices.map((price) =>
            price.id === editingTicketPrice.id ? updatedPrice : price
          )
        );
      } else {
        const newPrice = await ticketPricesService.createTicketPrice(ticketPriceData);
        setTicketPrices([...ticketPrices, newPrice]);
      }
      setShowPriceForm(false);
      setEditingTicketPrice(null);
    } catch (error) {
      alert("Error al guardar el precio: " + error.message);
    }
  };

  const handleClosePriceForm = () => {
    setShowPriceForm(false);
    setEditingTicketPrice(null);
  };

  const handleFilterChange = (filterName, value) => {
    setFilters({
      ...filters,
      [filterName]: value,
    });
  };

  const getTicketTypeInfo = (typeId) => {
    return ticketTypes.find((t) => t.id === typeId);
  };

  const getPriceRange = (ticketPrices) => {
    if (!ticketPrices || ticketPrices.length === 0) return "N/A";
    const prices = ticketPrices.map((p) => p.price);
    const min = Math.min(...prices);
    const max = Math.max(...prices);
    return min === max
      ? `S/ ${min.toFixed(2)}`
      : `S/ ${min.toFixed(2)} - S/ ${max.toFixed(2)}`;
  };

  const filteredShowtimes = showtimes.filter((showtime) => {
    if (
      filters.movieId &&
      showtime.movie?.id !== Number.parseInt(filters.movieId)
    )
      return false;
    if (
      filters.cinemaId &&
      showtime.cinema?.id !== Number.parseInt(filters.cinemaId)
    )
      return false;
    if (filters.date && showtime.showDate !== filters.date) return false;
    return true;
  });

  if (loading) {
    return (
      <AdminLayout>
        <div className={styles.loading}>Cargando horarios...</div>
      </AdminLayout>
    );
  }

  if (error) {
    return (
      <AdminLayout>
        <div className={styles.error}>
          <h3>Error de conexión</h3>
          <p>{error}</p>
          <button onClick={fetchData} className={styles.retryBtn}>
            Reintentar
          </button>
        </div>
      </AdminLayout>
    );
  }

  return (
    <AdminLayout>
      <div className={styles.showtimesManagement}>
        <div className={styles.pageHeader}>
          <h2>Gestión de Horarios y Precios</h2>
          
          {/* Tab Navigation */}
          <div className={styles.tabNavigation}>
            <button
              className={`${styles.tabBtn} ${activeTab === "showtimes" ? styles.active : ""}`}
              onClick={() => setActiveTab("showtimes")}
            >
              🎬 Horarios
            </button>
            <button
              className={`${styles.tabBtn} ${activeTab === "prices" ? styles.active : ""}`}
              onClick={() => setActiveTab("prices")}
            >
              💰 Precios
            </button>
          </div>

          <div className={styles.headerActions}>
            {activeTab === "showtimes" && (
              <button className={styles.addBtn} onClick={handleAddShowtime}>
                + Agregar Horario
              </button>
            )}
            {activeTab === "prices" && (
              <button className={styles.addBtn} onClick={handleAddTicketPrice}>
                + Agregar Precio
              </button>
            )}
          </div>
        </div>

        {/* Showtimes Tab */}
        {activeTab === "showtimes" && (
          <div className={styles.tabContent}>
            {/* Filtros */}
            <div className={styles.filtersSection}>
              <div className={styles.filtersRow}>
                <div className={styles.filterGroup}>
                  <label>Película:</label>
                  <select
                    value={filters.movieId}
                    onChange={(e) => handleFilterChange("movieId", e.target.value)}
                  >
                    <option value="">Todas las películas</option>
                    {movies.map((movie) => (
                      <option key={movie.id} value={movie.id}>
                        {movie.title}
                      </option>
                    ))}
                  </select>
                </div>

                <div className={styles.filterGroup}>
                  <label>Cine:</label>
                  <select
                    value={filters.cinemaId}
                    onChange={(e) => handleFilterChange("cinemaId", e.target.value)}
                  >
                    <option value="">Todos los cines</option>
                    {cinemas.map((cinema) => (
                      <option key={cinema.id} value={cinema.id}>
                        {cinema.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div className={styles.filterGroup}>
                  <label>Fecha:</label>
                  <input
                    type="date"
                    value={filters.date}
                    onChange={(e) => handleFilterChange("date", e.target.value)}
                  />
                </div>

                <button
                  className={styles.clearFiltersBtn}
                  onClick={() =>
                    setFilters({ movieId: "", cinemaId: "", date: "" })
                  }
                >
                  Limpiar Filtros
                </button>
              </div>
            </div>

            {/* Lista de horarios */}
            <div className={styles.showtimesTable}>
              <table>
                <thead>
                  <tr>
                    <th>Película</th>
                    <th>Cine</th>
                    <th>Sala</th>
                    <th>Fecha</th>
                    <th>Hora</th>
                    <th>Formato</th>
                    <th>Idioma</th>
                    <th>Precios</th>
                    <th>Disponibles</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredShowtimes.map((showtime) => (
                    <tr key={showtime.id}>
                      <td className={styles.movieTitle}>{showtime.movie?.title || 'N/A'}</td>
                      <td className={styles.cinemaName}>{showtime.cinema?.name || 'N/A'}</td>
                      <td className={styles.roomName}>
                        {showtime.room?.name || 'N/A'}
                        {showtime.room?.type && showtime.room.type !== "STANDARD" && (
                          <span
                            className={`${styles.roomTypeBadge} ${styles[showtime.room.type.toLowerCase()]}`}
                          >
                            {showtime.room.type}
                          </span>
                        )}
                      </td>
                      <td className={styles.showtimeDate}>{showtime.showDate}</td>
                      <td className={styles.showtimeTime}>{showtime.showTime}</td>
                      <td className={styles.formatCell}>
                        <span
                          className={`${styles.formatBadge} ${styles[showtime.format?.toLowerCase()]}`}
                        >
                          {formatDisplayName(showtime.format)}
                        </span>
                      </td>
                      <td className={styles.language}>{showtime.language}</td>
                      <td className={styles.pricesCell}>
                        <div className={styles.priceRange}>
                          {getPriceRange(showtime.ticketPrices)}
                        </div>
                        <div className={styles.priceDetails}>
                          {showtime.ticketPrices && showtime.ticketPrices.map((priceItem) => {
                            const typeInfo = getTicketTypeInfo(priceItem.type);
                            return (
                              <div key={priceItem.type} className={styles.priceItem}>
                                <span className={styles.priceType}>
                                  {typeInfo?.icon} {typeInfo?.name}:
                                </span>
                                <span className={styles.priceValue}>
                                  S/ {priceItem.price.toFixed(2)}
                                </span>
                              </div>
                            );
                          })}
                        </div>
                      </td>
                      <td className={styles.availabilityCell}>
                        <span
                          className={`${styles.availability} ${
                            showtime.availableSeats > 50
                              ? styles.high
                              : showtime.availableSeats > 20
                              ? styles.medium
                              : styles.low
                          }`}
                        >
                          {showtime.availableSeats}/{showtime.room?.capacity || 'N/A'}
                        </span>
                      </td>
                      <td className={styles.actions}>
                        <button
                          className={styles.editBtnSmall}
                          onClick={() => handleEditShowtime(showtime)}
                        >
                          ✏️
                        </button>
                        <button
                          className={styles.deleteBtnSmall}
                          onClick={() => handleDeleteShowtime(showtime.id)}
                        >
                          🗑️
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>

              {filteredShowtimes.length === 0 && (
                <div className={styles.emptyState}>
                  <p>No hay horarios que coincidan con los filtros seleccionados</p>
                </div>
              )}
            </div>
          </div>
        )}

        {/* Ticket Prices Tab */}
        {activeTab === "prices" && (
          <div className={styles.tabContent}>
            <div className={styles.pricesTable}>
              <table>
                <thead>
                  <tr>
                    <th>Película</th>
                    <th>Cine</th>
                    <th>Sala</th>
                    <th>Fecha</th>
                    <th>Hora</th>
                    <th>Tipo de Ticket</th>
                    <th>Precio</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {ticketPrices.map((ticketPrice) => {
                    const showtime = showtimes.find(s => s.id === ticketPrice.showtimeId);
                    const typeOption = [
                      { value: "GENERAL", label: "General/Adulto", icon: "👤" },
                      { value: "CHILD", label: "Niños", icon: "🧒" },
                      { value: "STUDENT", label: "Estudiantes", icon: "🎓" },
                      { value: "SENIOR", label: "Tercera Edad", icon: "👴" },
                      { value: "VIP", label: "VIP", icon: "⭐" }
                    ].find(t => t.value === ticketPrice.type);

                    if (!showtime) return null;

                    return (
                      <tr key={ticketPrice.id}>
                        <td className={styles.movieTitle}>{showtime.movie?.title || 'N/A'}</td>
                        <td className={styles.cinemaName}>{showtime.cinema?.name || 'N/A'}</td>
                        <td className={styles.roomName}>{showtime.room?.name || 'N/A'}</td>
                        <td className={styles.showtimeDate}>{showtime.showDate}</td>
                        <td className={styles.showtimeTime}>{showtime.showTime}</td>
                        <td className={styles.ticketType}>
                          <span className={styles.typeIcon}>{typeOption?.icon}</span>
                          {typeOption?.label}
                        </td>
                        <td className={styles.priceValue}>
                          S/ {ticketPrice.price.toFixed(2)}
                        </td>
                        <td className={styles.actions}>
                          <button
                            className={styles.editBtnSmall}
                            onClick={() => handleEditTicketPrice(ticketPrice)}
                          >
                            ✏️
                          </button>
                          <button
                            className={styles.deleteBtnSmall}
                            onClick={() => handleDeleteTicketPrice(ticketPrice.id)}
                          >
                            🗑️
                          </button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>

              {ticketPrices.length === 0 && (
                <div className={styles.emptyState}>
                  <p>No hay precios de tickets configurados</p>
                </div>
              )}
            </div>
          </div>
        )}

        {/* Forms */}
        {showForm && (
          <ShowtimeForm
            showtime={editingShowtime}
            movies={movies}
            cinemas={cinemas}
            onSave={handleSaveShowtime}
            onClose={handleCloseForm}
          />
        )}

        {showPriceForm && (
          <TicketPriceForm
            ticketPrice={editingTicketPrice}
            showtimes={showtimes}
            onSave={handleSaveTicketPrice}
            onClose={handleClosePriceForm}
          />
        )}
      </div>
    </AdminLayout>
  );
}

export default ShowtimesManagement;
