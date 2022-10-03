package com.ie.CA8.Errors;

public class MovieNotFoundError extends Exception {
    public String getMessage() {
        return "Movie with the given Id is not found";
    }
}