package springApplication.IEMDBClasses;

import java.util.HashMap;
public class Comment {
    private int id;
    private String userEmail;
    private int movieId;
    private String text;
    private HashMap<String, Integer> votes = new HashMap<>();
    private String userNickname;
    public void setId(int _id){
        id = _id;
    }

    public void setUserEmail(String user_email) {
        this.userEmail = user_email;
    }

    public void setMovieId(int movie_id) {
        this.movieId = movie_id;
    }

    public void setText(String _text) {
        this.text = _text;
    }

    public int getId(){
        return id;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public String getText(){
        return text;
    }

    public String getUserNickname(){
        return userNickname;
    }

    public void setUserNickname(String nickname){
        this.userNickname = nickname;
    }

    public void vote(String user_email, int vote) {
        votes.put(user_email, vote);
    }

    public HashMap<String, Integer> getVotesCount(){
        HashMap<String, Integer> votes_counts = new HashMap<>();

        int likes_count = 0;
        int dislikes_count = 0;
        int abstentions_count = 0;

        for (HashMap.Entry<String, Integer> vote : votes.entrySet()) {
            int vote_value = vote.getValue();
            if(vote_value == 1)
                likes_count += 1;
            else if(vote_value == 0)
                abstentions_count += 1;
            else if(vote_value == -1)
                dislikes_count += 1;
        }
        votes_counts.put("like",likes_count);
        votes_counts.put("abstention", abstentions_count);
        votes_counts.put("dislike", dislikes_count);
        return votes_counts;
    }
    public int getMovieId(){
        return movieId;
    }

}
