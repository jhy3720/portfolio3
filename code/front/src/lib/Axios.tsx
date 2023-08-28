import axios, { AxiosInstance, InternalAxiosRequestConfig } from 'axios';

axios.defaults.withCredentials = true;

const instance: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
});

  /**
  *------------------------------------------------------------------------
  * 2023.06.12 최현우
  * 
  * Axios를 이용하여 서버와 통신할 떄 요청을 하기전에 인터셉터를 사용하여
  * 해더에 토큰값을 추가해주는 메서드
  *------------------------------------------------------------------------
  */
  instance.interceptors.request.use((config: InternalAxiosRequestConfig) => {

      const token = localStorage.getItem('authToken');
      const key = localStorage.getItem('key');

      // config.headers 객체가 존재하지 않으면 빈 객체로 생성한 후, Authorization 값을 추가합니다.
      config.headers = config.headers || {};
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }

      if (key) {
        config.headers.key = `${key}`;
      }

      console.log(config);
      return config;
    },
    (error) => {
      return Promise.reject(error);
    },
  );

export default instance;  