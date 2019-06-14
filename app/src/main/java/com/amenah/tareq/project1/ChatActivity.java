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
import com.amenah.tareq.project1.ConnectionManager.Messages.Message;
import com.amenah.tareq.project1.ConnectionManager.MyTcpSocket;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;


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

        getWindow().setBackgroundDrawableResource(R.drawable.gradient_background);

        mbSendText = findViewById(R.id.btn_send_text);
        metMessage = findViewById(R.id.input_message);
        messagesLayout = findViewById(R.id.massagesLayout);


        receiverName = getIntent().getStringExtra("ReceiverName");
        String IPAddress = Constants.IPAddress;
        int portNumber = Constants.socketPortNumber;

        getSupportActionBar().setTitle(receiverName);
        getSupportActionBar().setIcon(R.drawable.ic_person);

        mbSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = metMessage.getText().toString();

                if (s != "") {
                    Message message = new Event_Text(User.getUsername(), receiverName, s);
                    message.sendMessage();
                    addTextMessageToLayout(message);

                }

                metMessage.setText("");
            }
        });


        MyApp myApp = (MyApp) getApplication();
        myApp.setSocketDetiels(IPAddress, portNumber, this);

        socket = myApp.getSocket();

        getChatHistory();

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
            case R.id.action_delete_chat_history:
                deleteChatHistory();
            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendTextMessage(View view) {

        String s = metMessage.getText().toString();

        if (s != "") {
            Message message = new Event_Text(User.getUsername(), receiverName, s);
            message.sendMessage();
            addTextMessageToLayout(message);

        }

        metMessage.setText("");
    }

    @Override
    public void addTextMessageToLayout(Message message) {

        LayoutInflater messageInflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        final View messageBubble;

        if (message.getSender().equals(User.getUsername())) {

            messageBubble = messageInflater.inflate(R.layout.my_message, null);
            TextView messageText = messageBubble.findViewById(R.id.message_body);
            messageText.setText(message.getText());


        } else {

            messageBubble = messageInflater.inflate(R.layout.their_message, null);
            TextView messageText = messageBubble.findViewById(R.id.message_body);
            messageText.setText(message.getText());


        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesLayout.addView(messageBubble);
            }
        });


    }

    @Override
    public void addImageMessageToLayout(final Message message) {

        Bitmap imageBitmap = BitmapFactory.decodeFile(message.getFilePath());

        LayoutInflater messageInflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View messageBubble;

        if (message.getSender().equals(User.getUsername())) {
            messageBubble = messageInflater.inflate(R.layout.my_message, null);
            TextView messageText = messageBubble.findViewById(R.id.message_body);
            messageText.setText("This is image:");
            ImageView image = messageBubble.findViewById(R.id.message_image);
            image.getLayoutParams().height = 256;
            image.getLayoutParams().width = 256;
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File myFile = new File(message.getFilePath());
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
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File myFile = new File(message.getFilePath());
                    try {
                        FileOpen.openFile(ChatActivity.this, myFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(ChatActivity.this, "Cann't open this file!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesLayout.addView(messageBubble);
            }
        });

    }

    @Override
    public void addBinaryFileMessageToLayout(final Message message) {
        LayoutInflater messageInflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View messageBubble;

        if (message.getSender().equals(User.getUsername())) {
            messageBubble = messageInflater.inflate(R.layout.my_message, null);
            TextView messageText = messageBubble.findViewById(R.id.message_body);
            messageText.setText("This is image:");
            ImageView image = messageBubble.findViewById(R.id.message_image);
            image.getLayoutParams().height = 128;
            image.getLayoutParams().width = 128;
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File myFile = new File(message.getFilePath());
                    try {
                        FileOpen.openFile(ChatActivity.this, myFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(ChatActivity.this, "Cann't open this file!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            image.setImageResource(R.drawable.ic_apk_box);

        } else {
            messageBubble = messageInflater.inflate(R.layout.their_message, null);
            TextView messageText = messageBubble.findViewById(R.id.message_body);
            messageText.setText("This is image:");
            ImageView image = messageBubble.findViewById(R.id.message_image);
            image.getLayoutParams().height = 128;
            image.getLayoutParams().width = 128;
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File myFile = new File(message.getFilePath());
                    try {
                        FileOpen.openFile(ChatActivity.this, myFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(ChatActivity.this, "Cann't open this file!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            image.setImageResource(R.drawable.ic_apk_box);
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

                Message message = new Event_Image(User.getUsername(), receiverName, picturePath);
                message.sendMessage();

                addImageMessageToLayout(message);

            }
        } else if (data != null && requestCode == FILE_PICKER_REQUEST_CODE) { // select binary file case
            if (resultCode == RESULT_OK) {

                String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

                Message message = new Event_BinaryFile(User.getUsername(), receiverName, path);
                message.sendMessage();

                addBinaryFileMessageToLayout(message);


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


    private void getChatHistory() {

        List<Message> messages = (List<Message>) StorageManager.getFriendChat(receiverName);
        User.setChatsOf(receiverName, messages);

        for (Message message : messages) {
            switch (message.getType()) {
                case "Text":
                    addTextMessageToLayout(message);
                    break;

                case "Image":
                    addImageMessageToLayout(message);
                    break;

                case "BinaryFile":
                    addBinaryFileMessageToLayout(message);
                    break;
            }
        }
    }


    private void deleteChatHistory() {
        StorageManager.deleteFriendChat(receiverName);
        clearChatListView();
        User.deleteFriendChat(receiverName);

    }

    private void clearChatListView() {
        int count = messagesLayout.getChildCount();
        View v = null;
        for (int i = 0; i < count; i++) {
            v = messagesLayout.getChildAt(i);
            v.setVisibility(View.GONE);
            //do something with your child element
        }
    }

    @Override
    protected void onDestroy() {
        StorageManager.saveFriendChat(receiverName, User.getChatsOf(receiverName));
        socket.closeConnection();
        Toast.makeText(this, "Socket connection closed!", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }


}