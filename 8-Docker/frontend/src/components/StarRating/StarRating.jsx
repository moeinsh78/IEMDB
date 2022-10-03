import './StarRating.css';
import React, { useState } from 'react';
import { rateMovie } from '../../requestAPIs/movieRequests.js';
import { FaStar } from 'react-icons/fa';

function StarRating({ movieId, setMovieRateSubmitted }) {

    const [hoverScore, setHoverScore] = useState(null);

    function handleRateSubmission(rate) {
        const reqData = {"movieId": movieId.toString(), "score": rate.toString()}
        console.log("Movie with id", movieId, "will be rated", rate, "!");
        rateMovie(reqData);
        setMovieRateSubmitted(true);
    }

    return (
        <div>
            <div className="stars-box">
            {[ ... Array(10)].map((star, iter) => {
                const ratingValue = iter + 1;

                return (
                    <label>
                        <input 
                            type="radio"
                            name="rating"
                            value={ratingValue}
                        />
                        <FaStar 
                            className="star"
                            size={18}
                            color = {ratingValue <= (hoverScore) ? "#ffc000" : "#e4e4e4"}
                            onMouseEnter={() => setHoverScore(ratingValue)}
                            onMouseLeave={() => setHoverScore(null)}
                            onClick={() => handleRateSubmission(ratingValue)}
                        />
                    </label>
                );
            })}
            </div>
        </div>
    );
}

export default StarRating;