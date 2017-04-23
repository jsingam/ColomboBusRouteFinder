package com.example.jeyanthasingam.busroutefinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class bus_Stop_Input extends AppCompatActivity {
    AutoCompleteTextView start;
    AutoCompleteTextView end;
    String[] stops={"Wellawatte Policestation","Wellawatte Roxy","Wellawatte arpico","Wellawatte littleasia","wellawatte mangala", "wellawatte manningplace","wellawatte mosque","wellawatte nolimit"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setSubtitle("Bus stop to Bus stop");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_bus__stop__input);
        start = (AutoCompleteTextView) findViewById(R.id.editText);
        end= (AutoCompleteTextView) findViewById(R.id.editText2);
        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,stops);

        start.setAdapter(adapter);
        end.setAdapter(adapter);
        start.setThreshold(1);
        end.setThreshold(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
