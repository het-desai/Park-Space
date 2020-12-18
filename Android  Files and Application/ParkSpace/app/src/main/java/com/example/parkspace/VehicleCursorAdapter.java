package com.example.parkspace;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parkspace.data.VehicleContract.VehicleEntry;

public class VehicleCursorAdapter extends CursorAdapter {
    public VehicleCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvVehicleNumberplate = (TextView) view.findViewById(R.id.number_plate);
        TextView tvVehicleDetails = (TextView) view.findViewById(R.id.vehicle_details);
        ImageView imgVehicleType = (ImageView) view.findViewById(R.id.vehicle_type);

        String numberplate = cursor.getString(cursor.getColumnIndexOrThrow(VehicleEntry.COLUMN_NUMBERPLATE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(VehicleEntry.COLUMN_DESCRIPTION));
        int type = cursor.getInt(cursor.getColumnIndexOrThrow(VehicleEntry.COLUMN_VEHICLE_TYPE));

        if(type == VehicleEntry.VEHICLE_CAR) {
            imgVehicleType.setImageResource(R.drawable.round_directions_car_white_24);
        } else if (type == VehicleEntry.VEHICLE_BIKE) {
            imgVehicleType.setImageResource(R.drawable.round_directions_bike_white_24);
        }
        tvVehicleNumberplate.setText(numberplate);
        tvVehicleDetails.setText(description);
    }
}
