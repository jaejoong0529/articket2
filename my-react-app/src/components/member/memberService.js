// components/member/memberService.js

const API_BASE_URL = "http://localhost:8080/"; // members API 기본 URL 설정

const handleResponse = async (response) => {
    if (!response.ok) {
        let errorMessage = `HTTP error! status: ${response.status}`;
        try {
            const errorData = await response.json();
            if (errorData && errorData.message) {
                errorMessage += `: ${errorData.message}`;
            }
        } catch (parseError) {
            console.error("Error parsing error response:", parseError);
        }
        throw new Error(errorMessage);
    }
    return response.json();
};

const getAccessToken = () => localStorage.getItem("accessToken");

const apiFetch = async (url, options) => {
    const defaultHeaders = {
        "Content-Type": "application/json",
        ...options.headers,
    };

    const response = await fetch(url, {
        ...options,
        headers: defaultHeaders,
    });

    return handleResponse(response);
};

const authApiFetch = async (url, options) => {
    const accessToken = getAccessToken();
    if (!accessToken) {
        throw new Error("인증이 필요합니다.");
    }

    const authHeaders = {
        Authorization: `Bearer ${accessToken}`,
    };

    return apiFetch(url, {
        ...options,
        headers: {
            ...options.headers,
            ...authHeaders,
        },
    });
};

export const findUsername = async (findUsernameData) => {
    try {
        return await apiFetch(`${API_BASE_URL}/findUsername`, {
            method: "POST",
            body: JSON.stringify(findUsernameData),
        });
    } catch (error) {
        console.error("Find username error:", error);
        throw error;
    }
};

export const findPassword = async (findPasswordData) => {
    try {
        return await apiFetch(`${API_BASE_URL}/findPassword`, {
            method: "POST",
            body: JSON.stringify(findPasswordData),
        });
    } catch (error) {
        console.error("Find password error:", error);
        throw error;
    }
};

export const changePassword = async (changePasswordData) => {
    try {
        return await authApiFetch(`${API_BASE_URL}/changePassword`, {
            method: "POST",
            body: JSON.stringify(changePasswordData),
        });
    } catch (error) {
        console.error("Change password error:", error);
        throw error;
    }
};

export const rechargeMoney = async (rechargeMoneyData) => {
    try {
        return await authApiFetch(`${API_BASE_URL}/recharge`, {
            method: "POST",
            body: JSON.stringify(rechargeMoneyData),
        });
    } catch (error) {
        console.error("Recharge money error:", error);
        throw error;
    }
};