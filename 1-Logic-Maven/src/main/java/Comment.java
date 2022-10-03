import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

public class Comment {
    private int id;
    private String user_email;
    private int movie_id;
    private String text;
    private HashMap<String, Integer> votes = new HashMap<>();
    private String comment_publish_time;

    public Comment(int _id, String _user_email, int _movie_id, String _text, String _comment_publish_time) {
        this.id = _id;
        this.user_email = _user_email;
        this.movie_id = _movie_id;
        this.text = _text;
        this.comment_publish_time = _comment_publish_time;
    }

    public int getId(){
        return id;
    }

    public String getUserEmail(){
        return user_email;
    }

    public String getText(){
        return text;
    }

    public void vote(String user_email, int vote) throws InvalidVoteScoreError{
        if(vote != -1 && vote != 1 && vote != 0)
            throw new InvalidVoteScoreError();
        votes.put(user_email, vote);
    }

    public HashMap<String, Integer> votesCount(){
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

}
