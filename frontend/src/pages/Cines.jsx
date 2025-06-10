import React from "react"; // Import React library
import "../styles/Cines.css"; // Import CSS for styling

// Array containing cinema details, including name, location, and image URL
const cines = [
  {
    nombre: "CINERAMA MIRAFLORES", // Name of the cinema
    ubicacion: "Edificio El Pacífico, Av. José Pardo 121, Miraflores Lima 18", // Address
    imagen: "https://www.cinerama.com.pe/_admin/assets/images/cines/pacifico.jpg" // Image URL for display
  },
  {
    nombre: "CINERAMA MINKA",
    ubicacion: "AV ARGENTINA 3093 CC MINKA 2DO NIVEL CALLAO",
    imagen: "https://www.cinerama.com.pe/_admin/assets/images/cines/minka.jpg"
  }
];

// Functional component for displaying individual cinema cards
const CineCard = ({ cine }) => (
  <div className="cine-card"> {/* Styled container for each cinema */}
    <img src={cine.imagen} alt={cine.nombre} /> {/* Cinema image */}
    <div className="cine-info-box"> {/* Container for cinema name and location */}
      <h2>{cine.nombre}</h2> {/* Cinema name */}
      <p>{cine.ubicacion}</p> {/* Cinema location */}
    </div>
  </div>
);

// Main component that displays the list of cinemas
const Cines = () => (
  <div className="cine-page"> {/* Main container for cinema listings */}
    <h1 className="page-title">NUESTRAS SEDES</h1> {/* Page title */}
    <div className="cine-list"> {/* Container for rendering cinema cards dynamically */}
      {cines.map((cine, index) => (
        <CineCard key={index} cine={cine} /> /* Render each cinema card */
      ))}
    </div>
  </div>
);

export default Cines; // Export component for use in other parts of the application
