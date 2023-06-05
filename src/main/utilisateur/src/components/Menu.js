import { Link, useLocation } from 'react-router-dom';

function Menu() {
    const location = useLocation();

    return (
        <div style={{
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            display: 'flex',
            justifyContent: 'center',
            backgroundColor: '#f8f9fa',
            padding: '20px 0',
            zIndex: 1000,
        }}>
            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                width: '40%',
            }}>
                <Link to="/accueil" className={`btn ${location.pathname === '/accueil' ? 'btn-primary' : 'btn-secondary'}`}>Accueil</Link>
                <Link to="/mes_chats" className={`btn ${location.pathname === '/mes_chats' ? 'btn-primary' : 'btn-secondary'}`}>Mes chats</Link>
                <Link to="/mes_invitations" className={`btn ${location.pathname === '/mes_invitations' ? 'btn-primary' : 'btn-secondary'}`}>Mes invitations</Link>
            </div>
        </div>
    );
}

export default Menu;
