import { useState, useEffect } from "react";
import { updateProduct } from "../api/productService";
import useAuth from "../auth/useAuth";
import "./components_stytles/EditProductModal.css";

const EditProductModal = ({ isOpen, product, onClose, onUpdate }) => {
  const { token } = useAuth();

  const [productName, setProductName] = useState("");
  const [url, setUrl] = useState("");
  const [targetPrice, setTargetPrice] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // Prefill form when modal opens
  useEffect(() => {
    if (product) {
      setProductName(product.productName || "");
      setUrl(product.url || "");
      setTargetPrice(product.targetPrice || "");
    }
  }, [product]);

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const updated = await updateProduct(
        product.id,
        {
          productName,
          url,
          targetPrice: parseFloat(targetPrice),
        },
        token
      );

      onUpdate(updated);
      onClose();
    } catch (err) {
      console.error(err);
      setError(err.response?.data || "Failed to update product");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal">
        <h2>Edit Product</h2>

        <form onSubmit={handleSubmit}>
          <label>Product Name</label>
          <input
            type="text"
            value={productName}
            onChange={(e) => setProductName(e.target.value)}
            required
          />

          <label>Product URL</label>
          <input
            type="url"
            value={url}
            onChange={(e) => setUrl(e.target.value)}
            required
          />

          <label>Target Price</label>
          <input
            type="number"
            value={targetPrice}
            onChange={(e) => setTargetPrice(e.target.value)}
            min="0"
            required
          />

          {error && <p className="error">{error}</p>}

          <div className="modal-buttons">
            <button type="submit" disabled={loading}>
              {loading ? "Saving..." : "Save Changes"}
            </button>
            <button type="button" onClick={onClose} disabled={loading}>
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditProductModal;
