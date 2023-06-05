import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Accueil from './components/Accueil';
import Menu from './components/Menu';
import MesChats from './components/MesChats';
import MesInvitations from './components/MesInvitations';
import Login from './components/Login';
import {useState} from "react";

function App() {
    const [user, setUser] = useState(null);

    const handleLogin = (user) => {
        setUser(user);
    };

    const handleLogout = () => {
        setUser(null);
    };

    return (
        <Router>
            {user && <Menu user={user} onLogout={handleLogout} />}
            <Routes>
                <Route path="/" element={user ? <Navigate to="/Accueil" /> : <Navigate to="/login" />} />
                <Route path="/login" element={user ? <Navigate to="/Accueil" /> : <Login onLogin={handleLogin} />} />
                <Route path="/Accueil" element={user ? <Accueil /> : <Navigate to="/login" />} />
                <Route path="/mes_chats" element={user ? <MesChats /> : <Navigate to="/login" />} />
                <Route path="/mes_invitations" element={user ? <MesInvitations /> : <Navigate to="/login" />} />
            </Routes>
        </Router>
    );
}

export default App;
