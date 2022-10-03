import React from 'react'
import './Card.css';
import { useEffect, useState } from 'react';

function Card({link, title, imageURL, text, hoverItemClass, movieImageClass, hoverInfoClass, age}) {
    const [score, setScore] = useState("");
    useEffect(() => {
        if(age){
            setScore("Birthday: ")
        }
        else{
            setScore("IMDB: ")
        }
    }, []);
    return (
        <div className="col-sm-6 col-lg-3">
            <div className={"hover-item " + hoverItemClass + " mx-auto"}>
                <a href={link}>
                    <img className={movieImageClass} src={imageURL} alt={title} />
                    <div className={"hover-info " + hoverInfoClass}>
                        <p className="hover-txt">{title}<br/>{score + text}</p>
                    </div>
                </a>
            </div>
        </div>
    );
}

export default Card