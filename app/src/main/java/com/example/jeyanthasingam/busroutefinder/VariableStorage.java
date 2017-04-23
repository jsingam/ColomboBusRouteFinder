package com.example.jeyanthasingam.busroutefinder;

import android.location.Location;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class VariableStorage implements Serializable {


    public String getLoc1Title() {
        return loc1Title;
    }

    public String getLoc2Title() {
        return loc2Title;
    }

    public void setLocation2(LatLng location2, String title) {
        this.location2 = location2;
        loc2Title=title;
    }

    public void setLocatoin1(LatLng locatoin1, String title) {
        this.locatoin1 = locatoin1;
        loc1Title=title;
    }

    public void setPlace1(Place place1) {
        this.place1 = place1;
    }

    public void setPlace2(Place place2) {
        this.place2 = place2;
    }

    public Place getPlace1() {
        return place1;
    }

    public LatLng getLocation2() {
        return location2;
    }

    public LatLng getLocatoin1() {
        return locatoin1;
    }

    public Place getPlace2() {
        return place2;
    }

    private Place place1;
    private Place place2;
    private LatLng locatoin1;
    private String loc2Title;
    private  String loc1Title;
    private LatLng location2;

    VariableStorage(){
        place1=null;
        place2=null;
        location2=null;
        loc2Title=null;
        locatoin1=null;
        locatoin1=null;
    }



}