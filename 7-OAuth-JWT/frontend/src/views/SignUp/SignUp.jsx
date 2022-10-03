import React from 'react';
import Nav from '../../components/NavBar/Nav.jsx';
import  AuthenticationService from '../../requestAPIs/authenticationService.js';
import './SignUp.css';
import { useState, useEffect } from 'react';


function SignUp() {

    useEffect(() => {
        if(AuthenticationService.getUserJWT() != null){
            window.location.replace("/");
            return;
        }
    }, []);


    const [nickname, setNickName] = useState("");
    const [password, setPassword] = useState("");
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [birthdate, setBirthdate] = useState("");

    function handleSignUp() {
        
        if(name && nickname && password && email && birthdate){
            if(/\d/.test(name)){
                alert("خطا! نام نمیتواند شامل عدد باشد.")
                return;
            }
            if(!email.includes('@')){
                alert("خطا! دامنه ایمیل وارد نشده است.")
                return;
            }
        }
        else{
            alert("لطفا اطلاعات خود را کامل وارد کنید!")
            return;
        }
        AuthenticationService.signup({
            name: name,
            nickname: nickname,
            email: email,
            password: password,
            birthdate: birthdate,
        }).then(response => {
            let userJWT = response.headers.token;
            let userEmail = response.headers.useremail;
            AuthenticationService.setUser(userJWT, userEmail);
            alert("ثبت نام با موفقیت انجام شد!")
            window.location.replace("/")
        }).catch(error => {
            window.location.reload(false);
            alert("اکانت دیگری با این ایمیل قبلا ثبت نام شده است.");
        });
    }

    return (
        <div dir="rtl">
            <Nav isMoviesPage={false} />
            <div className="form-box" dir='rtl'>
                <h3 className="form-title">ثبت نام</h3>
                <div  className="signup-form">
                    <div className="form-group">
                        <input type="text" className="form-control" placeholder="نام" onChange={(event) => {setName(event.target.value)}} required />
                    </div>
                    <div className="form-group">
                        <input type="text" className="form-control" placeholder="لقب" onChange={(event) => {setNickName(event.target.value)}} required />
                    </div>
                    <div className="form-group">
                        <input type="text" className="form-control" placeholder="ایمیل" onChange={(event) => {setEmail(event.target.value)}} required />
                    </div>
                    <div className="form-group">
                        <input id="password-field" type="password" className="form-control" placeholder="رمز عبور" onChange={(event) => {setPassword(event.target.value)}} required />
                    </div>
                    <div className="form-group birthday-form">
                        <label>تاریخ تولد</label>
                        <br />
                        <input type="date" id="datemin" name="datemin" min="1920-01-01" max="2014-12-30" onChange={(event) => {setBirthdate(event.target.value)}} required/><br />
                    </div>
                    <div className="form-group">
                        <button type="submit" className="form-control btn submit btn-success px-3" onClick={handleSignUp}>ثبت نام</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default SignUp;
