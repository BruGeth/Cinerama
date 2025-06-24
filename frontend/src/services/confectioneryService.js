const API_PRODUCTS = "/api/confectionery-products";
const API_CATEGORIES = "/api/confectionery-categories";

// Helper for auth headers
const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};

// PRODUCTS
export const fetchProducts = async () => {
  const res = await fetch(API_PRODUCTS, { headers: getAuthHeaders() });
  if (!res.ok) throw new Error("Error fetching products");
  return await res.json();
};

export const createProduct = async (productData) => {
  const res = await fetch(API_PRODUCTS, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(productData),
  });
  if (!res.ok) throw new Error("Error creating product");
  return await res.json();
};

export const updateProduct = async (id, productData) => {
  const res = await fetch(`${API_PRODUCTS}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(productData),
  });
  if (!res.ok) throw new Error("Error updating product");
  return await res.json();
};

export const deleteProduct = async (id) => {
  const res = await fetch(`${API_PRODUCTS}/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders(),
  });
  if (!res.ok) throw new Error("Error deleting product");
};

// CATEGORIES
export const fetchCategories = async () => {
  const res = await fetch(API_CATEGORIES, { headers: getAuthHeaders() });
  if (!res.ok) throw new Error("Error fetching categories");
  return await res.json();
};

export const createCategory = async (categoryData) => {
  const res = await fetch(API_CATEGORIES, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(categoryData),
  });
  if (!res.ok) throw new Error("Error creating category");
  return await res.json();
};

export const updateCategory = async (id, categoryData) => {
  const res = await fetch(`${API_CATEGORIES}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(categoryData),
  });
  if (!res.ok) throw new Error("Error updating category");
  return await res.json();
};

export const deleteCategory = async (id) => {
  const res = await fetch(`${API_CATEGORIES}/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders(),
  });
  if (!res.ok) throw new Error("Error deleting category");
};