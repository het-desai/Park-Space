package com.example.parkspace;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parkspace.data.VehicleContract.VehicleEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VehicleCatalog extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int VEHICLE_LOADER = 0;

    VehicleCursorAdapter mVehicleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleCatalog.this, VehicleEditor.class);
                startActivity(intent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        mVehicleCursorAdapter = new VehicleCursorAdapter(this, null);
        listView.setAdapter(mVehicleCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(VehicleCatalog.this, VehicleEditor.class);
                Uri currentUri = ContentUris.withAppendedId(VehicleEntry.CONTENT_URI, id);

                intent.setData(currentUri);

                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(VEHICLE_LOADER,null,this);
    }


    private void insertVehicle() {
        ContentValues values = new ContentValues();
        values.put(VehicleEntry.COLUMN_NUMBERPLATE, "TN-01-AA-2020");
        values.put(VehicleEntry.COLUMN_VEHICLE_TYPE, 1);
        values.put(VehicleEntry.COLUMN_DESCRIPTION, "Car/Bike Company name");

        Uri newUri = getContentResolver().insert(VehicleEntry.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_insert_dummy_data: {
                insertVehicle();
                return true;
            }
            case R.id.action_delete_all_entries: {
                deleteAllVehicle();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllVehicle() {
        int rowsDeleted = getContentResolver().delete(VehicleEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                VehicleEntry._ID,
                VehicleEntry.COLUMN_NUMBERPLATE,
                VehicleEntry.COLUMN_DESCRIPTION,
                VehicleEntry.COLUMN_VEHICLE_TYPE};

        return new CursorLoader(this, VehicleEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mVehicleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mVehicleCursorAdapter.swapCursor(null);
    }
}
