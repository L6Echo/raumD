package com.example.raumdapp;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.eddystone.Eddystone;

public class EstimoteManager {

    private static final String LOG_TAG = EstimoteManager.class.getSimpleName();

    private static final int NOTIFICATION_ID = 123;
    private static BeaconManager beaconManager;
    private static NotificationManager notificationManager;
    public static final String EXTRAS_BEACON = "extrasBeacon";
    //private static final String ESTIMOTE_PROXIMITY_UUID = "e2c56db5-dffb-48d2-b060-d0f5a71096e0";
    private static final String ESTIMOTE_PROXIMITY_UUID = "00000000-0000-edd1-ebea-c04e5defa017";
    //private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", UUID.fromString(ESTIMOTE_PROXIMITY_UUID), null, null);
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", null, null, null);


    private static String scanId;

    private static Context currentContext;

    // Create everything we need to monitor the beacons
    public static void Create(NotificationManager notificationMngr,
                              Context context, final Intent i) {
        try {
            notificationManager = notificationMngr;
            currentContext = context;

            // Create a beacon manager
            beaconManager = new BeaconManager(currentContext);

            // We want the beacons heartbeat to be set at one second.
            beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1),
                    0);

            beaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
                @Override
                public void onEddystonesFound(List<Eddystone> list) {
                    Log.d(LOG_TAG,"eddystone");
                }
            });

            // Connect to the beacon manager...
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    try {
                        // ... and start the monitoring
                        scanId = beaconManager.startEddystoneScanning();
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    // Pops a notification in the task bar
    public static void postNotificationIntent(String title, String msg, Intent i) {
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(
                currentContext, 0, new Intent[] { i },
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(currentContext)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setContentText(msg).setAutoCancel(true)
                .setContentIntent(pendingIntent).build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    // Stop beacons monitoring, and closes the service
    public static void stop() {
        try {
            beaconManager.stopEddystoneScanning(scanId);
            beaconManager.disconnect();
        } catch (Exception e) {
        }
    }
}