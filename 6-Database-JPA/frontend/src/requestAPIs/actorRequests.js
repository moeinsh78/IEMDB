import axios from './baseRequest';

export const getActor = (id) => {
    console.log('Getting actor', id);
    return axios.get('/actors/' + id);
}

export const getActorMovies = (id) => {
    console.log('Getting actor', id, 'movies');
    return axios.get('/actors/' + id + '/movies');
}
