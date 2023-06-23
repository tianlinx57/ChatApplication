// Importer les modules nécessaires de React
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from "react-router-dom";

const ChatForm = () => {
    const { id } = useParams();

    // Définir les états locaux avec useState
    const [chatTitle, setChatTitle] = useState(''); // Titre du chat
    const [deadline, setDeadline] = useState(''); // Date limite
    const [chatDescription, setChatDescription] = useState(''); // Description du chat
    const [chatMembers, setChatMembers] = useState([]); // Liste des membres
    const [selectedMembers, setSelectedMembers] = useState([]); // Liste des membres sélectionnés
    const [ownerEmail, setOwnerEmail] = useState(''); // E-mail du propriétaire
    const [successMessage, setSuccessMessage] = useState(''); // Message de succès

    // Utiliser useEffect pour exécuter du code après le premier rendu du composant
    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/all_users');
                if (response.status === 200) {
                    const users = response.data.map(user => ({ email: user.mail }));
                    setChatMembers(users); // Mettre à jour la liste des membres
                } else {
                    console.error("Échec de la récupération des utilisateurs. Code de statut:", response.status);
                }
            } catch (error) {
                console.error("Une erreur s'est produite lors de la récupération des utilisateurs:", error);
            }
        };

        // Appeler la fonction pour récupérer les utilisateurs
        fetchUsers();

        // Si l'ID est différent de '0', récupérer les informations du chat
        if (id !== '0') {
            const fetchChat = async () => {
                try {
                    const response = await axios.get(`http://localhost:8080/api/chat/${id}`);
                    if (response.status === 200) {
                        const chatData = response.data;
                        setChatTitle(chatData.nom); // Mettre à jour le titre
                        setDeadline(chatData.deadline); // Mettre à jour la date limite
                        setChatDescription(chatData.description); // Mettre à jour la description
                        setSelectedMembers(chatData.users.map(user => user.mail)); // Mettre à jour les membres sélectionnés
                        setOwnerEmail(chatData.proprietaire.mail); // Mettre à jour l'e-mail du propriétaire
                    } else {
                        console.error("Échec de la récupération du chat. Code de statut:", response.status);
                    }
                } catch (error) {
                    console.error("Une erreur s'est produite lors de la récupération du chat:", error);
                }
            };

            // Appeler la fonction pour récupérer les informations du chat
            fetchChat();
        }
    }, []);

    // Gestionnaire d'événements pour les modifications de champs de saisie
    const handleInputChange = (event) => {
        const { name, value } = event.target; // Extraire le nom et la valeur du champ de saisie

        switch (name) {
            case 'chatTitle':
                setChatTitle(value);
                break;
            case 'deadline':
                setDeadline(value);
                break;
            case 'chatDescription':
                setChatDescription(value);
                break;
            default:
                break;
        }
    };

    const handleUserSelection = (event) => {
        const email = event.target.value; // Extraire la valeur de la case à cocher
        // Mettre à jour les membres sélectionnés
        setSelectedMembers(prevMembers =>
            event.target.checked
                ? [...prevMembers, email]
                : prevMembers.filter(member => member !== email)
        );
    };

    const handleSubmit = async (event) => {
        event.preventDefault(); // Empêcher le rechargement de la page

        const ownerEmail = sessionStorage.getItem('mail');
        const membersWithoutOwner = selectedMembers.filter(member => member !== ownerEmail);
        const chatData = {
            title: chatTitle,
            deadline: deadline,
            description: chatDescription,
            members: membersWithoutOwner,
            ownerEmail: ownerEmail
        };

        // Essayer de créer ou de mettre à jour le chat
        try {
            if (id == 0) { // Supposer que l'id est déjà défini
                // Créer un nouveau chat
                const response = await axios.post('http://localhost:8080/api/chat', chatData);
                if (response.status === 201) {
                    setSuccessMessage('Chat créé avec succès.');
                } else {
                    console.error("Échec de la création du chat. Code de statut:", response.status);
                }
            } else {
                // Mettre à jour un chat existant
                chatData.id = id; // Ajouter l'id aux données du chat
                const response = await axios.put(`http://localhost:8080/api/chat/${id}`, chatData);
                if (response.status === 200) {
                    setSuccessMessage('Chat mis à jour avec succès.');
                } else {
                    console.error("Échec de la mise à jour du chat. Code de statut:", response.status);
                }
            }
        } catch (error) {
            console.error("Une erreur s'est produite:", error);
        }
    };

    // Rendu du composant
    return (
        <div className="col-md-12">
            <div className="container grey mb-5" style={{ fontSize: '14px', borderRadius: '10px', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)', width: '400px', margin: '0 auto', padding: '20px' }}>
                <br />
                <div className="row mb-4">
                    <div className="col text-center">
                        {id === '0' ? <h2 style={{ fontSize: '1.8rem' }}>Create New Chat</h2> : <h2 style={{ fontSize: '1.8rem' }}>Modify Chat</h2>}
                    </div>
                </div>
                <form onSubmit={handleSubmit} action="#" method="post">
                    <div className="mb-1">
                        <label htmlFor="chatTitle" className="form-label">Chat Title</label>
                        <input
                            type="text"
                            className="form-control"
                            id="chatTitle"
                            name="chatTitle"
                            onChange={handleInputChange}
                            value={chatTitle}
                            required
                            style={{ fontSize: '14px', borderRadius: '5px', border: '1px solid #ccc' }}
                        />
                    </div>
                    <div className="mb-1">
                        <label htmlFor="deadline" className="form-label">Deadline</label>
                        <input
                            type="datetime-local"
                            className="form-control"
                            id="deadline"
                            name="deadline"
                            onChange={handleInputChange}
                            value={deadline}
                            required
                            style={{ fontSize: '14px', borderRadius: '5px', border: '1px solid #ccc' }}
                        />
                    </div>
                    <div className="mb-1">
                        <label htmlFor="chatDescription" className="form-label">Chat Description</label>
                        <textarea
                            className="form-control"
                            id="chatDescription"
                            name="chatDescription"
                            onChange={handleInputChange}
                            value={chatDescription}
                            required
                            style={{ fontSize: '14px', borderRadius: '5px', border: '1px solid #ccc' }}
                        ></textarea>
                    </div>
                    <div className="mb-2">
                        <label htmlFor="chatMembers" className="form-label">Chat Members</label>
                        <div className="chat-member-container" style={{ height: "150px", overflow: "auto", display: "flex", flexDirection: "column", border: '1px solid #ccc', borderRadius: '5px', padding: '10px' }}>
                            <ul className="chat-member-list" style={{ paddingLeft: "0", listStyle: "none" }}>
                                {chatMembers.map(member => (
                                    <li key={member.email} style={{ marginBottom: "10px" }}>
                                        <label style={{ display: "flex", alignItems: "center" }}>
                                            <input
                                                type="checkbox"
                                                value={member.email}
                                                onChange={handleUserSelection}
                                                checked={selectedMembers.includes(member.email) || member.email === ownerEmail}
                                                disabled={member.email === ownerEmail}
                                            />
                                            <span style={{ marginLeft: "5px" }}>{member.email}</span>
                                        </label>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </div>
                    <div style={{ textAlign: "center" }}>
                        <button type="submit" className="btn btn-dark" style={{ fontSize: '14px', borderRadius: '5px' }}> {id === '0' ? `Create Chat` : `Modify Chat`}</button>
                    </div>
                    <br />
                </form>
                {successMessage && <div className="alert alert-warning" style={{ fontSize: '14px' }}>{successMessage}</div>}
                <br />
            </div>
        </div>
    );
};

export default ChatForm;
