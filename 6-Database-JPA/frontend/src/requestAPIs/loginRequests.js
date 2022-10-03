import axios from './baseRequest';

export const login = data => {
    console.log('Login user ', data);
    return axios.post('/login', data);
}

export const getLoggedInUser = () => {
    console.log('Get Logged in User ');
    return axios.get('/login/user');
}

export const logout = () => {
    console.log('Logout User');
    return axios.post('/logout');
}