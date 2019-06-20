package com.amenah.tareq.project1.FriendsListRecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amenah.tareq.project1.R;

import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.MyHolder> {

    private Context context;
    private List<Friend> friendsList;

    public FriendsListAdapter(Context context, List<Friend> friendsList) {
        this.context = context;
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_friend, viewGroup, false);
        MyHolder myHolder = new MyHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        myHolder.tv_username.setText(friendsList.get(i).getUsername());
        myHolder.img.setImageResource(R.drawable.man);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView tv_username;
        public ImageView img;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            tv_username = itemView.findViewById(R.id.friend_list_item_username);
            img = itemView.findViewById(R.id.friend_list_item_image);

        }
    }
}
