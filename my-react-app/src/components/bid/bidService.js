import axios from 'axios';
import { getAccessToken } from '../member/authService';

const API_BASE_URL = "http://localhost:8080/bid";

export const bidProduct = async (productId, bidAmount) => {
    const token = getAccessToken();
    try {
        const response = await axios.post(`${API_BASE_URL}/bidProduct`, { productId, bidAmount }, {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        });
        return response.data; // 응답 데이터 반환
    } catch (error) {
        console.error("입찰 실패:", error);
        throw error; // 에러를 다시 던져서 호출하는 쪽에서 처리하도록 함
    }
};

export const getHighestBid = async (productId) => {
    const token = getAccessToken();
    try {
        const response = await axios.get(`${API_BASE_URL}/${productId}`, {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        });
        console.log("getHighestBid response data: ", response.data);
        console.log("getHighestBid response data type: ", typeof response.data); // 응답 데이터 타입 확인
        return response.data;
    } catch (error) {
        console.error("최고 입찰가 조회 실패:", error);
        throw error;
    }
};

export const getUserBids = async (memberId) => {
    const token = getAccessToken();
    try {
        const response = await axios.get(`${API_BASE_URL}/member/${memberId}`, {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        });
        return response.data;
    } catch (error) {
        console.error("사용자 입찰 내역 조회 실패:", error);
        throw error;
    }
};

export const buyProduct = async (productId) => {
    const token = getAccessToken();
    try {
        const response = await axios.post(`${API_BASE_URL}/buyProduct`, { productId }, {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        });
        return response.data;
    } catch (error) {
        console.error("즉시 구매 실패:", error);
        throw error;
    }
};