import React, { Component } from 'react';
import { Link } from 'react-router-dom';

//Les commentaires est meme comme à la page MesChat.js
class MesInvitations extends Component {
    constructor(props) {
        super(props);
        this.state = {
            chats: [],
            user: null
        };
    }

    componentDidMount() {
        const userId = sessionStorage.getItem('id');

        if (userId) {
            // Fetch user data here if needed
            const user = {
                id: sessionStorage.getItem('id'),
                firstName: sessionStorage.getItem('firstName'),
                lastName: sessionStorage.getItem('lastName'),
                mail: sessionStorage.getItem('mail')
            };
            this.setState({ user });

            fetch(`http://localhost:8080/api/user_chats/${userId}`)
                .then(response => response.json())
                .then(data => this.setState({ chats: data }))
                .catch(err => console.error(err));
        }
    }

    formatDateTime(dateTimeStr) {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
        return new Date(dateTimeStr).toLocaleDateString('fr-FR', options);
    }

    render() {
        const { chats } = this.state;

        return (
            <div className="container" style={{ fontSize: '14px', color: '#333' }}>
                <div className="row">
                    <div className="col-md-12">
                        <h1 className="mt-4 mb-4" style={{ fontSize: '1.4rem' }}>Mes invitations</h1>
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

export default MesInvitations;
