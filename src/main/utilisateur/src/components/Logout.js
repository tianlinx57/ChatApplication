import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const Logout = () => {
    const navigate = useNavigate();

    useEffect(() => {
        // Effectue ici la logique de déconnexion, par exemple, supprime les données de session
        sessionStorage.setItem('isAuthenticated', 'false');
        sessionStorage.removeItem('id');
        sessionStorage.removeItem('mail');
        sessionStorage.removeItem('firstName');
        sessionStorage.removeItem('lastName');

        // Ensuite, redirige l'utilisateur vers la page de connexion
        navigate('/login');
    });

    return null; // Ce composant n'a pas besoin de rendre du contenu
};

export default Logout;
