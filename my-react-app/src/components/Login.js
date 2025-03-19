import React, { useState } from "react";
import { login } from "./authService";
import Cookies from "js-cookie"; // js-cookie 라이브러리 import

function Login() {
    const [loginData, setLoginData] = useState({ username: "", password: "" });
    const [message, setMessage] = useState("");

    const handleChange = (e) => {
        setLoginData({ ...loginData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await login(loginData);
            Cookies.set("token", response.accessToken); // 쿠키에 accessToken 저장
            localStorage.setItem("refreshToken", response.refreshToken); // localStorage에 refreshToken 저장
            setMessage("로그인 성공");
        } catch (error) {
            setMessage(error.message || "로그인 실패");
        }
    };

    return (
        <div>
            <h2>로그인</h2>
            <form onSubmit={handleSubmit}>
                <input type="text" name="username" placeholder="Username" onChange={handleChange} />
                <input type="password" name="password" placeholder="Password" onChange={handleChange} />
                <button type="submit">로그인</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
}

export default Login;