package com.amenah.tareq.project1.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amenah.tareq.project1.FishGameActivity;
import com.amenah.tareq.project1.R;

public class GamesListFragment extends Fragment {

    View v;
    Button runFishGame;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_games_list, container, false);
        runFishGame = v.findViewById(R.id.run_fish_game);
        runFishGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FishGameActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }


}
