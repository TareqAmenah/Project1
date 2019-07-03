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

import com.amenah.tareq.project1.ConnectionManager.Constants;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.ApiServece;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.LoginUserModel;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.RetrofitServiceManager;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.SetPublicKeyModel;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.StanderResponse;
import com.amenah.tareq.project1.Controllers.UserModule;
import com.amenah.tareq.project1.Encryption.RSAKeysGenerer;
import com.amenah.tareq.project1.Encryption.RSAUtil;
import com.google.gson.JsonElement;
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


        try {
            UserModule.initializeUserFromMemory();
            acceptedLogin();
        } catch (UserModule.noUserInMemoryException e) {
            showToast("No user in memory!");
        }

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



            final String username = mUsername.getText().toString();
            String password = mPassword.getText().toString();

            LoginUserModel user = new LoginUserModel(username,password);

        final ApiServece retrofitManager = RetrofitServiceManager.retrofitManager;

            /* ********************* reference to static will bind the activity from destroy? *********************************************************** */
        //ApiService service = RetrofitServiceManager.retrofitManager;

            retrofitManager.login(user).enqueue(new Callback<StanderResponse>() {
                @Override
                public void onResponse(Call<StanderResponse> call, Response<StanderResponse> response) {
                    Log.v("Request response",response.toString());
                    mLoginLoader.hide();
                    mBlur.setVisibility(View.INVISIBLE);

                    if (response.body().getStatus()) {

                        JsonElement json = response.body().getData();
                        String token = json.getAsString();
                        UserModule.initializeUser(username, token);

                        //Generate and set keys for RSA
                        RSAKeysGenerer rsaKeysGenerer = new RSAKeysGenerer(2048);
                        UserModule.setPrivateKey(rsaKeysGenerer.getPrivateKey());
                        UserModule.setPublicKey(rsaKeysGenerer.getPublicKey());
                        Log.v("*****************", UserModule.getPrivateKey());
                        Log.v("*****************", UserModule.getPublicKey());

                        String xmlPublicKey = RSAUtil.RSAPublicKeyToXML(UserModule.getPublicKey());
                        Log.v("*****************", xmlPublicKey);
                        SetPublicKeyModel setPublicKeyModel = new SetPublicKeyModel(UserModule.getUsername(), xmlPublicKey);

                        retrofitManager.setPublicKey(setPublicKeyModel).enqueue(new Callback<StanderResponse>() {
                            @Override
                            public void onResponse(Call<StanderResponse> call, Response<StanderResponse> response) {
                                if (response.body().getStatus()) {
                                    showToast("Setting public key done!");
                                    acceptedLogin();
                                } else
                                    showToast("Error in setting public key!");
                            }

                            @Override
                            public void onFailure(Call<StanderResponse> call, Throwable t) {
                                showToast("Connection Error!");
                            }
                        });

                    } else {
                        showToast(response.body().getErrors().toString());
                    }
                }

                @Override
                public void onFailure(Call<StanderResponse> call, Throwable t) {
                    Log.e("***********************",t.toString());
                    showToast("Connection Error!\nPlease retry");
                    mLoginLoader.hide();
                    mBlur.setVisibility(View.INVISIBLE);

                }
            });

    }

    public void createAccount(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void anAcceptedLogin(){
        showToast("Wrong user details!");
    }

    private void acceptedLogin() {

        String IPAddress = Constants.IPAddress;
        int portNumber = Constants.socketPortNumber;

        MyApp myApp = (MyApp) getApplication();
        myApp.setSocketDetiels(IPAddress, portNumber);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //TODO test this condition
        if (myApp.getSocket().isConnected()) {

            showToast("Welcome " + UserModule.getUsername() + " ^_^");
            Intent goToChatActivity = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(goToChatActivity);
        } else
            showToast("Socket connection field!");

    }

    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    public void toSettings(View view) {
        Intent goToSettings = new Intent(this, ConnectionSettingsActivity.class);
        startActivity(goToSettings);
    }


}

