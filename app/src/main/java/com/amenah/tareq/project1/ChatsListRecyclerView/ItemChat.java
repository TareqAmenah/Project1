package com.amenah.tareq.project1.ChatsListRecyclerView;

public class ItemChat {

    private String username;
    private String imageUrl;
    private String lastMessage;

    public ItemChat(String username, String imageUrl, String lastMessage) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.lastMessage = lastMessage;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
