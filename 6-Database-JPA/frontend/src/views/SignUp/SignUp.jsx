import React from 'react';
import Nav from '../../components/NavBar/Nav.jsx';
import './SignUp.css';

function SignUp() {

    return (
        <div dir="rtl">
            <Nav isMoviesPage={false} />
            <div className="form-box">
                <h3 className="form-title">ثبت نام</h3>
                <form action="#" className="signup-form">
                    <div className="form-group">
                        <input type="text" className="form-control" placeholder="نام" required />
                    </div>
                    <div className="form-group">
                        <input type="text" className="form-control" placeholder="لقب" required />
                    </div>
                    <div className="form-group">
                        <input type="text" className="form-control" placeholder="نام کاربری" required />
                    </div>
                    <div className="form-group">
                        <input id="password-field" type="password" className="form-control" placeholder="رمز عبور" required />
                    </div>
                    <div className="form-group birthday-form">
                        <label>تاریخ تولد</label>
                        <br />
                        <label>
                            <input type="number" className="form-control birthday-field" placeholder="روز تولد" required />
                        </label>
                        <label>
                            <input type="number" className="form-control birthday-field" placeholder="ماه تولد" required />
                        </label>
                        <label>
                            <input type="number" className="form-control birthday-field"  placeholder="سال تولد" required />
                        </label>
                    </div>
                    <div className="form-group">
                        <button type="submit" className="form-control btn submit btn-success px-3">ثبت نام</button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default SignUp;
