package com.amenah.tareq.project1.ConnectionManager.Messages;

import com.amenah.tareq.project1.Controllers.UserModule;
import com.amenah.tareq.project1.Encryption.AESUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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

        String secretKey = UserModule.getSecretKeyOfFriend(receiver);
        byte[] fileAsByteArray = readFileToByteArray(filePath);
        try {
            byte[] encryptedFile = AESUtil.encrypt(fileAsByteArray, secretKey);
            return encryptedFile;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return new byte[0];
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
