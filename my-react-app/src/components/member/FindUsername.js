import React, { useState } from 'react';
import { findUsername } from './memberService'; // memberService.js에서 findUsername 함수 import
import '../../css/AuthForm.css';
import {useNavigate} from "react-router-dom"; // 공통 CSS 파일 import


function FindUsername() {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const navigate = useNavigate();


    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await findUsername({ name, email });
            alert(response);
        } catch (error) {
            alert('아이디 찾기 실패: ' + error.message);
        }
    };

    return (
        <div className="auth-container">
            <h2>아이디 찾기</h2>
            <form onSubmit={handleSubmit} className="auth-form">
                <input type="text" placeholder="이름" value={name} onChange={(e) => setName(e.target.value)}/>
                <input type="email" placeholder="이메일" value={email} onChange={(e) => setEmail(e.target.value)}/>
                <button type="submit">아이디 찾기</button>
                <button type="button" onClick={() => navigate(-1)}>← 뒤로가기</button>

            </form>
        </div>
    );
}

export default FindUsername;