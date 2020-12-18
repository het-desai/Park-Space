package com.example.parkspace;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

public class LoginLoader extends AsyncTaskLoader<String> {

    private String url;

    public LoginLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        if(url == null) {
            return null;
        }

        return QueryUtils.fetchLoginData(this.url);
    }
}
