package springApplication.Controllers;

import org.springframework.web.bind.annotation.*;
import springApplication.IEMDBClasses.IEMDBSystem;
import org.springframework.http.HttpStatus;
import springApplication.IEMDBClasses.User;

import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class LoginController {

    @ResponseStatus(value = HttpStatus.OK,reason = "کاربر با موفقیت لاگین شد.")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    protected void login(@RequestBody Map<String, String> user_info){
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        iemdbSystem.login(user_info.get("email"), user_info.get("password"));
    }
    @ResponseStatus(value = HttpStatus.OK,reason = "کاربر با موفقیت از سامانه خارج شد.")
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    protected void logout(){
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        iemdbSystem.logout();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/login/user",method = RequestMethod.GET)
    protected User getLoggedInUser(){
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        return iemdbSystem.getLoggedInUser();
    }
}
