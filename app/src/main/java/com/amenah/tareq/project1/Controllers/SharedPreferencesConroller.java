package com.amenah.tareq.project1.Controllers;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class SharedPreferencesConroller {

    public static final String MY_USER_PREFERENCES = "MyUserPrefs";
    public static final String KEY_USER_NAME = "MyUserName";
    public static final String KEY_USER_TOKEN = "MyUserToken";
    public static final String KEY_RECEIVER_NAME = "MyReceiverName";
    public static String receiverName;

    public static void saveCurrentUser(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MY_USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(KEY_USER_NAME, UserModule.getUsername());
        editor.putString(KEY_USER_TOKEN, UserModule.getAccessToken());
        editor.commit();

    }

    public static void saveCurrentUser(Context context, String receiverName) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MY_USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(KEY_USER_NAME, UserModule.getUsername());
        editor.putString(KEY_USER_TOKEN, UserModule.getAccessToken());
        editor.putString(KEY_RECEIVER_NAME, receiverName);

        editor.commit();
    }

    public static void deleteCurrentUser(Context context) {
        File f = new File("/data/data/" + context.getApplicationContext().getPackageName() + "/shared_prefs/" + MY_USER_PREFERENCES + ".bak");
        f.delete();

    }

    public static void getCurrentUser(Context context) throws NoCurrentUserExistsException {
        File f = new File("/data/data/" + context.getApplicationContext().getPackageName() + "/shared_prefs/" + MY_USER_PREFERENCES + ".bak");
        if (f.exists()) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(MY_USER_PREFERENCES, Context.MODE_PRIVATE);
            String userName = sharedPreferences.getString(KEY_USER_NAME, null);
            String userToken = sharedPreferences.getString(KEY_USER_TOKEN, null);
            receiverName = sharedPreferences.getString(KEY_RECEIVER_NAME, null);

            UserModule.setAccessToken(userToken);
            UserModule.setUsername(userName);
        } else {
            throw new NoCurrentUserExistsException();
        }
    }

}
