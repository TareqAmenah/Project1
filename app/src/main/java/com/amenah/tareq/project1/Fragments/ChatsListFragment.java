package com.amenah.tareq.project1.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amenah.tareq.project1.ChatsListRecyclerView.ChatsListAdapter;
import com.amenah.tareq.project1.ChatsListRecyclerView.ItemChat;
import com.amenah.tareq.project1.R;

import java.util.ArrayList;
import java.util.List;


public class ChatsListFragment extends Fragment {

    View v;
    private List<ItemChat> chatsList;
    private RecyclerView myRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_chats_list, container, false);
        myRecyclerView = v.findViewById(R.id.chat_list_recyclerview);
        ChatsListAdapter chatsListAdapter = new ChatsListAdapter(getContext(), chatsList);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(chatsListAdapter);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatsList = new ArrayList<>();
        chatsList.add(new ItemChat("Alaa", "url", "Hello Alaa!"));
        chatsList.add(new ItemChat("Sami", "url", "Hello Sami!"));
        chatsList.add(new ItemChat("Amjad", "url", "Hello Amjad!"));
        chatsList.add(new ItemChat("Ahmad", "url", "Hello Ahmad!"));
        chatsList.add(new ItemChat("Nader", "url", "Hello Nader!"));
        chatsList.add(new ItemChat("Khaled", "url", "Hello Khaled!"));
        chatsList.add(new ItemChat("Mahmood", "url", "Hello Mahmood!"));
        chatsList.add(new ItemChat("Noor", "url", "Hello Noor!"));
        chatsList.add(new ItemChat("Omar", "url", "Hello Omar!"));
        chatsList.add(new ItemChat("Salah", "url", "Hello Salah!"));


    }
}
