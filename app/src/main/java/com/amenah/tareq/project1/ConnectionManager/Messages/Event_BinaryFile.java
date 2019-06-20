package com.amenah.tareq.project1.ConnectionManager.Messages;

import com.amenah.tareq.project1.Controllers.UserModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event_BinaryFile extends Message {


    public Event_BinaryFile(String sender, String receiver, String filePath) {
        this.type = "BinaryFile";
        this.receiver = receiver;
        this.filePath = filePath;
        this.extension = getFileExtension(filePath);
        this.sender = sender;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        sentDate = dateFormat.format(date);

    }

    public Event_BinaryFile(JSONObject jsonMessage, String filePath) {
        try {
            type = jsonMessage.getString("type");
            receiver = UserModule.getUsername();
            extension = jsonMessage.getString("extension");
            sentDate = jsonMessage.getString("sentDate");
            sender = jsonMessage.getString("sender");
            this.filePath = filePath;

            saveMessage(sender); // friend name is the sender name

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject getJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("receiver", receiver);
            json.put("extension", extension);
            json.put("sentDate", sentDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public byte[] getBytes() {

        return readFileToByteArray(filePath);

    }


    private String getFileExtension(String file) {
        int lastIndexOf = file.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return file.substring(lastIndexOf);
    }


    private byte[] readFileToByteArray(String path) {
        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(path, "r");
            byte[] b = new byte[(int) f.length()];
            f.readFully(b);
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

}
