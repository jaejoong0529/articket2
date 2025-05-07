import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login, fetchCurrentUser } from './authService';
import '../../css/AuthForm.css'; // 공통 CSS 파일 import

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await login({ username, password }); // 로그인 → 토큰 저장
            const user = await fetchCurrentUser(); // 토큰으로 사용자 정보 가져오기
            console.log(user);
            alert('로그인 성공');

            if (user.role === 'ROLE_ADMIN') {
                navigate('/admin');
            } else {
                navigate('/products');
            }
        } catch (error) {
            alert('로그인 실패: ' + error.message);
        }
    };

    return (
        <div className="auth-container">
            <h2>로그인</h2>
            <form onSubmit={handleSubmit} className="auth-form">
                <input type="text" placeholder="아이디" value={username} onChange={(e) => setUsername(e.target.value)}/>
                <input type="password" placeholder="비밀번호" value={password}
                       onChange={(e) => setPassword(e.target.value)}/>
                <button type="submit">로그인</button>
                <button type="button" onClick={() => navigate(-1)}>← 뒤로가기</button>
            </form>
        </div>
    );
}

export default Login;
