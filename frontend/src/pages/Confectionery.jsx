import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Confectionery.css";

const Confectionery = () => {
  const [selectedCategory, setSelectedCategory] = useState("Todos");
  const [categories, setCategories] = useState([{ id: 0, name: "Todos" }]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedItems, setSelectedItems] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [showLoginWarning, setShowLoginWarning] = useState(false);
  const [showSummary, setShowSummary] = useState(false);
  const [purchaseSuccess, setPurchaseSuccess] = useState(false);
  const [receiptDetails, setReceiptDetails] = useState(null);
  const [isProcessing, setIsProcessing] = useState(false);
  const [paymentError, setPaymentError] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    fetch("/api/confectionery-categories")
      .then((res) => res.json())
      .then((data) => setCategories([{ id: 0, name: "Todos" }, ...data]))
      .catch(() => setCategories([{ id: 0, name: "Todos" }]));
  }, []);

  useEffect(() => {
    setLoading(true);
    fetch("/api/confectionery-products")
      .then((res) => res.json())
      .then((data) => {
        setProducts(data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  useEffect(() => {
    const logged =
      localStorage.getItem("isLoggedIn") === "true" ||
      !!localStorage.getItem("token");
    setIsLoggedIn(logged);
  }, []);

  const filteredProducts =
    selectedCategory === "Todos"
      ? products.filter((p) => p.stock > 0)
      : products.filter(
        (p) =>
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

    setSelectedItems((prevItems) => {
      const existing = prevItems.find((item) => item.id === product.id);
      if (existing) {
        if (existing.quantity < product.stock) {
          return prevItems.map((item) =>
            item.id === product.id
              ? { ...item, quantity: item.quantity + 1 }
              : item
          );
        }
        return prevItems;
      }
      return [...prevItems, { ...product, quantity: 1 }];
    });
  };

  const handleRemoveItem = (productId) => {
    const item = selectedItems.find((i) => i.id === productId);
    if (!item) return;

    setSelectedItems((prevItems) =>
      prevItems
        .map((item) =>
          item.id === productId
            ? { ...item, quantity: item.quantity - 1 }
            : item
        )
        .filter((item) => item.quantity > 0)
    );

    setProducts((prev) =>
      prev.map((p) =>
        p.id === productId ? { ...p, stock: p.stock + 1 } : p
      )
    );
  };

  const total = selectedItems.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  const exchangeRate = 0.2818;
  const amountUSD = Math.round(total * exchangeRate * 100) / 100;

  const handleBuy = async () => {
    setIsProcessing(true);
    setPaymentError("");

    try {
      const token = localStorage.getItem("token");
      console.log("🔐 Token:", token);

      const res = await fetch("/api/confectionery-purchase", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(
          selectedItems.map((item) => ({
            productId: item.id,
            quantity: item.quantity,
          }))
        ),
      });

      console.log("📡 Respuesta recibida:", res);

      if (res.ok) {
        let data;
        try {
          data = await res.json();
          console.log("✅ JSON recibido:", data);
          setReceiptDetails({
            total: data.total.toFixed(2),
            items: data.items,
            date: new Date(data.date).toLocaleString(),
          });
        } catch (e) {
          console.error("❌ No se pudo convertir a JSON:", e);
          return;
        }

        setPurchaseSuccess(true);
        setSelectedItems([]);
        setShowSummary(false);
        setTimeout(() => {
          setPurchaseSuccess(false);
          setReceiptDetails(null);
        }, 7000);
      } else {
        const errorData = await res.json().catch(() => ({}));
        console.error("❌ Error de API:", errorData);
        setPaymentError(errorData.error || "Error al registrar la compra.");
      }
    } catch (error) {
      console.error("❌ Error técnico:", error);
      setPaymentError("No se pudo conectar con el servidor.");
    }

    setIsProcessing(false);
  };


  return (
    <div className="confectionery-container">
      <h1 className="confectionery-title">Confitería</h1>

      {purchaseSuccess && receiptDetails && (
        <div className="success-ticket">
          <h3>🎫 Ticket de compra registrado</h3>
          <p><strong>Fecha:</strong> {receiptDetails.date}</p>
          <ul>
            {receiptDetails.items.map((item, index) => (
              <li key={index}>
                {item.name} x {item.quantity} — S/. {(item.price * item.quantity).toFixed(2)}
              </li>
            ))}
          </ul>
          <p><strong>Total pagado:</strong> S/. {receiptDetails.total}</p>
          <p style={{ marginTop: "0.5rem" }}>
            📩 Se ha enviado un correo con los detalles de tu compra.
          </p>
        </div>
      )}

      <p className="confectionery-description">
        Disfruta de nuestros deliciosos snacks...
      </p>

      <div className="category-buttons">
        {categories.map((cat) => (
          <button
            key={cat.id}
            className={`category-button ${selectedCategory === cat.name ? "active" : ""
              }`}
            onClick={() => setSelectedCategory(cat.name)}
          >
            {cat.name}
          </button>
        ))}
      </div>

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
                  disabled={!selectedItems.find((item) => item.id === prod.id)}
                >
                  −
                </button>
                <span className="quantity-count">
                  {selectedItems.find((item) => item.id === prod.id)?.quantity || 0}
                </span>
                <button
                  className="quantity-btn"
                  onClick={() => handleAddItem(prod)}
                  disabled={
                    prod.stock <= 0 ||
                    (selectedItems.find((item) => item.id === prod.id)?.quantity || 0) >= prod.stock
                  }
                >
                  +
                </button>
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
            <p style={{ fontWeight: 700, fontSize: "1.1rem", marginBottom: "1.5rem" }}>
              Debes iniciar sesión para agregar productos.
            </p>
            <button className="register-btn" onClick={() => navigate("/login")}>
              Ir al Inicio de Sesión
            </button>
            <button className="cancel-btn" onClick={() => setShowLoginWarning(false)}>
              Cancelar
            </button>
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
                <br />
                <span style={{ fontWeight: 400, fontSize: '1rem' }}>
                  (≈ ${amountUSD} USD)
                </span>
              </p>
              <div>
                <button
                  className="register-btn"
                  style={{ marginRight: '0.5rem' }}
                  onClick={handleBuy}
                  disabled={isProcessing}
                >
                  {isProcessing ? "Procesando compra..." : "Confirmar compra"}
                </button>

              </div>
              {paymentError && (
                <p style={{ color: 'red', marginTop: '1rem' }}>{paymentError}</p>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Confectionery;
