import { useState, useEffect } from "react";
import AdminLayout from "../../layouts/AdminLayout";
import ProductForm from "../../components/admin/ProductForm";
import CategoryForm from "../../components/admin/CategoryForm";
import {
  fetchProducts,
  createProduct,
  updateProduct,
  deleteProduct,
  fetchCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from "../../services/confectioneryService";
import "../../styles/ConfectioneryManagement.css";

function ConfectioneryManagement() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [activeTab, setActiveTab] = useState("products");
  const [showProductForm, setShowProductForm] = useState(false);
  const [showCategoryForm, setShowCategoryForm] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const [editingCategory, setEditingCategory] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedCategory, setSelectedCategory] = useState("all");
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [productsData, categoriesData] = await Promise.all([
        fetchProducts(),
        fetchCategories(),
      ]);
      setProducts(productsData);
      setCategories(categoriesData);
    } catch (err) {
      setError("Error al cargar los datos: " + err.message);
      console.error("Error fetching data:", err);
    } finally {
      setLoading(false);
    }
  };

  // Product handlers
  const handleAddProduct = () => {
    setEditingProduct(null);
    setShowProductForm(true);
  };

  const handleEditProduct = (product) => {
    setEditingProduct(product);
    setShowProductForm(true);
  };

  const handleDeleteProduct = async (productId) => {
    if (
      window.confirm("¿Estás seguro de que quieres eliminar este producto?")
    ) {
      try {
        await deleteProduct(productId);
        setProducts(products.filter((product) => product.id !== productId));
      } catch (err) {
        alert("Error al eliminar el producto: " + err.message);
      }
    }
  };

  const handleSaveProduct = async (productData) => {
    try {
      // Transform data to match API request format
      const requestData = {
        name: productData.name,
        description: productData.description,
        price: productData.price,
        image: productData.image || null,
        categoryId: productData.categoryId,
        stock: productData.stock,
        stockUnit: productData.stockUnit || "unidades",
      };

      let savedProduct;
      if (editingProduct) {
        savedProduct = await updateProduct(editingProduct.id, requestData);
        setProducts(
          products.map((product) =>
            product.id === editingProduct.id ? savedProduct : product
          )
        );
      } else {
        savedProduct = await createProduct(requestData);
        setProducts([...products, savedProduct]);
      }

      setShowProductForm(false);
      setEditingProduct(null);
    } catch (err) {
      alert("Error al guardar el producto: " + err.message);
    }
  };

  // Category handlers
  const handleAddCategory = () => {
    setEditingCategory(null);
    setShowCategoryForm(true);
  };

  const handleEditCategory = (category) => {
    setEditingCategory(category);
    setShowCategoryForm(true);
  };

  const handleDeleteCategory = async (categoryId) => {
    const productsInCategory = products.filter(
      (p) => p.category.id === categoryId
    );
    if (productsInCategory.length > 0) {
      alert(
        "No se puede eliminar la categoría porque tiene productos asociados."
      );
      return;
    }

    if (
      window.confirm("¿Estás seguro de que quieres eliminar esta categoría?")
    ) {
      try {
        await deleteCategory(categoryId);
        setCategories(
          categories.filter((category) => category.id !== categoryId)
        );
      } catch (err) {
        alert("Error al eliminar la categoría: " + err.message);
      }
    }
  };

  const handleSaveCategory = async (categoryData) => {
    try {
      // Transform data to match API request format
      const requestData = {
        name: categoryData.name,
      };

      let savedCategory;
      if (editingCategory) {
        savedCategory = await updateCategory(editingCategory.id, requestData);
        setCategories(
          categories.map((category) =>
            category.id === editingCategory.id ? savedCategory : category
          )
        );
      } else {
        savedCategory = await createCategory(requestData);
        setCategories([...categories, savedCategory]);
      }

      setShowCategoryForm(false);
      setEditingCategory(null);
    } catch (err) {
      alert("Error al guardar la categoría: " + err.message);
    }
  };

  const handleCloseProductForm = () => {
    setShowProductForm(false);
    setEditingProduct(null);
  };

  const handleCloseCategoryForm = () => {
    setShowCategoryForm(false);
    setEditingCategory(null);
  };

  const filteredProducts =
    selectedCategory === "all"
      ? products
      : products.filter(
          (p) => p.category.id === Number.parseInt(selectedCategory)
        );

  if (loading) {
    return (
      <AdminLayout>
        <div className="loading">Cargando confitería...</div>
      </AdminLayout>
    );
  }

  if (error) {
    return (
      <AdminLayout>
        <div className="error-container">
          <div className="error-message">{error}</div>
          <button className="retry-btn" onClick={fetchData}>
            Reintentar
          </button>
        </div>
      </AdminLayout>
    );
  }

  return (
    <AdminLayout>
      <div className="confectionery-management">
        <div className="page-header">
          <h2>Gestión de Confitería</h2>
          <div className="header-actions">
            <button
              className={`tab-btn ${activeTab === "products" ? "active" : ""}`}
              onClick={() => setActiveTab("products")}
            >
              Productos
            </button>
            <button
              className={`tab-btn ${
                activeTab === "categories" ? "active" : ""
              }`}
              onClick={() => setActiveTab("categories")}
            >
              Categorías
            </button>
          </div>
        </div>

        {activeTab === "products" && (
          <div className="products-section">
            <div className="section-header">
              <div className="filters">
                <select
                  value={selectedCategory}
                  onChange={(e) => setSelectedCategory(e.target.value)}
                  className="category-filter"
                >
                  <option value="all">Todas las categorías</option>
                  {categories.map((category) => (
                    <option key={category.id} value={category.id}>
                      {category.name}
                    </option>
                  ))}
                </select>
              </div>
              <button className="add-btn" onClick={handleAddProduct}>
                + Agregar Producto
              </button>
            </div>

            <div className="products-grid">
              {filteredProducts.map((product) => (
                <div key={product.id} className="product-card">
                  <div className="product-image default-placeholder">
                    {product.image && (
                      <img src={product.image} alt={product.name} />
                    )}
                  </div>

                  <div className="product-info">
                    <h3>{product.name}</h3>
                    <p className="product-description">{product.description}</p>
                    <div className="product-details">
                      <span className="product-category">
                        {product.category.name}
                      </span>
                      <div className="product-pricing">
                        <span className="product-price">
                          S/ {product.price.toFixed(2)}
                        </span>
                      </div>
                      <div className="product-stock">
                        <span
                          className={`stock-badge ${
                            product.stock > 20
                              ? "high"
                              : product.stock > 0
                              ? "low"
                              : "empty"
                          }`}
                        >
                          Stock: {product.stock}{" "}
                          {product.stockUnit || "unidades"}
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="product-actions">
                    <button
                      className="edit-btn"
                      onClick={() => handleEditProduct(product)}
                    >
                      ✏️ Editar
                    </button>
                    <button
                      className="delete-btn"
                      onClick={() => handleDeleteProduct(product.id)}
                    >
                      🗑️ Eliminar
                    </button>
                  </div>
                </div>
              ))}

              {filteredProducts.length === 0 && (
                <div className="empty-state">
                  <p>No hay productos en esta categoría</p>
                </div>
              )}
            </div>
          </div>
        )}

        {activeTab === "categories" && (
          <div className="categories-section">
            <div className="section-header">
              <h3>Categorías de Productos</h3>
              <button className="add-btn" onClick={handleAddCategory}>
                + Agregar Categoría
              </button>
            </div>

            <div className="categories-table">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Productos</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {categories.map((category) => {
                    const productCount = products.filter(
                      (p) => p.category.id === category.id
                    ).length;
                    return (
                      <tr key={category.id}>
                        <td className="category-id">#{category.id}</td>
                        <td className="category-name">{category.name}</td>
                        <td className="product-count">
                          {productCount} productos
                        </td>
                        <td className="category-actions">
                          <button
                            className="edit-btn-small"
                            onClick={() => handleEditCategory(category)}
                          >
                            ✏️
                          </button>
                          <button
                            className="delete-btn-small"
                            onClick={() => handleDeleteCategory(category.id)}
                            disabled={productCount > 0}
                          >
                            🗑️
                          </button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>

              {categories.length === 0 && (
                <div className="empty-state">
                  <p>No hay categorías registradas</p>
                </div>
              )}
            </div>
          </div>
        )}

        {showProductForm && (
          <ProductForm
            product={editingProduct}
            categories={categories}
            onSave={handleSaveProduct}
            onClose={handleCloseProductForm}
          />
        )}

        {showCategoryForm && (
          <CategoryForm
            category={editingCategory}
            onSave={handleSaveCategory}
            onClose={handleCloseCategoryForm}
          />
        )}
      </div>
    </AdminLayout>
  );
}

export default ConfectioneryManagement;
