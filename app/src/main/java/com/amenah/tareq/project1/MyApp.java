package com.amenah.tareq.project1;

import com.amenah.tareq.project1.ConnectionManager.MyTcpSocket;

public class MyApp extends android.app.Application {

    private MyTcpSocket socket;

    public MyTcpSocket getSocket() {
        return socket;
    }

    public void setSocketDetiels(String serverName, int portNumber, String token, ChatActivityControler context) {
        socket = new MyTcpSocket(serverName, portNumber, token, context);
        socket.start();
    }


//    @Override
//    public void onTerminate() {
//        socket.closeConnection();
//        Toast.makeText(this, "Socket connection closed!", Toast.LENGTH_LONG).show();
//        super.onTerminate();
//    }
}
