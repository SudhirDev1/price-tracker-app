import "./components_stytles/DeleteConfirmModal.css";

import { createPortal } from "react-dom";

const DeleteConfirmModal = ({ isOpen, onClose, onConfirm, loading }) => {
  if (!isOpen) return null;

  const handleOverlayClick = (e) => {
    if (e.target === e.currentTarget) onClose();
  };

  return createPortal(
    <div className="modal-overlay" onClick={handleOverlayClick}>
      <div className="modal">
        <h3>Delete Product</h3>
        <p>Are you sure you want to delete this product?</p>

        <div className="modal-buttons">
          <button className="danger" onClick={onConfirm} disabled={loading}>
            {loading ? "Deleting..." : "Yes, Delete"}
          </button>
          <button onClick={onClose} disabled={loading}>
            Cancel
          </button>
        </div>
      </div>
    </div>,
    document.body
  );
};

export default DeleteConfirmModal;