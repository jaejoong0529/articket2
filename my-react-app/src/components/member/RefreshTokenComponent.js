import React from 'react';
import { refreshToken } from './authService'; // authService.js에서 refreshToken 함수 import

function RefreshTokenComponent() {
    const handleRefreshToken = async () => {
        try {
            await refreshToken(localStorage.getItem('refreshToken'));
            alert('토큰 갱신 성공');
        } catch (error) {
            alert('토큰 갱신 실패: ' + error.message);
        }
    };

    return (
        <div>
            <button onClick={handleRefreshToken}>토큰 갱신</button>
        </div>
    );
}

export default RefreshTokenComponent;