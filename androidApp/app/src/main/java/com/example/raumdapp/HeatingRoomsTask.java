package com.example.raumdapp;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by jf on 11/22/15.
 */
public class HeatingRoomsTask extends TimerTask {
    private static Map<String, Long> heatingRoomMap;

    public HeatingRoomsTask () {
        heatingRoomMap = new HashMap<String, Long>();
    }

    public void run() {
        long now = new Date().getTime();

        Iterator<Map.Entry<String,Long>> iterator = heatingRoomMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if(entry.getValue() >= now)
                iterator.remove();
        }
    }

    public static void addRoom(final Room room) {
        heatingRoomMap.put(room.getBeaconId(), new Date().getTime());
    }

    public static boolean contains(final Room room) {
        return heatingRoomMap.get(room.getBeaconId()) != null;
    }
}
