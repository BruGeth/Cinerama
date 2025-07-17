import { useState, useEffect } from "react";
import AdminLayout from "../../layouts/AdminLayout";
import CinemaForm from "../../components/admin/CinemaForm";
import RoomForm from "../../components/admin/RoomForm";
import {
  fetchCinemas,
  fetchRooms,
  createCinema,
  updateCinema,
  deleteCinema,
  createRoom,
  updateRoom,
  deleteRoom,
} from "../../services/cinemasService";
import styles from "../../styles/CinemasManagement.module.css";

function CinemasManagement() {
  const [cinemas, setCinemas] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [activeTab, setActiveTab] = useState("cinemas");
  const [showCinemaForm, setShowCinemaForm] = useState(false);
  const [showRoomForm, setShowRoomForm] = useState(false);
  const [editingCinema, setEditingCinema] = useState(null);
  const [editingRoom, setEditingRoom] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedCinema, setSelectedCinema] = useState("all");

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [cinemasData, roomsData] = await Promise.all([
        fetchCinemas(),
        fetchRooms(),
      ]);
      setCinemas(cinemasData);
      setRooms(roomsData);
    } catch (error) {
      console.error("Error fetching data:", error);
    } finally {
      setLoading(false);
    }
  };

  // Cinema handlers
  const handleAddCinema = () => {
    setEditingCinema(null);
    setShowCinemaForm(true);
  };

  const handleEditCinema = (cinema) => {
    setEditingCinema(cinema);
    setShowCinemaForm(true);
  };

  const handleDeleteCinema = async (cinemaId) => {
    const roomsInCinema = rooms.filter((r) => r.cinemaId === cinemaId);
    if (roomsInCinema.length > 0) {
      alert("No se puede eliminar el cine porque tiene salas asociadas.");
      return;
    }

    if (window.confirm("¿Estás seguro de que quieres eliminar este cine?")) {
      try {
        await deleteCinema(cinemaId);
        setCinemas(cinemas.filter((cinema) => cinema.id !== cinemaId));
      } catch (error) {
        alert("Error al eliminar el cine: " + error.message);
      }
    }
  };

  const handleSaveCinema = async (cinemaData) => {
    try {
      if (editingCinema) {
        // Editar cine existente
        const updatedCinema = await updateCinema(editingCinema.id, cinemaData);
        setCinemas(
          cinemas.map((cinema) =>
            cinema.id === editingCinema.id
              ? { ...updatedCinema, id: editingCinema.id }
              : cinema
          )
        );
      } else {
        // Crear nuevo cine
        const newCinema = await createCinema(cinemaData);
        setCinemas([...cinemas, newCinema]);
      }
      setShowCinemaForm(false);
      setEditingCinema(null);
    } catch (error) {
      alert("Error al guardar el cine: " + error.message);
    }
  };

  // Room handlers
  const handleAddRoom = () => {
    setEditingRoom(null);
    setShowRoomForm(true);
  };

  const handleEditRoom = (room) => {
    setEditingRoom(room);
    setShowRoomForm(true);
  };

  const handleDeleteRoom = async (roomId) => {
    if (window.confirm("¿Estás seguro de que quieres eliminar esta sala?")) {
      try {
        await deleteRoom(roomId);
        setRooms(rooms.filter((room) => room.id !== roomId));
      } catch (error) {
        alert("Error al eliminar la sala: " + error.message);
      }
    }
  };

  const handleSaveRoom = async (roomData) => {
    try {
      if (editingRoom) {
        // Editar sala existente
        const updatedRoom = await updateRoom(editingRoom.id, roomData);
        setRooms(
          rooms.map((room) =>
            room.id === editingRoom.id
              ? { ...updatedRoom, id: editingRoom.id }
              : room
          )
        );
      } else {
        // Crear nueva sala
        const newRoom = await createRoom(roomData);
        setRooms([...rooms, newRoom]);
      }
      setShowRoomForm(false);
      setEditingRoom(null);
    } catch (error) {
      alert("Error al guardar la sala: " + error.message);
    }
  };

  const handleCloseCinemaForm = () => {
    setShowCinemaForm(false);
    setEditingCinema(null);
  };

  const handleCloseRoomForm = () => {
    setShowRoomForm(false);
    setEditingRoom(null);
  };

  const filteredRooms =
    selectedCinema === "all"
      ? rooms
      : rooms.filter((r) => r.cinemaId === Number.parseInt(selectedCinema));

  if (loading) {
    return (
      <AdminLayout>
        <div className={styles.loading}>Cargando cines y salas...</div>
      </AdminLayout>
    );
  }

  return (
    <AdminLayout>
      <div className={styles.cinemasManagement}>
        <div className={styles.pageHeader}>
          <h2>Gestión de Cines y Salas</h2>
          <div className={styles.headerActions}>
            <button
              className={`${styles.tabBtn} ${activeTab === "cinemas" ? styles.active : ""}`}
              onClick={() => setActiveTab("cinemas")}
            >
              Cines
            </button>
            <button
              className={`${styles.tabBtn} ${activeTab === "rooms" ? styles.active : ""}`}
              onClick={() => setActiveTab("rooms")}
            >
              Salas
            </button>
          </div>
        </div>

        {activeTab === "cinemas" && (
          <div className={styles.cinemasSection}>
            <div className={styles.sectionHeader}>
              <h3>Cines</h3>
              <button className={styles.addBtn} onClick={handleAddCinema}>
                + Agregar Cine
              </button>
            </div>

            <div className={styles.cinemasGrid}>
              {cinemas.map((cinema) => {
                const cinemaRooms = rooms.filter(
                  (r) => r.cinemaId === cinema.id
                );
                const totalCapacity = cinemaRooms.reduce(
                  (sum, room) => sum + room.capacity,
                  0
                );

                return (
                  <div key={cinema.id} className={styles.cinemaCard}>
                    <div className={styles.cinemaHeader}>
                      <h3>{cinema.name}</h3>
                      <span className={`${styles.statusBadge} ${styles[cinema.status.toLowerCase()]}`}>
                        {cinema.status === "ACTIVE" ? "Activo" : "Inactivo"}
                      </span>
                    </div>

                    <div className={styles.cinemaInfo}>
                      <p className={styles.cinemaAddress}>📍 {cinema.address}</p>
                      <p className={styles.cinemaPhone}>📞 {cinema.phone}</p>
                      <p className={styles.cinemaEmail}>✉️ {cinema.email}</p>
                    </div>

                    <div className={styles.cinemaStats}>
                      <div className={styles.stat}>
                        <span className={styles.statNumber}>
                          {cinemaRooms.length}
                        </span>
                        <span className={styles.statLabel}>Salas</span>
                      </div>
                      <div className={styles.stat}>
                        <span className={styles.statNumber}>{totalCapacity}</span>
                        <span className={styles.statLabel}>Asientos Totales</span>
                      </div>
                    </div>

                    <div className={styles.cinemaActions}>
                      <button
                        className={styles.editBtn}
                        onClick={() => handleEditCinema(cinema)}
                      >
                        ✏️ Editar
                      </button>
                      <button
                        className={styles.deleteBtn}
                        onClick={() => handleDeleteCinema(cinema.id)}
                        disabled={cinemaRooms.length > 0}
                      >
                        🗑️ Eliminar
                      </button>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {activeTab === "rooms" && (
          <div className={styles.roomsSection}>
            <div className={styles.sectionHeader}>
              <div className={styles.filters}>
                <select
                  value={selectedCinema}
                  onChange={(e) => setSelectedCinema(e.target.value)}
                  className={styles.cinemaFilter}
                >
                  <option value="all">Todos los cines</option>
                  {cinemas.map((cinema) => (
                    <option key={cinema.id} value={cinema.id}>
                      {cinema.name}
                    </option>
                  ))}
                </select>
              </div>
              <button className={styles.addBtn} onClick={handleAddRoom}>
                + Agregar Sala
              </button>
            </div>

            <div className={styles.roomsTable}>
              <table>
                <thead>
                  <tr>
                    <th>Sala</th>
                    <th>Cine</th>
                    <th>Capacidad</th>
                    <th>Tipo</th>
                    <th>Tecnologías</th>
                    <th>Sistema de Audio</th>
                    <th>Asientos Disponibles</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredRooms.map((room) => (
                    <tr key={room.id}>
                      <td className={styles.roomName}>{room.name}</td>
                      <td className={styles.cinemaName}>{room.cinema?.name || 
                        cinemas.find(c => c.id === room.cinemaId)?.name || 'N/A'}</td>
                      <td className={styles.roomCapacity}>
                        {room.capacity} asientos
                      </td>
                      <td>
                        <span
                          className={`${styles.typeBadge} ${styles[room.type.toLowerCase()]}`}
                        >
                          {room.type}
                        </span>
                      </td>
                      <td className={styles.format}>
                        {room.technology && room.technology.length > 0 
                          ? room.technology.map(tech => 
                              tech === 'TWO_D' ? '2D' : 
                              tech === 'THREE_D' ? '3D' : 
                              tech === 'FOUR_DX' ? '4DX' : tech
                            ).join(', ')
                          : 'N/A'}
                      </td>
                      <td className={styles.audioSystem}>
                        {room.audioSystem || 'N/A'}
                      </td>
                      <td className={styles.availableSeats}>
                        {room.availableSeats || room.capacity}
                      </td>
                      <td>
                        <span className={`${styles.statusBadge} ${styles[room.status.toLowerCase()]}`}>
                          {room.status === "ACTIVE" ? "Activa" : 
                           room.status === "INACTIVE" ? "Inactiva" : "Mantenimiento"}
                        </span>
                      </td>
                      <td className={styles.actions}>
                        <button
                          className={styles.editBtnSmall}
                          onClick={() => handleEditRoom(room)}
                        >
                          ✏️
                        </button>
                        <button
                          className={styles.deleteBtnSmall}
                          onClick={() => handleDeleteRoom(room.id)}
                        >
                          🗑️
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>

              {filteredRooms.length === 0 && (
                <div className={styles.emptyState}>
                  <p>No hay salas en este cine</p>
                </div>
              )}
            </div>
          </div>
        )}

        {showCinemaForm && (
          <CinemaForm
            cinema={editingCinema}
            onSave={handleSaveCinema}
            onClose={handleCloseCinemaForm}
          />
        )}

        {showRoomForm && (
          <RoomForm
            room={editingRoom}
            cinemas={cinemas}
            onSave={handleSaveRoom}
            onClose={handleCloseRoomForm}
          />
        )}
      </div>
    </AdminLayout>
  );
}

export default CinemasManagement;
