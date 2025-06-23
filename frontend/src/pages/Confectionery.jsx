// Import React and hooks
import React, { useState, useEffect } from "react";
// Import the CSS file for styles
import '../styles/Confectionery.css';

// Functional component for the Confectionery section
const Confectionery = () => {
  // State to keep track of the selected category
  const [selectedCategory, setSelectedCategory] = useState("Todos");
  // State for categories loaded from API
  const [categories, setCategories] = useState([{ id: 0, name: "Todos" }]);
  // State for products loaded from API
  const [products, setProducts] = useState([]);
  // State for loading indicator
  const [loading, setLoading] = useState(true);

  // Load categories from API
  useEffect(() => {
    fetch("/api/confectionery-categories")
      .then(res => res.json())
      .then(data => {
        setCategories([{ id: 0, name: "Todos" }, ...data]);
      })
      .catch(() => setCategories([{ id: 0, name: "Todos" }]));
  }, []);

  // Load products from API
  useEffect(() => {
    setLoading(true);
    fetch("/api/confectionery-products")
      .then(res => res.json())
      .then(data => {
        console.log("Products from API:", data); // <-- Add this line
        setProducts(data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  // Filter the products based on the selected category
const filteredProducts = selectedCategory === "Todos"
  ? products
  : products.filter(p => 
      (p.categoryName && p.categoryName === selectedCategory) ||
      (p.category && p.category.name === selectedCategory)
    );

  // JSX to render the component
  return (
    <div className="confectionery-container">
      {/* Main title */}
      <h1 className="confectionery-title">Confitería</h1>
      {/* Short description */}
      <p className="confectionery-description">Disfruta de nuestros deliciosos snacks...</p>

      {/* Category selection buttons */}
      <div className="category-buttons">
        {categories.map(cat => (
          <button
            key={cat.id}
            className={`category-button ${selectedCategory === cat.name ? 'active' : ''}`}
            onClick={() => setSelectedCategory(cat.name)} // Change selected category on click
          >
            {cat.name}
          </button>
        ))}
      </div>

      {/* Product cards grid */}
      {loading ? (
        <p>Cargando productos...</p>
      ) : (
        <div className={`product-grid ${selectedCategory !== "Todos" ? "horizontal" : ""}`}>
          {filteredProducts.map((prod) => (
            <div key={prod.id} className="product-card">
              {/* Product image */}
              <img src={prod.image} alt={prod.name} className="product-image" />
              {/* Product name */}
              <h3 className="product-title">{prod.name}</h3>
              {/* Product description */}
              <p className="product-description">{prod.description}</p>
              {/* Product price */}
              <p className="product-price">S/. {prod.price.toFixed(2)}</p>
              {/* Action button */}
              <button className="product-button">Añadir</button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

// Export the component
export default Confectionery;
