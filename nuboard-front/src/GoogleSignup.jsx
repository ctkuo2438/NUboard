export default function GoogleSignup() {

    const handleGoogleSignIn = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    };

    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100vh',
            backgroundColor: '#f5f5f5',
            fontFamily: 'Arial, sans-serif'
        }}>
            <div style={{
                backgroundColor: 'white',
                padding: '40px',
                borderRadius: '8px',
                boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
                textAlign: 'center',
                maxWidth: '400px',
                width: '90%'
            }}>
                <h1 style={{
                    color: '#202124',
                    fontSize: '24px',
                    marginBottom: '30px'
                }}>Welcome to NUboard</h1>
                
                <button
                    onClick={handleGoogleSignIn}
                    style={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        backgroundColor: 'white',
                        border: '1px solid #dadce0',
                        borderRadius: '4px',
                        padding: '12px 24px',
                        fontSize: '16px',
                        color: '#3c4043',
                        cursor: 'pointer',
                        width: '100%',
                        transition: 'background-color 0.2s',
                        ':hover': {
                            backgroundColor: '#f8f9fa'
                        }
                    }}
                >
                    <img 
                        src="https://www.google.com/favicon.ico" 
                        alt="Google"
                        style={{
                            width: '18px',
                            height: '18px',
                            marginRight: '10px'
                        }}
                    />
                    Sign in with Google
                </button>
            </div>
        </div>
    );
}
