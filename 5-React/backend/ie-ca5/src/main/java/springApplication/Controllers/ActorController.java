package springApplication.Controllers;

import springApplication.IEMDBClasses.IEMDBSystem;
import springApplication.IEMDBClasses.Actor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import springApplication.IEMDBClasses.Movie;

import java.util.HashMap;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ActorController {
    private IEMDBSystem iemdbSystem;
    public ActorController(){
        iemdbSystem = IEMDBSystem.getInstance();
    }

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
