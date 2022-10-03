import axios from 'axios';

export function getMovies() {
    return axios.get('http://138.197.181.131:5000/api/v2/movies', {
                headers: {
                    'Content-Type': 'application/json',
                }
            })
}

export function getActors() {
    return axios.get('http://138.197.181.131:5000/api/v2/actors', {
                headers: {
                    'Content-Type': 'application/json',
                }
            })
}


