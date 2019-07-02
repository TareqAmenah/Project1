package com.amenah.tareq.project1.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.ApiServece;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.RetrofitServiceManager;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.StanderResponse;
import com.amenah.tareq.project1.Controllers.UserModule;
import com.amenah.tareq.project1.FriendsListRecyclerView.FriendsListAdapter;
import com.amenah.tareq.project1.FriendsListRecyclerView.ItemUser;
import com.amenah.tareq.project1.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FriendsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View v;
    private RecyclerView myRecyclerView;
    private List<ItemUser> friendsList;

    SwipeRefreshLayout swipeLayout;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_friends_list, container, false);
        myRecyclerView = v.findViewById(R.id.friend_list_recyclerview);
        setFriendsListAdapter(friendsList);

        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    public void initializeAdapter() {

        friendsList = new ArrayList<>();

        ApiServece retrofitManager = RetrofitServiceManager.retrofitManager;
        retrofitManager.getFriends(UserModule.getUsername()).enqueue(new Callback<StanderResponse>() {
            @Override
            public void onResponse(Call<StanderResponse> call, Response<StanderResponse> response) {

                if (response.body().getStatus()) {

                    UserModule.clearFriendList();

                    JsonArray jsonArray = (JsonArray) response.body().getData();
                    for (JsonElement jsonElement : jsonArray) {
                        friendsList.add(new ItemUser(jsonElement.getAsString(), "url"));
                        String friendName = jsonElement.getAsString();
                        UserModule.addFriend(friendName);
                    }
                    setFriendsListAdapter(friendsList);

                } else {
                    showToast(response.body().getErrors().toString());
                }

            }

            @Override
            public void onFailure(Call<StanderResponse> call, Throwable t) {

            }
        });
    }


    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void setFriendsListAdapter(List<ItemUser> list) {
        if (list == null || list.size() == 0)
            list = friendsList;
        FriendsListAdapter friendsListAdapter = new FriendsListAdapter(getActivity(), list);
        myRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        myRecyclerView.setAdapter(friendsListAdapter);

    }


    @Override
    public void onRefresh() {
        initializeAdapter();
        swipeLayout.setRefreshing(false);

    }
}
