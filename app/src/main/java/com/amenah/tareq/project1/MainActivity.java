package com.amenah.tareq.project1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amenah.tareq.project1.Controllers.StorageManager;
import com.amenah.tareq.project1.Controllers.UserModule;
import com.amenah.tareq.project1.Fragments.ChatsListFragment;
import com.amenah.tareq.project1.Fragments.FriendsListFragment;
import com.amenah.tareq.project1.Fragments.GamesListFragment;


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
                        fm.beginTransaction().hide(active).show(friendsListFragment).commit();
                        active = friendsListFragment;
                        return true;
                    case R.id.navigation_games:
                        fm.beginTransaction().hide(active).show(gamesListFragment).commit();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        ((MyApp) getApplication()).getSocket().closeConnection();
        Toast.makeText(this, "Socket connection closed!", Toast.LENGTH_LONG).show();
        StorageManager.clearAll();
        UserModule.clearAll();
        super.onDestroy();
    }


}
