package com.amenah.tareq.project1.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amenah.tareq.project1.FriendsListRecyclerView.Friend;
import com.amenah.tareq.project1.FriendsListRecyclerView.FriendsListAdapter;
import com.amenah.tareq.project1.R;

import java.util.ArrayList;
import java.util.List;


public class FriendsListFragment extends Fragment {

    private View v;
    private RecyclerView myRecyclerView;
    private List<Friend> friendsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_friends_list, container, false);
        myRecyclerView = v.findViewById(R.id.friend_list_recyclerview);
        FriendsListAdapter friendsListAdapter = new FriendsListAdapter(getActivity(), friendsList);
        myRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        myRecyclerView.setAdapter(friendsListAdapter);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friendsList = new ArrayList<>();
        friendsList.add(new Friend("Ahmad", "url"));
        friendsList.add(new Friend("Alaa", "url"));
        friendsList.add(new Friend("Sami", "url"));
        friendsList.add(new Friend("Amjad", "url"));
        friendsList.add(new Friend("Nader", "url"));
        friendsList.add(new Friend("Mohammad", "url"));
        friendsList.add(new Friend("Khales", "url"));
        friendsList.add(new Friend("Omar", "url"));
        friendsList.add(new Friend("Mouaz", "url"));
        friendsList.add(new Friend("Ammar", "url"));
        friendsList.add(new Friend("Nour", "url"));

    }
}
