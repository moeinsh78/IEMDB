package Errors;

public class UserNotFoundError extends Exception {
    public String getMessage() {
        return "User with the given email is not found";
    }
}