import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const Logout = () => {
    const navigate = useNavigate();

    useEffect(() => {
        // 在这里执行注销逻辑，例如清除会话存储
        sessionStorage.setItem('isAuthenticated', 'false');
        sessionStorage.removeItem('id');
        sessionStorage.removeItem('mail');
        sessionStorage.removeItem('firstName');
        sessionStorage.removeItem('lastName');

        // 然后将用户重定向到登录页面
        navigate('/login');
    });

    return null; // 此组件不需要渲染任何内容
};

export default Logout;
