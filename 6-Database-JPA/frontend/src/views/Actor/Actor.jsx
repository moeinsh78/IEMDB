import React, { useEffect, useState } from 'react';
import Nav from '../../components/NavBar/Nav.jsx';
import Card from '../../components/Card/Card.jsx'
import './Actor.css';

import { Default } from 'react-spinners-css';
import { useParams } from 'react-router-dom';
import { getActor, getActorMovies } from '../../requestAPIs/actorRequests';

function Actor() {
    const [actor, setActor] = useState({});
    const [actorMovies, setActorMovies] = useState([]);
    const [loadingState, setLoadingState] = useState(true);

    const {id} = useParams();

    useEffect(() => {
        getActor(id).then(({ data }) => { 
            setActor(data);
        });
        getActorMovies(id).then(({ data }) => { 
            var result = [];
            for(var i in data)
                result.push(data[i]);
            setActorMovies(result)
            setLoadingState(false)
        })
    }, []);

    return (
        <div>
            {
                loadingState ? ( 
                    <div className="spinner">
                        <Default color="#b22024" />
                    </div>
                ) : (
                    <div dir='rtl'>
                        <Nav isMoviesPage={false}/>
                        <div className="actor-info">
                            <img id="actor-img" src={actor.image} alt="actorName:" />
                            <div id="info">
                                <div id="general-info">
                                    <p id="title">مشخصات بازیگر</p>
                                    <p id="description">
                                        نام :{actor.name} <br />
                                        تاریخ تولد : {actor.birthday} <br />
                                        ملیت : {actor.nationality} <br />
                                        تعداد فیلم ها : {actorMovies.length}
                                    </p>
                                </div>
                                <div id="movies">
                                    <p id="movie-title">فیلم ها</p>
                                    <div className="movies-imgs">
                                        {actorMovies.slice(0,3).map(({id, name, imdbRate, image}) => <Card key={id} link={'/movie/'+id} title={name} imageURL={image} text={imdbRate} hoverItemClass="hover-item-actor" movieImageClass="movie-image-actor" hoverInfoClass="hover-info-actor" age={false}/>)}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                )
            }
        </div>
    );
}

export default Actor;
