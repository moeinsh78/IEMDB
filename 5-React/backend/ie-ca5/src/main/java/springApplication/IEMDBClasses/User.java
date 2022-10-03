package springApplication.IEMDBClasses;

import java.util.ArrayList;

public class User {
    private String email;
    private String password;
    private String nickname;
    private String name;
    // private String birthDate;

    private ArrayList<Integer> watch_list = new ArrayList<>();

    public String getName() { return name; }
    public String getEmail() {
    return email;
}

    public String getNickname() {
        return nickname;
    }

    public String getPassword(){ return password;}
    public ArrayList<Integer> getWatchList() {
        return this.watch_list;
    }

    public void addMovieToWatchList(Movie movie){
        int movie_id = movie.getId();
        watch_list.add(movie_id);
    }
    public void removeMovieToWatchList(Integer movie_id) {
        watch_list.remove(movie_id);
    }
}