package com.example.raumdapp;


import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class App  extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("Application", "Here We go!!");

        startService(new Intent(this, BleScannerService.class));


    }
}
