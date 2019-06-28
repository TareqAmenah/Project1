package com.amenah.tareq.project1.FriendsListRecyclerView;

public class ItemUser {

    private String username;
    private String imageUrl;

    public ItemUser(String username, String imageUrl) {
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
