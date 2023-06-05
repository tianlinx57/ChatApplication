import { Link } from 'react-router-dom';

function MesChats() {
    return (
        <div className="container">
            <h1 className="mt-4 mb-4">Mes chats</h1>
            <div>
                <h2>Mes chats</h2>
                <ul className="list-group">
                    <li className="list-group-item d-flex justify-content-between">
                        <Link to="/chat_page">Chat 1</Link>
                        <Link to="/chat_page_edit" className="btn btn-info">Modifier</Link>
                        <button className="btn btn-danger">Supprimer</button>
                    </li>
                    <li className="list-group-item d-flex justify-content-between">
                        <Link to="/chat_page">Chat 2</Link>
                        <Link to="/chat_page_edit" className="btn btn-info">Modifier</Link>
                        <button className="btn btn-danger">Supprimer</button>
                    </li>
                </ul>
            </div>
        </div>
    );
}

export default MesChats;
