import { useState, useEffect } from "react"
import "./assets/styles.css"

export function ProfilePopup({ onClose }) {
  const [userData, setUserData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await fetch("http://localhost:9000/api/users/profile", {
          credentials: "include",
        })
        
        if (!response.ok) throw new Error("Failed to fetch profile")
        
        const data = await response.json()
        setUserData(data)
      } catch (err) {
        setError(err.message)
      } finally {
        setLoading(false)
      }
    }

    fetchProfile()
  }, [])

  // Close popup when clicking outside
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (e.target.classList.contains("profile-popup-overlay")) {
        onClose()
      }
    }

    document.addEventListener("mousedown", handleClickOutside)
    return () => document.removeEventListener("mousedown", handleClickOutside)
  }, [onClose])

  return (
    <div className="profile-popup-overlay">
      <div className="profile-popup-content">
        <div className="popup-header">
          <h2>User Profile</h2>
          <button className="close-button" onClick={onClose} aria-label="Close">
            &times;
          </button>
        </div>

        <div className="popup-body">
          {loading && <div className="loading-spinner">Loading...</div>}
          {error && <div className="error-message">{error}</div>}
          
          {userData && (
            <div className="profile-details">
              {Object.entries({
                'User ID': userData.userId,
                'Username': userData.username,
                'Email': userData.email,
                'Role': userData.role,
                'Created At': new Date(userData.created_at).toLocaleDateString()
              }).map(([label, value]) => (
                <div key={label} className="profile-detail-row">
                  <span className="detail-label">{label}:</span>
                  <span className="detail-value">{value}</span>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}