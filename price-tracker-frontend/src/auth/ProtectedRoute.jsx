import { Navigate, Outlet } from "react-router-dom";
import useAuth from "./useAuth";

const ProtectedRoute = ({ redirectPath = "/login" }) => {
  const { user } = useAuth();

  if (!user) {
    // Not logged in → redirect
    return <Navigate to={redirectPath} replace />;
  }

  // Logged in → render child routes
  return <Outlet />;
};

export default ProtectedRoute;
