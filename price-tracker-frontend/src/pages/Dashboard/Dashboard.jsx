import { useState, useEffect, useCallback } from "react";
import useAuth from "../../auth/useAuth.jsx";
import { getUserProducts } from "../../api/productService.js";
import ProductCard from "../../components/ProductCard.jsx";
import AddProductModal from "../../components/AddProductModal.jsx";
import EditProductModal from "../../components/EditProductModal.jsx";
import Loader from "../../components/Loader.jsx";
import "./Dashboard.css";

const Dashboard = () => {
  const { user, token } = useAuth();
  const userId = user?.id;

  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  const [addModalOpen, setAddModalOpen] = useState(false);
  const [editProduct, setEditProduct] = useState(null);

  const [error, setError] = useState("");

  const [searchTerm, setSearchTerm] = useState("");
  const [showSuggestions, setShowSuggestions] = useState(false);

  const fetchProducts = useCallback(async () => {
    if (!userId) return;

    setLoading(true);
    setError("");

    try {
      const data = await getUserProducts(userId, token);
      setProducts(data);
    } catch (err) {
      console.error(err);
      setError(err.response?.data || "Failed to fetch products");
    } finally {
      setLoading(false);
    }
  }, [userId, token]);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  const handleProductUpdate = (updatedProduct) => {
    setProducts((prev) =>
      prev.map((p) => (p.id === updatedProduct.id ? updatedProduct : p))
    );
  };

  const handleProductDelete = (productId) => {
    setProducts((prev) => prev.filter((p) => p.id !== productId));
  };

  const handleEditProduct = (product) => {
    setEditProduct(product);
  };

  const filteredProducts = products.filter((product) =>
    product.productName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const suggestions = products
    .filter((product) =>
      product.productName.toLowerCase().includes(searchTerm.toLowerCase())
    )
    .slice(0, 5);

  return (
    <div className="dashboard-container">
      <h2>Welcome, {user?.name}</h2>

      <button onClick={() => setAddModalOpen(true)}>Add New Product</button>

      {loading && <Loader />}
      {error && <p className="error">{error}</p>}

      <div className="search-container">
        <input
          type="text"
          placeholder="Search products..."
          value={searchTerm}
          onChange={(e) => {
            setSearchTerm(e.target.value);
            setShowSuggestions(true);
          }}
          onBlur={() => setTimeout(() => setShowSuggestions(false), 150)}
          className="search-input"
        />

        {showSuggestions && searchTerm && (
          <div className="suggestions-box">
            {suggestions.length > 0 ? (
              suggestions.map((product) => (
                <div
                  key={product.id}
                  className="suggestion-item"
                  onMouseDown={() => {
                    setSearchTerm(product.productName);
                    setShowSuggestions(false);
                  }}
                >
                  {product.productName}
                </div>
              ))
            ) : (
              <div className="suggestion-item muted">No matching products</div>
            )}
          </div>
        )}
      </div>

      <div className="products-grid">
        {filteredProducts.map((product) => (
          <ProductCard
            key={product.id}
            product={product}
            onUpdate={handleProductUpdate}
            onDelete={handleProductDelete}
            onEdit={handleEditProduct}
          />
        ))}
      </div>

      {/* Add product modal */}
      {addModalOpen && (
        <AddProductModal
          isOpen={addModalOpen}
          onClose={() => setAddModalOpen(false)}
          onProductAdded={fetchProducts}
        />
      )}

      {/* Edit product modal */}
      {editProduct && (
        <EditProductModal
          isOpen={true} // ✅ REQUIRED
          product={editProduct}
          onClose={() => setEditProduct(null)}
          onUpdate={handleProductUpdate} // ✅ CORRECT prop name
        />
      )}
    </div>
  );
};

export default Dashboard;
