import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String birth_date;

    private ArrayList<Integer> watch_list;

    public User(String _email, String _password, String _nickname, String _name, String _birth_date) {
        this.email = _email;
        this.password = _password;
        this.nickname = _nickname;
        this.name = _name;
        this.birth_date = _birth_date;
        this.watch_list = new ArrayList<>();
    }
    public String getEmail() {
        return email;
    }

    public ArrayList<Integer> getWatchList() {
        return this.watch_list;
    }

    private int getAge(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate today = LocalDate.now();
        dtf.format(today);
        LocalDate birthday = LocalDate.parse(birth_date);
        Period p = Period.between(birthday, today);
        return p.getYears();
    }

    public void addMovieToWatchList(Movie movie) throws AgeLimitError, MovieAlreadyExistsError{
        int movie_age_limit = movie.getAgeLimit();
        int movie_id = movie.getId();
        if(watch_list.contains(movie_id))
            throw new MovieAlreadyExistsError();
        if(movie_age_limit >= getAge())
            throw new AgeLimitError();
        watch_list.add(movie_id);
    }

    public void removeMovieToWatchList(Integer movie_id) throws MovieNotFoundError {
        if(!watch_list.contains(movie_id))
            throw new MovieNotFoundError();
        watch_list.remove(movie_id);
    }
}