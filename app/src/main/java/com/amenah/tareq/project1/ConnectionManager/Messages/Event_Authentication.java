package com.amenah.tareq.project1.ConnectionManager.Messages;

import com.amenah.tareq.project1.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Event_Authentication extends Message {


    public Event_Authentication() {
        this.type = "authentication";
    }
    
    @Override
    public JSONObject getJson() {
        JSONObject json = new JSONObject();

        try {
            json.put("type",type);
            json.put("AccessToken", User.getAccessToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
    
    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
