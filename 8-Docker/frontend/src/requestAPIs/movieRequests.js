import axios from './baseRequest';
import AuthenticationService from './authenticationService';

export const getMovie = (id) => {
    console.log('Getting Movie', id);
    return axios.get('/movie/' + id,{headers : {Authorization : AuthenticationService.getAuthenticationHeader()}});
}

export const getMovieCast = (id) => {
    console.log('Getting Movie cast', id);
    return axios.get('/movie/'+id+'/cast',{headers : {Authorization : AuthenticationService.getAuthenticationHeader()}});
}

export const addComment = data => {
    console.log('Adding Comment ', data);
    return axios.post('/movie/add_comment', data,{headers : {Authorization : AuthenticationService.getAuthenticationHeader()}});
}

export const rateMovie = data => {
    console.log('Rate Movie ', data);
    return axios.post('/movie/rate', data,{headers : {Authorization : AuthenticationService.getAuthenticationHeader()}});
}

export const addToWatchlist = data => {
    console.log('Adding to Movie ', data, 'to Watchlist');
    return axios.post('/movie/add_to_watchlist', data,{headers : {Authorization : AuthenticationService.getAuthenticationHeader()}});
}
