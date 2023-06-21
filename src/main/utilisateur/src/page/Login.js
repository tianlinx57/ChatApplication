import React, {useState} from 'react';
import './Login.css';
import axios from 'axios';

const Login = () => {
    const [mail, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);


    const handleSubmit = (event) => {
        event.preventDefault();
        const data = {
            mail: mail,
            password: password,
        };

        axios.post('http://localhost:8080/api/login', data)
            .then((response) => {
                const { id, mail, firstName, lastName, isDisabled } = response.data;
                if (isDisabled) {
                    alert('User is disabled');
                } else {
                    sessionStorage.setItem('isAuthenticated', 'true');
                    sessionStorage.setItem('id', id);
                    sessionStorage.setItem('mail', mail);
                    sessionStorage.setItem('firstName', firstName);
                    sessionStorage.setItem('lastName', lastName);
                    window.location.reload();
                }
            })
            .catch((error) => {
            let errorMessage = "An unknown error occurred";
            if (error.response) {
                errorMessage = "Identifiant ou mot de passe incorrect";
            } else {
                errorMessage = "There was an issue connecting to the server";
            }
            setError(errorMessage);
        });

    };

    return (
        <div className="login-container">
            <img src="/messenger.png" alt="Login" className="login-img" />
            <h1 style={{ fontSize: '2.5em', fontFamily: 'Arial, sans-serif', textAlign: 'center', marginBottom: '20px' }}>
                Messengeur
            </h1>
            <div className="card-padding">
                <form onSubmit={handleSubmit} className="mt-4">
                    {error && <div className="alert alert-danger" role="alert">{error}</div>}
                    <div className="form-group">
                        <label htmlFor="mail">Email</label>
                        <input type="text" id="mail" value={mail} onChange={e => setEmail(e.target.value)} className="form-control" placeholder="Enter email" required/>
                    </div>
                    <div className="form-group mt-2">
                        <label htmlFor="password">Password</label>
                        <input type="password" id="password" value={password} onChange={e => setPassword(e.target.value)} className="form-control" placeholder="Enter password" required/>
                    </div>
                    <button type="submit" className="btn btn-primary mt-3">Connexion</button>
                </form>
            </div>
        </div>
    );
};

export default Login;
