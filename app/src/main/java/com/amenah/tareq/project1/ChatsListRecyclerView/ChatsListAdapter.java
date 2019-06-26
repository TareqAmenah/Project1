package com.amenah.tareq.project1.ChatsListRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amenah.tareq.project1.ChatActivity;
import com.amenah.tareq.project1.R;

import java.util.List;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.MyViewHolder> {


    private Context context;
    private List<ItemChat> chatsList;

    public ChatsListAdapter(Context context, List<ItemChat> chatsList) {
        this.context = context;
        this.chatsList = chatsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v;
        v = (View) LayoutInflater.from(context).inflate(R.layout.item_chat, viewGroup, false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.tv_username.setText(chatsList.get(i).getUsername());
        myViewHolder.tv_lastMessage.setText(chatsList.get(i).getLastMessage());
        myViewHolder.img.setImageResource(R.drawable.man);
        myViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("ReceiverName", chatsList.get(i).getUsername());
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        if (chatsList == null)
            return 0;
        return chatsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_username;
        private TextView tv_lastMessage;
        private ImageView img;
        public ConstraintLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.item_chat);
            tv_username = itemView.findViewById(R.id.item_chat_user_name);
            tv_lastMessage = itemView.findViewById(R.id.item_chat_last_message);
            img = itemView.findViewById(R.id.item_chat_user_image);

        }
    }

}
