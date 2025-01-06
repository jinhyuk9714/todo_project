const BASE_URL = window.location.origin.includes('localhost')
    ? 'http://localhost:8080'
    : 'https://todo-project-j3jq.onrender.com';

const AUTH_URL = `${BASE_URL}/auth`; // 인증 관련 경로

// axios 요청 인터셉터 추가
axios.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("jwt_token"); // 로컬 스토리지에서 JWT 가져오기
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 응답 인터셉터로 만료된 토큰 처리
axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            alert("세션이 만료되었습니다. 다시 로그인하세요.");
            localStorage.removeItem("jwt_token");
            localStorage.removeItem("jwt_expiresAt");
            window.location.href = "/frontend/pages/login.html"; // 로그인 페이지로 리다이렉트
        }
        return Promise.reject(error);
    }
);

// 로그인 함수
async function loginUser(username, password) {
    try {
        const response = await axios.post(`${AUTH_URL}/login`, { username, password });
        console.log("JWT Token:", response.data.token); // 디버깅용

        const { token, expiresAt } = response.data;

        // 토큰 및 만료 시간 저장
        localStorage.setItem('jwt_token', token);
        localStorage.setItem('jwt_expiresAt', expiresAt); // 만료 시간 저장
        console.log(`토큰 만료 시간: ${new Date(expiresAt)}`);
        return response.data;
    } catch (error) {
        console.error('로그인 실패:', error.response?.data || error.message);
        throw error;
    }
}

// 회원가입 함수
async function registerUser(username, password) {
    try {
        const response = await axios.post(`${AUTH_URL}/register`, { username, password });
        return response.data;
    } catch (error) {
        console.error('회원가입 실패:', error.response?.data || error.message);
        alert(error.response?.data?.message || '회원가입에 실패했습니다. 다시 시도하세요.');
        throw error;
    }
}
