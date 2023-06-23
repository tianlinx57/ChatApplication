import React, { Component } from 'react';
import { Link } from 'react-router-dom';

class MesChats extends Component {
    constructor(props) {
        super(props);
        this.state = {
            chats: [],
            user: null,
            message: null
        };
    }

    componentDidMount() {
        // Obtenir l'ID utilisateur de la session
        const userId = sessionStorage.getItem('id');

        if (userId) {
            // Récupérer les données utilisateur ici si nécessaire
            const user = {
                id: sessionStorage.getItem('id'),
                firstName: sessionStorage.getItem('firstName'),
                lastName: sessionStorage.getItem('lastName'),
                mail: sessionStorage.getItem('mail')
            };
            this.setState({ user });

            // Récupérer les chats de l'utilisateur
            this.fetchChats(userId);
        }
    }

    fetchChats = (userId) => {
        // Récupérer les chats depuis le serveur
        fetch(`http://localhost:8080/api/proprietaire_chats/${userId}`)
            .then(response => response.json())
            .then(data => this.setState({ chats: data }))
            .catch(err => console.error(err));
    }

    handleDelete = (chatId) => {
        // Supprimer un chat
        fetch(`http://localhost:8080/api/chat/${chatId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    this.setState({ message: 'Chat supprimé avec succès!' });
                    const userId = sessionStorage.getItem('id');
                    this.fetchChats(userId);
                } else {
                    this.setState({ message: 'Échec de la suppression du chat' });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                this.setState({ message: 'Erreur lors de la suppression du chat' });
            });
    };

    formatDateTime(dateTimeStr) {
        // Formater la date et l'heure
        const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
        return new Date(dateTimeStr).toLocaleDateString('fr-FR', options);
    }

    render() {
        const { chats } = this.state;

        return (
            <div className="container" style={{ fontSize: '14px', color: '#333' }}>
                <div className="row">
                    <div className="col-md-12">
                        <h1 className="mt-4 mb-4" style={{ fontSize: '1.4rem' }}>Mes chats</h1>
                        {this.state.message && <div className="alert alert-info">{this.state.message}</div>}
                        <div>
                            <table className="table table-striped" style={{ fontSize: '14px', background: '#f8f9fa' }}>
                                <thead>
                                <tr>
                                    <th>Nom</th>
                                    <th>Créé à</th>
                                    <th>Fini à</th>
                                    <th>Description</th>
                                    <th>Propriétaire</th>
                                    <th>Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                {chats.map(chat => (
                                    <tr key={chat.id}>
                                        <td>{chat.nom}</td>
                                        <td>{this.formatDateTime(chat.createDate)}</td>
                                        <td>{this.formatDateTime(chat.deadline)}</td>
                                        <td>{chat.description}</td>
                                        <td>{chat.proprietaire.firstName} {chat.proprietaire.lastName}</td>
                                        <td>
                                            <Link to={`/chatpage/${chat.id}`} className="btn btn-warning mr-2" style={{ fontSize: '12px', borderRadius: '20px' }}>Entrer</Link>
                                            <Link to={`/chatform/${chat.id}`} className="btn btn-primary mr-2" style={{ fontSize: '12px', borderRadius: '20px' }}>Édition</Link>
                                            <button className="btn btn-danger" style={{ fontSize: '12px', borderRadius: '20px' }} onClick={() => this.handleDelete(chat.id)}>Suppression</button>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default MesChats;
