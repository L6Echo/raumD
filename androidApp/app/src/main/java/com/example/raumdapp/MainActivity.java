package com.example.raumdapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.estimote.sdk.EstimoteSDK;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView v = ((ListView)findViewById(R.id.rooms));

        RoomListAdapter adapter = new RoomListAdapter(this);

        adapter.add(new Room("Kitchen", "kitchen", "123", 24));

        v.setAdapter(adapter);
    }
}
