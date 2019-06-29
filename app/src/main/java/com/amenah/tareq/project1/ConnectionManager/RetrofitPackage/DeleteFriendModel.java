package com.amenah.tareq.project1.ConnectionManager.RetrofitPackage;

public class DeleteFriendModel {
    String username;
    String delete;

    public DeleteFriendModel(String username, String delete) {
        this.username = username;
        this.delete = delete;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }
}
