import axios from './baseRequest';

export const getWatchlist = () => {
    console.log('Getting Watchlist');
    return axios.get('/watchlist');
}

export const getRecommendedMovies = () => {
    console.log('Getting recommended movies');
    return axios.get('/watchlist/recommendation');
}

export const removeFromWatchlist = data => {
    console.log('Remove Movie ', data, 'from Watchlist');
    return axios.post('/watchlist/remove_from_watchlist', data);
}