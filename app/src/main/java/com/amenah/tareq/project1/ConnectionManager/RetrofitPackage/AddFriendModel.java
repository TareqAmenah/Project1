package com.amenah.tareq.project1.ConnectionManager.RetrofitPackage;

public class AddFriendModel {

    private String current;
    private String friend;

    public AddFriendModel(String current, String friend) {
        this.current = current;
        this.friend = friend;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }
}
