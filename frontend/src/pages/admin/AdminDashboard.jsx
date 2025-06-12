import { useState, useEffect } from "react"
import AdminLayout from "../../layouts/AdminLayout"
import "../../styles/AdminDashboard.css"
import { fetchMovies } from "../../services/movieService";

function AdminDashboard() {
  const [stats, setStats] = useState({
    totalMovies: 0,
    totalCinemas: 0,
    totalUsers: 0,
    todayRevenue: 0,
    todayTickets: 0,
    occupancyRate: 0,
  })

  const [recentActivity, setRecentActivity] = useState([])

  useEffect(() => {
  // Fetch movies from API and update stats
  const loadStats = async () => {
    try {
      const movies = await fetchMovies();
      setStats((prev) => ({
        ...prev,
        totalMovies: movies.length,
        // Here you can add more logic to fetch other stats from your API
      }));
    } catch (err) {
      // Manage error fetching movies
    }
  };

  loadStats();

  // Simulate fetching stats and recent activity
  setTimeout(() => {
    setStats((prev) => ({
      ...prev,
      totalCinemas: 8,
      totalUsers: 1250,
      todayRevenue: 15420,
      todayTickets: 342,
      occupancyRate: 78,
    }));

      setRecentActivity([
        { id: 1, action: "Nueva película agregada", item: "Deadpool 3", time: "2 horas ago" },
        { id: 2, action: "Horario actualizado", item: "Sala 3 - Joker", time: "4 horas ago" },
        { id: 3, action: "Usuario registrado", item: "juan.perez@email.com", time: "6 horas ago" },
        { id: 4, action: "Producto agregado", item: "Combo Familiar XL", time: "1 día ago" },
      ])
    }, 1000)
  }, [])

  return (
    <AdminLayout>
      <div className="dashboard">
        <div className="dashboard-header">
          <h2>Dashboard</h2>
          <p>Resumen general del sistema</p>
        </div>

        {/* Stats Cards */}
        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-icon">🎬</div>
            <div className="stat-content">
              <h3>{stats.totalMovies}</h3>
              <p>Películas Activas</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon">🏢</div>
            <div className="stat-content">
              <h3>{stats.totalCinemas}</h3>
              <p>Salas de Cine</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon">👥</div>
            <div className="stat-content">
              <h3>{stats.totalUsers}</h3>
              <p>Usuarios Registrados</p>
            </div>
          </div>

          <div className="stat-card revenue">
            <div className="stat-icon">💰</div>
            <div className="stat-content">
              <h3>S/ {stats.todayRevenue.toLocaleString()}</h3>
              <p>Ingresos Hoy</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon">🎟️</div>
            <div className="stat-content">
              <h3>{stats.todayTickets}</h3>
              <p>Boletos Vendidos Hoy</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon">📊</div>
            <div className="stat-content">
              <h3>{stats.occupancyRate}%</h3>
              <p>Ocupación Promedio</p>
            </div>
          </div>
        </div>

        {/* Recent Activity */}
        <div className="dashboard-section">
          <h3>Actividad Reciente</h3>
          <div className="activity-list">
            {recentActivity.map((activity) => (
              <div key={activity.id} className="activity-item">
                <div className="activity-content">
                  <strong>{activity.action}</strong>
                  <span className="activity-item-name">{activity.item}</span>
                </div>
                <span className="activity-time">{activity.time}</span>
              </div>
            ))}
          </div>
        </div>

        {/* Quick Actions */}
        <div className="dashboard-section">
          <h3>Acciones Rápidas</h3>
          <div className="quick-actions">
            <button className="quick-action-btn">
              <span className="action-icon">🎬</span>
              Agregar Película
            </button>
            <button className="quick-action-btn">
              <span className="action-icon">⏰</span>
              Crear Horario
            </button>
            <button className="quick-action-btn">
              <span className="action-icon">🍿</span>
              Nuevo Producto
            </button>
            <button className="quick-action-btn">
              <span className="action-icon">📈</span>
              Ver Reportes
            </button>
          </div>
        </div>
      </div>
    </AdminLayout>
  )
}

export default AdminDashboard
