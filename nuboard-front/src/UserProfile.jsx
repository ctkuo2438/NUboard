import React, { useState, useEffect } from 'react';
import axios from 'axios';

const UserProfile = () => {
    const [userData, setUserData] = useState(null);
    const [location, setLocation] = useState('');
    const [college, setCollege] = useState('');
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/profile', {
                    withCredentials: true
                });
                
                if (response.data) {
                    setUserData(response.data);
                    setLocation(response.data.location?.name || 'Not set');
                    setCollege(response.data.college?.name || 'Not set');
                }
            } catch (err) {
                console.error('Error fetching user data:', err);
                setError('Failed to fetch user data. Please try again.');
            }
        };
        fetchUserData();
    }, []);

    if (error) {
        return <div className="error-message">{error}</div>;
    }

    if (!userData) {
        return <div>Loading...</div>;
    }

    return (
        <div className="profile-container">
            <h1>User Profile</h1>
            <div className="profile-info">
                <div className="profile-field">
                    <label>Email:</label>
                    <span>{userData.email}</span>
                </div>
                <div className="profile-field">
                    <label>Username:</label>
                    <span>{userData.username || 'Not set'}</span>
                </div>
                <div className="profile-field">
                    <label>Program:</label>
                    <span>{userData.program || 'Not set'}</span>
                </div>
                <div className="profile-field">
                    <label>Location:</label>
                    <span>{location}</span>
                </div>
                <div className="profile-field">
                    <label>College:</label>
                    <span>{college}</span>
                </div>
            </div>
        </div>
    );
};

export default UserProfile; 