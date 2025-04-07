import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Import useNavigate hook
import useravatar from './useravatar.png';
import './assets/styles.css';

export function ProfileDropdown({ username }) {
  const [isOpen, setIsOpen] = useState(false);
  const navigate = useNavigate(); // Initialize useNavigate hook

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const handleLogout = async () => {
    try {
      const response = await fetch('http://localhost:9000/api/auth/logout', {
        method: 'POST', // Use POST as logout often involves session clearing
        credentials: 'include', // Include credentials like cookies for authentication
      });

      if (response.ok) {
        console.log('User successfully logged out');
        navigate('/'); // Redirect to login page
      } else {
        console.error('Failed to log out');
      }
    } catch (error) {
      console.error('Error during logout:', error);
    }
  };

  const handleProfileClick = () => {
    navigate('/profile');
  };

  const handleOrdersClick = () => {
    navigate('/orders'); // Navigate to the orders route
  };

  return (
    <div className="profile-dropdown">
      <button className="profile-button" onClick={toggleDropdown}>
        <img
          src={useravatar}
          alt="User Avatar"
          className="user-avatar"
          onError={(e) => { e.target.src = 'fallback-logo.png'; }} // Fallback for image error
        />
        <span className="username">{username || 'Guest'}</span> {/* Display username */}
      </button>
      {isOpen && (
        <div className="dropdown-menu">
          <button onClick={handleProfileClick}>Profile</button>
          <button onClick={handleOrdersClick}>Orders</button>
          <button onClick={handleLogout}>Logout</button>
        </div>
      )}
    </div>
  );
}