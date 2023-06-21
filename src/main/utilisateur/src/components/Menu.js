import { Link } from 'react-router-dom';
import React from "react";

function Menu({ onLogout }) {
    const user = {
        id: sessionStorage.getItem('id'),
        firstName: sessionStorage.getItem('firstName'),
        lastName: sessionStorage.getItem('lastName'),
        mail: sessionStorage.getItem('mail')
    };

    const menuStyle = {
        width: '25%',
        height: '100vh',
        background: '#f8f9fa',
        fontFamily: 'Arial, sans-serif',
        fontSize: '16px',
        color: '#333'
    };

    const headingStyle = {
        fontSize: '1.2rem',
        fontWeight: 'bold',
        marginBottom: '0.5em'
    };

    const cardTextStyle = {
        fontSize: '1rem'
    };

    return (
        <div className="d-flex flex-column" style={menuStyle}>
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', padding: '1em' }}>
                <img src="/messenger.png" alt="Login" className="login-img" style={{ width: '50%', height: 'auto' }} />
            </div>
            <div className="list-group list-group-flush">
                <Link to="/mes_chats" className="list-group-item list-group-item-action bg-light">Mes chats</Link>
                <Link to="/mes_invitations" className="list-group-item list-group-item-action bg-light">Mes invitations</Link>
                <Link to="/chatform/0" className="list-group-item list-group-item-action bg-light">Créer un chat</Link>
                <Link to="/logout" className="list-group-item list-group-item-action bg-light">Logout</Link>
            </div>
            <div className="p-3">
                <div className="card">
                    <div className="card-body">
                        <h5 className="card-title" style={headingStyle}>Mon profil</h5>
                        <p className="card-text" style={cardTextStyle}>ID: {user.id}</p>
                        <p className="card-text" style={cardTextStyle}>Nom: {user.firstName} {user.lastName}</p>
                        <p className="card-text" style={cardTextStyle}>Email: {user.mail}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Menu;
