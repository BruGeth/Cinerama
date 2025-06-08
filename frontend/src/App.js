import logo from './logo.svg';
import './App/imagen/logo.css';

function App() {
  return (
    <div className="App">
      {/* Header */}
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <h1>Bienvenido a Mi Página</h1>
        <nav>
          <a className="App-link" href="#inicio">Inicio</a>
          <a className="App-link" href="#servicios">Servicios</a>
          <a className="App-link" href="#contacto">Contacto</a>
        </nav>
      </header>

      {/* Contenido principal */}
      <main>
        <p>Edita el archivo <code>src/App.js</code> y guarda para recargar.</p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Aprende React
        </a>
      </main>

      {/* Footer */}
      <footer className="App-footer">
        <p>&copy; 2025 Mi Página Web. Todos los derechos reservados.</p>
      </footer>
    </div>
  );
}

export default App;
