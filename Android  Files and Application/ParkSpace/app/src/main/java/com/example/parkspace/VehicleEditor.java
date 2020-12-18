package com.example.parkspace;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.parkspace.data.VehicleContract.VehicleEntry;

public class VehicleEditor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText edtStateCode;
    private EditText edtDistrictCode;
    private EditText edtSeriesCode;
    private EditText edtNumberCode;
    private EditText edtDescription;
    private Spinner vehicleTypeSpinner;

    private int mVehicle = VehicleEntry.VEHICLE_UNKNOWN;

    private static final int EXISTING_VEHICLE_LOADER = 0;

    private Uri mCurrentVehicleUri;

    private boolean mVehicleHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mVehicleHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_editor);

        Intent intent = getIntent();
        mCurrentVehicleUri = intent.getData();

        if(mCurrentVehicleUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_vehicle));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a Vehicle that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            Log.v("Edit Vehicle Title", "Worked");
            setTitle(getString(R.string.editor_activity_title_edit_pet));

            getLoaderManager().initLoader(EXISTING_VEHICLE_LOADER, null,  this);
        }

        edtStateCode = (EditText) findViewById(R.id.state_code);
        edtDistrictCode = (EditText) findViewById(R.id.district_code);
        edtSeriesCode = (EditText) findViewById(R.id.series_code);
        edtNumberCode = (EditText) findViewById(R.id.number_code);
        edtDescription = (EditText) findViewById(R.id.vehicle_description);
        vehicleTypeSpinner = (Spinner) findViewById(R.id.select_vehicle_type);

        edtStateCode.setOnTouchListener(mTouchListener);
        edtDistrictCode.setOnTouchListener(mTouchListener);
        edtSeriesCode.setOnTouchListener(mTouchListener);
        edtNumberCode.setOnTouchListener(mTouchListener);
        edtDescription.setOnTouchListener(mTouchListener);
        vehicleTypeSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter vehicleSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_vehicle_options, android.R.layout.simple_spinner_item);

        vehicleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        vehicleTypeSpinner.setAdapter(vehicleSpinnerAdapter);

        vehicleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {

                    if (selection.equals(getString(R.string.vehicle_car))) {
                        mVehicle = VehicleEntry.VEHICLE_CAR; // Car
                    } else if (selection.equals(getString(R.string.vehicle_bike))) {
                        mVehicle = VehicleEntry.VEHICLE_BIKE; // Female
                    } else {
                        mVehicle = VehicleEntry.VEHICLE_UNKNOWN; //Unknown
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mVehicle = 1;
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                VehicleEntry._ID,
                VehicleEntry.COLUMN_NUMBERPLATE,
                VehicleEntry.COLUMN_VEHICLE_TYPE,
                VehicleEntry.COLUMN_DESCRIPTION};

        return new CursorLoader(this, mCurrentVehicleUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int numberplateColumnIndex = cursor.getColumnIndex(VehicleEntry.COLUMN_NUMBERPLATE);
            int vehicleTypeColumnIndex = cursor.getColumnIndex(VehicleEntry.COLUMN_VEHICLE_TYPE);
            int descriptionColumnIndex = cursor.getColumnIndex(VehicleEntry.COLUMN_DESCRIPTION);

            // Extract out the value from the Cursor for the given column index
            String numberplate = cursor.getString(numberplateColumnIndex);
            String description = cursor.getString(descriptionColumnIndex);
            int vehicleType = cursor.getInt(vehicleTypeColumnIndex);

            String []numberplateParts = numberplate.split("-");

            edtStateCode.setText(numberplateParts[0]);
            edtDistrictCode.setText(numberplateParts[1]);
            edtSeriesCode.setText(numberplateParts[2]);
            edtNumberCode.setText(numberplateParts[3]);
            edtDescription.setText(description);

            switch (vehicleType) {
                case VehicleEntry.VEHICLE_CAR: {
                    vehicleTypeSpinner.setSelection(1);
                    break;
                }
                case VehicleEntry.VEHICLE_BIKE: {
                    vehicleTypeSpinner.setSelection(2);
                    break;
                }
                default: {
                    vehicleTypeSpinner.setSelection(0);
                    break;
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        edtStateCode.setText("");
        edtDistrictCode.setText("");
        edtSeriesCode.setText("");
        edtNumberCode.setText("");
        vehicleTypeSpinner.setSelection(0);
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mVehicleHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteVehicle();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteVehicle() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentVehicleUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentVehicleUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_vehicle_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_vehicle_successful), Toast.LENGTH_SHORT).show();
            }
        }

        //Close the activity
        finish();
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentVehicleUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                saveVehicle();
                finish();
                return true;
            }
            case R.id.action_delete: {
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            }
            case android.R.id.home: {
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mVehicleHasChanged) {
                    NavUtils.navigateUpFromSameTask(VehicleEditor.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(VehicleEditor.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveVehicle() {

        String stateCodeString = edtStateCode.getText().toString().trim();
        String districtCodeString = edtDistrictCode.getText().toString().trim();
        String seriesCodeString = edtSeriesCode.getText().toString().trim();
        String numberCodeString = edtNumberCode.getText().toString().trim();
        if (mCurrentVehicleUri == null &&
                TextUtils.isEmpty(stateCodeString) &&
                TextUtils.isEmpty(districtCodeString) &&
                TextUtils.isEmpty(seriesCodeString) &&
                TextUtils.isEmpty(numberCodeString) &&
                mVehicle == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        String numberplate = stateCodeString + "-" + districtCodeString + "-" + seriesCodeString + "-" + numberCodeString;
        values.put(VehicleEntry.COLUMN_NUMBERPLATE, numberplate);
        values.put(VehicleEntry.COLUMN_VEHICLE_TYPE, mVehicle);

        String description = edtDescription.getText().toString().trim();
        if (TextUtils.isEmpty(description)) {
            if(mVehicle == VehicleEntry.VEHICLE_CAR) {
                description = "Car";
            } else if(mVehicle == VehicleEntry.VEHICLE_BIKE){
                description = "Bike";
            } else {
                description = "Unknown";
            }
        }

        values.put(VehicleEntry.COLUMN_DESCRIPTION, description);

        if (mCurrentVehicleUri == null) {
            // This is a NEW pet, so insert a new vehicle into the provider,
            // returning the content URI for the new pet.
            Uri newUri = getContentResolver().insert(VehicleEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_vehicle_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_vehicle_successful), Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentVehicleUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_vehicle_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_vehicle_successful), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
