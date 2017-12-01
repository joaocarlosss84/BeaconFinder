/*
 * Copyright (c) 2014-2015 EM Microelectronic-Marin SA. All rights reserved.
 * Developed by Glacier River Design, LLC.
 */

package com.emdeveloper.embeaconlib.embeaconspecific;

import android.test.InstrumentationTestCase;

import com.emdeveloper.embeaconlib.R;

/**
 *
 *  12 bit unsigned
 *  12 bit signed
 *  12 bit fixed point 4 fractional bits 2's compliment
 *  12 bit fixed point 6 fractional bits 2's compliment
 *
 */



public class SensorsTest extends InstrumentationTestCase {

/**
 * @brief TODO
 */
    public void setUp() throws Exception {
        super.setUp();

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
/**
 * @brief TODO
 */
    public void tearDown() throws Exception {

    }

    // public void testGetIcon() throws Exception {
    //     int icon = Sensors.getIcon(Sensors.TYPE_LIGHT);

    //     assertEquals("wrong icon",R.drawable.ic_lightbulb,icon);

    // }

/**
 * @brief TODO
 */
    public void testGetValue() throws Exception {
        String s;
        Sensors.unitChange = false;

        s = Sensors.getValue(Sensors.TYPE_LIGHT,sext12(0x123));
        assertEquals("Light"," 291",s);

        s = Sensors.getValue(Sensors.TYPE_LIGHT,sext12(0x823));
        assertEquals("Light","2083",s);

        s = Sensors.getValue(Sensors.TYPE_FWREV,sext12(0x123));
        assertEquals("Revision","1.2.3",s);        

        s = Sensors.getValue(Sensors.TYPE_AUTOCAL,sext12(0x123));
        assertEquals("AutoCal","1/35",s);

        s = Sensors.getValue(Sensors.TYPE_AUTOCAL,sext12(0x823));
        assertEquals("AutoCal","8/35",s);

        s = Sensors.getValue(Sensors.TYPE_GENERIC,sext12(0x923));
        assertEquals("Generic","2339",s);

        s = Sensors.getValue(Sensors.TYPE_TEMP,sext12(0x123));
        assertEquals("Temperature"," 18.19",s);

        s = Sensors.getValue(Sensors.TYPE_TEMP,sext12(0x0));
        assertEquals("Temperature","  0.00",s);
        // 7ff = 2047 -> 127.94
        s = Sensors.getValue(Sensors.TYPE_TEMP,sext12(0x7ff));
        assertEquals("Temperature","127.94",s);

        s = Sensors.getValue(Sensors.TYPE_TEMP,sext12(0x801));
        assertEquals("Temperature","-127.94",s);

        
        s = Sensors.getValue(Sensors.TYPE_PRES,sext12(0x123));
        assertEquals("Pressure","291.0",s);

        s = Sensors.getValue(Sensors.TYPE_HUMID,sext12(0x120));
        assertEquals("Humidity"," 18.00",s);

        s = Sensors.getValue(Sensors.TYPE_HUMID,1600);
        assertEquals("Humidity","100.00",s);

        s = Sensors.getValue(Sensors.TYPE_TIME,sext12(0x123)); // 0001 0010 0011 
        // s should b "2:23"                             0 0010 010 0011   2:23
        assertEquals("Time","02:23",s);

        s = Sensors.getValue(Sensors.TYPE_DATE,sext12(0x123));// 0001 0010 0011      
        // s should b "2/23 january 23"                  0 0010 0 10 0011      
        assertEquals("Date","02/23",s);

        s = Sensors.getValue(Sensors.TYPE_MAGNET,sext12(0x123));
        assertEquals("Magnet"," 18.19",s);

        s = Sensors.getValue(Sensors.TYPE_DAY,sext12(0x123));// 0001 0010 0011      
        // s should b "am 2 23"                         0 0 010 010 0011      
        assertEquals("Day","0 2 23",s);
        // s should b "18.1875"

        // 12 bit fixed point 6 fractional 2's compliment
        // 6 fractional is 2^6 = 64
        // xxxx xxxx xxxx
        // xxxx xx.xx xxxx   
        s = Sensors.getValue(Sensors.TYPE_ACCEL,sext12(sext12(0x123)));
        assertEquals("Acceleration","  4.547",s);
        // s should b "4.546875
        
        s = Sensors.getValue(Sensors.TYPE_ACCEL,sext12(0x7c0));
        assertEquals("Acceleration"," 31.000",s);
        // s should b "4.546875

       // s = Sensors.getValue(Sensors.TYPE_ACCEL,sext12(0x7c0));
        //        assertEquals("Acceleration"," 31.000",s);
        // s should b "4.546875

        s = Sensors.getValue(Sensors.TYPE_ACCEL,sext12(0x800));
        assertEquals("Acceleration","-32.000",s);
        // s should b "4.546875
        
        s = Sensors.getValue(Sensors.TYPE_ACCEL,sext12(0x140));
        assertEquals("Acceleration","  5.000",s);
        // s should b "4.546875
        
        s = Sensors.getValue(Sensors.TYPE_ACCEL,sext12(0x940));
        assertEquals("Acceleration","-27.000",s);
        // s should b "4.546875

        s = Sensors.getValue(Sensors.TYPE_GYRO,sext12(0x123));
        assertEquals("Gyro"," 291",s);

        s = Sensors.getValue(Sensors.TYPE_GYRO,sext12(0x923));
        assertEquals("Gyro","-1757",s);
        // -1757
        s = Sensors.getValue(Sensors.TYPE_RSSI,sext12(0x123));
        assertEquals("RSSI","291",s);
        // s should b " 291"
        s = Sensors.getValue(Sensors.TYPE_RSSI,sext12(0x923));
        assertEquals("RSSI","-1757",s);
        // s should b "-1757"
        s = Sensors.getValue(Sensors.TYPE_BATTERY,sext12(0x123));
      // fails  assertEquals("Battery"," 2.3 ",s);
        // 2.3


        // tests with different units
        // temperature, pressure, 

        Sensors.unitChange = true;
        Sensors.getConversion(Sensors.TYPE_PRES).setUnits(true);
        s = Sensors.getValue(Sensors.TYPE_PRES,300);
        assertEquals("Pressure","9165.2",s);

        s = Sensors.getValue(Sensors.TYPE_PRES,1100);
        assertEquals("Pressure", "-698.4", s);

        Sensors.getConversion(Sensors.TYPE_PRES).setUnits(false);
        Sensors.getConversion(Sensors.TYPE_TEMP).setUnits(true);
        s = Sensors.getValue(Sensors.TYPE_TEMP,sext12(0x00));
        assertEquals("Temperature"," 32.00",s);

        s = Sensors.getValue(Sensors.TYPE_TEMP,sext12(0x7ff));
        assertEquals("Temperature","262.29",s);

        s = Sensors.getValue(Sensors.TYPE_TEMP,sext12(0x801));
        assertEquals("Temperature", "-198.29", s);
        Sensors.getConversion(Sensors.TYPE_TEMP).setUnits(false);

    }

/**
 * @brief TODO
 */
    public void testGetUnits() throws Exception {
        int s;
        Sensors.unitChange = false;

        s = Sensors.getUnits(Sensors.TYPE_LIGHT);
        assertEquals("Light",R.string.Lux,s);

        s = Sensors.getUnits(Sensors.TYPE_FWREV);
        assertEquals("FirmwareRevision",R.string.Blank,s);

        s = Sensors.getUnits(Sensors.TYPE_AUTOCAL);
        assertEquals("AutoCal ",R.string.VCOMOD,s);

        s = Sensors.getUnits(Sensors.TYPE_GENERIC);
        assertEquals("Generic",R.string.Blank,s);

        s = Sensors.getUnits(Sensors.TYPE_TEMP);
        assertEquals("TemperatureC",R.string.DegreesC,s);

        s = Sensors.getUnits(Sensors.TYPE_PRES);
        assertEquals("Pressure",R.string.hPa,s);

        s = Sensors.getUnits(Sensors.TYPE_HUMID);
        assertEquals("Humidity",R.string.Percent,s);

        s = Sensors.getUnits(Sensors.TYPE_TIME);
        assertEquals("Time",R.string.Blank,s);

        s = Sensors.getUnits(Sensors.TYPE_DATE);
        assertEquals("Date",R.string.Blank,s);

        s = Sensors.getUnits(Sensors.TYPE_MAGNET);
        assertEquals("Magnetic",R.string.MicroTesla,s);

        s = Sensors.getUnits(Sensors.TYPE_DAY);
        assertEquals("Day",R.string.Blank,s);

        s = Sensors.getUnits(Sensors.TYPE_ACCEL);
        assertEquals("Acceleration",R.string.Gees,s);

        s = Sensors.getUnits(Sensors.TYPE_GYRO);
        assertEquals("Gyro",R.string.Degrees_per_second,s);

        // s = Sensors.getUnits(Sensors.TYPE_SPARED);
        // assertEquals("spared","",s);

        // s = Sensors.getUnits(Sensors.TYPE_SPAREE);
        // assertEquals("sparee","",s);

        // s = Sensors.getUnits(Sensors.TYPE_SPAREF);
        // assertEquals("sparef","",s);

        Sensors.getConversion(Sensors.TYPE_TEMP).setUnits(true);
        s = Sensors.getUnits(Sensors.TYPE_TEMP);
        assertEquals("TemperatureF",R.string.DegreesF, s);
        Sensors.getConversion(Sensors.TYPE_TEMP).setUnits(false);

        Sensors.getConversion(Sensors.TYPE_PRES).setUnits(true);
        s = Sensors.getUnits(Sensors.TYPE_PRES);
        assertEquals("Pressure",R.string.Meters,s);
        Sensors.getConversion(Sensors.TYPE_PRES).setUnits(false);

    }
}
