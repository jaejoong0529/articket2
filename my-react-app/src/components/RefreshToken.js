// RefreshToken.js
import React, { useState } from "react";
import { refreshToken } from "./authService";

function RefreshToken() {
    const [message, setMessage] = useState("");

    const handleRefreshToken = async () => {
        const refreshTokenValue = localStorage.getItem("refreshToken");
        if (!refreshTokenValue) {
            setMessage("리프레시 토큰이 없습니다.");
            return;
        }
        try {
            const response = await refreshToken(refreshTokenValue);
            localStorage.setItem("token", response.accessToken);
            setMessage("토큰 갱신 성공");
        } catch (error) {
            setMessage(error.message || "토큰 갱신 실패");
        }
    };

    return (
        <div>
            <button onClick={handleRefreshToken}>토큰 갱신</button>
            {message && <p>{message}</p>}
        </div>
    );
}

export default RefreshToken;