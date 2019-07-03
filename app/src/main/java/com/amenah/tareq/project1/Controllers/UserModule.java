package com.amenah.tareq.project1.Controllers;

import android.util.Log;

import com.amenah.tareq.project1.ConnectionManager.Messages.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModule implements Serializable {

    private static String username;
    private static String accessToken;
    private static List<String> friendsList;
    private static Map<String, String> friendsSecretKeys;
    private static String privateKey;
    private static String publicKey;
    private static Map<String, List<Message>> friendsChats = new HashMap<>();


    public static void setSecretKeyForFriend(String friendName, String secretKey) {
        if (friendsSecretKeys == null)
            friendsSecretKeys = new HashMap<>();

        Log.v("*****************", "Secret key of: " + friendName + " : " + secretKey);

        friendsSecretKeys.put(friendName, secretKey);
    }

    public static String getSecretKeyOfFriend(String friendName) {
        return friendsSecretKeys.get(friendName);
    }

    public static List<Message> getChatsOf(String friendName) {
        if (friendsChats == null)
            return null;

//        List<Message> list =  StorageManager.getFriendChat(friendName);
//        friendsChats.put(friendName,list);

        if (!friendsChats.containsKey(friendName))
            friendsChats.put(friendName, StorageManager.getFriendChat(friendName));

        List<Message> messageList = friendsChats.get(friendName);
        return messageList;

    }

    public static void setChatsOf(String friendName, List<Message> messages) {
        friendsChats.put(friendName, new ArrayList<Message>());
        for (Message message : messages) {
            friendsChats.get(friendName).add(message);
        }
    }

    public static void setUsername(String username) {
        UserModule.username = username;
    }

    public static String getUsername() {
        return username;
    }

    public static List<String> getFriendsList() {
        return friendsList;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setFriendsList(List<String> friendsList) {
        UserModule.friendsList = friendsList;
    }

    public static void setAccessToken(String accessToken) {
        UserModule.accessToken = accessToken;
    }

    public static void addFriend(String newFriendName) {
        if (friendsList == null)
            friendsList = new ArrayList<>();
        friendsList.add(newFriendName);
    }

    public static void addMessage(String friendName, Message message) {
        if (friendsChats.containsKey(friendName)) {
            friendsChats.get(friendName).add(message);
        } else {
            friendsChats.put(friendName, new ArrayList<Message>());
            friendsChats.get(friendName).add(message);
        }
    }

    public static void deleteFriendChat(String friendName) {
        friendsChats.get(friendName).clear();
    }

    public static void clearFriendList() {
        if (friendsList != null)
            friendsList.clear();
    }

    public static void clearAll() {
        username = null;
        accessToken = null;

        if (friendsList != null) {
            for (String s : friendsList) {
                List<Message> list = friendsChats.get(s);
                if (list != null)
                    list.clear();
            }
        }

        friendsChats = null;
        friendsSecretKeys = null;
        friendsList = null;

    }

    public static void initializeUser(String name, String token) {
        username = name;
        accessToken = token;
        friendsList = new ArrayList<>();
        friendsSecretKeys = new HashMap<>();
        friendsChats = new HashMap<>();
    }

    public static void saveUser() {

        UserForSaving userForSaving = new UserForSaving();
        userForSaving.setUsername(username);
        userForSaving.setAccessToken(accessToken);
        userForSaving.setFriendsList(friendsList);
        userForSaving.setFriendsSecretKeys(friendsSecretKeys);
        userForSaving.setPrivateKey(privateKey);
        userForSaving.setPublicKey(publicKey);

        StorageManager.saveUser(userForSaving);

    }

    public static void initializeUserFromMemory() throws noUserInMemoryException {

        UserForSaving userForSaving = StorageManager.getUser();
        if (userForSaving == null)
            throw new noUserInMemoryException();

        username = userForSaving.getUsername();
        accessToken = userForSaving.getAccessToken();
        friendsSecretKeys = userForSaving.getFriendsSecretKeys();
        friendsList = userForSaving.getFriendsList();
        privateKey = userForSaving.getPrivateKey();
        publicKey = userForSaving.getPublicKey();

        friendsChats = new HashMap<>();

    }

    public static String getPrivateKey() {
        return privateKey;
    }

    public static void setPrivateKey(String privateKey) {
        UserModule.privateKey = privateKey;
    }

    public static String getPublicKey() {
        return publicKey;
    }

    public static void setPublicKey(String publicKey) {
        UserModule.publicKey = publicKey;
    }

    public static class noUserInMemoryException extends Exception {

    }



}
