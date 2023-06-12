import React, { useState, useEffect } from 'react';
import { Route, Routes, Navigate, useNavigate } from 'react-router-dom';
import Menu from './components/Menu';
import Login from './page/Login';
import MesChats from './page/MesChats';
import MesInvitations from './page/MesInvitations';
import ChatPage from './page/ChatPage';
import ChatForm from './page/ChatForm';

function MainContent() {
    const [isAuthenticated, setIsAuthenticated] = useState(sessionStorage.getItem('isAuthenticated') === 'true');
    const navigate = useNavigate();

    const handleLogout = () => {
        sessionStorage.removeItem('isAuthenticated');
        sessionStorage.removeItem('id');
        sessionStorage.removeItem('mail');
        sessionStorage.removeItem('firstName');
        sessionStorage.removeItem('lastName');
        setIsAuthenticated(false);
        navigate('/login');
    };

    useEffect(() => {
        const sessionValue = sessionStorage.getItem('isAuthenticated') === 'true';
        if (sessionValue !== isAuthenticated) {
            setIsAuthenticated(sessionValue);
        }
    }, [isAuthenticated]);


    return (
        <div className="d-flex" style={{ height: '100vh' }}>
            {isAuthenticated && <Menu onLogout={handleLogout} />}
            <div style={{ flexGrow: 1 }}>
                <Routes>
                    <Route path="/" element={<Navigate to={isAuthenticated ? "/mes_chats" : "/login"} />} />
                    <Route path="/login" element={isAuthenticated ? <Navigate to="/mes_chats" /> : <Login />} />
                    <Route path="/mes_chats" element={isAuthenticated ? <MesChats /> : <Navigate to="/login" />} />
                    <Route path="/mes_invitations" element={isAuthenticated ? <MesInvitations /> : <Navigate to="/login" />} />
                    <Route path='/chatpage/:id' element={isAuthenticated ? <ChatPage /> : <Navigate to="/login" />} />
                    <Route path='/chatform/:id' element={isAuthenticated ? <ChatForm /> : <Navigate to="/login" />} />
                </Routes>
            </div>
        </div>
    );
}

export default MainContent;
