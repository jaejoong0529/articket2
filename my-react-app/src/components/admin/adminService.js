import axios from 'axios';
import { getAccessToken } from '../member/authService';

const API_BASE_URL = "http://localhost:8080/admin";

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
