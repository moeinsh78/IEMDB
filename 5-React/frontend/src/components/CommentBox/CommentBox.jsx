import React from 'react';
import './CommentBox.css'
import { Icon } from '@iconify/react';
import {likeComment, dislikeComment} from '../../requestAPIs/commentRequests'

function CommentBox({id, user, commentTxt, votes}) {

    const handleLikeComment = () => {
        var rec = {"commentId" : id.toString()}
        likeComment(rec).then(()=> {
            alert("کامنت را لایک کردید!")
            window.location.reload(false);
        })
    }
    const handleDislikeComment = () => {
        var rec = {"commentId" : id.toString()}
        dislikeComment(rec).then(()=> {
            alert("کامنت را دیس لایک کردید!")
            window.location.reload(false);
        })
    }

    return (
        <div className="comment">
                <h4 className="user-name" dir='rtl'>{user}</h4>
                <hr />
                <p className="comment-text">{commentTxt}</p>
                <div>
                    <button onClick={handleLikeComment} className="btn btn-success vote-btn shadow-none">
                        <Icon className="iconify arrow" icon="ant-design:up-outlined"/>
                        <span className="number">{votes.like}</span>
                    </button>
                    <button onClick={handleDislikeComment} className="btn btn-danger vote-btn shadow-none">
                        <Icon className="iconify arrow" icon="ant-design:down-outlined"/>
                        <span className="number">{votes.dislike}</span>
                    </button>
                </div>
        </div>
    );
};

export default CommentBox;
