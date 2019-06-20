package com.amenah.tareq.project1.ConnectionManager;

public class SendInSocketException extends Exception {

    @Override
    public String getMessage() {
        return "Error in sending throw socket";
    }
}
