package com.example.parkspace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.parkspace.appsharedpreferences.UserSharedPreferencesInfo;
import com.example.parkspace.data.VehicleContract.VehicleEntry;
import com.example.parkspace.locationdata.Locations;

import java.util.ArrayList;
import java.util.List;

public class BookParking extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String requestedURL = URLHandler.requestedURL + "booking_information_ps.php";

    private Spinner selectVehicle;

    private Spinner selectLocation;

    private Button btnFindSpace;
    private Button btnFindLocation;

    private String selectedVehicle = null;

    private String selectedLocation = null;

    private String selectedLocationLatitude = null;
    private String selectedLocationLongitude = null;

    private List<Locations> location_list_item = new ArrayList<>();

    private int parkingNo = 0;
    private int parkingSpot = 0;

    private UserSharedPreferencesInfo userSharedPreferencesInfo;

    private View loadingIndicator;

    private static final int BOOK_PARKING_LOADER_ID = 131;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_parking);

        selectVehicle = findViewById(R.id.select_vehicle);
        selectLocation = findViewById(R.id.select_location);
        btnFindSpace = findViewById(R.id.find_space);
        btnFindLocation = findViewById(R.id.find_location);

        loadingIndicator = findViewById(R.id.loading_indicator_book_parking);
        loadingIndicator.setVisibility(View.GONE);

        location_list_item.add(new Locations("Parking 1", "12.827272", "80.043434", "KRS Hostel"));

        location_list_item.add(new Locations("Parking 2", "12.826404", "80.043115", "SRM College of Pharmacy"));

        location_list_item.add(new Locations("Parking 3", "12.824318", "80.043819", "SRM Biotech Block"));

        location_list_item.add(new Locations("Parking 4", "12.823285", "80.040982", "Architecture Gate"));

        location_list_item.add(new Locations("Parking 5", "12.823240", "80.044936", "Java Canteen"));

        location_list_item.add(new Locations("Parking 6", "12.823248", "80.046697", "Indian Bank"));

        location_list_item.add(new Locations("Parking 7", "12.821380", "80.039066", "SRM Hitech Block"));

        location_list_item.add(new Locations("Parking 8", "12.821382", "80.043246", "Adhiyaman Hostel"));

        location_list_item.add(new Locations("Parking 9", "12.821106", "80.043991", "Sannasi C Block"));

        setUpSelectLocation();

        btnFindSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BookParking.this, ParkingArea.class), 1);
            }
        });

        btnFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedVehicle.equals("Vehicle") || selectedLocation.equals("Location")) {
                    Toast.makeText(getApplicationContext(), "Please select proper vehicle or location.", Toast.LENGTH_LONG).show();

                } else {
                    if (parkingSpot != 0 && parkingNo != 0) {
                        bookingDetailsUpdate();

                    } else {
                        Toast.makeText(getApplicationContext(), "Parking spot not selected.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void bookingDetailsUpdate() {

        loadingIndicator.setVisibility(View.VISIBLE);

        LoaderManager loaderManager = getSupportLoaderManager();

        loaderManager.initLoader(BOOK_PARKING_LOADER_ID, null, BookParking.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpSelectVehicle();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            parkingNo = data.getIntExtra("parkingNo", 0);
            parkingSpot = data.getIntExtra("parkingSpot", 0);

            Toast.makeText(BookParking.this, "" + parkingNo + " : " + parkingSpot, Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpSelectVehicle() {

        List<String> vehicleList = new ArrayList<>();
        vehicleList.add("Vehicle");
        vehicleList.add("Add/Edit Vehicle");
        List<String> list = getAllVehicles();
        vehicleList.addAll(list);

        ArrayAdapter<String> vehicleAdapter = new ArrayAdapter<>(BookParking.this, android.R.layout.simple_spinner_dropdown_item, vehicleList);
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectVehicle.setAdapter(vehicleAdapter);

        selectVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectItem = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selectItem)) {
                    if (selectItem.equals("Add/Edit Vehicle")) {
                        Intent intent = new Intent(BookParking.this, VehicleCatalog.class);
                        startActivity(intent);
                    } else {
                        selectedVehicle = selectItem;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpSelectLocation() {

        List<String> locationsList = new ArrayList<>();
        locationsList.add("Location");

        for (Locations locations : location_list_item) {
            locationsList.add(locations.getLocationNearby());
        }

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(BookParking.this, android.R.layout.simple_spinner_dropdown_item, locationsList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectLocation.setAdapter(locationAdapter);

        selectLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectItem = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selectItem)) {
                    selectedLocation = selectItem;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bookpark_vehicle_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            UserSharedPreferencesInfo userSharedPreferencesInfo = new UserSharedPreferencesInfo(BookParking.this);
            userSharedPreferencesInfo.setUsername("");
            userSharedPreferencesInfo.setPassword("");
            deleteAllVehicle();
            startActivity(new Intent(BookParking.this, LogInSignUp.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllVehicle() {
        int rowsDeleted = getContentResolver().delete(VehicleEntry.CONTENT_URI, null, null);
        Log.v("BookParking", rowsDeleted + " rows deleted from pet database");
    }

    public List<String> getAllVehicles() {
        List<String> numberplate = new ArrayList<>();
        String[] projection = {VehicleEntry.COLUMN_NUMBERPLATE};
        Cursor cursor = getContentResolver().query(VehicleEntry.CONTENT_URI, projection, null, null, null);

        assert cursor != null;
        if (cursor.moveToFirst()) {
            do {
                numberplate.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // database closing connection
        cursor.close();

        // returning number plates
        return numberplate;
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {

        UserSharedPreferencesInfo userSharedPreferencesInfo = new UserSharedPreferencesInfo(getApplicationContext());

        Uri baseUri = Uri.parse(requestedURL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        String actualUrl = uriBuilder.toString() + "?email_id=" + userSharedPreferencesInfo.getUsername() + "&number_plate=" + selectVehicle.getSelectedItem().toString() + "&parking_no=" + parkingNo + "&parking_spot_no=" + parkingSpot;

        loadingIndicator.setVisibility(View.VISIBLE);

        return new BookParkingLoader(this, actualUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        /*if (cm.isActiveNetworkMetered()) {
            Toast.makeText(BookParking.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }*/

        loadingIndicator.setVisibility(View.GONE);

        if (data == null || data.equals("Unable to Connect")) {
            Toast.makeText(BookParking.this, "Server not respond.", Toast.LENGTH_SHORT).show();

        } else if(data.equals("Please fill all values")) {
            Toast.makeText(BookParking.this, "Please fill all details", Toast.LENGTH_SHORT).show();

        } else if(data.equals("Vehicle or User already parked")) {
            Toast.makeText(BookParking.this, "Vehicle or User already parked", Toast.LENGTH_SHORT).show();

        } else if(data.equals("Oops! Please try again!")) {
            Toast.makeText(BookParking.this, "Something went wrong", Toast.LENGTH_SHORT).show();

        } else if (data.equals("Slot already booked")) {
            Toast.makeText(BookParking.this, "Booked", Toast.LENGTH_SHORT).show();

            selectedLocationLatitude = location_list_item.get(selectLocation.getSelectedItemPosition() - 1).getLocationLatitude();
            selectedLocationLongitude = location_list_item.get(selectLocation.getSelectedItemPosition() - 1).getLocationLongitude();

            parkingNo = 0;
            parkingSpot = 0;

            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + selectedLocationLatitude + "," + selectedLocationLongitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        parkingNo = 0;
        parkingSpot = 0;
    }
}