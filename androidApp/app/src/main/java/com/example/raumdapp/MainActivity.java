package com.example.raumdapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private BleScannerService bleScannerService;
    public boolean mBound;


    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, BleScannerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView v = ((ListView)findViewById(R.id.rooms));

        RoomListAdapter adapter = new RoomListAdapter(this);

        adapter.add(new Room("Kitchen", "kitchen", "123", 24));

        v.setAdapter(adapter);


    }

    private void onServiceConnected(){

        // After this callback service is available

        bleScannerService.addRoom(new Room("Kitchen","kitchen","123",24));

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
        }
    };

}
