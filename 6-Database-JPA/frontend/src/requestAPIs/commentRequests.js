import axios from './baseRequest';

export const likeComment = commentId => {
    console.log('like comment ', commentId);
    return axios.post('/comment/like', commentId);
}

export const dislikeComment = commentId => {
    console.log('dislike comment ', commentId);
    return axios.post('/comment/dislike', commentId);
}
