package com.example.jeyanthasingam.busroutefinder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jeyanthasingam on 3/31/2017.
 */
public class VariableStorageTest {

    VariableStorage variableStorage;
    VariableStorageTest(){
        variableStorage= new VariableStorage();
    }
    @Test
    public void getLoc1Title() throws Exception {
            variableStorage.setLocatoin1(null,"singam");
        assertEquals("singam",variableStorage.getLoc1Title());
    }

    @Test
    public void getLoc2Title() throws Exception {
        variableStorage.setLocation2(null,"singam");
        assertEquals("singam",variableStorage.getLoc2Title());
    }

}