import axios from './baseRequest';
import AuthenticationService from './authenticationService';

export const getWatchlist = () => {
    console.log('Getting Watchlist');
    return axios.get('/watchlist',{headers : {Authorization : AuthenticationService.getAuthenticationHeader()}});
}

export const getRecommendedMovies = () => {
    console.log('Getting recommended movies');
    return axios.get('/watchlist/recommendation',{headers : {Authorization : AuthenticationService.getAuthenticationHeader()}});
}

export const removeFromWatchlist = data => {
    console.log('Remove Movie ', data, 'from Watchlist');
    return axios.post('/watchlist/remove_from_watchlist', data,{headers : {Authorization : AuthenticationService.getAuthenticationHeader()}});
}