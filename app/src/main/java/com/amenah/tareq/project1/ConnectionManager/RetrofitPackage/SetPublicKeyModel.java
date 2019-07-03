package com.amenah.tareq.project1.ConnectionManager.RetrofitPackage;

public class SetPublicKeyModel {


    String username;
    String platform = "android";
    String publicKey;

    public SetPublicKeyModel(String username, String publicKey) {
        this.username = username;
        this.publicKey = publicKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPublickey() {
        return publicKey;
    }

    public void setPublickey(String publicKey) {
        this.publicKey = publicKey;
    }
}
