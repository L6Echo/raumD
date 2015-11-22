package com.example.raumdapp;

import java.util.Collection;

public interface RoomListOperations {
    void addRoom(Room room);
    void removeRoom(Room room);
    void updateRoom(Room room);
    Collection<Room> getAllRooms();

}
