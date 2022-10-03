package com.ie.CA8.Controller;

import com.ie.CA8.Service.IEMDBSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CommentController {
    @Autowired
    private IEMDBSystem iemdbSystem;

    @ResponseStatus(value = HttpStatus.OK,reason = "کامنت با موفقیت لایک شد.")
    @RequestMapping(value = "/comment/like",method = RequestMethod.POST)
    public void likeComment (@RequestBody Map<String, String> commentId){
        iemdbSystem.likeComment(Integer.valueOf(commentId.get("commentId")));
    }
    @ResponseStatus(value = HttpStatus.OK,reason = "کامنت با موفقیت دیس لایک شد.")
    @RequestMapping(value = "/comment/dislike",method = RequestMethod.POST)
    public void dislikeComment (@RequestBody Map<String, String> commentId){
        iemdbSystem.dislikeComment(Integer.valueOf(commentId.get("commentId")));
    }
}
