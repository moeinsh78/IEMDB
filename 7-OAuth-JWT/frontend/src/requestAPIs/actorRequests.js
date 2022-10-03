import axios from './baseRequest';
import AuthenticationService from './authenticationService';

export const getActor = (id) => {
    console.log('Getting actor', id);
    return axios.get('/actors/' + id, {headers : {Authorization : AuthenticationService.getAuthenticationHeader()}});
}

export const getActorMovies = (id) => {
    console.log('Getting actor', id, 'movies');
    return axios.get('/actors/' + id + '/movies',{headers : {Authorization : AuthenticationService.getAuthenticationHeader()}});
}