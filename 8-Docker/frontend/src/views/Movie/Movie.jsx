import React, { useEffect, useState } from 'react';
import Header from '../../components/Header/Header.jsx';
import Nav from '../../components/NavBar/Nav.jsx';
import CommentBox from '../../components/CommentBox/CommentBox.jsx';
import CastBox from '../../components/CastBox/CastBox.jsx';
import './Movie.css';
import { useParams } from 'react-router-dom';
import { addComment, getMovie, getMovieCast } from '../../requestAPIs/movieRequests.js';
import AuthenticationService from '../../requestAPIs/authenticationService';
import "react-multi-carousel/lib/styles.css";
import { Default } from 'react-spinners-css';

function Movie() {
    const [movie, setMovie] = useState({});
    const [castList, setCastList] = useState([]);
    const [writersSrting, setwritersSrting] = useState("");
    const [usersRating, setUsersRating] = useState("");
    const [rateClass, setRateClass] = useState("");
    const [commentTxt, setCommentTxt] = useState("");
    const [commentList, setCommentList] = useState([]);
    const [movieRateSubmitted, setMovieRateSubmitted] = useState(false);
    const [loadingState, setLoadingState] = useState(true);


    const {id} = useParams();

    useEffect(() => {
        let userJWT = AuthenticationService.getUserJWT();
        if(userJWT == null) {
            alert("برای مشاهده ی این صفحه باید ابتدا وارد حساب کاربری خود شوید!")
            window.location.reload(false);
            window.location.replace("/login")
        }
        else {
            getMovie(id).then(({ data }) => {
                setMovie(data)
                var writersStr = data.writers[0];
                for (let i = 1; i < data.writers.length; i++) 
                    writersStr += (", " +  data.writers[i]);
    
                if (data.ratingsAverage == "NaN") {
                    setUsersRating("هنوز به این فیلم امتیازی داده نشده است.");
                    setRateClass("users-rate-text")
                }
                else {
                    setUsersRating(data.ratingsAverage);
                    setRateClass("users-rate")
                }
                setCommentList(data.comments)
                setwritersSrting(writersStr);
            }).catch(error => {
               AuthenticationService.logout()
               alert("به علت کار نکردن بیش از حد، از حساب کاربری خود خارج شدید.")
                window.location.reload(false);
                window.location.replace("/login")
            });
            getMovieCast(id).then(({data}) =>{
                setCastList(data);
            })
            setLoadingState(false);
            setMovieRateSubmitted(false);
        }
    }, [movieRateSubmitted]);



    var castBoxes = [];
    var j = 0;
    for(var i=0;i < (castList.length / 5);i++){
        castBoxes.push(<CastBox cast={ castList.slice(j, j + 5) }/>);
        j += 5;
    }

    const handleAddComment = () => {
        const comment = { "movieId" :  id, "commentTxt" :  commentTxt};
        addComment(comment).then(({data}) => { 
            alert("کامنت شما اضافه شد.")
        })
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
                    <Nav isMoviesPage={false} />
                    <Header 
                        movie={movie} 
                        writers={writersSrting} 
                        rate={usersRating} 
                        rateId={rateClass} 
                        setMovieRateSubmitted = { setMovieRateSubmitted }
                    />
                    <div className="container extra-info">
                        <div id="cast" dir='rtl'>
                            <h5 className="title">بازیگران</h5>
                            {castBoxes}
                        </div>
                        <div id="comments">
                            <h5 className="title">دیدگاه ها</h5>
                            <div className="add-comment-form">
                                <p id="form-label" dir='rtl'>دیدگاه خود را اضافه کنید:</p>
                                <hr />
                                <form onSubmit={handleAddComment}>
                                    <textarea className="form-control" aria-label="With textarea" onChange={(event) => {setCommentTxt(event.target.value)}} required></textarea>
                                    <button type="submit" className="btn btn-success comment-btn">ثبت</button>
                                </form>
                            </div>
                            {commentList.map(({id, userNickname, text, likes, dislikes}) => <CommentBox key={id} id={id} user={userNickname} commentTxt={text} likes={likes} dislikes={dislikes} />)}
                        </div>
                    </div>
                </div>    
            )
        }
        </div>
    );
}

export default Movie;
