import { useState } from "react";
import { refreshProductPrice, deleteProduct } from "../api/productService";
import useAuth from "../auth/useAuth";
import DeleteConfirmModal from "./DeleteConfirmModal";
import "./components_stytles/ProductCard.css";
const ProductCard = ({ product, onUpdate, onDelete, onEdit }) => {
  const { token } = useAuth();

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  const handleRefresh = async () => {
    setLoading(true);
    setError("");
    try {
      const updatedProduct = await refreshProductPrice(product.id, token);
      onUpdate(updatedProduct);
    } catch (err) {
      console.error(err);
      setError(err.response?.data || "Failed to refresh price");
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteConfirm = async () => {
    setLoading(true);
    setError("");
    try {
      await deleteProduct(product.id, token);
      onDelete(product.id);
      setShowDeleteModal(false);
    } catch (err) {
      console.error(err);
      setError(err.response?.data || "Failed to delete product");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="product-card">
      <h3>{product.productName}</h3>

      <p>
        <strong>Current Price:</strong>{" "}
        {product.currentPrice ? `₹${product.currentPrice}` : "N/A"}
      </p>

      <p>
        <strong>Target Price:</strong> ₹{product.targetPrice}
      </p>

      <p>
        <a href={product.url} target="_blank" rel="noopener noreferrer">
          View Product
        </a>
      </p>

      {error && <p className="error">{error}</p>}

      <div className="product-card-buttons">
        <button onClick={handleRefresh} disabled={loading}>
          {loading ? "Refreshing..." : "Refresh Price"}
        </button>

        <button onClick={() => onEdit(product)}>
          Edit
        </button>

        <button
          onClick={() => setShowDeleteModal(true)}
          disabled={loading}
        >
          Delete
        </button>
      </div>

      {/* Delete confirmation modal */}
      <DeleteConfirmModal
        isOpen={showDeleteModal}
        onClose={() => setShowDeleteModal(false)}
        onConfirm={handleDeleteConfirm}
        loading={loading}
      />
    </div>
  );
};

export default ProductCard;