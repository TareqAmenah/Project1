package com.amenah.tareq.project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.amenah.tareq.project1.RetrofitPackage.ApiServece;
import com.amenah.tareq.project1.RetrofitPackage.LoginUserModel;
import com.amenah.tareq.project1.RetrofitPackage.RetrofitServiceManager;
import com.amenah.tareq.project1.RetrofitPackage.StanderResponse;
import com.google.gson.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private EditText mUsername;
    private EditText mPassword;
    FrameLayout mBlur;
    AVLoadingIndicatorView mLoginLoader;
    Button mLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPassword = findViewById(R.id.password);
        mUsername = findViewById(R.id.user_name);
        mLoginLoader = findViewById(R.id.login_loader);
        mBlur = findViewById(R.id.background_blur_login);
        mLogin = findViewById(R.id.sign_in);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    public void login() {

        mBlur.setBackgroundResource(R.drawable.button_background);
        mBlur.setVisibility(View.VISIBLE);
        mLoginLoader.setVisibility(View.VISIBLE);
        mLoginLoader.show();



            //TODO: implement log in method
            final String username = mUsername.getText().toString();
            String password = mPassword.getText().toString();

            LoginUserModel user = new LoginUserModel(username,password);

            ApiServece retrofitManager = RetrofitServiceManager.retrofitManager;

            /* ********************* reference to static will bind the activity from destroy? *********************************************************** */
            //ApiServece service = RetrofitServiceManager.retrofitManager;

            retrofitManager.login(user).enqueue(new Callback<StanderResponse>() {
                @Override
                public void onResponse(Call<StanderResponse> call, Response<StanderResponse> response) {
                    //TODO: implement onResponse method of log in request
                    Log.v("Request response",response.toString());
                    mLoginLoader.hide();
                    //mLoginLoader.setVisibility(View.INVISIBLE);
                    mBlur.setVisibility(View.INVISIBLE);
                    mUsername.setText("");
                    mPassword.setText("");

                    int resultCode = response.code();

                    switch (resultCode){
                        case 404:
                            anAcceptedLogin();
                            break;

                        case 200:
                            boolean responseStatus = response.body().isStatus();
                            if(!responseStatus){
                                anAcceptedLogin();
                                break;
                            }else{
                                JsonObject responseData = (JsonObject) response.body().getData();
                                String token = responseData.get("token").getAsString();
                                acceptedLogin(username,token);
                                break;

                            }

                        default:
                            showToast("Strange case -_-");

                    }

                }

                @Override
                public void onFailure(Call<StanderResponse> call, Throwable t) {
                    //TODO: implement onFailure method of log in request
                    Log.e("***********************",t.toString());
                    showToast("Connection Error!\nPlease retry");

                }
            });


    }



    //check th validation of username and password
//    public boolean validate() {
//        boolean valid = true;
//
//        String email = metUsername.getText().toString();
//        String password = metpassword.getText().toString();
//
//
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            metpassword.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            metpassword.setError(null);
//        }
//
//        return valid;
//    }


    public void createAccount(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void anAcceptedLogin(){
        showToast("Wrong user details!");
    }

    private void acceptedLogin(String username, String token){
        showToast("Welcome " + username + " ^_^");
        Intent goToChatActivity = new Intent(LoginActivity.this,ChatActivity.class);
        goToChatActivity.putExtra("Token",token);
        startActivity(goToChatActivity);
    }

    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

}
