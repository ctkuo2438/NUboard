export default function GoogleSignup() {

    const handleGoogleSignIn = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    };

    return (
        <div className="login-page">
            <div className="login-container">
                {/* Background decoration */}
                <div className="login-decoration">
                    <div className="decoration-circle circle-1"></div>
                    <div className="decoration-circle circle-2"></div>
                    <div className="decoration-circle circle-3"></div>
                </div>

                {/* Login Card */}
                <div className="login-card">
                    {/* Logo */}
                    <div className="login-logo">
                        <div className="logo-icon">NU</div>
                        <h1 className="logo-text">NUboard</h1>
                    </div>

                    {/* Welcome Text */}
                    <div className="login-welcome">
                        <h2>Welcome Back!</h2>
                        <p>Sign in to access your campus events and connect with the Northeastern community.</p>
                    </div>

                    {/* Features */}
                    <div className="login-features">
                        <div className="feature-item">
                            <span className="feature-icon">📅</span>
                            <span>Discover campus events</span>
                        </div>
                        <div className="feature-item">
                            <span className="feature-icon">👥</span>
                            <span>Connect with students</span>
                        </div>
                        <div className="feature-item">
                            <span className="feature-icon">🎓</span>
                            <span>Manage registrations</span>
                        </div>
                    </div>

                    {/* Google Sign In Button */}
                    <button onClick={handleGoogleSignIn} className="google-signin-btn">
                        <svg className="google-icon" viewBox="0 0 24 24" width="24" height="24">
                            <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                            <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                            <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
                            <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
                        </svg>
                        <span>Continue with Google</span>
                    </button>

                    {/* Divider */}
                    <div className="login-divider">
                        <span>Northeastern University SSO</span>
                    </div>

                    {/* Footer */}
                    <div className="login-footer">
                        <p>By signing in, you agree to our Terms of Service and Privacy Policy</p>
                    </div>
                </div>

                {/* Bottom branding */}
                <div className="login-branding">
                    <p>© 2024 NUboard - Northeastern University Event Management</p>
                </div>
            </div>

            <style>{`
                .login-page {
                    min-height: 100vh;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    background: linear-gradient(135deg, #c8102e 0%, #8b0a1e 50%, #5a0613 100%);
                    padding: 20px;
                    position: relative;
                    overflow: hidden;
                }

                .login-container {
                    width: 100%;
                    max-width: 420px;
                    position: relative;
                    z-index: 10;
                }

                .login-decoration {
                    position: fixed;
                    top: 0;
                    left: 0;
                    right: 0;
                    bottom: 0;
                    pointer-events: none;
                    overflow: hidden;
                }

                .decoration-circle {
                    position: absolute;
                    border-radius: 50%;
                    background: rgba(255, 255, 255, 0.05);
                }

                .circle-1 {
                    width: 400px;
                    height: 400px;
                    top: -100px;
                    right: -100px;
                    animation: float 20s ease-in-out infinite;
                }

                .circle-2 {
                    width: 300px;
                    height: 300px;
                    bottom: -50px;
                    left: -50px;
                    animation: float 15s ease-in-out infinite reverse;
                }

                .circle-3 {
                    width: 200px;
                    height: 200px;
                    top: 50%;
                    left: 50%;
                    transform: translate(-50%, -50%);
                    animation: pulse 10s ease-in-out infinite;
                }

                @keyframes float {
                    0%, 100% { transform: translateY(0) rotate(0deg); }
                    50% { transform: translateY(-30px) rotate(10deg); }
                }

                @keyframes pulse {
                    0%, 100% { transform: translate(-50%, -50%) scale(1); opacity: 0.05; }
                    50% { transform: translate(-50%, -50%) scale(1.2); opacity: 0.1; }
                }

                .login-card {
                    background: white;
                    border-radius: 24px;
                    padding: 48px 40px;
                    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.4);
                    animation: slideUp 0.6s ease;
                }

                @keyframes slideUp {
                    from {
                        opacity: 0;
                        transform: translateY(40px);
                    }
                    to {
                        opacity: 1;
                        transform: translateY(0);
                    }
                }

                .login-logo {
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    margin-bottom: 32px;
                }

                .logo-icon {
                    width: 80px;
                    height: 80px;
                    background: linear-gradient(135deg, #c8102e 0%, #a00d24 100%);
                    border-radius: 20px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 32px;
                    font-weight: 800;
                    color: white;
                    margin-bottom: 16px;
                    box-shadow: 0 10px 30px rgba(200, 16, 46, 0.3);
                }

                .logo-text {
                    font-size: 28px;
                    font-weight: 700;
                    color: #1f2937;
                    margin: 0;
                }

                .login-welcome {
                    text-align: center;
                    margin-bottom: 32px;
                }

                .login-welcome h2 {
                    font-size: 24px;
                    font-weight: 600;
                    color: #1f2937;
                    margin: 0 0 8px 0;
                }

                .login-welcome p {
                    font-size: 14px;
                    color: #6b7280;
                    margin: 0;
                    line-height: 1.6;
                }

                .login-features {
                    display: flex;
                    flex-direction: column;
                    gap: 12px;
                    margin-bottom: 32px;
                    padding: 20px;
                    background: #f9fafb;
                    border-radius: 12px;
                }

                .feature-item {
                    display: flex;
                    align-items: center;
                    gap: 12px;
                    font-size: 14px;
                    color: #374151;
                }

                .feature-icon {
                    font-size: 20px;
                }

                .google-signin-btn {
                    width: 100%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    gap: 12px;
                    padding: 16px 24px;
                    background: white;
                    border: 2px solid #e5e7eb;
                    border-radius: 12px;
                    font-size: 16px;
                    font-weight: 500;
                    color: #374151;
                    cursor: pointer;
                    transition: all 0.2s ease;
                }

                .google-signin-btn:hover {
                    background: #f9fafb;
                    border-color: #d1d5db;
                    transform: translateY(-2px);
                    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                }

                .google-signin-btn:active {
                    transform: translateY(0);
                }

                .google-icon {
                    flex-shrink: 0;
                }

                .login-divider {
                    display: flex;
                    align-items: center;
                    margin: 24px 0;
                    color: #9ca3af;
                    font-size: 12px;
                }

                .login-divider::before,
                .login-divider::after {
                    content: '';
                    flex: 1;
                    height: 1px;
                    background: #e5e7eb;
                }

                .login-divider span {
                    padding: 0 16px;
                    white-space: nowrap;
                }

                .login-footer {
                    text-align: center;
                }

                .login-footer p {
                    font-size: 12px;
                    color: #9ca3af;
                    margin: 0;
                    line-height: 1.5;
                }

                .login-branding {
                    text-align: center;
                    margin-top: 24px;
                }

                .login-branding p {
                    font-size: 12px;
                    color: rgba(255, 255, 255, 0.6);
                    margin: 0;
                }

                @media (max-width: 480px) {
                    .login-card {
                        padding: 32px 24px;
                        border-radius: 20px;
                    }

                    .logo-icon {
                        width: 64px;
                        height: 64px;
                        font-size: 24px;
                    }

                    .logo-text {
                        font-size: 24px;
                    }

                    .login-welcome h2 {
                        font-size: 20px;
                    }
                }
            `}</style>
        </div>
    );
}
