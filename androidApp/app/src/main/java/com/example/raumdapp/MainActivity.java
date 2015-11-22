package com.example.raumdapp;


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements AddRoomDialog.RoomAdder {

    private BleScannerService bleScannerService;
    public boolean mBound;


    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, BleScannerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private RoomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called after the service callback is available.
     */
    private void onServiceConnected() {
        // update available rooms
        adapter = new RoomListAdapter(this, bleScannerService);
        ((ListView)findViewById(R.id.rooms)).setAdapter(adapter);

        // enable action button
        FloatingActionButton actionButton = (FloatingActionButton)findViewById(R.id.add_room);
        actionButton.setOnClickListener(addRoomClickListener);
    }


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BleScannerService.LocalBinder binder = (BleScannerService.LocalBinder) service;
            bleScannerService = binder.getService();
            mBound = true;
            MainActivity.this.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;

            // TODO remove adapter
        }
    };

    private View.OnClickListener addRoomClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog d = AddRoomDialog.show(MainActivity.this, adapter, MainActivity.this);
        }
    };

    /**
     * Callback from AddRoomDialog for when a room is added.
     */
    public void addRoom(Room room) {
        adapter.add(room);
    }

    @Override
    public void onBackPressed() {
        if (mBound) {
            bleScannerService.stopForeground(true);
            bleScannerService.stopSelf();
        }

        super.onBackPressed();
    }
}
