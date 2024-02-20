import React, { useEffect, useState, useRef } from 'react';
import { useParams } from 'react-router-dom';
import './ChatPage.css';
import { useNavigate } from 'react-router-dom';

const ChatPage = () => {
    const navigate = useNavigate();

    const [chatInfo, setChatInfo] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const { id: chatId } = useParams();
    const webSocket = useRef(null);
    const messagesEndRef = useRef(null);
    const [userStatus, setUserStatus] = useState({});

    const [isModalOpen, setIsModalOpen] = useState(false);

    // Chercher les informations sur le chat quand l'ID du chat change
    useEffect(() => {
        if (chatId) {
            fetch(`http://localhost:8080/api/chat/${chatId}`)
                .then(response => response.json())
                .then(data => {
                    setChatInfo(data);
                    const initialStatus = {};
                    data.users.forEach(user => {
                        initialStatus[user.id] = 'red';
                    });
                    setUserStatus(initialStatus);
                })
                .catch(err => console.error(err));
        }
    }, [chatId]);

    // Démarrer la connexion WebSocket
    const startWebSocket = () => {
        const url = `ws://localhost:8080/websocket/${chatId}/${sessionStorage.getItem('mail')}`;
        webSocket.current = new WebSocket(url);

        webSocket.current.onopen = () => {
            console.log("Establishing a websocket connection...");
        };

        webSocket.current.onmessage = (event) => {
            const data = JSON.parse(event.data);
            setMessages((prevMessages) => [...prevMessages, data]);
            messagesEndRef.current.scrollTop = messagesEndRef.current.scrollHeight;

            // Mettre à jour l'état de l'utilisateur (en ligne/hors ligne)
            if (data.content === 'joined the chat!') {
                setUserStatus(prevStatus => ({ ...prevStatus, [data.email]: 'green' }));
            } else if (data.content === 'left the chat!') {
                setUserStatus(prevStatus => ({ ...prevStatus, [data.email]: 'red' }));
            }
        };

        webSocket.current.onerror = (event) => {
            console.log("Error in websocket...", event);
        };

        webSocket.current.onclose = () => {
            console.log("Close the websocket connection...");
            setIsModalOpen(true);
        };
    };

    // Démarrer la connexion WebSocket après un délai
    useEffect(() => {
        const email = sessionStorage.getItem('mail');
        if (email && chatId) {
            const timer = setTimeout(() => {
                startWebSocket();
            }, 1000); // Retarder de 1 seconde

            return () => {
                clearTimeout(timer);
                if (webSocket.current && webSocket.current.readyState === WebSocket.OPEN) {
                    webSocket.current.close();
                }
            };
        }
    }, [chatId]);

    // Envoyer le message lors de l'appui sur la touche Entrée
    const handleSubmit = (event) => {
        event.preventDefault();
        sendMessage();
    };

    // Faire défiler les messages automatiquement
    const sendMessage = () => {
        const email = sessionStorage.getItem('mail');
        const timestamp = Math.floor(Date.now() / 1000);
        const message = JSON.stringify({ email, content: newMessage, timestamp });
        webSocket.current.send(message);
        setNewMessage('');
    }

    const handleKeyDown = (event) => {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            sendMessage();
        }
    };

    useEffect(() => {
        messagesEndRef.current.scrollTop = messagesEndRef.current.scrollHeight;
    }, [messages]);


    const closeModal = () => {
        setIsModalOpen(false);
        navigate("/mes_chats");
    };

    // Code de rendu pour l'interface utilisateur
    return (
        <div className="container chat-container">
            {isModalOpen && (
                <div className="custom-modal-backdrop">
                    <div className="custom-modal" style={{ boxShadow: '0 40px 80px rgba(0, 0, 0, 0.1)' }}>
                        <div className="custom-modal-header">
                            <h5 className="custom-modal-title">Chat Closed</h5>
                        </div>
                        <div className="custom-modal-body">
                            <p>The chat has been closed.</p>
                        </div>
                        <div className="custom-modal-footer">
                            <button className="btn btn-primary" onClick={closeModal}>Return to Chats</button>
                        </div>
                    </div>
                </div>
            )}

            <div className="row">
                <div className="col-md-8" style={{ boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)', borderRadius: '10px', padding: '20px' }}>
                    {chatInfo && (
                        <div className="d-flex align-items-center mb-4">
                            <h1 className="mr-3" style={{ fontSize: '1.8rem' }}>{chatInfo.nom}</h1>
                            <p className="description mb-0 mr-3" style={{ fontSize: '1rem' }}>{chatInfo.description}</p>
                            <p className="deadline mb-0" style={{ fontSize: '1rem' }}>Fini à: {new Date(chatInfo.deadline).toLocaleString()}</p>
                        </div>
                    )}
                    <div className="chat-messages" ref={messagesEndRef}>
                        {messages.map((msg, index) => (
                            <div key={index} className="card mb-2" style={{ boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)' }}>
                                <div className="card-body">
                                    <h5 className="card-title" style={{ fontSize: '1.2rem' }}>{msg.email}</h5>
                                    <h6 className="card-subtitle mb-2 text-muted" style={{ fontSize: '0.9rem' }}>{new Date(msg.timestamp * 1000).toLocaleString()}</h6>
                                    <p className="card-text" style={{ fontSize: '1rem' }}>{msg.content}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
                <div className="col-md-4" style={{ boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)', borderRadius: '10px', padding: '20px' }}>
                    <h2 style={{ fontSize: '1.8rem' }}>Utilisateurs</h2>
                    <ul className="list-group user-list">
                        {chatInfo && chatInfo.proprietaire && (
                            <li className="list-group-item" key={chatInfo.proprietaire.id} style={{ backgroundColor: userStatus[chatInfo.proprietaire.mail] }}>
                                <div className="font-weight-bold" style={{ fontSize: '1rem' }}>{chatInfo.proprietaire.mail}</div>
                                <div>
                                    {chatInfo.proprietaire.firstName} {chatInfo.proprietaire.lastName} (Propriétaire)
                                </div>
                            </li>
                        )}
                        {chatInfo && chatInfo.users && chatInfo.users.map(user => (
                            <li className="list-group-item" key={user.id} style={{ backgroundColor: userStatus[user.mail] }}>
                                <div className="font-weight-bold" style={{ fontSize: '1rem' }}>{user.mail}</div>
                                <div>{user.firstName} {user.lastName}</div>
                            </li>
                        ))}
                    </ul>
                    <div className="input-container">
                        <h2 style={{ fontSize: '1.8rem' }}>Nouveau message</h2>
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                            <textarea
                                className="form-control"
                                rows="3"
                                value={newMessage}
                                onChange={e => setNewMessage(e.target.value)}
                                onKeyDown={handleKeyDown}
                                style={{ fontSize: '1rem', borderRadius: '5px', border: '1px solid #ccc' }}
                            ></textarea>
                            </div>
                            <button type="submit" className="btn btn-primary" style={{ fontSize: '1rem', borderRadius: '5px' }}>Envoyer</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ChatPage;