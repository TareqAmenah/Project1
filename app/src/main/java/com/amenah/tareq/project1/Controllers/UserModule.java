package com.amenah.tareq.project1.Controllers;

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
    private static Map<String, List<Message>> friendsChats = new HashMap<>();


    public static List<Message> getChatsOf(String friendName) {
        if (!friendsChats.containsKey(friendName))
            friendsChats.put(friendName, new ArrayList<Message>());

        return friendsChats.get(friendName);

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

        for (String s : friendsList) {
            friendsChats.get(s).clear();
        }
        friendsChats = null;
        friendsList = null;

    }

}
