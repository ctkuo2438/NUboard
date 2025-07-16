import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

// Configure axios defaults
axios.defaults.withCredentials = true;

function ProfileForm() {
    const [formData, setFormData] = useState({
        username: '',
        program: '',
        locationId: '',
        collegeId: ''
    });
    const [email, setEmail] = useState('');
    const [locations, setLocations] = useState([]);
    const [colleges, setColleges] = useState([]);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const userRes = await axios.get('http://localhost:8080/api/user');
                if (userRes.data && userRes.data.email) {
                    setEmail(userRes.data.email);
                } else {
                    setError('Failed to get user information. Please try signing in again.');
                    return;
                }

                // Check if profile already exists
                try {
                    const profileRes = await axios.get('http://localhost:8080/api/profile');
                    // If profile exists, redirect to main app
                    if (profileRes.data) {
                        navigate('/nuboard');
                        return;
                    }
                } catch (profileErr) {
                    // Profile doesn't exist, continue to show creation form
                    console.log('No existing profile found, showing creation form');
                }

                const [locationsRes, collegesRes] = await Promise.all([
                    axios.get('http://localhost:8080/api/locations'),
                    axios.get('http://localhost:8080/api/colleges')
                ]);
                console.log('Locations response:', locationsRes.data);
                console.log('Colleges response:', collegesRes.data);
                setLocations(locationsRes.data.data);
                setColleges(collegesRes.data.data);
            } catch (err) {
                console.error('Error fetching data:', err);
                setError('Failed to load form data. Please try again.');
            }
        };
        fetchData();
    }, [navigate]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/profile/create', formData);
            if (response.data) {
                navigate('/nuboard');
            } else {
                setError('Failed to create profile. Please try again.');
            }
        } catch (err) {
            console.error('Error creating profile:', err);
            const errorMessage = err.response?.data || 'Failed to create profile. Please try again.';
            setError(errorMessage);
        }
    };

    if (error) {
        return (
            <div>
                {error}
                <button 
                    onClick={() => window.location.href = 'http://localhost:8080/oauth2/authorization/google'}
                >
                    Sign in with Google
                </button>
            </div>
        );
    }

    return (
        <div>
            <h2>Create Your Profile</h2>
            {error && <div>{error}</div>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="email" style={{ display: 'block'}}>Email:</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        disabled
                    />
                </div>

                <div>
                    <label htmlFor="username" style={{ display: 'block'}}>Username:</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="program" style={{ display: 'block'}}>Program:</label>
                    <input
                        type="text"
                        id="program"
                        name="program"
                        value={formData.program}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="locationId" style={{ display: 'block'}}>Location:</label>
                    <select
                        id="locationId"
                        name="locationId"
                        value={formData.locationId}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Select a location</option>
                        {console.log('Rendering locations:', locations)}
                        {locations.map(location => (
                            <option key={location.id} value={location.id}>
                                {location.name}
                            </option>
                        ))}
                    </select>
                </div>

                <div>
                    <label htmlFor="collegeId" style={{ display: 'block'}}>College:</label>
                    <select
                        id="collegeId"
                        name="collegeId"
                        value={formData.collegeId}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Select a college</option>
                        {console.log('Rendering colleges:', colleges)}
                        {colleges.map(college => (
                            <option key={college.id} value={college.id}>
                                {college.name}
                            </option>
                        ))}
                    </select>
                </div>

                <button type="submit">
                    Create Profile
                </button>
            </form>
        </div>
    );
}

export default ProfileForm;