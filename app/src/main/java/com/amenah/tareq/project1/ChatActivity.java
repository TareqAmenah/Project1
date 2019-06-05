package com.amenah.tareq.project1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amenah.tareq.project1.ConnectionManager.Constants;
import com.amenah.tareq.project1.ConnectionManager.Messages.Event_BinaryFile;
import com.amenah.tareq.project1.ConnectionManager.Messages.Event_Image;
import com.amenah.tareq.project1.ConnectionManager.Messages.Event_Text;
import com.amenah.tareq.project1.ConnectionManager.MyTcpSocket;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.IOException;


public class ChatActivity extends AppCompatActivity implements ChatActivityControler {

    public static final int PERMISSIONS_REQUEST_CODE = 12;
    public static final int FILE_PICKER_REQUEST_CODE = 11;
    public static final int IMAGE_PICKER_REQUEST_CODE = 10;


    MyTcpSocket socket;
    ImageButton mbSendText;
    EditText metMessage;
    LinearLayout messagesLayout;

    String receiverName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setBackgroundDrawableResource(R.mipmap.chat_background);

        mbSendText = findViewById(R.id.btn_send_text);
        metMessage = findViewById(R.id.input_message);
        messagesLayout = findViewById(R.id.massagesLayout);


        String token = getIntent().getStringExtra("Token");
        receiverName = getIntent().getStringExtra("ReceiverName");
        String IPAddress = Constants.IPAddress;
        int portNumber = Constants.socketPortNumber;

        getSupportActionBar().setTitle(receiverName);
        getSupportActionBar().setIcon(R.drawable.ic_person);


        MyApp myApp = (MyApp) getApplication();
        myApp.setSocketDetiels(IPAddress, portNumber, token, this);

        socket = myApp.getSocket();
//        socket = new MyTcpSocket(IPAddress, portNumber, token, this);
//        socket.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_get_image:
                getImageFromDevice();
                return true;
            case R.id.action_get_file:
                getFileFromDevice();
                return true;
            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendTextMessage(View view) {


        String s = metMessage.getText().toString();
        if (s != "") {
            new Event_Text(receiverName, s).sendMessage();
        }

        metMessage.setText("");
        addTextMessageToLayout("me", s);
    }

    @Override
    public void addTextMessageToLayout(String sender, String message) {

        LayoutInflater messageInflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        final View messageBubble;

        if (sender == "me") {

            messageBubble = messageInflater.inflate(R.layout.my_message, null);
            TextView messageText = messageBubble.findViewById(R.id.message_body);
            messageText.setText(message);


        } else {

            messageBubble = messageInflater.inflate(R.layout.their_message, null);
            TextView messageText = messageBubble.findViewById(R.id.message_body);
            messageText.setText(message);


        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesLayout.addView(messageBubble);
            }
        });


    }

    @Override
    public void addImageMessageToLayout(String sender, final String filePath) {

        Bitmap imageBitmap = BitmapFactory.decodeFile(filePath);

        LayoutInflater messageInflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View messageBubble;

        if (sender == "me") {
            messageBubble = messageInflater.inflate(R.layout.my_message, null);
            TextView messageText = messageBubble.findViewById(R.id.message_body);
            messageText.setText("This is image:");
            ImageView image = messageBubble.findViewById(R.id.message_image);
            image.getLayoutParams().height = 256;
            image.getLayoutParams().width = 256;
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File myFile = new File(filePath);
                    try {
                        FileOpen.openFile(ChatActivity.this, myFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(ChatActivity.this, "Cann't open this file!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            image.setImageBitmap(imageBitmap);

        } else {
            messageBubble = messageInflater.inflate(R.layout.their_message, null);
            TextView messageText = messageBubble.findViewById(R.id.message_body);
            messageText.setText("This is image:");
            ImageView image = messageBubble.findViewById(R.id.message_image);
            image.getLayoutParams().height = 256;
            image.getLayoutParams().width = 256;
            image.setImageBitmap(imageBitmap);
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesLayout.addView(messageBubble);
            }
        });

    }

    public void getImageFromDevice() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
    }

    public void getFileFromDevice() {

        checkPermissionsAndOpenFilePicker();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //get image from the device
        if (data != null && requestCode == IMAGE_PICKER_REQUEST_CODE) {

            if (resultCode == RESULT_OK) { // select image case
                Uri uri = data.getData();

                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String picturePath = cursor.getString(idx);

                new Event_Image(receiverName, picturePath).sendMessage();

                addImageMessageToLayout("me", picturePath);

            }
        } else if (data != null && requestCode == FILE_PICKER_REQUEST_CODE) { // select binary file case
            if (resultCode == RESULT_OK) {

                String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

                new Event_BinaryFile(receiverName, path).sendMessage();


                //addImageMessageToLayout("me",filePath);

            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker();
                } else {
                    showError();
                }
            }
        }
    }

    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
    }

    private void showError() {
        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }

    private void openFilePicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(true)
                .withTitle("Sample title")
                .start();
    }


    @Override
    protected void onDestroy() {
        socket.closeConnection();
        Toast.makeText(this, "Socket connection closed!", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }


}