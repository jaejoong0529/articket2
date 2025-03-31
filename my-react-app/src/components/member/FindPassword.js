import React, { useState } from 'react';
import { findPassword } from './memberService'; // memberService.js에서 findPassword 함수 import

function FindPassword() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await findPassword({ username, email });
            alert(response);
        } catch (error) {
            alert('비밀번호 찾기 실패: ' + error.message);
        }
    };

    return (
        <div>
            <h2>비밀번호 찾기</h2>
            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="아이디" value={username} onChange={(e) => setUsername(e.target.value)} />
                <input type="email" placeholder="이메일" value={email} onChange={(e) => setEmail(e.target.value)} />
                <button type="submit">비밀번호 찾기</button>
            </form>
        </div>
    );
}

export default FindPassword;