package com.example.parkspace.appsharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSharedPreferencesInfo {
    private Context context;
    private SharedPreferences sharedPreferences;
    private String username;
    private String password;

    public UserSharedPreferencesInfo(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("login_details", Context.MODE_PRIVATE);
    }

    public String getUsername() {
        this.username = sharedPreferences.getString("uname", "");
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        sharedPreferences.edit().putString("uname", username).apply();
    }

    public String getPassword() {
        this.password = sharedPreferences.getString("upass", "");
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        sharedPreferences.edit().putString("upass", password).apply();
    }

    public boolean removeUser() {
        return sharedPreferences.edit().clear().commit();
    }
}
