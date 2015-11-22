package com.example.raumdapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

/**
 * Created by mihai on 22.11.15.
 */
public class RoomListAdapter extends ArrayAdapter<Room> {

    public RoomListAdapter(Context context) {
        super(context, R.layout.list_item_room, R.id.room_name);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        Room item = getItem(position);

        AutoCompleteTextView textView = (AutoCompleteTextView)v.findViewById(R.id.room_name);
        textView.setAdapter(new RoomNameAdapter(getContext()));
        textView.setText(item.getName());

        // TODO set icon

        DiscreteSeekBar DiscreteSeekBar = (DiscreteSeekBar)v.findViewById(R.id.room_desired_temperature);
        DiscreteSeekBar.setProgress(item.getDesiredTemperature());

        Button deleteButton = (Button)v.findViewById(R.id.delete_room);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomListAdapter.this.remove(RoomListAdapter.this.getItem(position));
                RoomListAdapter.this.notifyDataSetChanged();
            }
        });

        return v;
    }
}
