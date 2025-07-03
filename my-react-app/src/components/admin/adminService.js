import axios from 'axios';
import { getAccessToken } from '../member/authService';

const API_BASE_URL = "http://localhost:8080/api/admin";

const getAuthHeaders = () => {
    const token = getAccessToken();
    return {
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
        },
    };
};

// 회원 전체 조회
export const fetchMembers = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/members`, getAuthHeaders());
        return response.data;
    } catch (error) {
        console.error("회원 목록 조회 실패:", error);
        throw error;
    }
};

// 상품 전체 조회
export const fetchProducts = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/products`, getAuthHeaders());
        return response.data;
    } catch (error) {
        console.error("상품 목록 조회 실패:", error);
        throw error;
    }
};

// 거래 내역 전체 조회
export const fetchTransactions = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/transactions`, getAuthHeaders());
        return response.data;
    } catch (error) {
        console.error("거래 내역 조회 실패:", error);
        throw error;
    }
};

// 회원 삭제
export const deleteMember = async (id) => {
    try {
        await axios.delete(`${API_BASE_URL}/members/${id}`, getAuthHeaders());
    } catch (error) {
        console.error(`회원(ID: ${id}) 삭제 실패:`, error);
        throw error;
    }
};

// 상품 삭제
export const deleteProduct = async (id) => {
    try {
        await axios.delete(`${API_BASE_URL}/products/${id}`, getAuthHeaders());
    } catch (error) {
        console.error(`상품(ID: ${id}) 삭제 실패:`, error);
        throw error;
    }
};

// 거래 내역 삭제
export const deleteTransaction = async (id) => {
    try {
        await axios.delete(`${API_BASE_URL}/transactions/${id}`, getAuthHeaders());
    } catch (error) {
        console.error(`거래(ID: ${id}) 삭제 실패:`, error);
        throw error;
    }
};

// 회원 검색
export const searchMembers = async (keyword) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/members/search?keyword=${encodeURIComponent(keyword)}`, getAuthHeaders());
        return response.data;
    } catch (error) {
        console.error(`회원 검색 실패 (keyword: ${keyword}):`, error);
        throw error;
    }
};

// 상품 판매 여부 필터링
export const filterProducts = async (isSold) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/products/filter?isSold=${isSold}`, getAuthHeaders());
        return response.data;
    } catch (error) {
        console.error(`상품 필터링 실패 (isSold: ${isSold}):`, error);
        throw error;
    }
};

// 주간 요약 통계 조회
export const fetchSummary = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/summary`, getAuthHeaders());
        return response.data;
    } catch (error) {
        console.error("주간 요약 통계 조회 실패:", error);
        throw error;
    }
};
