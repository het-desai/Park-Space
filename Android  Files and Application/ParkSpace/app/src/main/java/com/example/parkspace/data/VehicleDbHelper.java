package com.example.parkspace.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.parkspace.data.VehicleContract.VehicleEntry;

public class VehicleDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "parkspaces.db";
    public static final int DATABASE_VERSION = 1;

    public VehicleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_VEHICLE_TABLE = "CREATE TABLE " + VehicleEntry.TABLE_NAME + " ("
                + VehicleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VehicleEntry.COLUMN_NUMBERPLATE + " TEXT NOT NULL, "
                + VehicleEntry.COLUMN_DESCRIPTION + " TEXT, "
                + VehicleEntry.COLUMN_VEHICLE_TYPE + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_VEHICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
