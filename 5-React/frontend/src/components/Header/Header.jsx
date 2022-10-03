import './Header.css';
import React, { useState } from 'react';
import { addToWatchlist } from '../../requestAPIs/movieRequests';
import StarRating from '../StarRating/StarRating.jsx';
import { FaStar } from 'react-icons/fa';

function Header({ movie, writers, rate, rateId, setMovieRateSubmitted }) {

    const [starsHovered, setStarsHovered] = useState(false);

    const handleAddToWatchlist = () => {
        var data = {"movieId" : movie.id}
        addToWatchlist(data).then(()=> {
            alert("این فیلم به watchlist شما اضافه شد.")
        })
    }

    return (
        <header>
            <div dir='rtl'>
                <img src={movie.coverImage} alt="txt" className="movie-img" />
                <div className="container info-box">
                    <div className="rate-square">
                        <h2 id="rate-number">{movie.imdbRate}</h2>
                        <p id="rate-text">
                            امتیاز کاربران
                            {movie.ratingsCount > 0 &&
                                <h6>
                                    {movie.ratingsCount} رای
                                </h6>
                            }
                        
                        </p> 
                        <br /> <br /> <br />
                        <div className="rate-stars" onMouseEnter={() => setStarsHovered(true)} onMouseLeave={() => setStarsHovered(false)}>
                            {
                                starsHovered ?
                                (<StarRating movieId={ movie.id } setMovieRateSubmitted={ setMovieRateSubmitted }/>)
                                :
                                (<FaStar className="star" size={18} color="#e4e4e4"/>)
                            }
                            
                        </div>
                        <h4 id={rateId}>{rate}</h4>
                    </div>
                    <div className="movie-info">
                        <p className="movie">{movie.name}</p>
                        <br />
                        <p id="info">کارگردان: {movie.director} <br />نویسندگان: {writers}
                        <br />مدت زمان:{movie.duration} دقیقه</p>
                        <p className="movie">تاریخ انتشار:{movie.releaseDate}</p>
                        <hr />
                        <p id="summary">{movie.summary}</p>
                    </div>
                    <div className="movie-poster">
                        <img src={movie.image} width="180" height="250" alt="txt" />
                    </div>
                    <button onClick={handleAddToWatchlist} className="btn watchlist-btn">افزودن به لیست</button>
                </div>
            </div>
        </header>
    );
}

export default Header;