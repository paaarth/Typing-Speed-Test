import { Link, useNavigate } from 'react-router-dom';
import { Keyboard } from 'lucide-react';
import { useAuth } from '../../context/AuthContext.jsx';
import './Navbar.css';

export default function Navbar() {
  const { isAuthenticated, isAdmin, username, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="navbar">
      <div className="navbar-inner container">
        <Link to="/" className="navbar-brand">
          <span className="navbar-brand-icon">
            <Keyboard size={20} strokeWidth={2.5} />
          </span>
          SpeedType
        </Link>

        <nav className="navbar-links">
          <Link to="/" className="navbar-link">Home</Link>
          {isAuthenticated && (
            <Link to="/profile" className="navbar-link">Profile</Link>
          )}
          {isAdmin && (
            <Link to="/admin" className="navbar-link">Admin</Link>
          )}
        </nav>

        <div className="navbar-auth">
          {isAuthenticated ? (
            <>
              <span className="navbar-username">Hi, {username}</span>
              <button className="keycap keycap-ghost navbar-btn" onClick={handleLogout}>
                Log out
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="keycap keycap-ghost navbar-btn">Log in</Link>
              <Link to="/register" className="keycap keycap-strawberry navbar-btn">Sign up</Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
