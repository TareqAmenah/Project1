package com.amenah.tareq.project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.ApiServece;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.RetrofitServiceManager;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.StanderResponse;
import com.amenah.tareq.project1.Controllers.StorageManager;
import com.amenah.tareq.project1.Controllers.UserModule;
import com.amenah.tareq.project1.Fragments.ChatsListFragment;
import com.amenah.tareq.project1.Fragments.FriendsListFragment;
import com.amenah.tareq.project1.Fragments.GamesListFragment;
import com.amenah.tareq.project1.FriendsListRecyclerView.ItemUser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {


    final Fragment chatsListFragment = new ChatsListFragment();
    final Fragment friendsListFragment = new FriendsListFragment();
    final Fragment gamesListFragment = new GamesListFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = chatsListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);



        fm.beginTransaction().add(R.id.main_container, gamesListFragment, "3").hide(gamesListFragment).commit();
        fm.beginTransaction().add(R.id.main_container, friendsListFragment, "2").hide(friendsListFragment).commit();
        fm.beginTransaction().add(R.id.main_container, chatsListFragment, "1").commit();

//        ((ChatsListFragment)chatsListFragment).initializeAdapter();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_chats:
                        ((ChatsListFragment) chatsListFragment).initializeAdapter();
                        fm.beginTransaction().hide(active).show(chatsListFragment).commit();
                        active = chatsListFragment;
                        return true;
                    case R.id.navigation_friends:
                        ((FriendsListFragment) friendsListFragment).initializeAdapter();
                        fm.beginTransaction().hide(active).show(friendsListFragment).commit();
                        active = friendsListFragment;
                        return true;
                    case R.id.navigation_games:
                        fm.beginTransaction().hide(active).show(gamesListFragment).commitAllowingStateLoss();
                        active = gamesListFragment;
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        final List<ItemUser> usersList = new ArrayList<>();

                        ApiServece retrofitManager = RetrofitServiceManager.retrofitManager;
                        retrofitManager.searchOnUsers(s).enqueue(new Callback<StanderResponse>() {
                            @Override
                            public void onResponse(Call<StanderResponse> call, Response<StanderResponse> response) {
                                if (response.body().getStatus()) {

                                    Log.v("*****************", response.body().getData().toString());
                                    JsonArray jsonArray = (JsonArray) response.body().getData();
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        JsonObject user = (JsonObject) jsonArray.get(i);
                                        String name = user.get("name").getAsString();
                                        usersList.add(new ItemUser(name, "url"));
                                    }
                                } else {
                                    showToast(response.body().getErrors().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<StanderResponse> call, Throwable t) {
                                showToast("Connection Error");
                            }
                        });
                        ((FriendsListFragment) friendsListFragment).setFriendsListAdapter(usersList);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        if (s.equals(""))
                            s = "%£$%^&35";
                        final List<ItemUser> usersList = new ArrayList<>();


                        ApiServece retrofitManager = RetrofitServiceManager.retrofitManager;
                        retrofitManager.searchOnUsers(s).enqueue(new Callback<StanderResponse>() {
                            @Override
                            public void onResponse(Call<StanderResponse> call, Response<StanderResponse> response) {
                                if (response.body().getStatus()) {

                                    Log.v("*****************", response.body().getData().toString());
                                    JsonArray jsonArray = (JsonArray) response.body().getData();
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        JsonObject user = (JsonObject) jsonArray.get(i);
                                        String name = user.get("name").getAsString();
                                        usersList.add(new ItemUser(name, "url"));
                                    }
                                    ((FriendsListFragment) friendsListFragment).setFriendsListAdapter(usersList);
                                } else {
                                    showToast(response.body().getErrors().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<StanderResponse> call, Throwable t) {
                                showToast("Connection Error");
                            }
                        });

                        return false;
                    }
                }
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log_out:
                StorageManager.clearAll();
                UserModule.clearAll();
                ((MyApp) getApplication()).getSocket().closeConnection();
                Toast.makeText(this, "Socket connection closed!", Toast.LENGTH_LONG).show();

                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                break;
            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }

    private List<ItemUser> getUsersFromServer(String query) {
        final List<ItemUser> usersList = new ArrayList<>();

        ApiServece retrofitManager = RetrofitServiceManager.retrofitManager;
        retrofitManager.searchOnUsers(query).enqueue(new Callback<StanderResponse>() {
            @Override
            public void onResponse(Call<StanderResponse> call, Response<StanderResponse> response) {
                if (response.body().getStatus()) {

                    Log.v("*****************", response.body().getData().toString());
                    JsonArray jsonArray = (JsonArray) response.body().getData();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject user = (JsonObject) jsonArray.get(i);
                        String name = user.get("name").toString();
                        usersList.add(new ItemUser(name, "url"));
                    }
                } else {
                    showToast(response.body().getErrors().toString());
                }
            }

            @Override
            public void onFailure(Call<StanderResponse> call, Throwable t) {
                showToast("Connection Error");
            }
        });

        if (usersList.size() == 0)
            return null;
        return usersList;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserModule.saveUser();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
