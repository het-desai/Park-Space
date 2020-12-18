package com.example.parkspace;

import com.example.parkspace.appsharedpreferences.UserSharedPreferencesInfo;

public class Validation {

    private String validUsername;
    private String validPassword;

    private Validation() {}

    public static boolean validDetailes(UserSharedPreferencesInfo userSharedPreferencesInfo) {
        if(userSharedPreferencesInfo.getUsername().equals("admin") && userSharedPreferencesInfo.getPassword().equals("admin")) {
            return true;
        } else {
            return false;
        }
    }
}
