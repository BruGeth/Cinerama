// Cines.jsx
import React from "react";
import "../styles/Cines.css";

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

const CineCard = ({ cine }) => (
  <div className="cine-card">
  <img src={cine.imagen} alt={cine.nombre} />
  <div className="cine-info-box">
    <h2>{cine.nombre}</h2>
    <p>{cine.ubicacion}</p>
  </div>
</div>
);

const Cines = () => (
  <div className="cine-list">
    {cines.map((cine, index) => (
      <CineCard key={index} cine={cine} />
    ))}
  </div>
);

export default Cines;
