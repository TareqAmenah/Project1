package com.amenah.tareq.project1.RetrofitPackage;

public class SignUpResponse {


    private String err;
    private String token;

    public SignUpResponse(String err, String token) {
        this.err = err;
        this.token = token;
    }

    public String getErr() {
        return err;
    }

    public String getToken() {
        return token;
    }
}