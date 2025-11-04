import axios from "axios";

const API_BASE_URL = "http://localhost:8080"; // your Spring Boot backend

const api = axios.create({
    baseURL: API_BASE_URL,
});

// Attach JWT token automatically if exists
api.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;