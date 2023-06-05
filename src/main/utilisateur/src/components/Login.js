import React, { useState } from 'react';
import './Login.css'; // 引入你的CSS文件

const Login = ({ onLogin }) => {
    const [mail, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ mail, password }),
            });

            if (!response.ok) {
                throw new Error('Invalid email or password');
            }

            const user = await response.json();
            onLogin(user);
        } catch (error) {
            setError(error.message);
        }
    };


    return (
        <div className="login-container">
            <img src="/tiktok.png" alt="Login" className="login-img" />
            <form onSubmit={handleSubmit} className="mt-4">
                {error && <div className="alert alert-danger" role="alert">{error}</div>}
                <div className="form-group">
                    <label htmlFor="email">Email</label>
                    <input type="text" id="email" value={mail} onChange={e => setEmail(e.target.value)} className="form-control" placeholder="Enter email" required/>
                </div>
                <div className="form-group mt-2">
                    <label htmlFor="password">Password</label>
                    <input type="password" id="password" value={password} onChange={e => setPassword(e.target.value)} className="form-control" placeholder="Enter password" required/>
                </div>
                <button type="submit" className="btn btn-primary mt-3">Connexion</button>
            </form>
        </div>
    );
};

export default Login;
