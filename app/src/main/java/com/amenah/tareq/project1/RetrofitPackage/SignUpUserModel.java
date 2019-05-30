package com.amenah.tareq.project1.RetrofitPackage;

public class SignUpUserModel {

    private String username;
    private String password;
    private String email;

    public SignUpUserModel(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
