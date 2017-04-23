package com.example.jeyanthasingam.busroutefinder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String Tag = "SingamMsg";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getSupportActionBar().setSubtitle("Home");
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_main);
        Log.i(Tag,"onCreate");



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void onClickUber(View view){
        String packageName = "com.ubercab";
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

        if(intent == null) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+packageName));
        }
        startActivity(intent);
    }

    public void onClickPick(View view){
        String packageName = "com.pickme.passenger";
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

        if(intent == null) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+packageName));
        }
        startActivity(intent);
    }

    public void onClickTrain(View view){
        String packageName = "lk.icta.mobile.apps.railway";
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

        if(intent == null) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+packageName));
        }
        startActivity(intent);
    }

    public void onClickSingam(View view){
        Intent i = new Intent(this, singam.class);
        startActivity(i);
    }


    public void onClickHalt(View view){
        Intent intent = new Intent(this, bus_stops.class);
        startActivity(intent);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_name) {
            Intent i = new Intent(this, Help.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickBustop(View view){
        Log.i(Tag,"onClickBustop");
        Intent i = new Intent(this, bus_Stop_Input.class);
        Log.i(Tag,"intent created");
        startActivity(i);
    }


    public void onClickBusRoute(View view){
        Log.i(Tag,"onClickBusRoute");
        Intent i = new Intent(this, BusRoute.class);
        Log.i(Tag,"intent created");
        startActivity(i);
    }


    public void onClickLocation(View view){
        Log.i(Tag,"onClickLocation");
        Intent i = new Intent(this, DestinationInput.class);
        Log.i(Tag,"intent created");
        startActivity(i);
    }


    public void onButtonClick(View view){
        String packageName = "com.electricsheep.asi";
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

        if(intent == null) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+packageName));
        }
        startActivity(intent);
    }
}
