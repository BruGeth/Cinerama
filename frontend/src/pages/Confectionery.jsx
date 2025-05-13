import React, { useState } from "react";
import '../styles/Confectionery.css';

const Confectionery = () => {
  const categorias = ["Todos", "Palomitas", "Gaseosas", "Dulces", "Combos"];
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState("Todos");

  const productos = [
    {
      categoria: "Palomitas",
      nombre: "Palomitas Grandes",
      descripcion: "Palomitas recién hechas, tamaño grande",
      precio: 16.90,
      image: '/images/PromoSlider1.jpeg',
    },
    {
      categoria: "Palomitas",
      nombre: "Palomitas Medianas",
      descripcion: "Palomitas tamaño mediano",
      precio: 13.00,
      imagen: "/img/palomitas-medianas.png",
    },
    {
      categoria: "Gaseosas",
      nombre: "Refresco Grande",
      descripcion: "Refresco a elegir, tamaño grande",
      precio: 16.90,
      imagen: "/img/refresco-grande.png",
    },
    {
      categoria: "Gaseosas",
      nombre: "Refresco Mediano",
      descripcion: "Refresco a elegir, tamaño mediano",
      precio: 12.00,
      imagen: "/img/refresco-mediano.png",
    },
    {
      categoria: "Dulces",
      nombre: "Gomitas",
      descripcion: "Surtido de gomitas de frutas",
      precio: 7.00,
      imagen: "/img/gomitas.png",
    },
    {
      categoria: "Combos",
      nombre: "Combo Individual",
      descripcion: "1 palomita mediana, 1 gaseosa mediana y 1 hot dog",
      precio: 37.00,
      imagen: "/img/combo-individual.png",
    },
  ];

  const productosFiltrados = categoriaSeleccionada === "Todos"
    ? productos
    : productos.filter(p => p.categoria === categoriaSeleccionada);

  return (
    <div className="confectionery-container">
  <h1 className="confectionery-title">Confitería</h1>
  <p className="confectionery-description">Disfruta de nuestros deliciosos snacks...</p>

  <div className="category-buttons">
    {categorias.map(cat => (
      <button
        key={cat}
        className={`category-button ${categoriaSeleccionada === cat ? 'active' : ''}`}
        onClick={() => setCategoriaSeleccionada(cat)}
      >
        {cat}
      </button>
    ))}
  </div>

  <div className="product-grid">
    {productosFiltrados.map((prod, idx) => (
      <div key={idx} className="product-card">
        <img src={prod.imagen} alt={prod.nombre} className="product-image" />
        <h3 className="product-title">{prod.nombre}</h3>
        <p className="product-description">{prod.descripcion}</p>
        <p className="product-price">S/. {prod.precio.toFixed(2)}</p>
        <button className="product-button">Añadir</button>
      </div>
    ))}
  </div>
</div>
  );
};

export default Confectionery;
