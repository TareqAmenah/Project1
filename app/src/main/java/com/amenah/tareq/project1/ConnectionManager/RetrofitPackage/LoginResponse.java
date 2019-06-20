package com.amenah.tareq.project1.ConnectionManager.RetrofitPackage;

public class LoginResponse {

    private String err;
    private String token;

    public LoginResponse(String err, String token) {
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
