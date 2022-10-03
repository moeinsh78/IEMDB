import React, { useState } from 'react'
import logo from '../../assets/logo.png'
import Dropdowncomp from '../Dropdown/DropdownComp.jsx'
import './Nav.css';

function Nav({ isMoviesPage, searchFunc }) {
    const [searchMethod, setSearchMethod] = useState(null);
    const [searchedText, setSearchedText] = useState("");

    function performSearch() {
        searchFunc(searchMethod, searchedText);
    }

    return (
        <nav className="nav navbar navbar-expand-lg fixed-top" id="mainNav" dir='rtl'>
            <Dropdowncomp />
            {isMoviesPage &&
                <div className="d-flex mx-auto">
                    <button className="btn btn-default shadow-none" onClick={performSearch}>
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-search" >
                            <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z" />
                        </svg>
                    </button>
                    <label>
                        <input type="text" className="form-control" placeholder="عبارت مورد نظر را وارد کنید..." name="search_query" onChange={(event) => {setSearchedText(event.target.value)}}/>
                    </label>
                    <select id="select" onChange={(event) => {setSearchMethod(event.target.value)}}>
                        <option value="" selected disabled hidden>جستجو بر اساس ...</option>
                        <option value="movieName">نام فیلم</option>
                        <option value="releaseDate">تاریخ تولید</option>
                        <option value="genre">ژانر</option>
                    </select>
                </div>
            }

            <div className="mr-auto">
                <a href="/"><img src={logo} alt="Logo" className="logo" /></a>
            </div>
        </nav>
    );
}

export default Nav