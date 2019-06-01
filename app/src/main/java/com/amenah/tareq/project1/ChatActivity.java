package com.amenah.tareq.project1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amenah.tareq.project1.ConnectionManager.Constants;
import com.amenah.tareq.project1.ConnectionManager.Messages.Event_Image;
import com.amenah.tareq.project1.ConnectionManager.Messages.Event_Text;
import com.amenah.tareq.project1.ConnectionManager.MyTcpSocket;

public class ChatActivity extends AppCompatActivity implements ChatActivityControler {

    MyTcpSocket socket;
    ImageButton mbSendText;
    ImageButton mbSendImage;
    EditText metMessage;
    LinearLayout massegesLayout;
    ImageView mivImageFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getWindow().setBackgroundDrawableResource(R.mipmap.chat_background);

        mbSendText = findViewById(R.id.btn_send_text);
        mbSendImage = findViewById(R.id.btn_send_image);
        metMessage = findViewById(R.id.input_message);
        massegesLayout = findViewById(R.id.massagesLayout);
        //mivImageFromServer = findViewById(R.id.image_from_server);

        String token = getIntent().getStringExtra("Token");
        String IPAddress = Constants.IPAddress;
        int portNumber = Constants.socketPortNumber;
        socket = new MyTcpSocket(IPAddress, portNumber, token, this);

    }


    public void sendTextMessage(View view) {


        String s = metMessage.getText().toString();
        if(s != ""){
            new Event_Text("tareq",s).sendMessage();
        }

        metMessage.setText("");
        addTextMessageTolayout("me", s);
    }



    @Override
    public void addTextMessageTolayout(String sender, String message) {

        LayoutInflater messageInflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        final View messageBubble;

        if(sender == "me"){

            messageBubble = messageInflater.inflate(R.layout.my_message,null);
            TextView messageText = messageBubble.findViewById(R.id.message_body);
            messageText.setText(message);


        }
        else{

            messageBubble = messageInflater.inflate(R.layout.their_message,null);
            TextView messageText = messageBubble.findViewById(R.id.message_body);
            messageText.setText(message);
            TextView senderName = messageBubble.findViewById(R.id.name);
            senderName.setText(sender);


        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                massegesLayout.addView(messageBubble);
            }
        });


    }

    @Override
    public void addImageMessageToLayout(String sender, String filePath) {

        Bitmap imageBitmap = BitmapFactory.decodeFile(filePath);

        LayoutInflater messageInflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View messageBubble;

        if(sender=="me"){
            messageBubble = messageInflater.inflate(R.layout.my_message,null);
            TextView messageText = messageBubble.findViewById(R.id.message_body);
            messageText.setText("This is image:");
            ImageView image = messageBubble.findViewById(R.id.message_image);
            image.getLayoutParams().height = 256;
            image.getLayoutParams().width = 256;
            image.setImageBitmap(imageBitmap);

        }else {
            messageBubble = messageInflater.inflate(R.layout.their_message,null);
            TextView senderName = messageBubble.findViewById(R.id.name);
            senderName.setText(sender);
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
                massegesLayout.addView(messageBubble);
            }
        });

    }


    public void getImageFromDevice(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //get image from the device
        if (data != null && requestCode == 0) {

            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                Uri uri = data.getData();

                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String picturePath = cursor.getString(idx);

                new Event_Image("tareq",picturePath).sendMessage();

                addImageMessageToLayout("me",picturePath);

            }
        }
    }



//    private void selectImage() {
//        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo"))
//                {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent, 1);
//                }
//                else if (options[item].equals("Choose from Gallery"))
//                {
//                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, 2);
//                }
//                else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }


}
