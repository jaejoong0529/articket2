import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { signup } from './authService';
import '../../css/AuthForm.css'; // 공통 CSS 파일 import

function Signup() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [nickname, setNickname] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await signup({ username, password, name, email, nickname });
            alert(response);
            navigate('/');
        } catch (error) {
            alert('회원가입 실패: ' + error.message);
        }
    };

    return (
        <div className="auth-container">
            <h2>회원가입</h2>
            <form onSubmit={handleSubmit} className="auth-form">
                <input type="text" placeholder="아이디" value={username} onChange={(e) => setUsername(e.target.value)}/>
                <input type="password" placeholder="비밀번호" value={password}
                       onChange={(e) => setPassword(e.target.value)}/>
                <input type="text" placeholder="이름" value={name} onChange={(e) => setName(e.target.value)}/>
                <input type="email" placeholder="이메일" value={email} onChange={(e) => setEmail(e.target.value)}/>
                <input type="text" placeholder="닉네임" value={nickname} onChange={(e) => setNickname(e.target.value)}/>
                <button type="submit">회원가입</button>
                <button type="button" onClick={() => navigate(-1)}>← 뒤로가기</button>

            </form>
        </div>
    );
}

export default Signup;
