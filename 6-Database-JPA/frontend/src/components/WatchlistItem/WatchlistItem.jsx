import React, { useEffect, useState } from 'react';
import { Icon } from '@iconify/react';
import './WatchlistItem.css';
import { removeFromWatchlist } from '../../requestAPIs/watchlistRequests';

function WatchlistItem({ movieItem, setItemDeleted }) {
    const [genresStr, setGenresStr] = useState("");
    const [usersRating, setUsersRating] = useState("");

    useEffect(() => {
        var genresStr = movieItem.genres[0];
        for (let i = 1; i < movieItem.genres.length; i++) 
            genresStr += (", " +  movieItem.genres[i]);
        
        if (movieItem.ratingsAverage == "NaN") 
            setUsersRating("هنوز به این فیلم امتیازی داده نشده است.");
        else 
            setUsersRating(movieItem.ratingsAverage);

        setGenresStr(genresStr);
    }, []);


    function deleteItem(movieId) {
        const reqData = {"movieId": movieId.toString()}
        removeFromWatchlist(reqData);
        setItemDeleted(true);
    }

    return (
        <div className="watchlist-box">
            <div className="watchlist-description">
                <div className="watchlist-description-header">
                    <div className="watchlist-title">
                        <b> {movieItem.name} </b>
                    </div>
                    <div className="watchlist-trash">
                        <button className="trash-btn">
                        <Icon icon="gridicons:trash" color="#e75309" width="30" height="30" onClick={() => deleteItem(movieItem.id)} />
                            {/* <span className="iconify" data-icon="gridicons:trash" style={{color: "#E75309"}} data-width="30" data-height="30"></span> */}
                        </button>
                    </div>
                </div>
                <div className="watchlist-description-bottom">
                    <div className="watchlist-info">
                        <p>
                            <b> کارگردان: </b>
                            {movieItem.director}
                        </p>
                        <p>
                            <b> ژانر: </b>
                            {genresStr}
                        </p>
                        <p>
                            <b> تاریخ انتشار: </b>
                            {movieItem.releaseDate}
                        </p>
                        <p>
                            <b> مدت زمان: </b>
                            {movieItem.duration} دقیقه
                        </p>
                    </div>
                    <div className="watchlist-grades">
                        <p>
                            <b> IMDB </b>
                            :{movieItem.imdbRate}
                        </p>
                        <p>
                            <b> امتیاز کاربران: </b>
                            {usersRating}
                        </p>
                    </div>
                </div>
            </div>
            <div className="watchlist-image-div">
                <img className="watchlist-image" src={movieItem.image} height="100%" width="100%" alt="..." />
            </div>
        </div>
    );
}

export default WatchlistItem