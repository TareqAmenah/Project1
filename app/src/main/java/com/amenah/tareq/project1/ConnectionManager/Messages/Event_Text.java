package com.amenah.tareq.project1.ConnectionManager.Messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event_Text extends Message {
    
    private String receiver;
    private String text;
    private String sentDate;
    
    public Event_Text(String receiver, String text) {
        this.type = "Text";
        this.receiver = receiver;
        this.text = text;
    
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        sentDate = dateFormat.format(date);
        
    }
    
    @Override
    public JSONObject getJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("type",type);
            json.put("receiver",receiver);
            json.put("message",text);
            json.put("sentDate",sentDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.v("***********",json.toString());

        return json;
    }
    
    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
