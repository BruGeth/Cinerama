import React, { useState } from "react";
import '../styles/Confectionery.css';

const Confectionery = () => {
  const categorias = ["Todos", "Palomitas", "Refrescos", "Dulces", "Combos"];
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState("Todos");

  const productos = [
    {
      categoria: "Palomitas",
      nombre: "Palomitas Grandes",
      descripcion: "Palomitas recién hechas, tamaño grande",
      precio: 16.90,
      imagen: "https://p.turbosquid.com/ts-thumb/q1/WlHUvk/lQ/001/jpg/1625651436/600x600/fit_q87/d39477d2dde168a604144720e97ff678c657037f/001.jpg",
    },
    {
      categoria: "Palomitas",
      nombre: "Palomitas Medianas",
      descripcion: "Palomitas tamaño mediano",
      precio: 13.00,
      imagen: "https://p.turbosquid.com/ts-thumb/q1/WlHUvk/iI/005/jpg/1625651396/1920x1080/fit_q99/c79b4c54b2c6d892dc8c955e89e9dde571a4599f/005.jpg",
    },
    {
      categoria: "Refrescos",
      nombre: "Refresco Grande",
      descripcion: "Refresco a elegir, tamaño grande",
      precio: 16.90,
      imagen: "https://media.istockphoto.com/id/909938470/es/foto/comida-r%C3%A1pida-cola-bebe-copa-y-pajita.jpg?s=612x612&w=0&k=20&c=MV_j5gIoFgobJM_lTmhgDnpSt1g_eStiww1xrPFw8uA=  ",
    },
    {
      categoria: "Refrescos",
      nombre: "Refresco Mediano",
      descripcion: "Refresco a elegir, tamaño mediano",
      precio: 12.00,
      imagen: "https://media.istockphoto.com/id/956386632/es/foto/vaso-desechable-blanco-y-rojo-en-blanco-con-paja-imitan-para-arriba.jpg?s=612x612&w=0&k=20&c=de8VsML7qXdKd0HbxgMrejMfWCZpS7RpYTC5ApROuJI=",
    },
    {
      categoria: "Dulces",
      nombre: "Gomitas",
      descripcion: "Surtido de gomitas de frutas",
      precio: 7.00,
      imagen: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSXPs2WEZERBz0Vh52PG_Ha30zwOr7j88n3Mg&s",
    },
    {
      categoria: "Combos",
      nombre: "Combo Individual",
      descripcion: "1 palomita mediana, 1 gaseosa mediana y 1 hot dog",
      precio: 37.00,
      imagen: "https://media.istockphoto.com/id/681903568/es/foto/palomitas-de-ma%C3%ADz-en-caja-con-cola.jpg?s=612x612&w=0&k=20&c=BFq5KXeKl9d29uciTycAu1hZYJg2ZF_3FM1avCq1vsk=",
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

  <div className={`product-grid ${categoriaSeleccionada !== "Todos" ? "horizontal" : ""}`}>
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
