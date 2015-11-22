package com.example.raumdapp;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by jf on 11/22/15.
 */
public class HeatingRoomsTask extends TimerTask {
    private static final String LOG_TAG = HeatingRoomsTask.class.getSimpleName();

    private static final long OLDEST_MILLIS = TimeUnit.SECONDS.toMillis(5);

    private Map<String, Long> heatingRoomMap;

    private IOnRemoveRoom callback;

    public HeatingRoomsTask (IOnRemoveRoom callback) {
        heatingRoomMap = new HashMap<String, Long>();
        this.callback = callback;
    }

    public void run() {
        Log.d(LOG_TAG, "run timer task");
        final long oldestTimeStamp = new Date().getTime() - OLDEST_MILLIS;

        Iterator<Map.Entry<String,Long>> iterator = heatingRoomMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if(entry.getValue() <= oldestTimeStamp) {
                final String roomID  = entry.getKey();
                iterator.remove();
                Log.d(LOG_TAG, "remove: " + roomID);
                callback.onRemoveRoom(roomID);
            }
        }
    }

    public void addRoom(final Room room) {
        heatingRoomMap.put(room.getBeaconId(), new Date().getTime());
    }

    public boolean containsRoom(final Room room) {
        return heatingRoomMap.get(room.getBeaconId()) != null;
    }
}
