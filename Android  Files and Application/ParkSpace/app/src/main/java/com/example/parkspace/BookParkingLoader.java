package com.example.parkspace;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class BookParkingLoader extends AsyncTaskLoader {

    private String url;

    public BookParkingLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground() {
        if(url == null) {
            return null;
        }

        return QueryUtils.fetchBookParkingData(this.url);
    }
}
