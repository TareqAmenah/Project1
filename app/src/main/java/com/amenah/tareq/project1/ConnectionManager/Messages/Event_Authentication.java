package com.amenah.tareq.project1.ConnectionManager.Messages;

import org.json.JSONException;
import org.json.JSONObject;

public class Event_Authentication extends Message {
    
    
    public Event_Authentication(String token) {
        this.type = "authentication";
        //TODO get the Access token of the user
        accessToken = token;
    }
    
    @Override
    public JSONObject getJson() {
        JSONObject json = new JSONObject();

        try {
            json.put("type",type);
            json.put("AccessToken",accessToken);
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
