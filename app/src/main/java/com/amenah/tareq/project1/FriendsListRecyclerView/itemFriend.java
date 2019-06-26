package com.amenah.tareq.project1.FriendsListRecyclerView;

public class itemFriend {

    private String username;
    private String imageUrl;

    public itemFriend(String username, String imageUrl) {
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
