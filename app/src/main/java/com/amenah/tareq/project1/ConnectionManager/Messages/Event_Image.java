package com.amenah.tareq.project1.ConnectionManager.Messages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event_Image extends Message {

    private String receiver;
    private String filePath;
    private String extension;
    private String sentDate;


    public Event_Image(String receiver, String filePath) {
        this.type = "Image";
        this.receiver = receiver;
        this.filePath = filePath;
        this.extension = ".jpg";

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
            json.put("extension",extension);
            json.put("sentDate",sentDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public byte[] getBytes() {

        Bitmap bitMap = getBitmap(filePath, 2);
        return getBytesFromBitmap(bitMap);
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
