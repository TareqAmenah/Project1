package com.amenah.tareq.project1.ConnectionManager;

import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.RetrofitServiceManager;

public class ConnectionSettingsController {

    public static void setConnectionSettings(String ip, int httpPort, int socketPort) {
        Constants.IPAddress = ip;
        Constants.httpPortNumber = httpPort;
        Constants.socketPortNumber = socketPort;

        RetrofitServiceManager.set("http://" + ip + ":" + httpPort);

    }

}
