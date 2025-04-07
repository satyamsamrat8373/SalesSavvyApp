// src/ProfilePage.jsx
import React, { useState, useEffect } from 'react';
import { Header } from './Header';
import { Footer } from './Footer';
import './assets/styles.css';

export default function ProfilePage() {
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [username, setUsername] = useState('');
  const [cartCount, setCartCount] = useState(0);
  const [isCartLoading, setIsCartLoading] = useState(true);

  useEffect(() => {
    fetchUserProfile();
  }, []);

  useEffect(() => {
    if (username) {
      fetchCartCount();
    }
  }, [username]);

  const fetchCartCount = async () => {
    setIsCartLoading(true);
    try {
      const response = await fetch(`http://localhost:9000/api/cart/items/count?username=${username}`, {
        credentials: 'include',
      });
      if (!response.ok) throw new Error('Failed to fetch cart count');
      const count = await response.json();
      setCartCount(count);
    } catch (error) {
      console.error('Error fetching cart count:', error);
      setCartCount(0);
    } finally {
      setIsCartLoading(false);
    }
  };

  const fetchUserProfile = async () => {
    try {
      const response = await fetch('http://localhost:9000/api/users/profile', {
        credentials: 'include',
      });
      
      if (!response.ok) {
        throw new Error('Failed to fetch profile');
      }
      
      const data = await response.json();
      setUserData(data);
      setUsername(data.username);
      setError(null);
    } catch (err) {
      setError('Failed to fetch profile. Please try logging in again.');
      console.error('Profile fetch error:', err);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    try {
      return new Date(dateString).toLocaleString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch (error) {
      console.error('Date formatting error:', error);
      return 'Invalid Date';
    }
  };

  return (
    <div className="maindiv">
      <div className="customer">
        <Header 
          cartCount={isCartLoading ? '...' : cartCount} 
          username={username || 'Guest'} 
        />
        <main className="main-content">
          <h1 className="form-title">User Profile</h1>
          {loading && <p className="loading-message">Loading profile...</p>}
          {error && <p className="error-message">{error}</p>}
          {!loading && !error && userData && (
            <div className="profile-details-container">
              <div className="profile-section">
                <h2>Personal Information</h2>
                <div className="profile-detail-row">
                  <span className="detail-label">User ID:</span>
                  <span className="detail-value">{userData.userId}</span>
                </div>
                <div className="profile-detail-row">
                  <span className="detail-label">Username:</span>
                  <span className="detail-value">{userData.username}</span>
                </div>
                <div className="profile-detail-row">
                  <span className="detail-label">Email:</span>
                  <span className="detail-value">{userData.email}</span>
                </div>
                <div className="profile-detail-row">
                  <span className="detail-label">Role:</span>
                  <span className="detail-value">{userData.role}</span>
                </div>
                <div className="profile-detail-row">
                  <span className="detail-label">Member Since:</span>
                  <span className="detail-value">
                    {formatDate(userData.created_at)}
                  </span>
                </div>
                <div className="profile-detail-row">
                  <span className="detail-label">Last Updated:</span>
                  <span className="detail-value">
                    {formatDate(userData.updated_at)}
                  </span>
                </div>
              </div>
            </div>
          )}
          {!loading && !error && !userData && (
            <p className="no-data-message">No profile data available. Please log in again.</p>
          )}
        </main>
        <Footer />
      </div>
    </div>
  );
}