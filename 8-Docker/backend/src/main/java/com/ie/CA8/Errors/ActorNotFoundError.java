package com.ie.CA8.Errors;

public class ActorNotFoundError extends Exception {
    public String getMessage() {
        return "Actor Not Found";
    }
}