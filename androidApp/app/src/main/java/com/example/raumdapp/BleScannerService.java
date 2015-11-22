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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class BleScannerService extends IntentService implements RoomListOperations {


    private static final String LOG_TAG = BleScannerService.class.getSimpleName();
    private static final String STORAGE_FILENAME = "data";

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

        load();
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


    @Override
    public void addRoom(Room room){

        if(room == null) throw new IllegalStateException();

        EstimoteManager.getRoomMap().put(room.getBeaconId(), room);
        persist();

        Log.d(LOG_TAG, "Added room " + room.getName());
    }

    @Override
    public void removeRoom(Room room){

        if(room == null) throw new IllegalStateException();

        EstimoteManager.getRoomMap().remove(room.getBeaconId());
        persist();


        Log.d(LOG_TAG, "Removed room " + room.getName());

    }

    @Override
    public void updateRoom(Room room){

        if(room == null) throw new IllegalStateException();

        Room room1 = EstimoteManager.getRoomMap().get(room.getBeaconId());

        if(room1 == null) throw new IllegalStateException();

        room1.setDesiredTemperature(room.getDesiredTemperature());
        persist();

        Log.d(LOG_TAG,"Updated room "+room.getName());
    }

    public Collection<Room> getAllRooms(){
        return EstimoteManager.getRoomMap().values();
    }


    private void persist(){



        try {
            FileOutputStream fos = openFileOutput(STORAGE_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(EstimoteManager.getRoomMap());
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }

    }

    private void load(){
        Map<String, Room> toReturn;
        FileInputStream fis;
        try {
            fis = openFileInput(STORAGE_FILENAME);
            ObjectInputStream oi = new ObjectInputStream(fis);
            toReturn = (Map<String, Room>) oi.readObject();

            EstimoteManager.getRoomMap().clear();
            EstimoteManager.getRoomMap().putAll(toReturn);

            oi.close();
        } catch (FileNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
    }
}
