package com.amenah.tareq.project1.ConnectionManager.Messages;

import android.util.Log;

import com.amenah.tareq.project1.Controllers.UserModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event_Text extends Message {

    public Event_Text(String sender, String receiver, String text) {
        this.type = "Text";
        this.receiver = receiver;
        this.text = text;
        this.sender = sender;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        sentDate = dateFormat.format(date);
        
    }

    public Event_Text(JSONObject jsonMessage) {
        try {
            type = jsonMessage.getString("type");
            receiver = UserModule.getUsername();
            text = jsonMessage.getString("message");
            sentDate = jsonMessage.getString("sentDate");
            sender = jsonMessage.getString("sender");

            saveMessage(sender); // friend name is the sender name

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
