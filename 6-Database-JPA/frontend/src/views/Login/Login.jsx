import React, { useState } from 'react';
import Nav from '../../components/NavBar/Nav.jsx';
import './Login.css';
import { login } from '../../requestAPIs/loginRequests';

function Login() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    function handleLogin() {
        const user = { "email" :  username, "password" :  password};
        login(user).then(({data}) => { 
            if(data.status == 200) {
                window.location.replace("/")
            }
            else {
                alert("نام کاربری یا رمز عبور اشتباه است. دوباره تلاش کنید!")
            }
            
        })
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
                        <div>
                            <label className="checkbox-wrap checkbox-primary">من را به خاطر بسپار
                                <input type="checkbox" checked />
                                <span className="checkmark"></span>
                            </label>
                        </div>
                        <div>
                            <a href="#" className="forget-pass-link">رمز عبور را فراموش کرده‌ام!</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;
