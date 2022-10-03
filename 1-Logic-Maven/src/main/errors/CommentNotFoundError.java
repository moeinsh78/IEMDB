
public class CommentNotFoundError extends Exception {
    public String getMessage() {
        return "Comment with the given Id is not found";
    }
}