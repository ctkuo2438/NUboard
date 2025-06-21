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
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/user', {
                    withCredentials: true
                });
                if (response.data && response.data.email) {
                    setUserEmail(response.data.email);
                }
            } catch (error) {
                console.error('Error fetching user data:', error);
                navigate('/GoogleSignup');
            }
        };

        fetchUserData();
    }, [navigate]);

    const navItems = [
        { path: '/nuboard', label: 'NUboard', component: NUboard },
        { path: '/nuboard/create-user', label: 'Create User', component: CreateUser },
        { path: '/nuboard/update-user', label: 'Update User', component: UpdateUser },
        { path: '/nuboard/search-user', label: 'Search User', component: SearchUser },
        { path: '/nuboard/delete-user', label: 'Delete User', component: DeleteUser },
        { path: '/nuboard/user-list', label: 'User List', component: UserList },
        { path: '/nuboard/college-list', label: 'College List', component: CollegeList },
        { path: '/nuboard/location-list', label: 'Location List', component: LocationList },
        { path: '/nuboard/user-profile', label: 'User Profile', component: UserProfile },
    ];

    const handleLogout = () => {
        setUserEmail('');
        navigate('/GoogleSignup');
    };

    return (
        <div className="app-container">
            <header className="header-bar">
                <div className="welcome-text">
                    Welcome, {userEmail}
                </div>
                <button
                    onClick={handleLogout}
                    className="logout-button"
                >
                    Logout
                </button>
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
        </div>
    );
}

export default App;
