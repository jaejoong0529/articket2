import axios from 'axios';
import { getAccessToken } from '../member/authService';

const API_BASE_URL = "http://localhost:8080/product";

export const getProducts = async () => {
    const token = getAccessToken();
    return axios.get(API_BASE_URL, {
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
        },
    });
};

export const getProductDetail = async (id) => {
    const token = getAccessToken();
    return axios.get(`${API_BASE_URL}/${id}`, {
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
        },
    });
};

export const createProduct = async (productData) => {
    const token = getAccessToken();
    const formData = new FormData();
    formData.append("request", new Blob([JSON.stringify(productData.request)], { type: "application/json" }));
    if (productData.image) {
        formData.append("image", productData.image);
    }

    return axios.post(`${API_BASE_URL}/create`, formData, {
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "multipart/form-data",
        },
    });
};

export const updateProduct = async (id, productData) => {
    const token = getAccessToken();
    const formData = new FormData();
    formData.append("request", new Blob([JSON.stringify(productData.request)], { type: "application/json" }));
    if (productData.image) {
        formData.append("image", productData.image);
    }

    return axios.put(`${API_BASE_URL}/${id}`, formData, {
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "multipart/form-data",
        },
    });
};

export const deleteProduct = async (id) => {
    const token = getAccessToken();
    return axios.delete(`${API_BASE_URL}/${id}`, {
        headers: {
            "Authorization": `Bearer ${token}`,
        },
    });
};