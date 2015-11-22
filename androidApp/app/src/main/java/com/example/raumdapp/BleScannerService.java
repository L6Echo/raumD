package com.example.raumdapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class BleScannerService extends IntentService {


    private static final String LOG_TAG = BleScannerService.class.getSimpleName();

    public class LocalBinder extends Binder {
        BleScannerService getService() {
            return BleScannerService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    public BleScannerService() {
        super("BleService");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            EstimoteManager.Create((NotificationManager) this
                            .getSystemService(Context.NOTIFICATION_SERVICE), this,
                    intent);
        }catch (Exception e){
            Log.e(LOG_TAG,"Exception",e);
        }
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        EstimoteManager.stop();
        super.onDestroy();
        Log.d(LOG_TAG,"destroyed");
    }
}
