package com.ie.CA6.Controller;

import com.ie.CA6.Entity.User;
import com.ie.CA6.Service.IEMDBSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class LoginController {
    @Autowired
    private IEMDBSystem iemdbSystem;

    @ResponseStatus(value = HttpStatus.OK,reason = "کاربر با موفقیت لاگین شد.")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    protected void login(@RequestBody Map<String, String> user_info){
        iemdbSystem.login(user_info.get("email"), user_info.get("password"));
    }
    @ResponseStatus(value = HttpStatus.OK,reason = "کاربر با موفقیت از سامانه خارج شد.")
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    protected void logout(){
        iemdbSystem.logout();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/login/user",method = RequestMethod.GET)
    protected User getLoggedInUser(){ return iemdbSystem.getLoggedInUser(); }
}
