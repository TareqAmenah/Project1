package com.amenah.tareq.project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.amenah.tareq.project1.ConnectionManager.ConnectionSettingsController;


public class ConnectionSettingsActivity extends AppCompatActivity {

    EditText mIp;
    EditText mHttpPort;
    EditText mSocketPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_settings);

        mIp = findViewById(R.id.ip_address);
        mHttpPort = findViewById(R.id.http_port_number);
        mSocketPort = findViewById(R.id.socket_port_number);

    }


    public void done(View view) {
        String ip = mIp.getText().toString();
        int httpPort = Integer.valueOf(mHttpPort.getText().toString());
        int socketPort = Integer.valueOf(mSocketPort.getText().toString());
        ConnectionSettingsController.setConnectionSettings(ip, httpPort, socketPort);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

}
