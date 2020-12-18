package com.example.parkspace.locationdata;

public class Locations {

    private String locationname;
    private String locationlatitude;
    private String locationlongitude;
    private String nearby;

    public Locations(String locationname, String locationlatitude, String locationlongitude, String nearby) {
        this.locationname = locationname;
        this.locationlatitude = locationlatitude;
        this.locationlongitude = locationlongitude;
        this.nearby = nearby;
    }

    public String getLocationName() {
        return locationname;
    }

    public String getLocationLatitude() {
        return locationlatitude;
    }

    public String getLocationLongitude() {
        return locationlongitude;
    }

    public String getLocationNearby() {
        return nearby;
    }
}
