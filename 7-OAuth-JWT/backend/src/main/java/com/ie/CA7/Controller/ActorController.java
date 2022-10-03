package com.ie.CA7.Controller;

import com.ie.CA7.Service.IEMDBSystem;
import com.ie.CA7.Entity.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.ie.CA7.Entity.Movie;

import java.util.HashMap;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ActorController {
    @Autowired
    private IEMDBSystem iemdbSystem;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/actors/{id}", method = RequestMethod.GET)
    public Actor getActor(@PathVariable("id") String id){
        int actor_id = Integer.parseInt(id);
        return iemdbSystem.getActorById(actor_id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/actors/{id}/movies", method = RequestMethod.GET)
    public HashMap<Integer, Movie> getActorMovies(@PathVariable("id") String id){
        int actor_id = Integer.parseInt(id);
        return iemdbSystem.getActorMovies(actor_id);
    }

}
