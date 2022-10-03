import React, { useState, useEffect } from 'react';
import AuthenticationService from '../../requestAPIs/authenticationService';
import { Default } from 'react-spinners-css';
import { useParams } from 'react-router-dom';



function Callback() {
    const [loadingState, setLoadingState] = useState(true);
    let search = window.location.search;
    let params = new URLSearchParams(search.split('?')[1]);
    useEffect(() => {
        if(AuthenticationService.getUserJWT() != null){
            window.location.replace("/");
        }
        const req = { "code" :  params.get('code')};
        console.log(req)
        AuthenticationService.callback(req).then((response) => { 
            let userJWT = response.headers.token;
            let userEmail = response.headers.useremail;
            AuthenticationService.setUser(userJWT, userEmail)
            window.location.replace("/")
        })
        .catch(console.error);
    }, []);
    return (
        <div>
             {
                loadingState ? ( 
                    <div className="spinner">
                        <Default color="#b22024" />
                    </div>
                ) : (
                    <div></div>
                )
            }
        </div>
    );
}

export default Callback;
