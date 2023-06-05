import { useEffect, useState } from 'react';

function Accueil() {
    const [chats, setChats] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8080/api/all_chats/13')
            .then(response => response.json())
            .then(data => setChats(data))
            .catch(err => console.error(err));
    }, []);

    const formatDateTime = (dateTimeStr) => {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
        return new Date(dateTimeStr).toLocaleDateString('fr-FR', options);
    }


    return (
        <div className="container">
            <h1 className="mt-4 mb-4">Accueil utilisateur</h1>
            <div>
                <h2>Tous les chats</h2>
                <table className="table">
                    <thead>
                    <tr>
                        <th>Nom</th>
                        <th>Créé à</th>
                        <th>Description</th>
                        <th>Propriétaire</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {chats.map(chat => (
                        <tr key={chat.id}>
                            <td>{chat.nom}</td>
                            <td>{formatDateTime(chat.createDate)}</td>
                            <td>{chat.description}</td>
                            <td>{chat.proprietaire.firstName} {chat.proprietaire.lastName}</td>
                            <td>
                                <a href="#" className="btn btn-warning mr-2">Entrer</a>
                                <a href="#" className="btn btn-primary mr-2">Édition</a>
                                <a href="#" className="btn btn-danger">Suppression</a>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default Accueil;
