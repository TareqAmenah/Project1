package com.amenah.tareq.project1.ConnectionManager.Messages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amenah.tareq.project1.Controllers.UserModule;
import com.amenah.tareq.project1.Encryption.AESUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Event_Image extends Message {


    public Event_Image(String sender, String receiver, String filePath) {
        this.type = "Image";
        this.receiver = receiver;
        this.filePath = filePath;
        this.extension = ".jpg";
        this.sender = sender;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        sentDate = dateFormat.format(date);

    }

    public Event_Image(JSONObject jsonMessage, String filePath) {
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
            json.put("type",type);
            json.put("receiver",receiver);
            json.put("extension",extension);
            json.put("sentDate",sentDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public byte[] getBytes() {

        Bitmap bitMap = getBitmap(filePath, 1);
        byte[] imageAsByteArray = getBytesFromBitmap(bitMap);
        String secretKey = UserModule.getSecretKeyOfFriend(receiver);
        try {
            byte[] encryptedImage = AESUtil.encrypt(imageAsByteArray, secretKey);
            return encryptedImage;
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

    // convert from bitmap to byte array
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
        return stream.toByteArray();
    }


    public Bitmap getBitmap(String path, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(path, options);

    }

}
