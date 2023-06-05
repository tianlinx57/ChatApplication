import { Link } from 'react-router-dom';

function MesInvitations() {
    return (
        <div className="container">
            <h1 className="mt-4 mb-4">Mes invitations</h1>
            <div>
                <h2>Mes invitations</h2>
                <ul className="list-group">
                    <li className="list-group-item">
                        <Link to="/chat_page">Chat 1</Link>
                    </li>
                    <li className="list-group-item">
                        <Link to="/chat_page">Chat 2</Link>
                    </li>
                </ul>
            </div>
        </div>
    );
}

export default MesInvitations;
