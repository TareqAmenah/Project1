package com.amenah.tareq.project1.ConnectionManager.RetrofitPackage;

public class UnBlockFriendModel {

    String username;
    String unblock;

    public UnBlockFriendModel(String username, String unblock) {
        this.username = username;
        this.unblock = unblock;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUnblock() {
        return unblock;
    }

    public void setUnblock(String unblock) {
        this.unblock = unblock;
    }
}
