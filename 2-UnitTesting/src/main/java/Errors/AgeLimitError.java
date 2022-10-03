package Errors;

public class AgeLimitError extends Exception {
    public String getMessage() {
        return "User's age is under the movie's age limit";
    }
}
