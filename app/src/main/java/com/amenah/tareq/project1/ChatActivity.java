package com.amenah.tareq.project1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amenah.tareq.project1.ConnectionManager.Messages.Event_BinaryFile;
import com.amenah.tareq.project1.ConnectionManager.Messages.Event_Image;
import com.amenah.tareq.project1.ConnectionManager.Messages.Event_Text;
import com.amenah.tareq.project1.ConnectionManager.Messages.Message;
import com.amenah.tareq.project1.ConnectionManager.MyTcpSocket;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.ApiServece;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.RetrofitServiceManager;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.StanderResponse;
import com.amenah.tareq.project1.Controllers.SharedPreferencesConroller;
import com.amenah.tareq.project1.Controllers.StorageManager;
import com.amenah.tareq.project1.Controllers.UserModule;
import com.amenah.tareq.project1.MainChatRecyclerView.MessageListAdapter;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity {

    public static final int PERMISSIONS_REQUEST_CODE = 12;
    public static final int FILE_PICKER_REQUEST_CODE = 11;
    public static final int IMAGE_PICKER_REQUEST_CODE = 10;

    public static boolean isActive = false;
    public static String receiverName;
    private static RecyclerView messagesListRecyclerView;
    private static List<Message> messageList;
    private static MessageListAdapter messageListAdapter;

    private MyTcpSocket socket;
    private ImageButton mbSendText;
    private EditText metMessage;


    public static void addMessageToLayout(String friendName) {


        if (isActive && messageList.size() > 0) {

            if (friendName.equals(receiverName)) {

                messageListAdapter.notifyItemInserted(messageList.size() - 1);
                messagesListRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        messagesListRecyclerView.smoothScrollToPosition(messageListAdapter.getItemCount() - 1);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setBackgroundDrawableResource(R.mipmap.chat_background);

        mbSendText = findViewById(R.id.btn_send_text);
        metMessage = findViewById(R.id.input_message);
        messagesListRecyclerView = findViewById(R.id.message_list_recyclerview);

        receiverName = getIntent().getStringExtra("ReceiverName");

        getSupportActionBar().setTitle(receiverName);
        getSupportActionBar().setIcon(R.drawable.man);
        setLastSeen();

        mbSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = metMessage.getText().toString();

                if (!s.equals("")) {
                    Message message = new Event_Text(UserModule.getUsername(), receiverName, s);
                    message.sendMessage();
                    addMessageToLayout(receiverName);

                }

                metMessage.setText("");
            }
        });

        // set the adapter of the recycler view
        messageList = UserModule.getChatsOf(receiverName);
        messageListAdapter = new MessageListAdapter(messageList, this);
        messagesListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesListRecyclerView.setAdapter(messageListAdapter);


        socket = ((MyApp) getApplication()).getSocket();

        isActive = true;

    }

    private void logout() {
        SharedPreferencesConroller.deleteCurrentUser(this);
        StorageManager.deleteAllFriendsChat();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void getImageFromDevice() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
    }

    private void openFilePicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(true)
                .withTitle("Sample title")
                .start();
    }

    private void deleteChatHistory() {
        StorageManager.deleteFriendChat(receiverName);
        clearChatListView();
        UserModule.deleteFriendChat(receiverName);

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

                Message message = new Event_Image(UserModule.getUsername(), receiverName, picturePath);
                message.sendMessage();

                //addImageMessageToLayout(message);

            }
        } else if (data != null && requestCode == FILE_PICKER_REQUEST_CODE) { // select binary file case
            if (resultCode == RESULT_OK) {

                String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

                Message message = new Event_BinaryFile(UserModule.getUsername(), receiverName, path);
                message.sendMessage();

                //addBinaryFileMessageToLayout(message);


            }

        }
    }

    private void checkPermissionsAndOpenImagePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            getImageFromDevice();
        }
    }

    private void clearChatListView() {

        int size = messageList.size();
        messageList.clear();
        messageListAdapter.notifyItemRangeChanged(0, size);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_get_image:
                checkPermissionsAndOpenImagePicker();
                return true;
            case R.id.action_get_file:
                checkPermissionsAndOpenFilePicker();
                return true;
            case R.id.action_delete_chat_history:
                deleteChatHistory();
                break;
            case R.id.action_log_out:
                logout();
            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        StorageManager.saveFriendChat(receiverName);
        StorageManager.saveChatsList();

        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    public void setLastSeen() {

        ApiServece retrofitManager = RetrofitServiceManager.retrofitManager;

        retrofitManager.getLastSeen(receiverName).enqueue(new Callback<StanderResponse>() {
            @Override
            public void onResponse(Call<StanderResponse> call, Response<StanderResponse> response) {
                if (response.body().getStatus()) {
                    getSupportActionBar().setSubtitle(response.body().getData().getAsString());
                } else {
                    showToast(response.body().getErrors().getAsString());
                }
            }

            @Override
            public void onFailure(Call<StanderResponse> call, Throwable t) {
                showToast("Connection Error!");
            }
        });

    }

}