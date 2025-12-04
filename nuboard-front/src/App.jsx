import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import NUboard from './NUboard';
import CreateUser from './CreateUser.jsx';
import SearchUser from './SearchUser.jsx';
import UserList from './UserList.jsx';
import CollegeList from './CollegeList.jsx';
import LocationList from './LocationList.jsx';
import UpdateUser from './UpdateUser.jsx';
import DeleteUser from './DeleteUser.jsx';
import UserProfile from './UserProfile.jsx';
import './App.css';

axios.defaults.withCredentials = true;

function App() {
    const [page, setPage] = useState('/nuboard');
    const [userEmail, setUserEmail] = useState('');
    const [userName, setUserName] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/user', {
                    withCredentials: true
                });
                if (response.data && response.data.email) {
                    setUserEmail(response.data.email);
                    setUserName(response.data.name || response.data.email.split('@')[0]);
                }
            } catch (error) {
                console.error('Error fetching user data:', error);
                navigate('/GoogleSignup');
            }
        };

        fetchUserData();
    }, [navigate]);

    const navItems = [
        { path: '/nuboard', label: '📅 Events', component: NUboard },
        { path: '/nuboard/user-profile', label: '👤 My Profile', component: UserProfile },
        { path: '/nuboard/create-user', label: '➕ Create User', component: CreateUser },
        { path: '/nuboard/update-user', label: '✏️ Update User', component: UpdateUser },
        { path: '/nuboard/search-user', label: '🔍 Search User', component: SearchUser },
        { path: '/nuboard/delete-user', label: '🗑️ Delete User', component: DeleteUser },
        { path: '/nuboard/user-list', label: '📋 User List', component: UserList },
        { path: '/nuboard/college-list', label: '🏫 Colleges', component: CollegeList },
        { path: '/nuboard/location-list', label: '📍 Locations', component: LocationList },
    ];

    const handleLogout = () => {
        setUserEmail('');
        setUserName('');
        navigate('/GoogleSignup');
    };

    const getInitials = (name) => {
        if (!name) return '?';
        return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
    };

    return (
        <div className="app-container">
            <header className="header-bar">
                <div className="header-logo">
                    <div className="header-logo-icon">NU</div>
                    <h1>NUboard</h1>
                </div>
                
                <div className="welcome-text">
                    <span>Welcome back,</span>
                    <span className="user-email">{userName || userEmail}</span>
                    <button onClick={handleLogout} className="logout-button">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
                            <polyline points="16 17 21 12 16 7"></polyline>
                            <line x1="21" y1="12" x2="9" y2="12"></line>
                        </svg>
                        Logout
                    </button>
                </div>
            </header>

            <nav className="nav-bar">
                {navItems.map(({ path, label }) => (
                    <button
                        key={path}
                        onClick={() => setPage(path)}
                        className={`nav-button ${page === path ? 'active' : ''}`}
                    >
                        {label}
                    </button>
                ))}
            </nav>

            <main className="content">
                {navItems.map(({ path, component: Component }) => (
                    page === path && <Component key={path} />
                ))}
            </main>

            <footer className="footer">
                <p>© 2024 NUboard - Northeastern University Event Management System</p>
            </footer>
        </div>
    );
}

export default App;
