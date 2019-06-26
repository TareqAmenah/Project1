package com.amenah.tareq.project1.ConnectionManager.Messages;

import com.amenah.tareq.project1.ChatActivity;
import com.amenah.tareq.project1.ConnectionManager.MyTcpSocket;
import com.amenah.tareq.project1.ConnectionManager.SendInSocketException;
import com.amenah.tareq.project1.Controllers.UserModule;

import org.json.JSONObject;

import java.io.Serializable;

public abstract class Message implements Serializable {
    
    protected String type;
    protected String receiver;
    protected String sender;
    protected String text;
    protected String extension;
    protected String filePath;
    protected String sentDate;
    private int id;

    public abstract JSONObject getJson();
    public abstract byte[] getBytes();

    public String getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    public String getExtension() {
        return extension;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getSentDate() {
        return sentDate;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public void sendMessage(){

        System.out.println("Sending message ....");
        if (this.type == "authentication") {
            try {
                MyTcpSocket.sendJson(this.getJson());
            } catch (SendInSocketException e) {
                e.printStackTrace();
            }
        }
        //Send Binary file
        else if (this.type == "Image" || this.type == "BinaryFile") {
            try {
                MyTcpSocket.sendJson(this.getJson());
                MyTcpSocket.sendBinaryFile(this.getBytes());
                saveMessage(receiver); // friend name is the receiver name

            } catch (SendInSocketException e) {
                e.printStackTrace();
            }


        }else{
            try {
                MyTcpSocket.sendJson(this.getJson());
                saveMessage(receiver); // friend name is the receiver name
            } catch (SendInSocketException e) {
                e.printStackTrace();
            }
        }

    }

    protected void saveMessage(String friendName) {
        UserModule.addMessage(friendName, this);
        ChatActivity.addMessageToLayout(friendName);

    }


}
