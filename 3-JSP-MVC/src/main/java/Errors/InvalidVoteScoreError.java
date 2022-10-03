package Errors;

public class InvalidVoteScoreError extends Exception {
    public String getMessage() {
        return "Invalid value for voting the comment";
    }
}
