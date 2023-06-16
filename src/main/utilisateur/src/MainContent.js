import React, { useEffect } from 'react';
import { Route, Routes, Navigate, useNavigate } from 'react-router-dom';
import Menu from './components/Menu';
import Login from './page/Login';
import MesChats from './page/MesChats';
import MesInvitations from './page/MesInvitations';
import ChatPage from './page/ChatPage';
import ChatForm from './page/ChatForm';
import Logout from "./components/Logout";

function MainContent() {
    const navigate = useNavigate();

    const isAuthenticated = () => sessionStorage.getItem('isAuthenticated') === 'true';

    useEffect(() => {
        const handleStorageChange = (event) => {
            if (event.key === 'isAuthenticated' && event.newValue === 'false') {
                navigate('/login');
            }
        };

        window.addEventListener('storage', handleStorageChange);

        return () => {
            window.removeEventListener('storage', handleStorageChange);
        };
    }, [navigate]);

    return (
        <div className="d-flex" style={{ height: '100vh' }}>
            {isAuthenticated() && <Menu />}
            <div style={{ flexGrow: 1 }}>
                <Routes>
                    <Route path="/" element={<Navigate to={isAuthenticated() ? "/mes_chats" : "/login"} />} />
                    <Route path="/login" element={isAuthenticated() ? <Navigate to="/mes_chats" /> : <Login />} />
                    <Route path="/logout" element={<Logout />} />
                    <Route path="/mes_chats" element={isAuthenticated() ? <MesChats /> : <Navigate to="/login" />} />
                    <Route path="/mes_invitations" element={isAuthenticated() ? <MesInvitations /> : <Navigate to="/login" />} />
                    <Route path='/chatpage/:id' element={isAuthenticated() ? <ChatPage /> : <Navigate to="/login" />} />
                    <Route path='/chatform/:id' element={isAuthenticated() ? <ChatForm /> : <Navigate to="/login" />} />
                </Routes>
            </div>
        </div>
    );
}

export default MainContent;
