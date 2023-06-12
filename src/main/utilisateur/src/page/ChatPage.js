import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const ChatPage = () => {
    const [chatInfo, setChatInfo] = useState(null);
    const { id: chatId } = useParams();

    useEffect(() => {
        if (chatId) {
            fetch(`http://localhost:8080/api/chat/${chatId}`)
                .then(response => response.json())
                .then(data => setChatInfo(data))
                .catch(err => console.error(err));
        }
    }, [chatId]);

    return (
        <div className="container" style={{fontSize: '70%' }}>
            <div className="row">
                <div className="col-md-8">
                    <h1 className="mt-4 mb-4">Chat Page</h1>
                    <textarea className="form-control mb-3" rows="10" readOnly>
                        {/* 这里你可以从 chatInfo 加载聊天信息 */}
                    </textarea>
                    <h2>Nouveau message</h2>
                    <form>
                        <div className="form-group">
                            <textarea className="form-control" rows="3"></textarea>
                        </div>
                        <button type="submit" className="btn btn-primary">Envoyer</button>
                    </form>
                </div>
                <div className="col-md-4">
                    <h2>Utilisateurs connectés</h2>
                    <ul className="list-group">
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
                </div>
            </div>
        </div>
    );
}

export default ChatPage;
