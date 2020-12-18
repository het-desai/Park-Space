package com.example.parkspace.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.parkspace.data.VehicleContract.VehicleEntry;

public class VehicleProvider extends ContentProvider {
    private static final int VEHICLES = 100;

    private static final int VEHICLE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(VehicleContract.CONTENT_AUTHORITY, VehicleContract.PATH_VEHICLES, VEHICLES);

        sUriMatcher.addURI(VehicleContract.CONTENT_AUTHORITY, VehicleContract.PATH_VEHICLES + "/#", VEHICLE_ID);
    }

    public static final String LOG_TAG = VehicleProvider.class.getSimpleName();

    private VehicleDbHelper db;

    @Override
    public boolean onCreate() {
        db = new VehicleDbHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Get readable database
        SQLiteDatabase database = db.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case VEHICLES: {
                cursor = database.query(VehicleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case VEHICLE_ID: {

                selection = VehicleEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(VehicleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            default: {
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
            }
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case VEHICLES:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPet(Uri uri, ContentValues values) {

        String numberplate = values.getAsString(VehicleEntry.COLUMN_NUMBERPLATE);
        if (numberplate == null) {
            throw new IllegalArgumentException("Vehicle requires a numberplate");
        }

        Integer vehicleType = values.getAsInteger(VehicleEntry.COLUMN_VEHICLE_TYPE);
        if (vehicleType == null || !VehicleEntry.isValidVehicle(vehicleType)) {
            throw new IllegalArgumentException("Vehicle requires valid type");
        }

        String description = values.getAsString(VehicleEntry.COLUMN_DESCRIPTION);
        if (description == null) {
            throw new IllegalArgumentException("Pet requires a description");
        }

        SQLiteDatabase database = db.getWritableDatabase();

        long id = database.insert(VehicleEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case VEHICLES: {
                return updatePet(uri, contentValues, selection, selectionArgs);
            }
            case VEHICLE_ID: {
                selection = VehicleEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            }
            default: {
                throw new IllegalArgumentException("Update is not supported for " + uri);
            }
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(VehicleEntry.COLUMN_NUMBERPLATE)) {
            String numberplate = values.getAsString(VehicleEntry.COLUMN_NUMBERPLATE);
            if (numberplate == null) {
                throw new IllegalArgumentException("Vehicle requires a numberplate");
            }
        }

        if (values.containsKey(VehicleEntry.COLUMN_VEHICLE_TYPE)) {
            Integer vehicleType = values.getAsInteger(VehicleEntry.COLUMN_VEHICLE_TYPE);
            if (vehicleType == null || !VehicleEntry.isValidVehicle(vehicleType)) {
                throw new IllegalArgumentException("Vehicle requires valid type");
            }
        }

        if (values.containsKey(VehicleEntry.COLUMN_DESCRIPTION)) {
            String description = values.getAsString(VehicleEntry.COLUMN_DESCRIPTION);
            if (description == null) {
                throw new IllegalArgumentException("Vehicle requires valid description");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = db.getWritableDatabase();

        int rowsUpdated = database.update(VehicleEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted;

        SQLiteDatabase database = db.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case VEHICLES: {
                rowsDeleted = database.delete(VehicleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case VEHICLE_ID: {
                selection = VehicleEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(VehicleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default: {
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
            }
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case VEHICLES: {
                return VehicleEntry.CONTENT_LIST_TYPE;
            }
            case VEHICLE_ID: {
                return VehicleEntry.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
            }
        }

    }
}
