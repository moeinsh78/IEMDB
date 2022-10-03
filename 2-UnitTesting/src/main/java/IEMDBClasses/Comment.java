package IEMDBClasses;

import Errors.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
public class Comment {
    private int id;
    private String userEmail;
    private int movieId;
    private String text;
    private HashMap<String, Integer> votes = new HashMap<>();
    private String comment_publish_time;
    private String userNickname;

    public Comment() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.comment_publish_time = dtf.format(now);
    }
    public void setId(int _id){
        id = _id;
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

    public void vote(String user_email, int vote) throws InvalidVoteScoreError {
        if(vote != -1 && vote != 1 && vote != 0)
            throw new InvalidVoteScoreError();
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
