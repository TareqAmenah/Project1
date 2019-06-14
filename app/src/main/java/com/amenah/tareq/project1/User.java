package com.amenah.tareq.project1;

import com.amenah.tareq.project1.ConnectionManager.Messages.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {

    private static String username;
    private static String accessToken;
    private static List<String> friendsList;
    private static Map<String, List<Message>> friendsChats = new HashMap<>();


    public static List<Message> getChatsOf(String friendName) {
        return friendsChats.get(friendName);
    }

    public static void setChatsOf(String friendName, List<Message> messages) {
        friendsChats.put(friendName, new ArrayList<Message>());
        for (Message message : messages) {
            friendsChats.get(friendName).add(message);
        }
    }

    public static void setFriendsList(List<String> friendsList) {
        User.friendsList = friendsList;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        User.accessToken = accessToken;
    }

    public static void addFriend(String newFriendName) {
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


}
