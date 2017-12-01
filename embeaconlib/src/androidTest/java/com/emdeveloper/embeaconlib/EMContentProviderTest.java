package com.emdeveloper.embeaconlib;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.emdeveloper.embeaconlib.data.AltBeaconTestData;
import com.emdeveloper.embeaconlib.data.Data;
import com.emdeveloper.embeaconlib.data.EDTLMTestData;
import com.emdeveloper.embeaconlib.data.EDUIDTestData;
import com.emdeveloper.embeaconlib.data.EDURLTestData;
import com.emdeveloper.embeaconlib.data.EMBeaconTestData;
import com.emdeveloper.embeaconlib.data.IDDataTestData;
import com.emdeveloper.embeaconlib.database.EMALTManufacturerData;
import com.emdeveloper.embeaconlib.database.EMBeaconAdvertisement;
import com.emdeveloper.embeaconlib.database.EMBeaconDevice;
import com.emdeveloper.embeaconlib.database.EMBeaconManufacturerData;
import com.emdeveloper.embeaconlib.database.EMBluetoothDatabase;
import com.emdeveloper.embeaconlib.database.EMContentProvider;
import com.emdeveloper.embeaconlib.database.EMEDTLMManufacturerData;
import com.emdeveloper.embeaconlib.database.EMEDUIDManufacturerData;
import com.emdeveloper.embeaconlib.database.EMEDURLManufacturerData;
import com.emdeveloper.embeaconlib.database.EMIDManufacturerData;
import com.emdeveloper.embeaconlib.database.EMSQL;
import com.emdeveloper.embeaconlib.database.IEMBluetoothDatabase;

import java.util.ArrayList;

/**
 * Created by chris on 8/25/15.
 *
 *  tests:
 *  - add different advertisements
 *  - verify devices
 *  - verify advertisements
 *  - verify specific data
 *  - test queries
 * 
 */
public class EMContentProviderTest extends ProviderTestCase2<EMContentProvider> {
   private final String TAG = "EMContentProviderTest";

   EMContentProvider mEMContentProvider;
   Context mMockContext;
   ContentResolver mMockResolver;
    private EMSQL mSql;
    private IEMBluetoothDatabase mDb;
   /**
    * Constructor.
    *
    * @param providerClass     The class name of the provider under test
    * @param providerAuthority The provider's authority string
    */
   
//    public EMContentProviderTest(Class<EMContentProvider> providerClass, String providerAuthority)
   public EMContentProviderTest(){
      super(EMContentProvider.class,EMContentProvider.Constants.AUTHORITY);
   }
   
