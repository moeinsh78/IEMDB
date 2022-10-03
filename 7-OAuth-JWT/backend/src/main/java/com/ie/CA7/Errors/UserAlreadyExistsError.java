package com.ie.CA7.Errors;

public class UserAlreadyExistsError extends Exception {
    public String getMessage() {
        return "User already exists";
    }
}