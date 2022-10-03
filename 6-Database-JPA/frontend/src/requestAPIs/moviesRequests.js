import axios from './baseRequest';

export const getMovies = () => {
    console.log('Getting Movies');
    return axios.get('/movies');
}

export const sortMovies = data => {
    console.log('Sorting Movies ', data);
    return axios.get('/movies/sort', {params:data});
}

export const searchMovies = data => {
    console.log('Getting Movies ', data);
    return axios.get('/movies/search', {params:data});
}