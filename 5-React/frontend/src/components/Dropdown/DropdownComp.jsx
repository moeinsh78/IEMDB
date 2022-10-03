import { useEffect, useState } from 'react';
import { Icon } from '@iconify/react';
import Dropdown from 'react-bootstrap/Dropdown';
import './DropdownComp.css';
import { getLoggedInUser, logout } from '../../requestAPIs/loginRequests.js'


function DropdownComp() {
    const [loggedInUser, setLoggedInUser] = useState("");

    useEffect(() => {
        getLoggedInUser().then(({data}) => { 
            setLoggedInUser(data);
        })
    }, []);
    
    function logoutUser() {
        logout();
        window.location.replace("/");
    }

    return (
        <Dropdown>
            <Dropdown.Toggle className="p-0 no-border shadow-none" variant="" id="dropdown-basic">
                <Icon icon="healthicons:ui-user-profile" className="iconify prof-icon" />
            </Dropdown.Toggle>
            {
                loggedInUser == "" ?
                    (
                        <Dropdown.Menu className="user-actions-dropdown">
                            <Dropdown.Item href="/login"><b>ورود</b></Dropdown.Item>
                            <Dropdown.Item href="/signup"><b>ثبت نام</b></Dropdown.Item>
                        </Dropdown.Menu>
                    )
                    :
                    (
                        <Dropdown.Menu className="user-actions-dropdown">
                            <Dropdown.Item href="#"><b>{loggedInUser.email}</b></Dropdown.Item>
                            <Dropdown.Item href="/watchlist"><b>Watchlist</b></Dropdown.Item>
                            <Dropdown.Item onClick={logoutUser}><b>Logout</b></Dropdown.Item>
                        </Dropdown.Menu>
                    )
            }
        </Dropdown>
    );
}

export default DropdownComp;