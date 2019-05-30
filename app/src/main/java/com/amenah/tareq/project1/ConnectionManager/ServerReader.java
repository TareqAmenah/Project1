package com.amenah.tareq.project1.ConnectionManager;

public class ServerReader extends Thread {

    private MyTcpSocket socket;
    
    public ServerReader(MyTcpSocket socket){
        this.socket = socket;
        this.start();
    }
    
    @Override
    public void run() {
        while(true){
        
        }
    
    }
}
