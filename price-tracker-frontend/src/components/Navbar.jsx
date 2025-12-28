import { Link, useNavigate } from "react-router-dom";
import useAuth from "../auth/useAuth";
import "./components_stytles/Navbar.css";

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link to="/" className="navbar-logo">
          PriceTracker
        </Link>
      </div>
      <div className="navbar-right">
        {user ? (
          <>
            <Link to="/dashboard" className="navbar-link">
              Dashboard
            </Link>
            <Link to="/profile" className="navbar-link">
              Profile
            </Link>
            <button onClick={handleLogout} className="navbar-button">
              Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/login" className="navbar-link">
              Login
            </Link>
            <Link to="/register" className="navbar-link">
              Register
            </Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
