import axios from 'axios';
import store from '@/store'; // Путь к вашему хранилищу Vuex

const api = axios.create({
    headers: {
        'Content-Type': 'application/json',
    },
});

// Перехватчик запросов перед отправкой
api.interceptors.request.use(
    (config) => {
        const token = store.getters["user/getToken"];

        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        } else {
            delete config.headers['Authorization'];
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default api;