import React, { useState } from 'react';
import { changePassword } from './memberService'; // memberService.js에서 changePassword 함수 import
import '../../css/AuthForm.css';
import {useNavigate} from "react-router-dom"; // 공통 CSS 파일 import


function ChangePassword() {
    const [username, setUsername] = useState('');
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const navigate = useNavigate();


    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await changePassword({ username, currentPassword, newPassword });
            alert(response);
        } catch (error) {
            alert('비밀번호 변경 실패: ' + error.message);
        }
    };

    return (
        <div>
            <h2>비밀번호 변경</h2>
            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="아이디" value={username} onChange={(e) => setUsername(e.target.value)}/>
                <input type="password" placeholder="현재 비밀번호" value={currentPassword}
                       onChange={(e) => setCurrentPassword(e.target.value)}/>
                <input type="password" placeholder="새 비밀번호" value={newPassword}
                       onChange={(e) => setNewPassword(e.target.value)}/>
                <button type="submit">비밀번호 변경</button>
                <button type="button" onClick={() => navigate(-1)}>← 뒤로가기</button>

            </form>
        </div>
    );
}

export default ChangePassword;
