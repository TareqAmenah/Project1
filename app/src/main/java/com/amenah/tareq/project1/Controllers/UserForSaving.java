package com.amenah.tareq.project1.Controllers;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class UserForSaving implements Serializable {

    private String username;
    private String accessToken;
    private List<String> friendsList;
    private Map<String, String> friendsSecretKeys;
    private String privateKey;
    private String publicKey;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<String> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<String> friendsList) {
        this.friendsList = friendsList;
    }

    public Map<String, String> getFriendsSecretKeys() {
        return friendsSecretKeys;
    }

    public void setFriendsSecretKeys(Map<String, String> friendsSecretKeys) {
        this.friendsSecretKeys = friendsSecretKeys;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

}
