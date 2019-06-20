package com.amenah.tareq.project1.Controllers;

public class NoCurrentUserExistsException extends Exception {
    @Override
    public String getMessage() {
        return "No user saved in shared preferences";
    }
}
