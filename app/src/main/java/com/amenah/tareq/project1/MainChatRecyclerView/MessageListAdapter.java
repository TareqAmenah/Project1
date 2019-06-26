package com.amenah.tareq.project1.MainChatRecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amenah.tareq.project1.ConnectionManager.Messages.Message;
import com.amenah.tareq.project1.Controllers.FileOpen;
import com.amenah.tareq.project1.Controllers.UserModule;
import com.amenah.tareq.project1.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {


    private final int TYPE_ME = 0;
    private final int TYPE_THEME = 0;
    private Context context;
    private List<Message> messageList;

    public MessageListAdapter(List<Message> chatsOf, Context context) {
        this.context = context;
        messageList = chatsOf;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v;
        if (i == TYPE_ME)
            v = LayoutInflater.from(context).inflate(R.layout.my_message, viewGroup, false);
        else
            v = LayoutInflater.from(context).inflate(R.layout.their_message, viewGroup, false);

        MyViewHolder myViewHolder = new MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        switch (messageList.get(i).getType()) {
            case "Text":
                myViewHolder.text.setText(messageList.get(i).getText());
                myViewHolder.img.setVisibility(View.INVISIBLE);
                break;

            case "Image":
                myViewHolder.text.setVisibility(View.GONE);
                myViewHolder.img.getLayoutParams().height = 256;
                myViewHolder.img.getLayoutParams().width = 256;

                Glide.with(context)
                        .load(messageList.get(i).getFilePath())
                        .apply(new RequestOptions().centerCrop())
                        .into(myViewHolder.img);

                myViewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File myFile = new File(messageList.get(i).getFilePath());
                        try {
                            FileOpen.openFile(context, myFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Can't open this file!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            case "BinaryFile":
                myViewHolder.text.setVisibility(View.GONE);
                myViewHolder.img.getLayoutParams().height = 128;
                myViewHolder.img.getLayoutParams().width = 128;

                Glide.with(context)
                        .load(R.drawable.ic_any_file)
                        .apply(new RequestOptions().centerCrop())
                        .into(myViewHolder.img);

                myViewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File myFile = new File(messageList.get(i).getFilePath());
                        try {
                            FileOpen.openFile(context, myFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Can't open this file!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }


    }

    @Override
    public int getItemCount() {
        if (messageList == null)
            return 0;
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSender().equals(UserModule.getUsername())) {
            return TYPE_ME;
        } else
            return TYPE_THEME;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public ImageView img;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.message_body);
            img = itemView.findViewById(R.id.message_image);

        }
    }
}
