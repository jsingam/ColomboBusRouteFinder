package com.example.jeyanthasingam.busroutefinder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.example.jeyanthasingam.busroutefinder.AT_Adapter;
import com.example.jeyanthasingam.busroutefinder.Recycle_Listener;
import com.example.jeyanthasingam.busroutefinder.Constants;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsInput extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, OnMapReadyCallback {
    protected GoogleApiClient mGoogleApiClient;
    private static final String Tag = "singamsMsg";
    VariableStorage variableStorage;

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-6.689440, 79.816714), new LatLng(7.004013, 80.006400));

    private EditText mAutocompleteView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private AT_Adapter mAutoCompleteAdapter;
    SlidingUpPanelLayout mlayout;
    View mview;
    InputMethodManager imm;
    ImageView delete;
    String data;
    String info;
    private float lon, lat;
    GoogleMap mMap;
    Marker marker;
    Geocoder geocoder;
    String txt;
    LatLng location;
    Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(Tag, "Oncreate 1");
        super.onCreate(savedInstanceState);
//        if(getIntent().getExtras().containsKey("onStartMapClickPLace")) {
//            variableStorage = (VariableStorage) getIntent().getExtras().get("onStartMapClickPLace");
//            txt="onStartMapClickPLace";
//        }
//        else{
//
//            variableStorage = (VariableStorage) getIntent().getExtras().get("onDesMapClick");
//            txt="onDesMapClick";
//
//        }
        buildGoogleApiClient();
        Log.i(Tag, "onCreate 2");
        setContentView(R.layout.activity_maps_input);
        getSupportActionBar().setSubtitle("Bus Route");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(Tag, "onCreate 3");
        geocoder = new Geocoder(this, Locale.getDefault());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()

                .findFragmentById(R.id.map);

        Log.i(Tag, "onCreate 4");
        mapFragment.getMapAsync(this);
        Log.i(Tag, "onCreate 5");
        if(getIntent().getExtras()!=null) {
            if (getIntent().getExtras().containsKey("onStartMapClick")) {
                location = getIntent().getExtras().getParcelable("onStartMapClick");
                Log.i(Tag, location.toString());
//                if (marker != null) {
//                    marker.remove();
//                }

                //marker = mMap.addMarker(new MarkerOptions().position(location).title("Marker")
                       // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
        mview = findViewById(R.id.autocomplete_places);
        imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        mAutocompleteView = (EditText) findViewById(R.id.autocomplete_places);
        Log.i(Tag, "Oncreate 2");
        //delete=(ImageView)findViewById(R.id.cross);
        AutocompleteFilter filter =
                new AutocompleteFilter.Builder().setCountry("LK").build();

        mAutoCompleteAdapter = new AT_Adapter(this, R.layout.activity_search_row,
                mGoogleApiClient, BOUNDS_INDIA, filter);
        Log.i(Tag, "Oncreate 3");
        mlayout = (SlidingUpPanelLayout) findViewById(R.id.slidingLayout);
        Log.i(Tag, "Oncreate 3");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);
//        if(googleServicesAvailable()){
//            Toast.makeText(this, "Perfect :)", Toast.LENGTH_SHORT).show();
//        }
//        initMap();
        Log.i(Tag, "Oncreate 4");
        //delete.setOnClickListener(this);
        Log.i(Tag, "Oncreate 5");
        if(getIntent().getExtras()!=null){
            if(getIntent().getExtras().containsKey("onStartMapClick")){
                txt="onStartMapClick";
                location=getIntent().getParcelableExtra("onStartMapClick");

            }
            else if(getIntent().getExtras().containsKey("onDesMapClick")){
                txt="onDesMapClick";
                location=getIntent().getParcelableExtra("onDesMapClick");
            }
        }
        mAutocompleteView.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    //Log.e(Constants.PlacesTag,Constants.API_NOT_CONNECTED);
                }

            }


            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int height = displayMetrics.heightPixels;
                    height = (int) (height * 0.74);
                    Log.i(Tag, String.valueOf(height));
                    mlayout.setPanelHeight(height);

                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
                    Toast.makeText(getApplicationContext(), "Google API client is not connected", Toast.LENGTH_SHORT).show();

                }

            }

            public void afterTextChanged(Editable s) {

            }

        });
        Log.i(Tag, "Oncreate 6");
        mRecyclerView.addOnItemTouchListener(
                new Recycle_Listener(this, new Recycle_Listener.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, int position) {
                        final AT_Adapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "Autocomplete item selected: " + item.title);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getCount() == 1) {
                                    //Do the things here on Click.....
                                    Toast.makeText(getApplicationContext(), String.valueOf(places.get(0).getLatLng()), Toast.LENGTH_SHORT).show();
                                    Log.i(Tag, String.valueOf(places.get(0).getLatLng()));
                                    data = String.valueOf(places.get(0).getLatLng());
                                    Log.i(Tag, "Oncreate 7");
                                    info = String.valueOf(places.get(0).getName());
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


                                    Log.i(Tag, "Oncreate 8");
                                    update();
                                    location=null;
                                    place=places.get(0);

                                    Log.i(Tag, "Oncreate 9");

                                } else {
                                    Toast.makeText(getApplicationContext(), Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
//                        Log.i("TAG", "Clicked: " + item.description);
//                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);

                    }
                })

        );
        Log.i(Tag, "Oncreate 10");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }


    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "can't connect to google services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public void update() {
        String[] dat = data.split(" ");
        Log.i(Tag, "onedit 3");
        String[] da = dat[1].split(",");
        Log.i(Tag, "onedit 4");

        String date = da[0].substring(1);
        Log.i(Tag, date);
        da[0] = date;
        date = da[1].substring(0, da[1].length() - 2);
        Log.i(Tag, date);
        da[1] = date;

        Log.i(Tag, "onedit 6");
        lon = Float.valueOf(da[0]);
        Log.i(Tag, "onedit 7");
        lat = Float.valueOf(da[1]);
        Log.i(Tag, "onedit 8");
        Log.i(Tag, "log , lat  : " + lon + "   " + lat);
        LatLng position = new LatLng(lon, lat);
        Log.i(Tag, "onMapReady 2");

        if (marker != null) {
            marker.remove();
        }

        marker=mMap.addMarker(new MarkerOptions().position(position).title("Marker in Sydney"));
        Log.i(Tag, "onMapReady 3");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16.0f));
        mAutocompleteView.setText(info);
        Log.i(Tag, "onMapReady 4");
        mlayout.setPanelHeight(0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_select) {
//            if(txt.equals("onStartMapClickPLace")){
//                if(location!=null){
//                    variableStorage.setLocatoin1(location);
//                    variableStorage.setPlace1(null);
//                }
//                else {
//                    variableStorage.setLocatoin1(null);
//                    variableStorage.setPlace1(place);
//                }
//
//            }
//            else {
//                if(location!=null){
//                    variableStorage.setLocation2(location);
//                    variableStorage.setPlace2(null);
//                }
//                else {
//                    variableStorage.setLocation2(null);
//                    variableStorage.setPlace2(place);
//                }
//            }
        if(id==R.id.action_select) {
            Log.i(Tag, "bla" + place.getName().toString());
            Log.i(Tag, "bla" + location.toString());
            Log.i(Tag, "njdfkbfr");
        }
//        if (txt.equals("#onStartMapClick")) {
////
//            Intent i = new Intent(this, DestinationInput.class);
//            if (place != null) {
//                i.putExtra("mapLocation", place.getLatLng());
//                i.putExtra("mapTitle", place.getName().toString());
//                i.putExtra("mapInputType",txt);
//                Log.i(Tag, place.getName().toString());
//                //startActivity(i);
//            }
//            else if (location != null) {
//                i.putExtra("mapLocation", location);
//                i.putExtra("mapTitle", location.toString());
//                i.putExtra("mapInputType",txt);
//                Log.i(Tag, location.toString());
//                //startActivity(i);
//            }
//
//
//        }
//        else if (txt.equals("onDesMapClick")) {
////
//            Intent i = new Intent(this, DestinationInput.class);
//            if (place != null) {
//                i.putExtra("mapLocation", place.getLatLng());
//                i.putExtra("mapTitle", place.getName().toString());
//                i.putExtra("mapInputType",txt);
//                Log.i(Tag, "bla"+place.getName().toString());
//                //startActivity(i);
//            }
//            else if (location != null) {
//                i.putExtra("mapLocation", location);
//                i.putExtra("mapTitle", location.toString());
//                i.putExtra("mapInputType",txt);
//                Log.i(Tag, "bla"+location.toString());
//                //startActivity(i);
//            }
//
//
//        }
        return false;
    }



        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        //return super.onOptionsItemSelected(item);



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("Google API Callback", "Connection Done");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Google API Callback", "Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this, Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v == delete) {
            mAutocompleteView.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            Log.v("Google API", "Connecting");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            Log.v("Google API", "Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng position = new LatLng(6.884101, 79.86136);
        Log.i(Tag, "onMapReady 2");
        Log.i(Tag, "onMapReady 3");
        if(location!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f));
            marker = mMap.addMarker(new MarkerOptions().position(location
            ).title("Marker")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16.0f));
        }
        mMap.setMinZoomPreference(9.0f);

        //mMap.setMyLocationEnabled(true);

        Log.i(Tag, "onMapReady 4");
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){
            @Override
            public void onMapLongClick(LatLng point) {
                //save current location

                Log.i(Tag,"onLong Click Listener");
                location = point;

                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                android.location.Address address = addresses.get(0);

                if (address != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
                        sb.append(address.getAddressLine(i) + "\n");
                    }
                    Log.i(Tag, String.valueOf(sb));
                }

                //remove previously placed Marker

                if (marker != null) {
                    marker.remove();
                }

                location=point;
                place=null;


                //place marker where user just clicked
                marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mAutocompleteView.setText(address.getAddressLine(0));
                mlayout.setPanelHeight(0);

            }
        });
    }



    public void onCloseClick(View view){

        mAutocompleteView.setText("");
        mlayout.setPanelHeight(0);

    }


}
