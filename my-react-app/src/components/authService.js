// authService.js

const API_BASE_URL = "http://localhost:8080/auth"; // API 기본 URL 설정

export const signup = async (signupData) => {
    try {
        const response = await fetch(`${API_BASE_URL}/signup`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(signupData),
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.text(); // 성공 메시지 반환
    } catch (error) {
        console.error("Signup error:", error);
        throw error; // 에러를 상위 컴포넌트로 전달
    }
};

export const login = async (loginData) => {
    try {
        const response = await fetch(`${API_BASE_URL}/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(loginData),
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const responseData = await response.json();
        return responseData; // 로그인 응답 (토큰 포함) 반환
    } catch (error) {
        console.error("Login error:", error);
        throw error; // 에러를 상위 컴포넌트로 전달
    }
};

// export const logout = async () => {
//     try {
//         const response = await fetch(`${API_BASE_URL}/logout`, {
//             method: "POST",
//             headers: {
//                 "Authorization": `Bearer ${localStorage.getItem("token")}`, // 토큰 포함
//             },
//         });
//
//         if (!response.ok) {
//             throw new Error(`HTTP error! status: ${response.status}`);
//         }
//
//         return await response.text(); // 로그아웃 성공 메시지 반환
//     } catch (error) {
//         console.error("Logout error:", error);
//         throw error; // 에러를 상위 컴포넌트로 전달
//     }
// };

export const refreshToken = async (refreshTokenValue) => {
    try {
        const response = await fetch(`${API_BASE_URL}/refresh`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ refreshToken: refreshTokenValue }),
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const responseData = await response.json();
        return responseData; // 새 토큰 반환
    } catch (error) {
        console.error("Refresh token error:", error);
        throw error; // 에러를 상위 컴포넌트로 전달
    }
};