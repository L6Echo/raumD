package com.example.raumdapp;

/**
 * Created by mihai on 22.11.15.
 */
public class Room {
    /**
     * The name of the room.
     */
    private String name;

    /**
     * An icon associated with the room.
     */
    private String icon;

    /**
     * The Bluetooth LE beacon ID.
     */
    private String beaconId;

    /**
     * The user-specified desired temperature, in degrees Celsius
     */
    private int desiredTemperature;


    public Room(String name, String icon, String beaconId, int desiredTemperature) {
        this.name = name;
        this.icon = icon;
        this.beaconId = beaconId;
        this.desiredTemperature = desiredTemperature;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public int getDesiredTemperature() {
        return desiredTemperature;
    }

    public void setDesiredTemperature(int desiredTemperature) {
        this.desiredTemperature = desiredTemperature;
    }
}
