import axios from './baseRequest';
import AuthenticationService from './authenticationService';

export const likeComment = commentId => {
    console.log('like comment ', commentId);
    return axios.post('/comment/like', commentId,{headers : {Authorization : AuthenticationService.getAuthenticationHeader()}});
}

export const dislikeComment = commentId => {
    console.log('dislike comment ', commentId);
    return axios.post('/comment/dislike', commentId,{headers : {Authorization : AuthenticationService.getAuthenticationHeader()}});
}
