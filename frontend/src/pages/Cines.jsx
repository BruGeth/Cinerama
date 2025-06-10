
import React from "react";
import "../styles/Cines.css";// Import styles for layout and design

// Array containing cinema details, including name, location, and image URL
const cines = [
  {
    nombre: "CINERAMA MIRAFLORES",
    ubicacion: "Edificio El Pacífico, Av. José Pardo 121, Miraflores Lima 18",
    imagen: "https://www.cinerama.com.pe/_admin/assets/images/cines/pacifico.jpg"
  },
  {
    nombre: "CINERAMA MINKA",
    ubicacion: "AV ARGENTINA 3093 CC MINKA 2DO NIVEL CALLAO",
    imagen: "https://www.cinerama.com.pe/_admin/assets/images/cines/minka.jpg"
  }
];

// Functional component for displaying individual cinema cards
const CineCard = ({ cine }) => (
  <div className="cine-card">
  <img src={cine.imagen} alt={cine.nombre} />
  <div className="cine-info-box">
    <h2>{cine.nombre}</h2>
    <p>{cine.ubicacion}</p>
  </div>
</div>
);

// Main component that displays the cinema list
const Cines = () => (
  <div className="cine-page">
    <h1 className="page-title">NUESTRAS SEDES</h1>
    <div className="cine-list">{/* Container for displaying cinemas */}
      {cines.map((cine, index) => (
        <CineCard key={index} cine={cine} /> /* Render each cinema as a card */
      ))}
    </div>
  </div>
);
export default Cines;// Export component for use in other parts of the application
