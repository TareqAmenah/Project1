package com.amenah.tareq.project1.ConnectionManager;

import android.util.Log;

import com.amenah.tareq.project1.ChatActivityControler;
import com.amenah.tareq.project1.ConnectionManager.Messages.Event_Authentication;
import com.amenah.tareq.project1.ConnectionManager.Messages.Event_BinaryFile;
import com.amenah.tareq.project1.ConnectionManager.Messages.Event_Image;
import com.amenah.tareq.project1.ConnectionManager.Messages.Event_Text;
import com.amenah.tareq.project1.Controllers.StorageManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class MyTcpSocket extends Thread {
    
    
    private String serverName;
    private int portNumber;
    private static DataInputStream reader;
    private static DataOutputStream writer;
    private static Socket socket;

    ChatActivityControler chatActivityControler;
    private String charsetName = "UTF-8";

    public MyTcpSocket(String serverName, int portNumber, ChatActivityControler chatActivityControler) {
        this.serverName = serverName;
        this.chatActivityControler = chatActivityControler;
        this.portNumber = portNumber;

    }

    public static void sendBinaryFile(byte[] file) throws SendInSocketException {

        try {
            writer.write(intToByteArray(file.length));
            writer.write(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SendInSocketException();
        }

    }

    public static void sendJson(JSONObject json) throws SendInSocketException {

        try {
            sendString(json.toString());
        } catch (SocketException e) {
            e.printStackTrace();
            throw new SendInSocketException();
        }
        System.out.println("Json sent successfully ");

    }

    public static void sendString(String s) throws SocketException {
        try {
            byte[] b = stringToByteArray(s, s.length());
            writer.write(intToByteArray(b.length));
            writer.write(b);
            System.out.println("String sent successfully ");
        } catch (IOException e) {
            System.err.println("Error while sending String message ... !");
            e.printStackTrace();
            throw new SocketException();
        }
    }

    @Override
    public void run() {
        try {

            socket = new Socket(serverName, portNumber);
            reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());
            System.out.println("now you are connected to: " + socket.getRemoteSocketAddress());

            new Event_Authentication().sendMessage();
            new ServerListener().start();


        } catch (IOException e) {
            System.out.println("Connection error ..!");
            e.printStackTrace();
        }
    }

    public static byte[] stringToByteArray(String str, int numBytes) {
        byte[] stringBytes = null;
        int i = numBytes;
        try {
            stringBytes = str.getBytes(StandardCharsets.UTF_8);
            Log.v("*****************", new String(stringBytes, StandardCharsets.UTF_8));
            return stringBytes;

        } catch (Exception e) {
            e.printStackTrace();
            return stringBytes;
        }
//        byte [] bytes = new byte[numBytes];
//
//        int len = stringBytes.length;
//        if (len > numBytes) {
//            len = numBytes;
//        }
//
//        for (int i=0;i<len;i++) {
//            bytes[i] = stringBytes[i];
//        }
//
//        Log.v("*****************", new String (bytes, StandardCharsets.UTF_8));
//
//        return bytes;
    }

    public void closeConnection() {
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v("**************", "Socket connection closed");
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) (a & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 24) & 0xFF)
        };
    }

    public static int byteArrayToInt(byte[] b) {
        return b[0] & 0xFF |
                (b[1] & 0xFF) << 8 |
                (b[2] & 0xFF) << 16 |
                (b[3] & 0xFF) << 24;
    }

    class ServerListener extends Thread{
        @Override
        public void run() {
            boolean connected = socket.isConnected();
            System.out.println("Listening to server ....");
            while(connected){
                System.out.println(socket.isConnected());
                try {
                    byte[] l = new byte[4];
                    reader.read(l,0,4);

                    int messageLength = 0;
                    messageLength = byteArrayToInt(l);
                    Thread.sleep(500);

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
                                    chatActivityControler.addTextMessageToLayout(new Event_Text(jsonMessage));
                                    break;

                                case "Image":
                                    byte[] imageSizeBytes = new byte[4];
                                    reader.read(imageSizeBytes,0,4);
                                    int imageSizeInt = byteArrayToInt(imageSizeBytes);
                                    Log.v("************","received image size: " + imageSizeInt);
                                    byte[] imageBytes = new byte[imageSizeInt];
                                    reader.read(imageBytes,0,imageSizeInt);

                                    String imagePath = StorageManager.saveImage(imageBytes,
                                            jsonMessage.getString("sender"), jsonMessage.getString("extension"));

                                    chatActivityControler.addImageMessageToLayout(new Event_Image(jsonMessage, imagePath));

                                    break;


                                case "BinaryFile":
                                    byte[] fileSizeBytes = new byte[4];
                                    reader.read(fileSizeBytes, 0, 4);
                                    int fileSizeInt = byteArrayToInt(fileSizeBytes);
                                    Log.v("************", "received image size: " + fileSizeInt);
                                    byte[] fileBytes = new byte[fileSizeInt];
                                    reader.read(fileBytes, 0, fileSizeInt);

                                    String filePath = StorageManager.saveBinaryFile(fileBytes,
                                            jsonMessage.getString("sender"), jsonMessage.getString("extension"));

                                    chatActivityControler.addBinaryFileMessageToLayout(new Event_BinaryFile(jsonMessage, filePath));

                                    break;

                                default:

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("*****************", "can't parsing the json");
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

}
