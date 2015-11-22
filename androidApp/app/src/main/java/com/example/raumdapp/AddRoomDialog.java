package com.example.raumdapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.w3c.dom.Text;

/**
 * Created by mihai on 22.11.15.
 */
public class AddRoomDialog {
    public static AlertDialog show(final Context context, final RoomListAdapter adapter, final RoomAdder adder) {

        final Room nextTestRoom = fetchNextTestRoom(adapter);

        final View v = LayoutInflater.from(context).inflate(R.layout.view_add_room, null);

        AutoCompleteTextView textView = (AutoCompleteTextView)v.findViewById(R.id.room_name);
        //textView.setAdapter(new RoomNameAdapter(context));
        textView.setText(nextTestRoom.getName());

        DiscreteSeekBar s = (DiscreteSeekBar)v.findViewById(R.id.room_desired_temperature);
        s.setProgress(nextTestRoom.getDesiredTemperature());

        v.findViewById(R.id.delete_room).setVisibility(View.GONE);

        AlertDialog ret = new AlertDialog.Builder(context)
                .setTitle(R.string.add_room)
                .setView(v)
                .setPositiveButton(R.string.button_add_room, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = ((TextView)v.findViewById(R.id.room_name)).getText().toString();
                        int desiredTemperature = ((DiscreteSeekBar)v.findViewById(R.id.room_desired_temperature)).getProgress();

                        adder.addRoom(new Room(name, nextTestRoom.getIcon(),
                                nextTestRoom.getBeaconId(), desiredTemperature));
                    }
                }).create();

        ret.setCanceledOnTouchOutside(false);

        ret.show();
        return ret;
    }

    private static Room[] testBeaconRooms = new Room[] {
            new Room("Kitchen", "", "c793e20c44d4",20),
            new Room("Bathroom", "", "f7589002245c", 30),
            new Room("Living Room", "", "e5b554af496d", 25)
    };

    public static Room fetchNextTestRoom(RoomListAdapter adapter) {
        return testBeaconRooms[(adapter.getCount()+1) % testBeaconRooms.length];
    }

    public static interface RoomAdder {
        public void addRoom(Room room);
    }
}
