package com.emdeveloper.embcfinder;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ProviderInfo;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;

import com.emdeveloper.embeaconlib.data.AltBeaconTestData;
import com.emdeveloper.embeaconlib.data.Data;
import com.emdeveloper.embeaconlib.data.EDTLMTestData;
import com.emdeveloper.embeaconlib.data.EDUIDTestData;
import com.emdeveloper.embeaconlib.data.EDURLTestData;
import com.emdeveloper.embeaconlib.data.EMBeaconTestData;
import com.emdeveloper.embeaconlib.data.IDDataTestData;
import com.emdeveloper.embeaconlib.database.EMContentProvider;
import com.emdeveloper.embeaconlib.database.EMSQL;
import com.emdeveloper.embeaconlib.database.IEMBluetoothDatabase;

public class BeaconListActivityTest extends ActivityInstrumentationTestCase2<BeaconListActivity> {
   private final String TAG = "BeaconListActivityTest";
   MyMockContext mMockContext;
   ContentResolver mMockResolver;
    EMContentProvider mEMContentProvider;
    private EMSQL mSql;
    private IEMBluetoothDatabase mDb;

    /**
     * Creates an {@link android.test.ActivityInstrumentationTestCase2}.
     *
     * @param activityClass The activity to test. This must be a class in the instrumentation
     *                      targetPackage specified in the AndroidManifest.xml
     */
    public BeaconListActivityTest(Class<BeaconListActivity> activityClass) {
        super(activityClass);
    }
   private void addALT(){
      IEMBluetoothAdvertisement ad = AltBeaconTestData.create(3, 0x5A00, 0x354D, -10, 50);
      mDb.addAdvertisement(ad);
      ad = AltBeaconTestData.create(3, 0x5A00, 0x354D, -10, 50);
      mDb.addAdvertisement(ad);
   }
   private void addID(){
      IEMBluetoothAdvertisement ad = IDDataTestData.create(3, 0x5A00, 0x354D, -10);
      mDb.addAdvertisement(ad);
      ad = IDDataTestData.create(3, 0x5A00, 0x354D, -11);
      mDb.addAdvertisement(ad);
   }
   private void addEMB(){
      IEMBluetoothAdvertisement ad = EMBeaconTestData.create(3, "EMBeacon12345", 0xb011, 0x0033, 0x2233, 0x30, "02");
      mDb.addAdvertisement(ad);
      ad = EMBeaconTestData.create(3, "EMBeacon12345", 0xb011, 0x0033, 0x2233, 0x30, "02");
      mDb.addAdvertisement(ad);
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
    public BeaconListActivityTest() {
        super(BeaconListActivity.class);
    }


    public void setUp() throws Exception {
        super.setUp();
        oldcontext = getContext();
      mMockContext = new MyMockContext(getContext());
      mMockResolver = mMockContext.getContentResolver();

      // Spin up an EM provider as well and put it under the same mock test framework
     // mEMContentProvider = this.getProvider();
      int dd = Data.datasize;
      ProviderInfo info = new ProviderInfo();
      info.authority = EMContentProvider.Constants.AUTHORITY;
        ContentProvider cp = this.getContext().getContentResolver().acquireContentProviderClient("com.emdeveloper.embeaconlib.xxxxx").getLocalContentProvider();
        mEMContentProvider = (EMContentProvider)cp;
     // mEMContentProvider.attachInfo(mMockContext, info);
    //  assertNotNull(mEMContentProvider);
    //  ((MockContentResolver) mMockResolver)
     //         .addProvider(EMContentProvider.Constants.AUTHORITY, mEMContentProvider);
        //Log.i(TAG, "endsetup");
    }

    public void tearDown() throws Exception {

    }
   SQLiteOpenHelper oh;
   SQLiteOpenHelper noh;

    public void testDummyData() throws Exception {
        mycontext = new MyMockContext(getInstrumentation().getTargetContext());
        setContext(mycontext);
        Intent mActivityIntent = new Intent(mycontext,BeaconListActivity.class);

        BeaconListActivity activity =  this.getActivity();
        mSql = activity.getEMSQL();
        mSql.Clear();
        mDb = activity.getEMBluetoothDatabase();
        oh = mEMContentProvider.getOpenHelper();
        addALT();
        addEMB();
        addID();
        addEd();
        noh = mEMContentProvider.getOpenHelper();
    }

    //@Override
    public void setContext(Context context) {
       // this.getInstrumentation().getContext();
       //getInstrumentation().
    }

   // @Override
    public Context getContext() {
        return getInstrumentation().getContext();
    }
    Context oldcontext;
    MyMockContext mycontext;
    public static class MyMockContext extends ContextWrapper {
        public MyMockContext(Context base) {
            super(base);
        }

        @Override
        public ComponentName startService(Intent service) {
            return super.startService(service);
        }

        @Override
        public boolean bindService(Intent service, ServiceConnection conn, int flags) {
            return super.bindService(service, conn, flags);
        }
    }
    
}
