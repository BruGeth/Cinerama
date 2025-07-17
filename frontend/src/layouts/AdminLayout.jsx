import { useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "../styles/AdminLayout.css";

function AdminLayout({ children }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [sidebarOpen, setSidebarOpen] = useState(true);

  const handleLogout = async () => {
    await logout();
    navigate("/login");
  };

  const menuItems = [
    { path: "/admin/dashboard", label: "Dashboard", icon: "📊" },
    { path: "/admin/movies", label: "Películas", icon: "🎬" },
    { path: "/admin/cinemas", label: "Salas", icon: "🏢" },
    { path: "/admin/showtimes", label: "Horarios", icon: "⏰" },
    { path: "/admin/confectionery", label: "Confitería", icon: "🍿" },
    { path: "/admin/reports", label: "Reportes", icon: "📈" },
    { path: "/admin/users", label: "Usuarios", icon: "👥" },
    {path: "/admin/backup", label: "Respaldo", icon: "☁️" },
  ];

  return (
    <div className="admin-layout">
      {/* Sidebar */}
      <aside className={`admin-sidebar ${sidebarOpen ? "open" : "closed"}`}>
        <div className="sidebar-header">
          <div className="admin-logo">
            {sidebarOpen ? (
              <>
                <span className="logo-ciner">CINER</span>
                <span className="logo-ama">AMA</span>
              </>
            ) : (
              <span className="logo-ciner">C</span>
            )}
          </div>
          <button
            className="sidebar-toggle"
            onClick={() => setSidebarOpen(!sidebarOpen)}
          >
            {sidebarOpen ? "←" : "→"}
          </button>
        </div>

        <nav className="sidebar-nav">
          {menuItems.map((item) => (
            <Link
              key={item.path}
              to={item.path}
              className={`nav-item ${
                location.pathname === item.path ? "active" : ""
              }`}
            >
              <span className="nav-icon">{item.icon}</span>
              {sidebarOpen && <span className="nav-label">{item.label}</span>}
            </Link>
          ))}
        </nav>

        <div className="sidebar-footer">
          <div className="user-info">
            {sidebarOpen && (
              <>
                <div className="user-name">
                  {user?.name
                    ? user.name
                    : user?.email
                    ? user.email.split("@")[0]
                    : "Usuario"}
                </div>
                <div className="user-role">Administrador</div>
              </>
            )}
          </div>
          <button
            className="logout-btn"
            onClick={handleLogout}
            title="Cerrar sesión"
          >
            🚪
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <div className="admin-main">
        <header className="admin-header">
          <h1>Panel de Administración</h1>
          <div className="header-actions">
            <Link to="/" className="view-site-btn">
              Ver Sitio
            </Link>
          </div>
        </header>

        <main className="admin-content">{children}</main>
      </div>
    </div>
  );
}

export default AdminLayout;
