import axios from 'axios';

const API_BASE_URL = "http://localhost:8080/api/auth";

const storeTokens = (tokens) => {
    localStorage.setItem("accessToken", tokens.accessToken);
    localStorage.setItem("refreshToken", tokens.refreshToken);
};

export const getAccessToken = () => {
    return localStorage.getItem("accessToken");
};

const clearTokens = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
};

export const signup = async (signupData) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/signup`, signupData);
        return response.data;
    } catch (error) {
        console.error("Signup error:", error);
        throw error;
    }
};

export const login = async (loginData) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/login`, loginData);
        storeTokens(response.data);
        return response.data;
    } catch (error) {
        console.error("Login error:", error);
        throw error;
    }
};

export const logout = async () => {
    try {
        const accessToken = getAccessToken();
        const response = await axios.post(`${API_BASE_URL}/logout`, {}, {
            headers: { Authorization: `Bearer ${accessToken}` }
        });
        clearTokens();
        return response.data;
    } catch (error) {
        console.error("Logout error:", error);
        throw error;
    }
};

export const refreshToken = async (refreshTokenValue) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/refresh`, { refreshToken: refreshTokenValue });
        storeTokens(response.data);
        return response.data;
    } catch (error) {
        console.error("Refresh token error:", error);
        throw error;
    }
};
export const fetchCurrentUser = async () => {
    try {
        const accessToken = getAccessToken();
        const response = await axios.get("http://localhost:8080/api/me", {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        });
        return response.data; // { username: "...", role: "ROLE_ADMIN" }
    } catch (error) {
        console.error("Fetch current user error:", error);
        throw error;
    }
};