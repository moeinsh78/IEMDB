package Errors;

public class InvalidCommandError extends Exception {
    public String getMessage() {
        return "Invalid command entered";
    }
}