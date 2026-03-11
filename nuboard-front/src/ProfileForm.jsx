import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

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
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const userRes = await axios.get('http://localhost:8080/api/user');
                if (userRes.data && userRes.data.email) {
                    setEmail(userRes.data.email);
                } else {
                    setError('Failed to get user information. Please try signing in again.');
                    setLoading(false);
                    return;
                }

                try {
                    const profileRes = await axios.get('http://localhost:8080/api/profile');
                    if (profileRes.data) {
                        navigate('/nuboard');
                        return;
                    }
                } catch (profileErr) {
                    console.log('No existing profile found, showing creation form');
                }

                const [locationsRes, collegesRes] = await Promise.all([
                    axios.get('http://localhost:8080/api/locations'),
                    axios.get('http://localhost:8080/api/colleges')
                ]);
                setLocations(locationsRes.data.data || []);
                setColleges(collegesRes.data.data || []);
            } catch (err) {
                console.error('Error fetching data:', err);
                setError('Failed to load form data. Please try again.');
            } finally {
                setLoading(false);
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
        setSubmitting(true);
        setError('');
        
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
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) {
        return (
            <div className="profile-form-page">
                <div className="loading-overlay">
                    <div className="spinner"></div>
                </div>
            </div>
        );
    }

    if (error && !email) {
        return (
            <div className="profile-form-page">
                <div className="profile-form-container">
                    <div className="card">
                        <div className="card-body text-center">
                            <div className="empty-state-icon" style={{ fontSize: '48px', marginBottom: '16px' }}>⚠️</div>
                            <h3 style={{ marginBottom: '16px', color: 'var(--gray-900)' }}>Authentication Required</h3>
                            <p style={{ color: 'var(--gray-600)', marginBottom: '24px' }}>{error}</p>
                            <button
                                className="btn btn-primary btn-lg"
                                onClick={() => window.location.href = 'http://localhost:8080/oauth2/authorization/google'}
                            >
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor" style={{ marginRight: '8px' }}>
                                    <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                                    <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                                    <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
                                    <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
                                </svg>
                                Sign in with Google
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="profile-form-page">
            <div className="profile-form-container">
                <div className="profile-form-card">
                    {/* Header */}
                    <div className="profile-form-header">
                        <div className="profile-form-logo">
                            <div className="header-logo-icon" style={{ width: '60px', height: '60px', fontSize: '24px' }}>NU</div>
                        </div>
                        <h1 className="profile-form-title">Complete Your Profile</h1>
                        <p className="profile-form-subtitle">
                            Welcome to NUboard! Please fill in your details to get started.
                        </p>
                    </div>

                    {/* Error Alert */}
                    {error && (
                        <div className="alert alert-error" style={{ margin: '0 24px' }}>
                            <span>⚠️</span>
                            <span>{error}</span>
                        </div>
                    )}

                    {/* Form */}
                    <form onSubmit={handleSubmit} className="profile-form-body">
                        <div className="form-group">
                            <label className="form-label" htmlFor="email">
                                📧 Email Address
                            </label>
                            <input
                                type="email"
                                id="email"
                                className="form-input"
                                value={email}
                                disabled
                                style={{ backgroundColor: 'var(--gray-100)' }}
                            />
                            <small className="text-muted" style={{ marginTop: '4px', display: 'block' }}>
                                This email is linked to your Google account
                            </small>
                        </div>

                        <div className="form-group">
                            <label className="form-label" htmlFor="username">
                                👤 Username <span className="text-error">*</span>
                            </label>
                            <input
                                type="text"
                                id="username"
                                name="username"
                                className="form-input"
                                placeholder="Choose a unique username"
                                value={formData.username}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label className="form-label" htmlFor="program">
                                📚 Program <span className="text-error">*</span>
                            </label>
                            <input
                                type="text"
                                id="program"
                                name="program"
                                className="form-input"
                                placeholder="e.g., Computer Science, Business Administration"
                                value={formData.program}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-row">
                            <div className="form-group">
                                <label className="form-label" htmlFor="locationId">
                                    📍 Campus Location <span className="text-error">*</span>
                                </label>
                                <select
                                    id="locationId"
                                    name="locationId"
                                    className="form-input form-select"
                                    value={formData.locationId}
                                    onChange={handleChange}
                                    required
                                >
                                    <option value="">Select location</option>
                                    {locations.map(location => (
                                        <option key={location.id} value={location.id}>
                                            {location.name}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div className="form-group">
                                <label className="form-label" htmlFor="collegeId">
                                    🏫 College <span className="text-error">*</span>
                                </label>
                                <select
                                    id="collegeId"
                                    name="collegeId"
                                    className="form-input form-select"
                                    value={formData.collegeId}
                                    onChange={handleChange}
                                    required
                                >
                                    <option value="">Select college</option>
                                    {colleges.map(college => (
                                        <option key={college.id} value={college.id}>
                                            {college.name}
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>

                        <button
                            type="submit"
                            className="btn btn-primary btn-lg w-full"
                            disabled={submitting}
                            style={{ marginTop: '16px' }}
                        >
                            {submitting ? (
                                <>
                                    <div className="spinner" style={{ width: '20px', height: '20px', borderWidth: '2px' }}></div>
                                    Creating Profile...
                                </>
                            ) : (
                                <>
                                    🚀 Create My Profile
                                </>
                            )}
                        </button>
                    </form>

                    {/* Footer */}
                    <div className="profile-form-footer">
                        <p>
                            By creating a profile, you agree to NUboard's Terms of Service and Privacy Policy.
                        </p>
                    </div>
                </div>
            </div>

            <style>{`
                .profile-form-page {
                    min-height: 100vh;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
                    padding: var(--spacing-xl);
                }

                .profile-form-container {
                    width: 100%;
                    max-width: 500px;
                }

                .profile-form-card {
                    background-color: white;
                    border-radius: var(--radius-xl);
                    box-shadow: var(--shadow-xl);
                    overflow: hidden;
                    animation: slideUp 0.5s ease;
                }

                .profile-form-header {
                    text-align: center;
                    padding: var(--spacing-2xl) var(--spacing-xl) var(--spacing-lg);
                    background: linear-gradient(135deg, var(--gray-50) 0%, white 100%);
                    border-bottom: 1px solid var(--gray-100);
                }

                .profile-form-logo {
                    display: flex;
                    justify-content: center;
                    margin-bottom: var(--spacing-lg);
                }

                .profile-form-title {
                    font-size: var(--font-size-2xl);
                    font-weight: 700;
                    color: var(--gray-900);
                    margin-bottom: var(--spacing-sm);
                }

                .profile-form-subtitle {
                    font-size: var(--font-size-sm);
                    color: var(--gray-500);
                    margin: 0;
                }

                .profile-form-body {
                    padding: var(--spacing-xl);
                }

                .profile-form-footer {
                    padding: var(--spacing-lg) var(--spacing-xl);
                    background-color: var(--gray-50);
                    border-top: 1px solid var(--gray-100);
                    text-align: center;
                }

                .profile-form-footer p {
                    font-size: var(--font-size-xs);
                    color: var(--gray-500);
                    margin: 0;
                }

                @keyframes slideUp {
                    from {
                        opacity: 0;
                        transform: translateY(30px);
                    }
                    to {
                        opacity: 1;
                        transform: translateY(0);
                    }
                }
            `}</style>
        </div>
    );
}

export default ProfileForm;
