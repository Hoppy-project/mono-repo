import axios from 'axios';
import {
    GET_USER,
} from './types';

//withCredentials 전역 설정 -- CORS error
axios.defaults.withCredentials = false;

export function getUser(props) {
    const token = localStorage.getItem('Authorization')

    if (!token) {
        alert('토큰이 존재하지 않습니다')
    }

    const request = axios.get(`https://hoppy.kro.kr/api/profile`, {
        headers: {
            Authorization: token
        }, withCredentials: false
    })
    .then(response => response.data);

    return {
        type: GET_USER,
        payload: request
    }
}
