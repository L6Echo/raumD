package com.example.raumdapp;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by mihai on 22.11.15.
 */
public class RoomNameAdapter extends ArrayAdapter<String> {
    public RoomNameAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1, new String[] {"Living Room", "Bedroom", "Dining Room", "Kitchen", "Bathroom"});
    }
}
