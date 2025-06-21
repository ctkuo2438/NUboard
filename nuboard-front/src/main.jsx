import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import App from './App';
import GoogleSignup from './GoogleSignup';
import ProfileForm from './ProfileForm';
import './index.css';

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/GoogleSignup" replace />} />
                <Route path="/GoogleSignup" element={<GoogleSignup />} />
                <Route path="/create-profile" element={<ProfileForm />} />
                <Route path="/nuboard/*" element={<App />} />
            </Routes>
        </BrowserRouter>
    </StrictMode>
);
