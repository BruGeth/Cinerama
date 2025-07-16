/* ===========================
Cinerama Confectionery Page - React Component
===========================
- Handles category/product fetching, selection, and order summary
- Integrates PayPal payment flow with currency conversion (PEN to USD)
- Prevents duplicate payment processing on reload
- Shows modals for login, summary, and purchase success
- All logic and UI changes are clearly commented
===========================*/
import React, { useState, useEffect } from 'react';
import '../styles/Confectionery.css';
import { useNavigate } from "react-router-dom";

// Main component for the confectionery page
const Confectionery = () => {

  // --- State management ---

  // Currently selected category (default: "Todos" = All)
  const [selectedCategory, setSelectedCategory] = useState("Todos");

  // List of categories (fetched from backend)
  const [categories, setCategories] = useState([{ id: 0, name: "Todos" }]);

  // List of products (fetched from backend)
  const [products, setProducts] = useState([]);

  // Loading state for product fetching
  const [loading, setLoading] = useState(true);

  // Items added to the order (side panel/cart)
  const [selectedItems, setSelectedItems] = useState([]);

  // Simulated user login state (should be replaced by real auth context)
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  // Show login warning modal if user tries to add without logging in
  const [showLoginWarning, setShowLoginWarning] = useState(false);

  // Show summary modal (order review and payment)
  const [showSummary, setShowSummary] = useState(false);

  // Show success modal after successful purchase
  const [showSuccess, setShowSuccess] = useState(false);

  // Show loading indicator for PayPal processing
  const [showPayPalLoading, setShowPayPalLoading] = useState(false);

  // Prevents duplicate payment processing on reload/redirect
  const [hasProcessedPayment, setHasProcessedPayment] = useState(false);

  // React Router navigation hook
  const navigate = useNavigate();

  // --- Fetch categories from API on mount ---
  useEffect(() => {
    fetch("/api/confectionery-categories")
      .then(res => res.json())
      .then(data => {
        // Add "Todos" (All) as the first category
        setCategories([{ id: 0, name: "Todos" }, ...data]);
      })
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

  // --- Check login status on mount ---
  useEffect(() => {
    // Example: check if user is logged in via localStorage (adjust as needed)
    const logged = localStorage.getItem("isLoggedIn") === "true" || !!localStorage.getItem("token");
    setIsLoggedIn(logged);
  }, []);

  // --- Handle PayPal return (prevents duplicate processing) ---
  useEffect(() => {
    // Read PayPal return params from URL
    const urlParams = new URLSearchParams(window.location.search);
    const paymentId = urlParams.get('paymentId');
    const payerId = urlParams.get('PayerID');
    const success = urlParams.get('success');
    const canceled = urlParams.get('canceled');

    // Immediately clean the URL to avoid reprocessing on reload
    if (paymentId || payerId || success || canceled) {
      window.history.replaceState({}, document.title, window.location.pathname);
    }

    // If payment was successful and not already processed, complete the purchase
    if (success === 'true' && paymentId && payerId && !hasProcessedPayment) {
      setHasProcessedPayment(true);
      completePayPalPurchase(paymentId, payerId);
    } else if (canceled === 'true') {
      alert('Payment canceled');
    }
  }, [hasProcessedPayment]);

  // --- Filter products by selected category and stock ---
  // Only show products in stock and matching the selected category
  const filteredProducts = selectedCategory === "Todos"
    ? products.filter(p => p.stock > 0)
    : products.filter(p =>
      ((p.categoryName && p.categoryName === selectedCategory) ||
        (p.category && p.category.name === selectedCategory)) &&
      p.stock > 0
    );

  // --- Add product to side panel (order/cart) ---
  const handleAddItem = (product) => {
    if (!isLoggedIn) {
      setShowLoginWarning(true);
      return;
    }
    if (product.stock <= 0) return;

    setSelectedItems(prevItems => {
      const existing = prevItems.find(item => item.id === product.id);
      if (existing) {
        // Allow adding up to the maximum stock
        if (existing.quantity < product.stock) {
          return prevItems.map(item =>
            item.id === product.id ? { ...item, quantity: item.quantity + 1 } : item
          );
        }
        return prevItems;
      }
      // Add new product to the cart
      return [...prevItems, { ...product, quantity: 1 }];
    });
  };

  // --- Remove product from side panel (order/cart) ---
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

    // Restore local stock (optional, for UI feedback)
    setProducts(prev =>
      prev.map(p =>
        p.id === productId ? { ...p, stock: p.stock + 1 } : p
      )
    );
  };

  // --- Calculate total price in real time (PEN) ---
  const total = selectedItems.reduce((sum, item) => sum + item.price * item.quantity, 0);

  // --- Convert total to USD for PayPal (using fixed exchange rate) ---
  const exchangeRate = 0.2818; // 1 sol ≈ 0.2818 USD
  const totalUSD = Math.round(total * exchangeRate * 100) / 100;

  // --- Handle PayPal purchase (create order and redirect) ---
  const handlePayPalPurchase = async () => {
    try {
      setShowPayPalLoading(true);

      // Prepare PayPal return/cancel URLs
      const returnUrl = `${window.location.origin}/confectionery?success=true`;
      const cancelUrl = `${window.location.origin}/confectionery?canceled=true`;

      // Create PayPal order via backend
      const response = await fetch('/api/confectionery-purchase/paypal/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          items: selectedItems.map(item => ({
            productId: item.id, // or "id" depending on backend
            quantity: item.quantity
          })),
          returnUrl,
          cancelUrl
        })
      });

      if (!response.ok) {
        throw new Error('Error creating PayPal order');
      }

      const data = await response.json();

      if (data.approval_url) {
        // Redirect to PayPal approval page
        window.location.href = data.approval_url;
      } else {
        throw new Error('No PayPal URL received');
      }

    } catch (error) {
      alert("Error processing PayPal payment: " + error.message);
      setShowPayPalLoading(false);
    }
  };

  // --- Complete PayPal purchase (after redirect) ---
  const completePayPalPurchase = async (paymentId, payerId) => {
    try {
      const response = await fetch('/api/confectionery-purchase/paypal/complete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ paymentId, payerId })
      });

      if (response.ok) {
        // Refresh products to update stock
        const res = await fetch("/api/confectionery-products");
        const data = await res.json();
        setProducts(data);
        setSelectedItems([]); // Clear cart
        setShowSummary(false); // Close summary modal
        setShowSuccess(true);  // Show success modal
      } else {
        throw new Error('Error completing purchase');
      }
    } catch (error) {
      alert("Error completing purchase: " + error.message);
    }
  };

  // --- Handle old direct purchase (for compatibility, not used with PayPal) ---
  const handleBuy = async () => {
    try {
      // Send selected products to backend to update stock
      await fetch('/api/confectionery-purchase', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ items: selectedItems })
      });
      // Refresh products to update stock
      const res = await fetch("/api/confectionery-products");
      const data = await res.json();
      setProducts(data);
      setSelectedItems([]); // Clear cart
      setShowSummary(false); // Close summary modal
      setShowSuccess(true);  // Show success modal
    } catch (error) {
      alert("Error processing purchase. Please try again.");
    }
  };

  // --- Render UI ---
  return (
    <div className="confectionery-container">
      {/* Page title and description */}
      <h1 className="confectionery-title">Confitería</h1>
      <p className="confectionery-description">Disfruta de nuestros deliciosos snacks...</p>

      {/* Category buttons (dynamic, with active state) */}
      <div className="category-buttons">
        {categories.map(cat => (
          <button
            key={cat.id}
            className={`category-button ${selectedCategory === cat.name ? 'active' : ''}`}
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
              {/* Product image */}
              <img src={prod.image} alt={prod.name} />

              {/* Product name */}
              <h3>{prod.name}</h3>

              {/* Product description */}
              <p>{prod.description}</p>

              {/* Product price */}
              <div className="product-price">S/ {prod.price}</div>

              {/* Quantity controls (add/remove) */}
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

      {/* Finalize button (shows summary modal) */}
      {selectedItems.length > 0 && !showSummary && (
        <button
          className="finalize-btn"
          onClick={() => setShowSummary(true)}
        >
          Finalize
        </button>
      )}

      {/* Login warning modal (shown if user tries to add without logging in) */}
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

      {/* Summary modal (shows order details and PayPal button) */}
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
            {/* Unified total row: shows both PEN and USD in a single line */}
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end' }}>
              <div className="total-row">
                <span className="total">Total: S/. {total.toFixed(2)}</span>
                <span className="total-separator">|</span>
                <span className="total-usd">≈ USD {totalUSD.toFixed(2)}</span>
              </div>
              <div>
                {/* PayPal button */}
                <button
                  className="register-btn"
                  style={{ marginRight: '0.5rem', backgroundColor: '#0070ba', borderColor: '#0070ba' }}
                  onClick={handlePayPalPurchase}
                  disabled={showPayPalLoading}
                >
                  {showPayPalLoading ? 'Procesando...' : 'Pagar con PayPal'}
                </button>
                {/* Cancel button */}
                <button
                  className="cancel-btn"
                  onClick={() => setShowSummary(false)}
                  disabled={showPayPalLoading}
                >
                  Cancelar
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Success modal (shown after successful purchase) */}
      {showSuccess && (
        <div className="login-warning-modal">
          <div className="modal-content">
            <h2 style={{ marginBottom: '1rem', color: '#28a745' }}>¡Compra exitosa!</h2>
            <p style={{ marginBottom: '1.5rem' }}>
              Tu compra fue realizada con éxito. ¡Gracias por tu preferencia!
            </p>
            <button
              className="register-btn"
              onClick={() => {
                setShowSuccess(false);
                navigate("/"); // Redirige a la página de inicio
              }}
            >
              Cerrar
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Confectionery; 