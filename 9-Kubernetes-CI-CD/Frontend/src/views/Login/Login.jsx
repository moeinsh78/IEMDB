import React, { useState , useEffect } from 'react';
import Nav from '../../components/NavBar/Nav.jsx';
import './Login.css';
import AuthenticationService from '../../requestAPIs/authenticationService';
import {MarkGithubIcon} from 'react-icons'

function Login() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    useEffect(() => {
        if(AuthenticationService.getUserJWT() != null){
            window.location.replace("/");
            return;
        }
    }, []);

    function handleLogin() {
        if(password && username){
            if(!username.includes('@')){
                alert("خطا! دامنه ایمیل وارد نشده است.")
                return;
            }
        }
        else{
            alert("لطفا اطلاعات خود را کامل وارد کنید!")
            return;
        }
        const user = { "email" :  username, "password" :  password};
        AuthenticationService.login(user).then(response => { 
            let userJWT = response.headers.token;
            let userEmail = response.headers.useremail;
            console.log(response)
            AuthenticationService.setUser(userJWT, userEmail)
            window.location.replace("/")
            }).catch(error => {
                alert("نام کاربری و یا رمز عبور اشتباه است.");
            });
    }

    return (
        <div>
            <Nav isMoviesPage={false} />
            <div className="form-box" dir="rtl">
                <h3 className="form-title">ورود</h3>
                <div className="signin-form" >
                    <div className="form-group">
                        <input type="text" className="form-control" placeholder="نام کاربری" 
                        onChange={(event) => {setUsername(event.target.value)}} required />
                    </div>
                    <div className="form-group">
                        <input id="password-field" type="password" className="form-control" placeholder="رمز عبور"
                        onChange={(event) => {setPassword(event.target.value)}} required />
                    </div>
                    <div className="form-group">
                        <button type="submit" className="form-control login-btn submit px-3" onClick={handleLogin}>ورود</button>
                    </div>
                    <div className="form-group">
                        <label>با اکانت github وارد شوید:</label>
                        <br/>
                        <a href='http://github.com/login/oauth/authorize?client_id=4e1a1049c6dea3c2480a&scope=user'>
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" width="40" height="40">
                                <path fill-rule="evenodd" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z"></path>
                            </svg>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;
