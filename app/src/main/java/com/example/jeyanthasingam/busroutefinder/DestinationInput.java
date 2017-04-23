package com.example.jeyanthasingam.busroutefinder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.location.LocationListener;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.example.jeyanthasingam.busroutefinder.AT_Adapter;
import com.example.jeyanthasingam.busroutefinder.Recycle_Listener;
import com.example.jeyanthasingam.busroutefinder.Constants;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.Serializable;

public class DestinationInput extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, LocationListener{
    protected GoogleApiClient mGoogleApiClient;
    private static final String Tag = "singamsMsg";
    Context context;

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-6.689440, 79.816714), new LatLng(7.004013, 80.006400));

    private EditText mAutocompleteView1;
    private  EditText mAutocompleteView2;
    EditText current;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private AT_Adapter mAutoCompleteAdapter;
    View view;
    SlidingUpPanelLayout mlayout;
    View mview1;
    View mview2;
    InputMethodManager imm;
    String title;

    LatLng location;

    static final VariableStorage store = new VariableStorage();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(Tag,"Oncreate 1");
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setContentView(R.layout.activity_destination_input);
        Log.i(Tag, "onCreate 4");
        Log.i(Tag, "onCreate 5");
        mview1 = findViewById(R.id.startIn);
        mview2 = findViewById(R.id.DesIn);
        imm= (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        mAutocompleteView1 = (EditText)findViewById(R.id.startIn);
        mAutocompleteView2 = (EditText) findViewById(R.id.DesIn);
        Bundle extra = getIntent().getExtras();
        if(extra!=null){
            Log.i(Tag,extra.getString("mapInputType"));
            if(extra.get("mapInputType").equals("onDesMapClick")){
                Log.i(Tag,"onDesMapClick");
                location=extra.getParcelable("mapLocation");
                Log.i(Tag,location.toString());
                title=extra.getString("mapTitle");
                store.setLocation2(location,title);
            }
            else if(extra.get("mapInputType").equals("onStartMapClick")){
                Log.i(Tag,"onDesMapClick");
                location=extra.getParcelable("mapLocation");
                title=extra.getString("mapTitle");
                Log.i(Tag,"onDesMapClick");
                store.setLocatoin1(location,title);
            }
        }
        Log.i(Tag,"Oncreate 2");
        //delete=(ImageView)findViewById(R.id.cross);
        AutocompleteFilter filter =
                new AutocompleteFilter.Builder().setCountry("LK").build();

        mAutoCompleteAdapter =  new AT_Adapter(this, R.layout.activity_search_row,
                mGoogleApiClient, BOUNDS_INDIA, filter);
        Log.i(Tag,"Oncreate 3");
        mlayout=(SlidingUpPanelLayout) findViewById(R.id.slidingLayout);
        Log.i(Tag,"Oncreate 3");
        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        mLinearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);
        if(store.getLocatoin1() != null){
            mAutocompleteView1.setText(store.getLoc1Title().toString());
        }
        if(store.getPlace1() != null){
            mAutocompleteView1.setText(store.getPlace1().getName());
        }
        if(store.getLocation2() != null){
            mAutocompleteView2.setText(store.getLoc2Title().toString());
        }
        if(store.getPlace2() != null){
            mAutocompleteView2.setText(store.getPlace1().getName());
        }
        Log.i(Tag,"Oncreate 4");
        //delete.setOnClickListener(this);
        Log.i(Tag,"Oncreate 5");
        mAutocompleteView1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                }else if(!mGoogleApiClient.isConnected()){
                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
                    //Log.e(Constants.PlacesTag,Constants.API_NOT_CONNECTED);
                }

            }


            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if(!s.toString().equals("") && mGoogleApiClient.isConnected() &&!s.toString().equals("Current Location")){
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int height = displayMetrics.heightPixels;
                    height= (int) ( height*0.7);
                    Log.i(Tag,String.valueOf(height));
                    mlayout.setPanelHeight(height);
                    current=mAutocompleteView1;
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                }
                else if(!mGoogleApiClient.isConnected()){
                    Toast.makeText(getApplicationContext(),"Google API client is not connected",Toast.LENGTH_SHORT).show();

                }

            }

            public void afterTextChanged(Editable s) {

            }

        });
        Log.i(Tag,"Oncreate 6");
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
                                if(places.getCount()==1){
                                    //Do the things here on Click.....
                                    Toast.makeText(getApplicationContext(),String.valueOf(places.get(0).getLatLng()),Toast.LENGTH_SHORT).show();
                                    Log.i(Tag, String.valueOf(places.get(0).getLatLng()));
                                    Log.i(Tag,"Oncreate 7");

                                    current.setText(places.get(0).getName());
                                    Log.i(Tag,"Oncreate 8");
                                    if(current == mAutocompleteView1){
                                        Log.i(Tag,"Oncreate 9");
                                        store.setPlace1(places.get(0));
                                        Log.i(Tag,"Oncreate 10");
                                    }

                                    else{
                                        Log.i(Tag,"Oncreate 11");
                                        store.setPlace2(places.get(0));
                                        Log.i(Tag,"Oncreate 12");
                                    }
                                    mlayout.setPanelHeight(0);

                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


                                    Log.i(Tag,"Oncreate 13");
                                    Log.i(Tag,"Oncreate 14");

                                }else {
                                    Toast.makeText(getApplicationContext(),Constants.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
//                        Log.i("TAG", "Clicked: " + item.description);
//                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);

                    }
                })

        );
        Log.i(Tag,"Oncreate 10");





        mAutocompleteView2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                }else if(!mGoogleApiClient.isConnected()){
                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
                    //Log.e(Constants.PlacesTag,Constants.API_NOT_CONNECTED);
                }

            }


            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if(!s.toString().equals("") && mGoogleApiClient.isConnected()){
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int height = displayMetrics.heightPixels;
                    height= (int) ( height*0.55);
                    Log.i(Tag,String.valueOf(height));
                    mlayout.setPanelHeight(height);
                    current=mAutocompleteView2;
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                }
                else if(!mGoogleApiClient.isConnected()){
                    Toast.makeText(getApplicationContext(),"Google API client is not connected",Toast.LENGTH_SHORT).show();

                }

            }

            public void afterTextChanged(Editable s) {

            }

        });
        Log.i(Tag,"Oncreate 6");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

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
        Log.v("Google API Callback","Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this, Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()){
            Log.v("Google API","Connecting");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            Log.v("Google API","Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onLocationChanged(Location location) {

    }


    public boolean onStartLocationClick(View view){
        //location1=location;
        Log.i(Tag,"onStartLocationClick");
        //places1=null;
        mAutocompleteView1.setText("Current Location");
        mlayout.setPanelHeight(0);
        //Toast.makeText(context, location1.toString(),Toast.LENGTH_SHORT).show();
        return true;

    }


    public void onDesLocationClick(View view){
        //location2=location;
        //places2=null;
        mAutocompleteView2.setText("Current Location");
        //Toast.makeText(context, location2.toString(),Toast.LENGTH_SHORT).show();
        mlayout.setPanelHeight(0);
    }

    public void onStartMapClick(View view){
        Intent i = new Intent(this, MapsInput.class);
        if(store.getLocatoin1()!=null){
            location=store.getLocatoin1();
            i.putExtra("onStartMapClick",location);
        }
        if(store.getPlace1()!=null){
            location=store.getPlace1().getLatLng();
            i.putExtra("onStartMapClick",location);
        }
        Log.i(Tag,"onStartMapClick1");

        Log.i(Tag,"onStartMapClick2");
        startActivity(i);
    }


    public void onDesMapClick(View view){
        Intent i = new Intent(this, MapsInput.class);
        if(store.getLocation2()!=null){
            location=store.getLocation2();
            i.putExtra("onDesMapClick",location);
        }
        if(store.getPlace2()!=null){
            location=store.getPlace2().getLatLng();
            i.putExtra("onDesMapClick" ,location);
        }
        Log.i(Tag,"onStartMapClick1");

        Log.i(Tag,"onStartMapClick2");
        startActivity(i);
    }
}
