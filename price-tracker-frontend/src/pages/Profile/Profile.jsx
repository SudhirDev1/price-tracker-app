import { useState, useEffect, useCallback } from "react";
import useAuth from "../../auth/useAuth.jsx";
import { getUserById } from "../../api/authService.js";
import Loader from "../../components/Loader.jsx";
import "./Profile.css";

const Profile = () => {
  const { user, token } = useAuth();
  const userId = user?.id;

  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Fetch user profile
  const fetchProfile = useCallback(async () => {
    if (!userId) return;

    setLoading(true);
    setError("");

    try {
      const data = await getUserById(userId, token);
      setProfile(data);
    } catch (err) {
      console.error(err);
      setError(err.response?.data || "Failed to fetch profile");
    } finally {
      setLoading(false);
    }
  }, [userId, token]);

  useEffect(() => {
    fetchProfile();
  }, [fetchProfile]);

  if (loading) return <Loader />;

  if (!profile) return <p>No profile data found.</p>;

  return (
    <div className="profile-container">
      <h2>Profile</h2>
      {error && <p className="error">{error}</p>}

      <div className="profile-form">
        <label>
          Name:
          <input type="text" value={profile.name || ""} disabled />
        </label>

        <label>
          Email:
          <input type="email" value={profile.email || ""} disabled />
        </label>
      </div>
    </div>
  );
};

export default Profile;
