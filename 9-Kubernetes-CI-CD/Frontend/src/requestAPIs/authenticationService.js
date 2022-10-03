import axios from './baseRequest';

export default class AuthenticationService {

    static getUserJWT(){
        return localStorage.getItem("userJWT");
    }

    static getUserEmail(){
        return localStorage.getItem("userEmail");
    }

    static setUser(userJWT, userEmail){
        localStorage.setItem("userJWT" , userJWT);
        localStorage.setItem("userEmail" , userEmail);
    }

    static getAuthenticationHeader() {
        return this.getUserJWT();
    }

    static signup(data) {
        console.log('Signup user ', data);
        return axios.post("/signup", data);
    }

    static login(data) {
        console.log('Login user ', data);
        return axios.post("/login", data);
    }

    static logout() {
        localStorage.removeItem("userJWT");
        localStorage.removeItem("userEmail");
    }

    static callback(code) {
        return axios.get("/callback", {params:code});
    }

}