import React, { useEffect, useState, useRef } from 'react';
import { useParams } from 'react-router-dom';
import './ChatPage.css';

const ChatPage = () => {
    const [chatInfo, setChatInfo] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const { id: chatId } = useParams();
    const webSocket = useRef(null);
    const messagesEndRef = useRef(null);

    useEffect(() => {
        if (chatId) {
            fetch(`http://localhost:8080/api/chat/${chatId}`)
                .then(response => response.json())
                .then(data => setChatInfo(data))
                .catch(err => console.error(err));
        }
    }, [chatId]);

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
        };

        webSocket.current.onerror = (event) => {
            console.log("Error in websocket...", event);
        };

        webSocket.current.onclose = () => {
            console.log("Close the websocket connection...");
        };
    };

    useEffect(() => {
        const email = sessionStorage.getItem('mail');
        if (email && chatId) {
            startWebSocket();
        }
        return () => {
            if (webSocket.current) {
                webSocket.current.close();
            }
        };
    },[chatId]);

    const handleSubmit = (event) => {
        event.preventDefault();
        sendMessage();
    };

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

    return (
        <div className="container chat-container">
            <div className="row">
                <div className="col-md-8">
                    <h1 className="mt-4 mb-4">Chat Page</h1>
                    <div className="chat-messages" ref={messagesEndRef}>
                        {messages.map((msg, index) => (
                            <div key={index} className="card mb-2">
                                <div className="card-body">
                                    <h5 className="card-title">{msg.email}</h5>
                                    <h6 className="card-subtitle mb-2 text-muted">{new Date(msg.timestamp * 1000).toLocaleString()}</h6>
                                    <p className="card-text">{msg.content}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
                <div className="col-md-4">
                    <h2>Utilisateurs</h2>
                    <ul className="list-group user-list">
                        {chatInfo && chatInfo.proprietaire && (
                            <li className="list-group-item" key={chatInfo.proprietaire.id}>
                                {chatInfo.proprietaire.firstName} {chatInfo.proprietaire.lastName} (Propriétaire)
                            </li>
                        )}
                        {chatInfo && chatInfo.users && chatInfo.users.map(user => (
                            <li className="list-group-item" key={user.id}>
                                {user.firstName} {user.lastName}
                            </li>
                        ))}
                    </ul>
                    <div className="input-container">
                        <h2>Nouveau message</h2>
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <textarea
                                    className="form-control"
                                    rows="3"
                                    value={newMessage}
                                    onChange={e => setNewMessage(e.target.value)}
                                    onKeyDown={handleKeyDown}
                                ></textarea>
                            </div>
                            <button type="submit" className="btn btn-primary">Envoyer</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ChatPage;