import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from "react-router-dom";

const ChatForm = () => {
    const { id } = useParams();
    const [chatTitle, setChatTitle] = useState('');
    const [deadline, setDeadline] = useState('');
    const [chatDescription, setChatDescription] = useState('');
    const [chatMembers, setChatMembers] = useState([]);
    const [selectedMembers, setSelectedMembers] = useState([]);
    const [ownerEmail, setOwnerEmail] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/all_users');
                if (response.status === 200) {
                    const users = response.data.map(user => ({ email: user.mail }));
                    setChatMembers(users);
                } else {
                    console.error("Failed to fetch users. Status code:", response.status);
                }
            } catch (error) {
                console.error("An error occurred while fetching the users:", error);
            }
        };

        fetchUsers();

        if (id !== '0') {
            const fetchChat = async () => {
                try {
                    const response = await axios.get(`http://localhost:8080/api/chat/${id}`);
                    if (response.status === 200) {
                        const chatData = response.data;
                        setChatTitle(chatData.nom);
                        setDeadline(chatData.deadline);
                        setChatDescription(chatData.description);
                        setSelectedMembers(chatData.users.map(user => user.mail));
                        setOwnerEmail(chatData.proprietaire.mail); // 设置所有者的电子邮件
                    } else {
                        console.error("Failed to fetch chat. Status code:", response.status);
                    }
                } catch (error) {
                    console.error("An error occurred while fetching the chat:", error);
                }
            };

            fetchChat();
        }
    }, []);




    const handleInputChange = (event) => {
        const { name, value } = event.target;

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
        const email = event.target.value;
        setSelectedMembers(prevMembers =>
            event.target.checked
                ? [...prevMembers, email]
                : prevMembers.filter(member => member !== email)
        );
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        // Getting mail from session storage
        const ownerEmail = sessionStorage.getItem('mail');

        // Removing ownerEmail from members array if exists
        const membersWithoutOwner = selectedMembers.filter(member => member !== ownerEmail);

        // Including ownerEmail in chatData
        const chatData = {
            title: chatTitle,
            deadline: deadline,
            description: chatDescription,
            members: membersWithoutOwner,
            ownerEmail: ownerEmail
        };

        try {
            if (id === 0) { // Assuming that id is already defined
                // Create new chat
                const response = await axios.post('http://localhost:8080/api/chat', chatData);
                if (response.status === 201) {
                    setSuccessMessage('Chat created successfully.');
                } else {
                    console.error("Failed to create chat. Status code:", response.status);
                }
            } else {
                // Update existing chat
                chatData.id = id; // Adding id to chatData
                const response = await axios.put(`http://localhost:8080/api/chat/${id}`, chatData);
                if (response.status === 200) {
                    setSuccessMessage('Chat updated successfully.');
                } else {
                    console.error("Failed to update chat. Status code:", response.status);
                }
            }
        } catch (error) {
            console.error("An error occurred:", error);
        }
    };




    return (
        <div className="col-md-12">
            <div className="container grey mb-5">
                <br/>
                <div className="row mb-4">
                    <div className="col text-center">
                        {id === '0' ? <h2>Create New Chat</h2> : <h2>Modify Chat</h2>}
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
                        ></textarea>
                    </div>
                    <div className="mb-2">
                        <label htmlFor="chatMembers" className="form-label">Chat Members</label>
                        <div className="chat-member-container" style={{ height: "150px", overflow: "auto", display: "flex", flexDirection: "column" }}>
                            <ul className="chat-member-list" style={{ paddingLeft: "0", listStyle: "none" }}>
                                {chatMembers.map(member => (
                                    <li key={member.email} style={{ marginBottom: "10px" }}>
                                        <label style={{ display: "flex", alignItems: "center" }}>
                                            <input
                                                type="checkbox"
                                                value={member.email}
                                                onChange={handleUserSelection}
                                                checked={selectedMembers.includes(member.email) || member.email === ownerEmail} // 如果是所有者，则勾选
                                                disabled={member.email === ownerEmail} // 如果是所有者，则禁用
                                            />
                                            <span style={{ marginLeft: "5px" }}>{member.email}</span>
                                        </label>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </div>
                    <div style={{ textAlign: "center" }}>
                        <button type="submit" className="btn btn-dark"> {id === '0' ? `Create Chat` : `Modify Chat`}</button>
                    </div>
                    <br/>
                </form>
                {successMessage && <div className="alert alert-warning">{successMessage}</div>}
                <br/>
            </div>
        </div>
    );
};

export default ChatForm;