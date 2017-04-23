package com.example.jeyanthasingam.busroutefinder;

/**
 * Created by Jeyanthasingam on 3/2/2017.
 */

public class Constants {

    public static final String API_NOT_CONNECTED = "Google API not connected";
    public static final String SOMETHING_WENT_WRONG = "OOPs!!! Something went wrong...";
    public static String PlacesTag = "Google Places Auto Complete";


    public static class Geometry {
        public static double MinLatitude = -90.0;
        public static double MaxLatitude = 90.0;
        public static double MinLongitude = -180.0;
        public static double MaxLongitude = 180.0;
        public static double MinRadius = 0.01; // kilometers
        public static double MaxRadius = 20.0; // kilometers
    }

    public static class SharedPrefs {
        public static String Geofences = "SHARED_PREFS_GEOFENCES";
    }
}
