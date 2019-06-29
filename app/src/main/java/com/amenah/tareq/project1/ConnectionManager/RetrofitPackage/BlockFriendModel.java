package com.amenah.tareq.project1.ConnectionManager.RetrofitPackage;

public class BlockFriendModel {
    String username;
    String block;

    public BlockFriendModel(String username, String block) {
        this.username = username;
        this.block = block;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
}
