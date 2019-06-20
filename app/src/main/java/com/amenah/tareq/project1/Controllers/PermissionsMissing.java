package com.amenah.tareq.project1.Controllers;

public class PermissionsMissing extends Exception {

    @Override
    public String getMessage() {
        return "Missing some permissions";
    }
}
