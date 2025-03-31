import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // useNavigate 훅 import
import { signup } from './authService';

function Signup() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [nickname, setNickname] = useState('');
    const navigate = useNavigate(); // useNavigate 훅 사용

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await signup({ username, password, name, email, nickname });
            alert(response); // 회원가입 성공 메시지
            navigate('/'); // 홈 화면으로 이동
        } catch (error) {
            alert('회원가입 실패: ' + error.message);
        }
    };

    return (
        <div>
            <h2>회원가입</h2>
            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="아이디" value={username} onChange={(e) => setUsername(e.target.value)} />
                <input type="password" placeholder="비밀번호" value={password} onChange={(e) => setPassword(e.target.value)} />
                <input type="text" placeholder="이름" value={name} onChange={(e) => setName(e.target.value)} />
                <input type="email" placeholder="이메일" value={email} onChange={(e) => setEmail(e.target.value)} />
                <input type="text" placeholder="닉네임" value={nickname} onChange={(e) => setNickname(e.target.value)} />
                <button type="submit">회원가입</button>
            </form>
        </div>
    );
}

export default Signup;