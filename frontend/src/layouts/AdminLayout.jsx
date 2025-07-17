import { useState, useEffect } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "../styles/AdminLayout.css";

function AdminLayout({ children }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [isMobile, setIsMobile] = useState(false);

  // Detectar si es dispositivo móvil
  useEffect(() => {
    const checkScreenSize = () => {
      const mobile = window.innerWidth <= 768;
      setIsMobile(mobile);
      if (mobile) {
        setSidebarOpen(false); // Cerrar sidebar en móvil por defecto
      } else {
        setSidebarOpen(true); // Abrir sidebar en desktop por defecto
      }
    };

    checkScreenSize();
    window.addEventListener('resize', checkScreenSize);

    return () => window.removeEventListener('resize', checkScreenSize);
  }, []);

  const handleLogout = async () => {
    await logout();
    navigate("/login");
  };

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  const closeSidebarOnMobile = () => {
    if (isMobile) {
      setSidebarOpen(false);
    }
  };

  const menuItems = [
    { path: "/admin/dashboard", label: "Dashboard", icon: "📊" },
    { path: "/admin/movies", label: "Películas", icon: "🎬" },
    { path: "/admin/cinemas", label: "Salas", icon: "🏢" },
    { path: "/admin/showtimes", label: "Horarios", icon: "⏰" },
    { path: "/admin/confectionery", label: "Confitería", icon: "🍿" },
    { path: "/admin/reports", label: "Reportes", icon: "📈" },
    { path: "/admin/users", label: "Usuarios", icon: "👥" },
  ];

  return (
    <div className="admin-layout">
      {/* Overlay para móviles */}
      {isMobile && sidebarOpen && (
        <div 
          className="sidebar-overlay" 
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* Sidebar */}
      <aside className={`admin-sidebar ${sidebarOpen ? "open" : "closed"} ${isMobile ? "mobile" : ""}`}>
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
            onClick={toggleSidebar}
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
              onClick={closeSidebarOnMobile}
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
          <div className="header-left">
            {isMobile && (
              <button 
                className="mobile-menu-btn" 
                onClick={toggleSidebar}
              >
                ☰
              </button>
            )}
            <h1>Panel de Administración</h1>
          </div>
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
