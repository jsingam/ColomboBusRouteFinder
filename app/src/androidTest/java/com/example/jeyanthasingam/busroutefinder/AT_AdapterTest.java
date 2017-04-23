package com.example.jeyanthasingam.busroutefinder;

import android.support.test.InstrumentationRegistry;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Jeyanthasingam on 3/30/2017.
 */
public class AT_AdapterTest {
    AT_Adapter at_adapter;


    @Test
    public void getItemCount() throws Exception {
        at_adapter=new AT_Adapter(InstrumentationRegistry.getTargetContext(),0,null,null,null);
        int data= at_adapter.getItemCount();
        assertEquals(0,data);
    }




}