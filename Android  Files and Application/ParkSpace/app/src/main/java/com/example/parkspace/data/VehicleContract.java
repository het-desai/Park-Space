package com.example.parkspace.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class VehicleContract {

    public static final String CONTENT_AUTHORITY = "com.example.parkspace";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_VEHICLES = "vehicles";

    private VehicleContract() {}

    public static final class VehicleEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLES;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLES;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VEHICLES);

        public static final String TABLE_NAME = "vehicles";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NUMBERPLATE = "numberplate";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VEHICLE_TYPE = "type";

        public static final int VEHICLE_UNKNOWN = 0;
        public static final int VEHICLE_CAR = 1;
        public static final int VEHICLE_BIKE = 2;

        public static boolean isValidVehicle(int vehicle) {
            if (vehicle == VEHICLE_CAR || vehicle == VEHICLE_BIKE) {
                return true;
            }
            return false;
        }

    }
}
