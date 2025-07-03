import React from 'react';
import { logout } from './authService'; // authService.js에서 logout 함수 import

function Logout() {
    const handleLogout = async () => {
        try {
            await logout();
            alert('로그아웃 성공');
        } catch (error) {
            alert('로그아웃 실패: ' + error.message);
        }
    };

    return (
        <div>
            <button onClick={handleLogout}>로그아웃</button>
        </div>
    );
}

export default Logout;
