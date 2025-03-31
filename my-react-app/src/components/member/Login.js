import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // useNavigate 훅 import
import { login } from './authService';

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await login({ username, password });
            alert('로그인 성공');
            navigate('/products'); // 로그인 성공 후 상품 목록 페이지로 이동
        } catch (error) {
            alert('로그인 실패: ' + error.message);
        }
    };

    return (
        <div>
            <h2>로그인</h2>
            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="아이디" value={username} onChange={(e) => setUsername(e.target.value)} />
                <input type="password" placeholder="비밀번호" value={password} onChange={(e) => setPassword(e.target.value)} />
                <button type="submit">로그인</button>
            </form>
        </div>
    );
}

export default Login;