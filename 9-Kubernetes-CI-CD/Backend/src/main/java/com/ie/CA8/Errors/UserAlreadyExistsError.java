package com.ie.CA8.Errors;

public class UserAlreadyExistsError extends Exception {
    public String getMessage() {
        return "User already exists";
    }
}