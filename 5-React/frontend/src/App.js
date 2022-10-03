import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

import Home from './views/Home/Home.jsx';
import Actor from './views/Actor/Actor.jsx'
import Movie from './views/Movie/Movie.jsx'
import Login from './views/Login/Login.jsx'
import Watchlist from './views/Watchlist/Watchlist.jsx'
import SignUp from './views/SignUp/SignUp.jsx';

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" exact element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/actors/:id" element={<Actor />} />
                <Route path="/movie/:id" element={<Movie /> } />
                <Route path="/watchlist" element={<Watchlist />}/>
                <Route path="/signup" element={<SignUp />}/>
            </Routes>
        </Router>
    );
};
export default App;

