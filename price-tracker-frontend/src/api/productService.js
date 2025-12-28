import api from "./axios";

// Get all products for a user
export const getUserProducts = async (userId, token) => {
  try {
    const response = await api.get(`/products/user/${userId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    throw error.response ? error.response.data : error;
  }
};

// Add a new tracked product
export const addProduct = async (productData, token) => {
  try {
    const response = await api.post("/products", productData, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    throw error.response ? error.response.data : error;
  }
};

// Update an existing product
export const updateProduct = async (id, productData, token) => {
  try {
    const response = await api.put(`/products/${id}`, productData, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    throw error.response ? error.response.data : error;
  }
};

// Delete (soft delete) a product
export const deleteProduct = async (id, token) => {
  try {
    const response = await api.delete(`/products/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    throw error.response ? error.response.data : error;
  }
};

// Refresh product price
export const refreshProductPrice = async (id, token) => {
  try {
    const response = await api.post(`/products/${id}/refresh`, {}, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    throw error.response ? error.response.data : error;
  }
};

// Get product by ID
export const getProductById = async (id, token) => {
  try {
    const response = await api.get(`/products/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    throw error.response ? error.response.data : error;
  }
};