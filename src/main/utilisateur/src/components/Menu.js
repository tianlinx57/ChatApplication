import { Link } from 'react-router-dom';

function Menu({ onLogout }) {
    const user = {
        id: sessionStorage.getItem('id'),
        firstName: sessionStorage.getItem('firstName'),
        lastName: sessionStorage.getItem('lastName'),
        mail: sessionStorage.getItem('mail')
    };

    return (
        <div className="d-flex flex-column" style={{ width: '25%', height: '100vh', background: '#f8f9fa' }}>
            <div className="list-group list-group-flush">
                <Link to="/mes_chats" className="list-group-item list-group-item-action bg-light">Mes chats</Link>
                <Link to="/mes_invitations" className="list-group-item list-group-item-action bg-light">Mes invitations</Link>
                <Link to="/chatform/0" className="list-group-item list-group-item-action bg-light">Créer un chat</Link>
                <Link to="/logout" className="list-group-item list-group-item-action bg-light">Logout</Link>
            </div>
            <div className="p-3">
                <div className="card">
                    <div className="card-body">
                        <h5 className="card-title" style={{ fontSize: '1.2rem' }}>Mon profil</h5>
                        <p className="card-text" style={{ fontSize: '1rem' }}>ID: {user.id}</p>
                        <p className="card-text" style={{ fontSize: '1rem' }}>Nom: {user.firstName} {user.lastName}</p>
                        <p className="card-text" style={{ fontSize: '1rem' }}>Email: {user.mail}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Menu;
