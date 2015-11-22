package com.example.raumdapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;

/**
 * Created by mihai on 22.11.15.
 */
public class RoomListAdapter extends BaseAdapter {

    private Context context;
    private final LayoutInflater inflater;

    /**
     * Interface to the backing store of the room list.
     */
    protected RoomListOperations roomListOperations;

    public RoomListAdapter(Context context, RoomListOperations roomListOperations) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.roomListOperations = roomListOperations;
    }

    @Override
    public Room getItem(int position) {
        ArrayList<Room> rooms = new ArrayList<>(roomListOperations.getAllRooms());
        return rooms.get(position);
    }

    @Override
    public int getCount() {
        return roomListOperations.getAllRooms().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;

        if (convertView == null)
            v = inflater.inflate(R.layout.list_item_room, parent, false);
        else
            v = convertView;

        Room item = getItem(position);

        AutoCompleteTextView textView = (AutoCompleteTextView)v.findViewById(R.id.room_name);
        textView.setAdapter(new RoomNameAdapter(context));
        textView.setText(item.getName());

        // TODO set icon

        DiscreteSeekBar discreteSeekBar = (DiscreteSeekBar)v.findViewById(R.id.room_desired_temperature);
        discreteSeekBar.setProgress(item.getDesiredTemperature());

        discreteSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                if (fromUser) {
                    Room room  = RoomListAdapter.this.getItem(position);
                    room.setDesiredTemperature(value);
                    roomListOperations.updateRoom(room);
                }
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        Button deleteButton = (Button)v.findViewById(R.id.delete_room);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomListOperations.removeRoom(RoomListAdapter.this.getItem(position));
                notifyDataSetChanged();
            }
        });

        return v;
    }

    public void add(Room room) {
        roomListOperations.addRoom(room);
        notifyDataSetChanged();
    }
}
