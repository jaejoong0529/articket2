// Signup.js
import React, { useState } from "react";
import { signup } from "./authService";

function Signup() {
    const [signupData, setSignupData] = useState({ username: "", password: "", email: "" });
    const [message, setMessage] = useState("");

    const handleChange = (e) => {
        setSignupData({ ...signupData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const responseMessage = await signup(signupData);
            setMessage(responseMessage);
        } catch (error) {
            setMessage(error.message || "회원가입 실패");
        }
    };

    return (
        <div>
            <h2>회원가입</h2>
            <form onSubmit={handleSubmit}>
                <input type="text" name="username" placeholder="Username" onChange={handleChange} />
                <input type="email" name="email" placeholder="Email" onChange={handleChange} />
                <input type="password" name="password" placeholder="Password" onChange={handleChange} />
                <button type="submit">회원가입</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
}

export default Signup;