   @Override
   public void setUp() throws Exception {
      super.setUp();
      mMockContext = getMockContext();
      mMockResolver = mMockContext.getContentResolver();

      // Spin up an EM provider as well and put it under the same mock test framework
      mEMContentProvider = this.getProvider();
      int dd = Data.datasize;
      ProviderInfo info = new ProviderInfo();
      info.authority = EMContentProvider.Constants.AUTHORITY;
      mEMContentProvider.attachInfo(mMockContext, info);
      dd = Data.datasize;
      assertNotNull(mEMContentProvider);
      ((MockContentResolver) mMockResolver)
              .addProvider(EMContentProvider.Constants.AUTHORITY, mEMContentProvider);
      //Log.i(TAG,"endsetup");
   }
   private void addALT(){
      IEMBluetoothAdvertisement ad = AltBeaconTestData.create(3, 0x5A00, 0x354D, -10, 50);
      mDb.addAdvertisement(ad);
      ad = AltBeaconTestData.create(3, 0x5A00, 0x354D, -10, 50);
      mDb.addAdvertisement(ad);
   }
   public void xtestaddALT(){
      // add embeacon
      // expect an embeacon
      mSql = EMSQL.create(mMockContext);
      mSql.Clear();
      mDb = EMBluetoothDatabase.create(mMockContext, mSql);
      oh = mEMContentProvider.getOpenHelper();
      addALT();
      noh = mEMContentProvider.getOpenHelper();
      Cursor c;
      c = mMockResolver.query(EMBeaconDevice.TABLE_CONTENT_URI(),null,null,null,null);
      //Log.i("emcpt", String.format("%s %d","Device",c.getCount()));
      assertEquals(1, c.getCount());
      assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMALTManufacturerData.TABLE_CONTENT_URI(),null,null,null,null);
      //Log.i("emcpt", String.format("%s %d", "Mdata", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMBeaconAdvertisement.TABLE_CONTENT_URI(),null,null,null,null);
      //Log.i("emcpt", String.format("%s %d", "Advert", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
   }
   private void addID(){
      IEMBluetoothAdvertisement ad = IDDataTestData.create(3, 0x5A00, 0x354D, -10);
      mDb.addAdvertisement(ad);
      ad = IDDataTestData.create(3, 0x5A00, 0x354D, -11);
      mDb.addAdvertisement(ad);
   }
   SQLiteOpenHelper oh;
   SQLiteOpenHelper noh;
   public void xtestaddID(){
      // add embeacon
      // expect an embeacon
      mSql = EMSQL.create(mMockContext);
      mSql.Clear();
      mDb = EMBluetoothDatabase.create(mMockContext, mSql);
      oh = mEMContentProvider.getOpenHelper();
      addID();
      noh = mEMContentProvider.getOpenHelper();
      Cursor c;
      c = mMockResolver.query(EMBeaconDevice.TABLE_CONTENT_URI(),null,null,null,null);
      assertNotNull("Cursor", c);
      //Log.i("emcpt", String.format("%s %d", "Device", c.getCount()));
      assertEquals(1, c.getCount());
      assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMIDManufacturerData.TABLE_CONTENT_URI(),null,null,null,null);
      assertNotNull("Cursor",c);
      //Log.i("emcpt", String.format("%s %d", "Mdata", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMBeaconAdvertisement.TABLE_CONTENT_URI(),null,null,null,null);
      //Log.i("emcpt", String.format("%s %d", "Advert", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
   }
   private void addEMB(){
      IEMBluetoothAdvertisement ad = EMBeaconTestData.create(3, "EMBeacon12345", 0xb011, 0x0033, 0x2233, 0x30, "02");
      mDb.addAdvertisement(ad);
      ad = EMBeaconTestData.create(3, "EMBeacon12345", 0xb011, 0x0033, 0x2233, 0x30, "02");
      mDb.addAdvertisement(ad);
   }
   public void xtestaddEMB(){
      // add embeacon
      // expect an embeacon
      mSql = EMSQL.create(mMockContext);
      mSql.Clear();
      mDb = EMBluetoothDatabase.create(mMockContext, mSql);
       oh = mEMContentProvider.getOpenHelper();
      addEMB();
      noh = mEMContentProvider.getOpenHelper();
      Cursor c;
      c = mMockResolver.query(EMBeaconDevice.TABLE_CONTENT_URI(),null,null,null,null);
      //Log.i("emcpt", String.format("%s %d","Device",c.getCount()));
      assertEquals(1, c.getCount());
      assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMBeaconManufacturerData.TABLE_CONTENT_URI(),null,null,null,null);
      //Log.i("emcpt", String.format("%s %d", "Mdata", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMBeaconAdvertisement.TABLE_CONTENT_URI(),null,null,null,null);
      //Log.i("emcpt", String.format("%s %d", "Advert", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
   }   

   void addEd(){
      Data.bytes namespace = Data.bytes.create("0102030405060708090a");
      Data.bytes id = Data.bytes.create("102030405060");
      String url = "emdeveloper\07";
      IEMBluetoothAdvertisement aduri = EDUIDTestData.create(3, -20, namespace.value(), id.value());
      IEMBluetoothAdvertisement adurl = EDURLTestData.create(3, -21, 3, url);
      IEMBluetoothAdvertisement adtlm = EDTLMTestData.create(3, 3000, 20, 1000, 10000);
      mDb.addAdvertisement(aduri);
      mDb.addAdvertisement(adurl);
      mDb.addAdvertisement(adtlm);      
      mDb.addAdvertisement(aduri);
      mDb.addAdvertisement(adurl);
      adtlm = EDTLMTestData.create(3,3000,20,1001,10010);      
      mDb.addAdvertisement(adtlm);      
   }
   public void xtestEd(){
      // add embeacon
      // add embeacon
      // expect an embeacon
      mSql = EMSQL.create(mMockContext);
      mSql.Clear();
      mDb = EMBluetoothDatabase.create(mMockContext, mSql);
      oh = mEMContentProvider.getOpenHelper();
      addEd();
      Cursor c;
      c = mMockResolver.query(EMBeaconDevice.TABLE_CONTENT_URI(),null,null,null,null);
      assertNotNull("Cursor", c);
      //Log.i(TAG, String.format("%s %d", "Device", c.getCount()));
      // assertEquals(1, c.getCount());
      // assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMBeaconAdvertisement.TABLE_CONTENT_URI(),null,null,null,null);
      //Log.i(TAG, String.format("%s %d", "Advert", c.getCount()));
     assertEquals(6, c.getCount());
     assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMEDURLManufacturerData.TABLE_CONTENT_URI(),null,null,null,null);
      assertNotNull("Cursor",c);
      //Log.i(TAG, String.format("%s %d", "EDURLdata", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMEDUIDManufacturerData.TABLE_CONTENT_URI(),null,null,null,null);
      assertNotNull("Cursor",c);
      //Log.i(TAG, String.format("%s %d", "EDURIdata", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMEDTLMManufacturerData.TABLE_CONTENT_URI(),null,null,null,null);
      assertNotNull("Cursor", c);
      //Log.i(TAG, String.format("%s %d", "EDTLMdata", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
   }      
   public void testaddall(){
      // add embeacon
      // add embeacon
      // expect an embeacon
      mSql = EMSQL.create(mMockContext);
      mSql.Clear();
      mDb = EMBluetoothDatabase.create(mMockContext, mSql);
      oh = mEMContentProvider.getOpenHelper();
      addALT();
      addEMB();
      noh = mEMContentProvider.getOpenHelper();
      addID();
      addEd();
      Cursor c;
      c = mMockResolver.query(EMBeaconDevice.TABLE_CONTENT_URI(),null,null,null,null);
      assertNotNull("Cursor", c);
      //Log.i(TAG, String.format("%s %d", "Device", c.getCount()));
      // should be 6?
       assertEquals(6, c.getCount());
       assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMBeaconAdvertisement.TABLE_CONTENT_URI(),null,null,null,null);
      //Log.i(TAG, String.format("%s %d", "Advert", c.getCount()));
      assertEquals(12, c.getCount());
      assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMIDManufacturerData.TABLE_CONTENT_URI(),null,null,null,null);
      assertNotNull("Cursor", c);
      //Log.i(TAG, String.format("%s %d", "IDdata", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMALTManufacturerData.TABLE_CONTENT_URI(),null,null,null,null);
      assertNotNull("Cursor", c);
      //Log.i(TAG, String.format("%s %d", "ALTdata", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMBeaconManufacturerData.TABLE_CONTENT_URI(),null,null,null,null);
      //Log.i(TAG, String.format("%s %d", "EMdata", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMEDURLManufacturerData.TABLE_CONTENT_URI(),null,null,null,null);
      assertNotNull("Cursor",c);
      //Log.i(TAG, String.format("%s %d", "EDURLdata", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMEDUIDManufacturerData.TABLE_CONTENT_URI(),null,null,null,null);
      assertNotNull("Cursor",c);
      //Log.i(TAG, String.format("%s %d", "EDURIdata", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
      c = mMockResolver.query(EMEDTLMManufacturerData.TABLE_CONTENT_URI(),null,null,null,null);
      assertNotNull("Cursor", c);
      //Log.i(TAG, String.format("%s %d", "EDTLMdata", c.getCount()));
     assertEquals(2, c.getCount());
     assertTrue(c.moveToFirst());
   };

   private void addAdvertisement(int n){
      IEMBluetoothAdvertisement iad = adverts.get(n);
      EMBluetoothAdvertisement ad = (EMBluetoothAdvertisement)iad;
      mDb.addAdvertisement(iad);
   }
   public static ArrayList<Data.Advert> advdata;
   public static ArrayList<IEMBluetoothAdvertisement>  adverts;
   public static IEMBluetoothAdvertisement createadvert(int i, int rssi, Data.Advert ad){
      long st = System.currentTimeMillis();
      while(st == System.currentTimeMillis());
      long currenttime  = System.currentTimeMillis();
      Long ct = Long.valueOf(currenttime);
      String address = String.format("01:02:03:04:05:%02x",i & 0xff);
      IEMBluetoothDevice iemd = EMBluetoothDevice.create(address);
      IEMBluetoothAdvertisement adv = EMBluetoothAdvertisement.create(iemd, rssi,ct, ad.getdata());
      return adv;
   }
   static int datasize;
   static {
      adverts = new ArrayList<IEMBluetoothAdvertisement>(100);
      datasize = 0;
      adverts.add(datasize++, AltBeaconTestData.create(3, 0x5a00, 0x4d35, -10, 50));
      adverts.add(datasize++, IDDataTestData.create(3, 0x5a00, 0x4d35, -12));
      adverts.add(datasize++, EMBeaconTestData.create(3, "EMBeacon12345", 0xb011, 0x0033, 0x2233, 0x30, "02"));

   }

   
}
