package com.emdeveloper.embeaconlib;

import android.test.AndroidTestCase;
import android.util.Log;

import com.emdeveloper.embeaconlib.database.EMBeaconAdvertisement;
import com.emdeveloper.embeaconlib.database.EMBeaconManufacturerData;

/**
 * Created by chris on 1/26/15.
 *
 *
 * 
 * Tests the EMBeaconAdvertisement class and sub classes
Packets
2015-01-26 15:10:09.889997 > 04 3E 2B 02 01 03 00 F1 34 12 EE F3 0E 1F
   0F 09 45 4D 42 65 61 63 6F 6E 31 32 38 38 31 00 0E FF 5A 00 20 00 30 31 30 00 00 00 1F 00 00 B4
EM 0F 09 45 4D 42 65 61 63 6F 6E 31 32 38 38 31 00 0E FF 5A 00 20 00 30 31 30 00 00 00 33 00 01

2015-01-26 15:10:10.553901 > 04 3E 2A 02 01 03 00 FF 02 01 EE F3 0E 1E
   02 01 04 1A FF 4C 00 02 15 69 9E BC 80 E1 F3 11 E3 9A 0F 0C F3 EE 3B C0 12 00 5A 32 4D C1 C6
ID 

2015-01-26 15:10:14.669245 > 04 3E 2B 02 01 03 00 F1 34 12 EE F3 0E 1F
   02 01 06 1B FF 5A 00 BE AC 69 9E BC 80 E1 F3 11 E3 9A 0F 0C F3 EE 3B C0 12 00 5A 32 51 C1 64 <C7
AL 02 01 06 1B FF 5A 00 BE AC 69 9E BC 80 E1 F3 11 E3 9A 0F 0C F3 EE 3B C0 12 00 5A 32 51 C1 64 
 */
public class BeaconAdvertisementTest extends AndroidTestCase {
    private final static String TAG = "BeaconAdvertisementTest";
/**
 * @brief TODO
 */
    static public byte[] emdata = {
        /* name */(byte)0x0F, (byte)0x09,
        /* name */  (byte)0x45, (byte)0x4D, (byte)0x42, (byte)0x65, (byte)0x61, (byte)0x63, (byte)0x6F, (byte)0x6E, (byte)0x31, (byte)0x32, (byte)0x38, (byte)0x38, (byte)0x31, (byte)0x00,
        /* manuf */(byte)0x0E, (byte)0xFF,
        /* code  */ (byte)0x5A, (byte)0x00,
        /* sense */ (byte)0x20, (byte)0x30,
        /* model */ (byte)0x30, (byte)0x31,
        /*battery*/ (byte)0x32,
        /*packets*/ (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x33,
        /*events */ (byte)0x00, (byte)0x01
    };
/**
 * @brief TODO
 */
    static public byte[] iddata = {
        (byte)0x02, (byte)0x01, (byte)0x04, (byte)0x1A, (byte)0xFF, (byte)0x4C, (byte)0x00, (byte)0x02, (byte)0x15, (byte)0x69, (byte)0x9E, (byte)0xBC, (byte)0x80, (byte)0xE1, (byte)0xF3, (byte)0x11, (byte)0xE3, (byte)0x9A, (byte)0x0F, (byte)0x0C, (byte)0xF3, (byte)0xEE, (byte)0x3B, (byte)0xC0, (byte)0x12, (byte)0x00, (byte)0x5A, (byte)0x32, (byte)0x51, (byte)0xC1
    };
/**
 * @brief TODO
 */
    static public byte[] altdata = {
         (byte)0x02, (byte)0x01, (byte)0x06, (byte)0x1B, (byte)0xFF, (byte)0x5A, (byte)0x00, (byte)0xBE, (byte)0xAC, (byte)0x69, (byte)0x9E, (byte)0xBC, (byte)0x80, (byte)0xE1, (byte)0xF3, (byte)0x11, (byte)0xE3, (byte)0x9A, (byte)0x0F, (byte)0x0C, (byte)0xF3, (byte)0xEE, (byte)0x3B, (byte)0xC0, (byte)0x12, (byte)0x00, (byte)0x5A, (byte)0x32, (byte)0x51, (byte)0xC1, (byte)0x64
    };

        
    public BeaconAdvertisementTest() {
        super();
    }
    /**
     * @brief tests em data for one advertisement
     */
    public void xtestemdataTest() throws Exception {
        byte[] uu = emdata;
        IEMBluetoothDevice device = EMBluetoothDevice.create("00:00:00:00:00:00");
        IEMBluetoothAdvertisement emba = EMBluetoothAdvertisement.create(device,3,(long)99,uu);
        EMBeaconAdvertisement em = new EMBeaconAdvertisement(emba);
        byte[] name = em.getAdvertisementData(0x9);
        String sname = new String(name,0,name.length-1);
      //  assertTrue(name.mType == (byte)0x9);
        assertEquals("Name","EMBeacon12881",sname);
        byte[] manufd = em.getAdvertisementData(0xff);
     //   assertTrue(manuf.mType == 0xff);
        EMBeaconManufacturerData embd = EMBeaconManufacturerData.create(emba);
        assertEquals("ModelID",embd.mModelID,"01");
        assertEquals("CompanyCode",embd.mCompanyCode.intValue(),90);
        assertEquals("Battery",embd.mBattery,3.2,0.001);
        assertEquals("PacketCount",embd.mPacketCount.intValue(),0x33);
        assertEquals("Event Type",embd.mEventType.intValue(),0);
        assertEquals("Event Value",embd.mEventValue.intValue(),1);
        assertEquals("Sense Type",embd.mOpenSenseType.intValue(),2);
        //Log.i(TAG,String.format("Sense Value %x ",embd.mOpenSenseValue.intValue()));
        assertEquals("Sense Value",embd.mOpenSenseValue.intValue(),0x30);


    }
    // @Test
    // public void testiddata() throws Exception {
    //     //        EMBeaconAdvertisement id = new EMBeaconAdvertisement(iddata);
    // }
    // @Test
    // public void testaltdata() throws Exception {
    //     //        EMBeaconAdvertisement alt = new EMBeaconAdvertisement(altdata);
    // }
    /**
     * @brief TODO
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        //        EMBeaconAdvertisement id = new EMBeaconAdvertisement(iddata);
        //        EMBeaconAdvertisement alt = new EMBeaconAdvertisement(altdata);
        
        
    }

    /**
     * @brief TODO
     */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
