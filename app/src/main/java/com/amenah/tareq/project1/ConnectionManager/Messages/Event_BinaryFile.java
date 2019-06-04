package com.amenah.tareq.project1.ConnectionManager.Messages;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event_BinaryFile extends Message {

    File binaryFile;
    private String receiver;
    private String filePath;
    private String extension;
    private String sentDate;

    public Event_BinaryFile(String receiver, String filePath) {
        this.type = "BinaryFile";
        this.receiver = receiver;
        this.filePath = filePath;
        binaryFile = new File(filePath);
        this.extension = getFileExtension(binaryFile);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        sentDate = dateFormat.format(date);

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


    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
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
