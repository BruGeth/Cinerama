import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import '../styles/Confectionery.css';

// Main component for the confectionery page
const Confectionery = () => {
  const [selectedCategory, setSelectedCategory] = useState("Todos");
  const [categories, setCategories] = useState([{ id: 0, name: "Todos" }]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedItems, setSelectedItems] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [showLoginWarning, setShowLoginWarning] = useState(false);
  const [showSummary, setShowSummary] = useState(false);

  const navigate = useNavigate();

  // --- State management ---
  const [selectedCategory, setSelectedCategory] = useState("Todos"); // Currently selected category
  const [categories, setCategories] = useState([{ id: 0, name: "Todos" }]); // List of categories
  const [products, setProducts] = useState([]); // List of products
  const [loading, setLoading] = useState(true); // Loading state

  // Side panel state
  const [selectedItems, setSelectedItems] = useState([]); // Items added to the order
  // Auth state (simulate user login)
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Should come from auth context or similar
  const [showLoginWarning, setShowLoginWarning] = useState(false); // Show login warning modal
  const [showSummary, setShowSummary] = useState(false); // Show summary panel

  // Navigation hook
  const navigate = useNavigate();

  // --- Fetch categories from API on mount ---
  useEffect(() => {
    fetch("/api/confectionery-categories")
      .then(res => res.json())
      .then(data => setCategories([{ id: 0, name: "Todos" }, ...data]))
      .catch(() => setCategories([{ id: 0, name: "Todos" }]));
  }, []);

  // --- Fetch products from API on mount ---
  useEffect(() => {
    setLoading(true);
    fetch("/api/confectionery-products")
      .then(res => res.json())
      .then(data => {
        setProducts(data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  useEffect(() => {
    const logged = localStorage.getItem("isLoggedIn") === "true" || !!localStorage.getItem("token");
    setIsLoggedIn(logged);
  }, []);

  const filteredProducts = selectedCategory === "Todos"
    ? products.filter(p => p.stock > 0)
    : products.filter(p =>
        ((p.categoryName && p.categoryName === selectedCategory) ||
         (p.category && p.category.name === selectedCategory)) &&
        p.stock > 0
      );

  const handleAddItem = (product) => {
    if (!isLoggedIn) {
      setShowLoginWarning(true);
      return;
    }
    if (product.stock <= 0) return;

    setSelectedItems(prevItems => {
      const existing = prevItems.find(item => item.id === product.id);
      if (existing) {
        if (existing.quantity < product.stock) {
          return prevItems.map(item =>
            item.id === product.id ? { ...item, quantity: item.quantity + 1 } : item
          );
        }
        return prevItems;
      }
      return [...prevItems, { ...product, quantity: 1 }];
    });
  };

  const handleRemoveItem = (productId) => {
    const item = selectedItems.find(i => i.id === productId);
    if (!item) return;

    setSelectedItems(prevItems =>
      prevItems
        .map(item =>
          item.id === productId ? { ...item, quantity: item.quantity - 1 } : item
        )
        .filter(item => item.quantity > 0)
    );

    setProducts(prev =>
      prev.map(p =>
        p.id === productId ? { ...p, stock: p.stock + 1 } : p
      )
    );
  };

  const total = selectedItems.reduce((sum, item) => sum + item.price * item.quantity, 0);

  const handleBuy = async () => {
    try {
      await fetch('/api/confectionery-purchase', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ items: selectedItems })
      });
      const res = await fetch("/api/confectionery-products");
      const data = await res.json();
      setProducts(data);
      setSelectedItems([]);
    } catch (error) {
      alert("Error al procesar la compra. Intenta nuevamente.");
    }
  };

  // --- Check login status on mount ---
  useEffect(() => {
    // Example: check if user is logged in via localStorage (adjust as needed)
    const logged = localStorage.getItem("isLoggedIn") === "true" || !!localStorage.getItem("token");
    setIsLoggedIn(logged);
  }, []);

  // --- Filter products by selected category and stock ---
  const filteredProducts = selectedCategory === "Todos"
    ? products.filter(p => p.stock > 0)
    : products.filter(p =>
        ((p.categoryName && p.categoryName === selectedCategory) ||
        (p.category && p.category.name === selectedCategory)) &&
        p.stock > 0
      );

  // --- Add product to side panel (order) ---
  const handleAddItem = (product) => {
    if (!isLoggedIn) {
      setShowLoginWarning(true);
      return;
    }
    if (product.stock <= 0) return;

    setSelectedItems(prevItems => {
      const existing = prevItems.find(item => item.id === product.id);
      if (existing) {
        // Permite agregar hasta el máximo stock
        if (existing.quantity < product.stock) {
          return prevItems.map(item =>
            item.id === product.id ? { ...item, quantity: item.quantity + 1 } : item
          );
        }
        return prevItems;
      }
      return [...prevItems, { ...product, quantity: 1 }];
    });
  };

  // --- Remove product from side panel (order) ---
  const handleRemoveItem = (productId) => {
    const item = selectedItems.find(i => i.id === productId);
    if (!item) return;

    setSelectedItems(prevItems =>
      prevItems
        .map(item =>
          item.id === productId ? { ...item, quantity: item.quantity - 1 } : item
        )
        .filter(item => item.quantity > 0)
    );

    // Restore local stock
    setProducts(prev =>
      prev.map(p =>
        p.id === productId ? { ...p, stock: p.stock + 1 } : p
      )
    );
  };

  // --- Calculate total price in real time ---
  const total = selectedItems.reduce((sum, item) => sum + item.price * item.quantity, 0);

  // --- Filter out items linked to movies for the ticket ---

  // --- Handle purchase action ---
  const handleBuy = async () => {
    try {
      // Envía los productos seleccionados al backend para actualizar el stock
      await fetch('/api/confectionery-purchase', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ items: selectedItems })
      });
      // Opcional: refresca productos para ver el nuevo stock
      const res = await fetch("/api/confectionery-products");
      const data = await res.json();
      setProducts(data);
      setSelectedItems([]); // Limpia el carrito
    } catch (error) {
      alert("Error al procesar la compra. Intenta nuevamente.");
    }
  };

  // --- Render UI ---
  return (
    <div className="confectionery-container">
      {/* Page title and description */}
      <h1 className="confectionery-title">Confitería</h1>
      <p className="confectionery-description">Disfruta de nuestros deliciosos snacks...</p>

      {/* Category buttons */}
      <div className="category-buttons">
        {categories.map(cat => (
          <button
            key={cat.id}
            className={`category-button ${selectedCategory === cat.name ? 'active' : ''}`}
            onClick={() => setSelectedCategory(cat.name)}
            onClick={() => setSelectedCategory(cat.name)}
          >
            {cat.name}
          </button>
        ))}
      </div>

      {/* Product grid or loading message */}
      {loading ? (
        <p>Cargando productos...</p>
      ) : (
        <div className={`product-grid ${selectedCategory !== "Todos" ? "horizontal" : ""}`}>
          {filteredProducts.map((prod) => (
            <div key={prod.id} className="product-card">
              <img src={prod.image} alt={prod.name} className="product-image" />
              <h3 className="product-title">{prod.name}</h3>
              <p className="product-description">{prod.description}</p>
              <p className="product-price">S/. {prod.price.toFixed(2)}</p>
              <div className="quantity-controls">
                <button
                  className="quantity-btn"
                  onClick={() => handleRemoveItem(prod.id)}
                  disabled={!selectedItems.find(item => item.id === prod.id)}
                >−</button>
                <span className="quantity-count">
                  {selectedItems.find(item => item.id === prod.id)?.quantity || 0}
                </span>
                <button
                  className="quantity-btn"
                  onClick={() => handleAddItem(prod)}
                  disabled={
                    prod.stock <= 0 ||
                    (selectedItems.find(item => item.id === prod.id)?.quantity || 0) >= prod.stock
                  }
                >+</button>
              </div>
              <div className="quantity-controls">
                <button
                  className="quantity-btn"
                  onClick={() => handleRemoveItem(prod.id)}
                  disabled={
                    !selectedItems.find(item => item.id === prod.id)
                  }
                >−</button>
                <span className="quantity-count">
                  {selectedItems.find(item => item.id === prod.id)?.quantity || 0}
                </span>
                <button
                  className="quantity-btn"
                  onClick={() => handleAddItem(prod)}
                  disabled={
                    prod.stock <= 0 ||
                    (selectedItems.find(item => item.id === prod.id)?.quantity || 0) >= prod.stock
                  }
                >+</button>
              </div>
            </div>
          ))}
        </div>
      )}

      {selectedItems.length > 0 && !showSummary && (
        <button className="finalize-btn" onClick={() => setShowSummary(true)}>
          Finalizar
        </button>
      )}

      {showLoginWarning && (
        <div className="login-warning-modal">
          <div className="modal-content">
            <p style={{ fontWeight: 700, fontSize: '1.1rem', marginBottom: '1.5rem' }}>
              Debes iniciar sesión para agregar productos.
            </p>
            <button className="register-btn" onClick={() => navigate("/login")}>Ir al Inicio de Sesión</button>
            <button className="cancel-btn" onClick={() => setShowLoginWarning(false)}>Cancelar</button>
          </div>
        </div>
      )}

      {showSummary && (
        <div className="login-warning-modal">
          <div className="modal-content">
            <h2 style={{ marginBottom: '1rem' }}>Resumen de tu compra</h2>
            <ul style={{ textAlign: 'left', marginBottom: '1.5rem' }}>
              {selectedItems.map(item => (
                <li key={item.id}>
                  {item.name} x {item.quantity} — S/. {(item.price * item.quantity).toFixed(2)}
                </li>
              ))}
            </ul>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end' }}>
              <p className="total" style={{ marginBottom: '1.5rem', fontWeight: 700, fontSize: '1.2rem' }}>
                Total: S/. {total.toFixed(2)}
              </p>
              <div>
                <button
                  className="register-btn"
                  style={{ marginRight: '0.5rem' }}
                  onClick={async () => {
                    await handleBuy();
                    setShowSummary(false);
                    navigate("/resumen-pago");
                  }}
                >
                  Comprar
                </button>
                <button className="cancel-btn" onClick={() => setShowSummary(false)}>Cancelar</button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Finalize button */}
      {selectedItems.length > 0 && !showSummary && (
        <button
          className="finalize-btn"
          onClick={() => setShowSummary(true)}
        >
          Finalizar
        </button>
      )}

      {/* Login warning modal */}
      {showLoginWarning && (
        <div className="login-warning-modal">
          <div className="modal-content">
            <p style={{fontWeight:700, fontSize:'1.1rem', marginBottom:'1.5rem'}}>Debes iniciar sesión para agregar productos.</p>
            <button className="register-btn" onClick={() => navigate("/login")}>Ir al Inicio de Sesión</button>
            <button className="cancel-btn" onClick={() => setShowLoginWarning(false)}>Cancelar</button>
          </div>
        </div>
      )}

      {/* Summary modal */}
      {showSummary && (
        <div className="login-warning-modal">
          <div className="modal-content">
            <h2 style={{marginBottom: '1rem'}}>Resumen de tu compra</h2>
            <ul style={{textAlign: 'left', marginBottom: '1.5rem'}}>
              {selectedItems.map(item => (
                <li key={item.id}>
                  {item.name} x {item.quantity} — S/. {(item.price * item.quantity).toFixed(2)}
                </li>
              ))}
            </ul>
            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'flex-end'}}>
              <p className="total" style={{marginBottom: '1.5rem', fontWeight: 700, fontSize: '1.2rem'}}>Total: S/. {total.toFixed(2)}</p>
              <div>
                <button
                  className="register-btn"
                  style={{marginRight: '0.5rem'}}
                  onClick={async () => {
                    await handleBuy(); // lógica para actualizar stock
                    setShowSummary(false);
                    navigate("/resumen-pago");
                  }}
                >
                  Comprar
                </button>
                <button
                  className="cancel-btn"
                  onClick={() => setShowSummary(false)}
                >
                  Cancelar
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Confectionery;