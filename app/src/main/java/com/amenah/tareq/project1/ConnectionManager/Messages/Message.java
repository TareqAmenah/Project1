package com.amenah.tareq.project1.ConnectionManager.Messages;

import com.amenah.tareq.project1.ConnectionManager.MyTcpSocket;

import org.json.JSONObject;

public abstract class Message {
    
    //region property
    protected String type;
    protected String accessToken;
    private int id;
    public abstract JSONObject getJson();
    public abstract byte[] getBytes();
    //endregion
    
    public void sendMessage(){
    
        System.out.println("Sending message ....");
        
        //Send Binary file
        if(this.type == "Image"){
            //TODO send Binary files
            MyTcpSocket.sendJson(this.getJson());
            MyTcpSocket.sendBinaryFile(this.getBytes());


        }else{
            System.out.println("String message: " + this.getJson());
            MyTcpSocket.sendJson(this.getJson());
        }


    }


}
