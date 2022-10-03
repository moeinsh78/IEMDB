package Errors;

public class UserAlreadyExistsError extends Exception {
    public String getMessage() {
        return "User already exists";
    }
}