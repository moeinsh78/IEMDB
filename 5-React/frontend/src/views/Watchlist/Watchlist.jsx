import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Nav from '../../components/NavBar/Nav.jsx';
import WatchlistItem from '../../components/WatchlistItem/WatchlistItem.jsx';
import Card from '../../components/Card/Card.jsx'
import './Watchlist.css';

import { Default } from 'react-spinners-css';
import { getWatchlist, getRecommendedMovies } from '../../requestAPIs/watchlistRequests';
import { getLoggedInUser } from '../../requestAPIs/loginRequests';

function Watchlist() {
    const [watchlistMovies, setWatchlistMovies] = useState([]);
    const [recommendedMovies, setRecommendedMovies] = useState([]);
    const [loadingState, setLoadingState] = useState(true);
    const [itemDeleted, setItemDeleted] = useState(false);

    useEffect(() => {
        getLoggedInUser().then(({data}) => { 
            if(data == "") {
                alert("برای مشاهده ی این صفحه باید ابتدا وارد حساب کاربری خود شوید!")
                window.location.reload(false);
                window.location.replace("/login")
            }
            else {
                getWatchlist().then(({ data }) => {
                    var watchlistResult = [];
                    for (var i in data)
                        watchlistResult.push(data[i]);
                    setWatchlistMovies(watchlistResult);
                    console.log("WatchList Data:", watchlistResult);
                    setItemDeleted(false)
                });
                getRecommendedMovies().then(({ data }) => {
                    var recommendedResult = [];
                    for (var i in data)
                        recommendedResult.push(data[i]);
                    setRecommendedMovies(recommendedResult);
                    console.log("Recommended Data:", recommendedResult);
                    setLoadingState(false);
                });
            }
        })
    }, [itemDeleted]);

    return (
        <div>
            {
                loadingState ? (
                    <div className="spinner">
                        <Default color="#b22024" />
                    </div>
                )
                :
                (
                    <div>
                        <Nav isMoviesPage={false}/>
                        <div dir="rtl" className="container watchlist-movies">
                            {watchlistMovies.map((movie) => <WatchlistItem key={movie.id} movieItem={movie} setItemDeleted={setItemDeleted} />)}
                        </div>

                        <div className="container suggestion-movies">
                            <div className="suggestion-box-header">
                                <p>فیلم های پیشنهادی</p>
                            </div>
                            <div className="suggestion-box-items">
                                {recommendedMovies.map(({id, name, imdbRate, image}) => <Card key={id} link={'/movie/'+id}title={name} imageURL={image} text={imdbRate} hoverItemClass="hover-item-watchlist" movieImageClass="movie-image-watchlist" hoverInfoClass="hover-info-watchlist" age={false}/>)}
                            </div>
                        </div>
                    </div>
                )
            }
        </div>
    );
}

export default Watchlist;