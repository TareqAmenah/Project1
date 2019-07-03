package com.amenah.tareq.project1.ConnectionManager.RetrofitPackage;

public class LoginUserModel {

    private String username;
    private String password;
    private String platform = "android";

    public LoginUserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
