package com.amenah.tareq.project1.FriendsListRecyclerView;

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
import android.widget.Toast;

import com.amenah.tareq.project1.ChatActivity;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.AddFriendModel;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.ApiServece;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.RetrofitServiceManager;
import com.amenah.tareq.project1.ConnectionManager.RetrofitPackage.StanderResponse;
import com.amenah.tareq.project1.Controllers.UserModule;
import com.amenah.tareq.project1.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.MyHolder> {


    private Context context;
    private List<ItemUser> friendsList;

    public FriendsListAdapter(Context context, List<ItemUser> friendsList) {
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
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {

        final String username = friendsList.get(i).getUsername();
        myHolder.tv_username.setText(username);
        myHolder.img.setImageResource(R.drawable.man);

        //when user search and get not friend users and add them
        if (UserModule.getFriendsList() != null && !UserModule.getFriendsList().contains(username)) {
            myHolder.backgroundColor.setBackgroundResource(R.color.colorSecondary);
            myHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddFriendModel addFriendModel = new AddFriendModel(UserModule.getUsername(), username);
                    ApiServece retrofitManager = RetrofitServiceManager.retrofitManager;
                    retrofitManager.addFriend(addFriendModel).enqueue(new Callback<StanderResponse>() {
                        @Override
                        public void onResponse(Call<StanderResponse> call, Response<StanderResponse> response) {
                            if (response.body().getStatus()) {
                                Intent intent = new Intent(context, ChatActivity.class);
                                intent.putExtra("ReceiverName", username);
                                context.startActivity(intent);
                            } else {
                                showToast(response.body().getErrors().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<StanderResponse> call, Throwable t) {
                        }
                    });
                }
            });
        } else {
            myHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("ReceiverName", friendsList.get(i).getUsername());
                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (friendsList != null)
            return friendsList.size();
        else
            return 0;

    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView tv_username;
        public ImageView img;
        public ConstraintLayout layout;
        public View backgroundColor;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.item_friend);
            tv_username = itemView.findViewById(R.id.friend_list_item_username);
            img = itemView.findViewById(R.id.friend_list_item_image);
            backgroundColor = itemView.findViewById(R.id.background_color);

        }
    }

}
