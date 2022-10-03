import axios from "axios";
import AuthenticationService from "./authenticationService";

var axiosInstance = axios.create({
  baseURL: "http://127.0.0.1:8080",
  responseType: "json",
  headers: {
    "Content-Type": "application/json;charset=UTF-8",
    Accept: "application/json"
  }
});

axiosInstance.interceptors.response.use( response => response );

export default axiosInstance;
