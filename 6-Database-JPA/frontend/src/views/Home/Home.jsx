import React, { useEffect, useState } from 'react';
import Card from '../../components/Card/Card';
import Nav from '../../components/NavBar/Nav.jsx';
import './Home.css';
import { getMovies, searchMovies, sortMovies } from '../../requestAPIs/moviesRequests';

import { Default } from 'react-spinners-css';

function Home() {
    const [moviesList, setMoviesList] = useState([]);
    const [searchMethod, setSearchMethod] = useState(null);
    const [searchedText, setSearchedText] = useState("");
    const [loadingState, setLoadingState] = useState(true);

    useEffect(() => {
        getMovies().then(({data}) => { 
            var result = [];
            for(var i in data)
                result.push(data[i]);
            setMoviesList(result)
            setLoadingState(false)
        })
        .catch(console.error);
    }, []);



    function sortByReleaseDate() {
        const req = { "sortMethod" :  "sort_by_date", "searchMethod" :  searchMethod , "searchedTxt" : searchedText};
        sortMovies(req).then(({data}) => { 
            var result = [];
            for(var i in data)
                result.push(data[i]);
            setMoviesList(result)
        })
        .catch(console.error);
    }


    function sortByImdbRate() {
        const req = { "sortMethod" :  "sort_by_imdb" , "searchMethod" :  searchMethod , "searchedTxt" : searchedText};
        sortMovies(req).then(({data}) => { 
            var result = [];
            for(var i in data)
                result.push(data[i]);
            setMoviesList(result)
        })
        .catch(console.error);
    }

    function search(method, text){
        setSearchMethod(method)
        setSearchedText(text)
        const req = { "searchMethod" :  method , "searchedTxt" : text};
        searchMovies(req).then(({data}) => { 
            var result = [];
            for(var i in data)
                result.push(data[i]);
            setMoviesList(result)
        })
        .catch(console.error);
    }

    return (
        <div>
            {
                loadingState ? ( 
                    <div className="spinner">
                        <Default color="#b22024" />
                    </div>
                ) : (
                    <div>
                        <Nav isMoviesPage={true} searchFunc={search}/>
                        <div dir='rtl' className="sort-factor">
                            <div className="sort-info-header">
                                <p>رتبه بندی بر اساس:</p>
                            </div>
                            <div className="sort-info-bottom">
                                <button className="sort-button" onClick={sortByReleaseDate}>تاریخ</button>
                                <br/>
                                <button className="sort-button" onClick={sortByImdbRate}>امتیاز IMDB</button>
                            </div>
                        </div>
                        <div className="container movies-table">
                            <div className="row justify-content-center">
                                {moviesList.map(({id, name, imdbRate, image}) => <Card key={id} link={'/movie/'+id} title={name} imageURL={image} text={imdbRate} hoverInfoClass="hover-info-home" movieImageClass="movie-image-home" age={false} />)}
                            </div>
                        </div>
                    </div>
                )
            }
        </div>
    );
}

export default Home;
