import axios from "axios";

const BASE_URL = "http://localhost:8080";

export const getBoughtProducts = () => {
    return axios.get(`${BASE_URL}/bought`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`
        }
    });
};

export const getSoldProducts = () => {
    return axios.get(`${BASE_URL}/sold`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`
        }
    });
};