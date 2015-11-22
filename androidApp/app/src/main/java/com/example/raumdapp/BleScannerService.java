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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


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



    public void addRoom(Room room){

        if(room == null) throw new IllegalStateException();

        EstimoteManager.getRoomMap().put(room.getBeaconId(), room);

        Log.d(LOG_TAG,"Added room "+room.getName());
    }

    public void removeRoom(Room room){

        if(room == null) throw new IllegalStateException();

        EstimoteManager.getRoomMap().remove(room.getBeaconId());


        Log.d(LOG_TAG, "Removed room " + room.getName());

    }

    public void updateRoom(Room room){

        if(room == null) throw new IllegalStateException();

        Room room1 = EstimoteManager.getRoomMap().get(room.getBeaconId());

        if(room1 == null) throw new IllegalStateException();

        room1.setDesiredTemperature(room.getDesiredTemperature());

        Log.d(LOG_TAG,"Updated room "+room.getName());
    }

    public Collection<Room> getAllRooms(){
        return EstimoteManager.getRoomMap().values();
    }


    private void persist(){

        try {
            InternalStorage.writeObject(this,"data", EstimoteManager.getRoomMap());
        } catch (Exception e) {
            Log.e(LOG_TAG, "persisting failed",e);
        }

    }

    private void load(){

        try {

            HashMap<String, Room> map = (HashMap<String, Room>) InternalStorage.readObject(this,"data");

        } catch (IOException e) {

            Log.e(LOG_TAG, "read data failed",e);

        } catch (ClassNotFoundException e) {

            Log.e(LOG_TAG, "read data failed", e);

        }

    }


    public static final class InternalStorage{

        private InternalStorage() {}

        public static void writeObject(Context context, String key, Object object) throws IOException {
            FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        }

        public static Object readObject(Context context, String key) throws IOException,
                ClassNotFoundException {
            FileInputStream fis = context.openFileInput(key);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            return object;
        }
    }
}
