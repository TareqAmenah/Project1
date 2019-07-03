package com.amenah.tareq.project1.ConnectionManager.Messages;

import android.util.Base64;
import android.util.Log;

import com.amenah.tareq.project1.Controllers.UserModule;
import com.amenah.tareq.project1.Encryption.AESUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Event_Text extends Message {

    public Event_Text(String sender, String receiver, String text) {
        this.type = "Text";
        this.receiver = receiver;
        this.text = text;
        this.sender = sender;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        sentDate = dateFormat.format(date);

        String secretKey = UserModule.getSecretKeyOfFriend(receiver);
        try {
            //String encryptedText = Base64.encodeToString(AESUtil.encrypt(text,secretKey),Base64.DEFAULT);
            String encryptedText = Base64.encodeToString(AESUtil.encrypt(text, secretKey), Base64.DEFAULT);
            this.encryptedText = encryptedText;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        saveMessage(receiver); // friend name is the receiver name

    }

    public Event_Text(JSONObject jsonMessage) {
        try {
            type = jsonMessage.getString("type");
            receiver = UserModule.getUsername();
            encryptedText = jsonMessage.getString("message");
            sentDate = jsonMessage.getString("sentDate");
            sender = jsonMessage.getString("sender");

            //TODO: decrypt the message
            text = new String(AESUtil.decrypt(Base64.decode(encryptedText, Base64.DEFAULT), UserModule.getSecretKeyOfFriend(sender)));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        saveMessage(sender); // friend name is the sender name

    }

    @Override
    public JSONObject getJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("type",type);
            json.put("receiver",receiver);
            json.put("message", encryptedText);
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
