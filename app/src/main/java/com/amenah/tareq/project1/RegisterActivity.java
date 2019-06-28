package com.amenah.tareq.project1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.ApiServece;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.RetrofitServiceManager;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.SignUpUserModel;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.StanderResponse;
import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    ImageView mivUserImage;

    EditText mUsername;
    EditText mEmail;
    EditText mPassword;
    EditText mRePassword;
    Button mRegisterButton;

    AVLoadingIndicatorView mRegisterLoader;
    FrameLayout mBlur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.user_name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRePassword = findViewById(R.id.re_password);
        mBlur = findViewById(R.id.background_blur_register);
        mRegisterLoader = findViewById(R.id.register_loader);
        mRegisterButton = findViewById(R.id.register_btn);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });



    }



    public void register() {

        mBlur.setBackgroundResource(R.drawable.button_background);
        mRegisterLoader.setVisibility(View.VISIBLE);
        mRegisterLoader.smoothToShow();



        final String username = mUsername.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        SignUpUserModel user = new SignUpUserModel(username,password,email);


        ApiServece service = RetrofitServiceManager.retrofitManager;

        service.signup(user).enqueue(new Callback<StanderResponse>() {
            @Override
            public void onResponse(Call<StanderResponse> call, Response<StanderResponse> response) {
                Log.v("Request response",response.toString());

                mRegisterLoader.smoothToHide();
                mBlur.setVisibility(View.INVISIBLE);

                if (response.body().getStatus()) {

                    String token = response.body().getData().toString();
                    acceptedRegister(username, token);

                } else {
                    showToast(response.body().getErrors().toString());
                }

            }

            @Override
            public void onFailure(Call<StanderResponse> call, Throwable t) {
                Log.e("****************",t.toString());
                showToast("Connection Error!\nPlease retry");
                mRegisterLoader.smoothToHide();
                mBlur.setVisibility(View.INVISIBLE);

            }
        });


        //mRegisterLoader.smoothToHide();
       // mRegisterLoader.setVisibility(View.INVISIBLE);
    }


    public void login(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    //TODO: check the validation of register data
    /*public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String age = _ageText.getText().toString();
        String school = _schoolText.getText().toString();


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (age.isEmpty() || Integer.valueOf(age) > 90 || Integer.valueOf(age) < 10) {
            _ageText.setError("age between 10 and 90");
            valid = false;
        } else {
            _ageText.setError(null);
        }

        if (school.isEmpty()) {
            _schoolText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _schoolText.setError(null);
        }


        return valid;
    }
       */

    public void uploadImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && requestCode == 0) {

            Uri selectedfile = data.getData(); //The uri with the location of the file

            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] byteArray = outputStream.toByteArray();

                //Use your Base64 String as you wish
                String encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);


                //set the image in the layout
                Glide.with(this)
                        .load(selectedfile)
                        .into(mivUserImage);

            }
        }
    }

    private void acceptedRegister(String username, String token){
        showToast("Welcome " + username + " ^_^");
        Intent goToChatActivity = new Intent(RegisterActivity.this, MainActivity.class);
        goToChatActivity.putExtra("Token",token);
        startActivity(goToChatActivity);
    }


    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

}
