import { useState } from "react";
import { addProduct } from "../api/productService";
import useAuth from "../auth/useAuth";
import "./components_stytles/AddProductModal.css";

const AddProductModal = ({ isOpen, onClose, onProductAdded }) => {
  const { user, token } = useAuth();
  const [url, setUrl] = useState("");
  const [productName, setProductName] = useState("");
  const [targetPrice, setTargetPrice] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const data = await addProduct(
        {
          userId: user.id,
          url,
          productName,
          targetPrice: parseFloat(targetPrice),
        },
        token
      );
      onProductAdded(data);
      onClose();
      setUrl("");
      setProductName("");
      setTargetPrice("");
    } catch (err) {
      console.error(err);
      setError(err.response?.data || "Failed to add product");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal">
        <h2>Add Product</h2>
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
            required
            min="0"
          />
          {error && <p className="error">{error}</p>}
          <div className="modal-buttons">
            <button type="submit" disabled={loading}>
              {loading ? "Adding..." : "Add Product"}
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

export default AddProductModal;
