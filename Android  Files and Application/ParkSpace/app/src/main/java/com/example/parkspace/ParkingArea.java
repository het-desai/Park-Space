package com.example.parkspace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ParkingArea extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]> {

    private static final String requestedURL = URLHandler.requestedURL + "park_status_info_ps.php";

    private TextView tvParkingNo;
    private TextView tvPlaceNo1;
    private TextView tvPlaceNo2;

    private String park1 = "";
    private String park2 = "";

    private int parkingNo = 1;

    private int clickable_one = 0;
    private int clickable_two = 0;

    private static final int PARKING_AREA_LOADER_ID = 1441;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_area);

        tvParkingNo = findViewById(R.id.park_area_no);
        tvPlaceNo1 = findViewById(R.id.parking_spot_no_1);
        tvPlaceNo2 = findViewById(R.id.parking_spot_no_2);

        currentStatus();
    }

    private void currentStatus() {

        LoaderManager loaderManager = getSupportLoaderManager();

        if(loaderManager.getLoader(PARKING_AREA_LOADER_ID) == null) {

            loaderManager.initLoader(PARKING_AREA_LOADER_ID, null, ParkingArea.this);
        } else {

            loaderManager.destroyLoader(PARKING_AREA_LOADER_ID);
            loaderManager.initLoader(PARKING_AREA_LOADER_ID, null, ParkingArea.this);
        }

    }

    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int id, @Nullable Bundle args) {
        Uri baseUri = Uri.parse(requestedURL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        String actualUrl = uriBuilder.toString() + "?parking_no=" + parkingNo;

        return new ParkingAreaLoader(this, actualUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] data) {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        /*if (cm.isActiveNetworkMetered()) {
            Toast.makeText(ParkingArea.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }*/

        park1 = data[0];
        park2 = data[1];

        if (park1.equals("Free") || park2.equals("Free")) {

            tvParkingNo.setText("Parking No. : " + parkingNo);

            if (park1.equals("Free")) {
                tvPlaceNo1.setBackgroundColor(Color.GREEN);
                clickable_one = 1;
            } else if (park1.equals("Booked")) {
                tvPlaceNo1.setBackgroundColor(Color.BLUE);
            } else if (park1.equals("Parked")) {
                tvPlaceNo1.setBackgroundColor(Color.RED);
            }

            if (park2.equals("Free")) {
                tvPlaceNo2.setBackgroundColor(Color.GREEN);
                clickable_two = 1;
            } else if (park2.equals("Booked")) {
                tvPlaceNo2.setBackgroundColor(Color.BLUE);
            } else if (park2.equals("Parked")) {
                tvPlaceNo2.setBackgroundColor(Color.RED);
            }

            tvPlaceNo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (clickable_one == 1) {
                        Intent chosenSpot = getIntent();
                        chosenSpot.putExtra("parkingNo", parkingNo);
                        chosenSpot.putExtra("parkingSpot", 1);
                        clickable_one = 0;
                        setResult(RESULT_OK, chosenSpot);
                        finish();
                    } else {
                        Toast.makeText(ParkingArea.this, "This place is already used", Toast.LENGTH_SHORT);
                    }

                }
            });

            tvPlaceNo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (clickable_two == 1) {
                        Intent chosenSpot = getIntent();
                        chosenSpot.putExtra("parkingNo", parkingNo);
                        chosenSpot.putExtra("parkingSpot", 2);
                        clickable_two = 0;
                        setResult(RESULT_OK, chosenSpot);
                        finish();
                    } else {
                        Toast.makeText(ParkingArea.this, "This place is already used", Toast.LENGTH_SHORT);
                    }

                }
            });

        } else if (parkingNo == 1) {
            parkingNo += 1;
            park1 = "";
            park2 = "";
            currentStatus();
        } else {
            tvParkingNo.setText("All spots are full.");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {
        park1 = "";
        park2 = "";
    }
}