package com.amenah.tareq.project1.ConnectionManager;

import android.os.Environment;
import android.util.Log;

import com.amenah.tareq.project1.ChatActivityControler;
import com.amenah.tareq.project1.ConnectionManager.Messages.Event_Authentication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

public class MyTcpSocket extends Thread {
    
    
    private String serverName;
    private int portNumber;
    private static DataInputStream reader;
    private static DataOutputStream writer;
    private static Socket socket;
    private static String token;

    @Override
    public void run() {
        try {

            this.socket = new Socket(serverName,portNumber);
            reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());
            System.out.println("now you are connected to: " + socket.getRemoteSocketAddress());

            new Event_Authentication(token).sendMessage();
            new ServerListener().start();


        } catch (IOException e) {
            System.out.println("Connection error ..!");
            e.printStackTrace();
        }
    }

    private String charsetName = "UTF-8";
    ChatActivityControler chatActivityControler;
    
    public MyTcpSocket(String serverName, int portNumber, String token, ChatActivityControler chatActivityControler) {
        this.serverName = serverName;
        this.chatActivityControler = chatActivityControler;
        this.portNumber = portNumber;
        this.token = token;
        this.start();
        
    }
    
    public static void sendBinaryFile(byte[] file){

        try {
            writer.write(intToByteArray(file.length));
            writer.write(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void sendJson(JSONObject json){
        
        //TODO send json message
        sendString(json.toString());
        System.out.println("Json sent successfully ");
    
    }
    
    
    public static void sendString(String s){
        try {
            writer.write(intToByteArray(s.length()));
            writer.write(stringToByteArray(s,s.length()));
            System.out.println("String sent successfully ");
        } catch (IOException e) {
            System.err.println("Error while sending String message ... !");
            e.printStackTrace();
        }
    }


    class ServerListener extends Thread{
        boolean connected = socket.isConnected();
        @Override
        public void run() {
            System.out.println("Listening to server ....");
            while(connected){
                System.out.println(socket.isConnected());
                try {
                    Thread.sleep(2000);
                    byte[] l = new byte[4];
                    reader.read(l,0,4);

                    int messageLength = 0;
                    Thread.sleep(1000);
                    messageLength = byteArrayToInt(l);

                    System.out.println("Message received size =  " + messageLength);
                    if(messageLength>0){
                        System.out.println("Message received size =  " + messageLength);
                        byte[] message = new byte[messageLength];
                        reader.read(message,0,messageLength);
                        String messageAsString = new String(message, charsetName);
                        System.out.println("Message received: " + messageAsString);

                        try {
                            JSONObject jsonMessage = new JSONObject(messageAsString);

                            switch (jsonMessage.getString("type")){

                                case "Text":
                                    chatActivityControler.addTextMessageTolayout("server",jsonMessage.getString("message"));
                                    break;

                                case "BinaryFile":
                                    byte[] imageSizeBytes = new byte[4];
                                    reader.read(imageSizeBytes,0,4);
                                    int imageSizeInt = byteArrayToInt(imageSizeBytes);
                                    Log.v("************","received image size: " + imageSizeInt);
                                    byte[] imageBytes = new byte[imageSizeInt];
                                    reader.read(imageBytes,0,imageSizeInt);

                                    String fileName = "from"+jsonMessage.getString("sender") + jsonMessage.getString("extension");
                                    String filePath = Environment.getExternalStorageDirectory() + File.separator + fileName;
                                    File file = new File(filePath);

                                    OutputStream outputStream = new FileOutputStream(file);
                                    outputStream.write(imageBytes);

                                    outputStream.close();

                                    chatActivityControler.addImageMessageToLayout(jsonMessage.getString("sender"),filePath);

                                    break;

                                default:


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("*****************","conn't parsing the jcon");
                        }



                    }else{
                        Thread.sleep(2000);
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                    connected = false;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }
            System.out.println("<<<<< Socket disconnected ... ! >>>>>");
    
        }
    }
    
    
    public static byte[] stringToByteArray(String str, int numBytes) {
        byte[] stringBytes = null;
        try {
            stringBytes = str.getBytes(Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return stringBytes;
        }
        byte [] bytes = new byte[numBytes];
        
        int len = stringBytes.length;
        if (len > numBytes) {
            len = numBytes;
        }
        
        for (int i=0;i<len;i++) {
            bytes[i] = stringBytes[i];
        }
        
        return bytes;
    }
    
    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) (a & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 24) & 0xFF)
        };
    }
    
    public static int byteArrayToInt(byte[] b) {
        return   b[0] & 0xFF |
                (b[1] & 0xFF) << 8 |
                (b[2] & 0xFF) << 16 |
                (b[3] & 0xFF) << 24;
    }
}
