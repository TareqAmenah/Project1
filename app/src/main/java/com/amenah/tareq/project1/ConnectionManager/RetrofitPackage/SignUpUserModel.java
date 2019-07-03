package com.amenah.tareq.project1.ConnectionManager.RetrofitPackage;

public class SignUpUserModel {

    private String username;
    private String password;
    private String email;
    private String platform = "android";


    public SignUpUserModel(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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
