/*
 * Copyright (c) 2014-2015 EM Microelectronic-Marin SA. All rights reserved.
 * Developed by Glacier River Design, LLC.
 */

package com.emdeveloper.embeaconlib.embeaconspecific;

import android.test.InstrumentationTestCase;

import com.emdeveloper.embeaconlib.R;
/**
 * @brief class to test the event formats from the embeacon 
 */
public class EventsTest extends InstrumentationTestCase {

    public void setUp() throws Exception {
        super.setUp();

    }




    public void tearDown() throws Exception {

    }

    /**
     * @brief sign extend an integer from the 12 bit
     */
    public int sext12(int i){
        if((i & 0x800) != 0){
            i |= 0xfffff000;
        }
        return i;
    }

    public void doTest(String TestName,String expected,String value){

    }

    public void doTest(String TestName,int expected,int value){

    }

    public void testGetValue() throws Exception {
        assertEquals("Button Presses","100",Events.getValue(Events.BUTTONPRESS,sext12(100)));

    }
    public void testGetIcon() throws Exception {
        assertEquals("Button Presses", R.drawable.ic_push_button,Events.getIcon(Events.BUTTONPRESS));
    }
    public void testGetUnits() throws Exception {
        assertEquals("Button Presses", R.string.ButtonPresses,Events.getUnits(Events.BUTTONPRESS));
    }
}
