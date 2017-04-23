package com.example.jeyanthasingam.busroutefinder;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class AllGeofencesActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, LocationListener {
  private static String nameP=null;
    protected GoogleApiClient mGoogleApiClient;
    private static LatLng loc;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-6.689440, 79.816714), new LatLng(7.004013, 80.006400));
    private EditText name;
    private  EditText place;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private AT_Adapter mAutoCompleteAdapter;
    View view;
    SlidingUpPanelLayout mlayout;
    InputMethodManager imm;
    AllGeofencesFragment frag;


  private static final String Tag="SingamsMsg";

  // region Overrides

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    Log.i(Tag,"AllGeofencesActivity   onCreate");

    super.onCreate(savedInstanceState);
      buildGoogleApiClient();
    setContentView(R.layout.activity_travel_helper);


      imm= (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
      name = (EditText)findViewById(R.id.name);
      place = (EditText) findViewById(R.id.place);
      AutocompleteFilter filter =
              new AutocompleteFilter.Builder().setCountry("LK").build();
      mAutoCompleteAdapter =  new AT_Adapter(this, R.layout.activity_search_row,
              mGoogleApiClient, BOUNDS_INDIA, filter);
      mlayout=(SlidingUpPanelLayout) findViewById(R.id.slidingLayout);
      mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
      mLinearLayoutManager=new LinearLayoutManager(this);
      mRecyclerView.setLayoutManager(mLinearLayoutManager);
      mRecyclerView.setAdapter(mAutoCompleteAdapter);


      place.addTextChangedListener(new TextWatcher() {

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
                  height= (int) ( height*0.52);
                  Log.i(Tag,String.valueOf(height));
                  mlayout.setPanelHeight(height);
                  mAutoCompleteAdapter.getFilter().filter(s.toString());
              }
              else if(!mGoogleApiClient.isConnected()){
                  Toast.makeText(getApplicationContext(),"Google API client is not connected",Toast.LENGTH_SHORT).show();

              }

          }

          public void afterTextChanged(Editable s) {

          }

      });


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
                                  loc=places.get(0).getLatLng();

                                  place.setText(places.get(0).getName());
                                  place.setFocusable(false);

                                  Log.i(Tag,"Oncreate 8");
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

      frag= new AllGeofencesFragment();


    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
              .add(R.id.container, frag)
              .commit();
    }

    GeofenceController.getInstance().init(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    Log.i(Tag,"AllGeofencesActivity   onCreateOptionsMenu");
    getMenuInflater().inflate(R.menu.menu_all_geofences, menu);

    MenuItem item = menu.findItem(R.id.action_delete_all);

    if (GeofenceController.getInstance().getNamedGeofences().size() == 0) {
      item.setVisible(false);
    }

    return true;
  }

  @Override
  protected void onResume() {

    Log.i(Tag,"AllGeofencesActivity   onResume");
    super.onResume();

    int googlePlayServicesCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    Log.i(AllGeofencesActivity.class.getSimpleName(), "googlePlayServicesCode = " + googlePlayServicesCode);

    if (googlePlayServicesCode == 1 || googlePlayServicesCode == 2 || googlePlayServicesCode == 3) {
      GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCode, this, 0).show();
    }
      if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()){
          Log.v("Google API","Connecting");
          mGoogleApiClient.connect();
      }
  }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

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

    public void onButtonClick(View view){
        if(name.getText().toString()!= null){
            if(loc!=null){
        Log.i(Tag,"1");
                nameP=name.getText().toString();
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        Log.i(Tag,"2");
                AddGeofenceFragment add = new AddGeofenceFragment();
        Log.i(Tag,"3");
                add.setListener(frag);
        Log.i(Tag,"4");
                add.sample(nameP, loc);
                name.setText("");
                place.setText("");
        Log.i(Tag,"5");
            }

        }
    }



    // endregion
}
