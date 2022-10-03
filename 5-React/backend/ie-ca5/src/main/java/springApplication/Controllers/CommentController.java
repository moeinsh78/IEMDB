package springApplication.Controllers;

import springApplication.IEMDBClasses.IEMDBSystem;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CommentController {
    private IEMDBSystem iemdbSystem;

    public CommentController(){
        iemdbSystem = IEMDBSystem.getInstance();
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "کامنت با موفقیت لایک شد.")
    @RequestMapping(value = "/comment/like",method = RequestMethod.POST)
    public void likeComment (@RequestBody Map<String, String> commentId){
            iemdbSystem.voteComment(iemdbSystem.getLoggedInUser().getEmail(), Integer.valueOf(commentId.get("commentId")),1);
        }    
    @ResponseStatus(value = HttpStatus.OK,reason = "کامنت با موفقیت دیس لایک شد.")
    @RequestMapping(value = "/comment/dislike",method = RequestMethod.POST)
    public void dislikeComment (@RequestBody Map<String, String> commentId){
            iemdbSystem.voteComment(iemdbSystem.getLoggedInUser().getEmail(), Integer.valueOf(commentId.get("commentId")),-1);
        }    
}
