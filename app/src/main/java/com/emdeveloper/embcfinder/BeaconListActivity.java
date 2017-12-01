/*
 ** ############################################################################
 **
 ** file    BeaconListActivity.java
 ** brief   activity for displaying the list of beacons
 **
 ** Copyright (c) 2015 EM Microelectronic-US Inc. All rights reserved.
 ** Developed by Glacier River Design, LLC.
 **
 ** ############################################################################
 ** EM Microelectronic-US Inc. License Agreement
 ** 
 ** Please read this License Agreement ("Agreement") carefully before 
 ** accessing, copying, using, incorporating, modifying or in any way providing 
 ** ("Using" or "Use") this source code.  By Using this source code, you: (i) 
 ** warrant and represent that you have obtained all authorizations and other 
 ** applicable consents required empowering you to enter into and (ii) agree to 
 ** be bound by the terms of this Agreement.  If you do not agree to this 
 ** Agreement, then you are not permitted to Use this source code, in whole or 
 ** in part.
 ** 
 ** Pursuant to the terms in the accompanying software license agreement and 
 ** Terms of Use located at: www.emdeveloper.com/emassets/emus_termsofuse.html 
 ** (the terms of each are incorporated herein by this reference) and subject to 
 ** the disclaimer and limitation of liability set forth below, EM  
 ** Microelectronic US Inc. ("EM"), grants strictly to you, without the right to 
 ** sublicense, a non-exclusive, non-transferable, revocable, worldwide license 
 ** to use the source code to modify the software program for the sole purpose 
 ** of developing object and executable versions that execute solely and 
 ** exclusively on devices manufactured by or for EM or your products that use 
 ** or incorporate devices manufactured by or for EM; provided that, you clearly 
 ** notify third parties regarding the source of such modifications or Use.
 ** 
 ** Without limiting any of the foregoing, the name "EM Microelectronic-US 
 ** Inc." or that of any of the EM Parties (as such term is defined below) must 
 ** not be Used to endorse or promote products derived from the source code 
 ** without prior written permission from an authorized representative of EM 
 ** Microelectronic US Inc.
 ** 
 ** THIS SOURCE CODE IS PROVIDED "AS IS" AND "WITH ALL FAULTS", WITHOUT ANY 
 ** SUPPORT OR ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED 
 ** TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 ** PURPOSE ARE DISCLAIMED.  ALSO, THERE IS NO WARRANTY OF NON-INFRINGEMENT, 
 ** TITLE OR QUIET ENJOYMENT.
 ** 
 ** IN NO EVENT SHALL EM MICROELECTRONIC US INC., ITS AFFILIATES, PARENT AND 
 ** ITS/THEIR RESPECTIVE LICENSORS, THIRD PARTY PROVIDERS, REPRESENTATIVES, 
 ** AGENTS AND ASSIGNS ("COLLECTIVLEY, "EM PARTIES") BE LIABLE FOR ANY DIRECT, 
 ** INDIRECT, INCIDENTAL, SPECIAL, PUNITIVE, EXEMPLARY, OR CONSEQUENTIAL 
 ** DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 ** SERVICES; LOSS OF USE, DATA, EQUIPMENT, SYSTEMS, SOFTWARE, TECHNOLOGY, 
 ** SERVICES, GOODS, CONTENT, MATERIALS OR PROFITS; BUSINESS INTERRUPTION OR 
 ** OTHER ECONOMIC LOSS OR ANY CLAIMS BY THIRD PARTIES (INCLUDING BUT NOT 
 ** LIMITED TO ANY DEFENSE THEREOF) HOWEVER CAUSED AND ON ANY THEORY OF 
 ** LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 ** NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOURCE 
 ** CODE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  NOTWITHSTANDING 
 ** ANYTHING ELSE TO THE CONTRARY, IN NO EVENT WILL THE EM PARTIES' AGGREGATE 
 ** LIABILITY UNDER THIS AGREEMENT OR ARISING OUT OF YOUR USE OF THE SOURCE 
 ** CODE EXCEED ONE HUNDRED U.S. DOLLARS (U.S. $100).
 ** 
 ** Please refer to the accompanying software license agreement and Terms of 
 ** Use located at: www.emdeveloper.com/emassets/emus_termsofuse.html to better 
 ** understand all of your rights and obligations hereunder. 
 ** ############################################################################
 */
package com.emdeveloper.embcfinder;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.emdeveloper.embeaconlib.EMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.bluetooth.EMBluetoothLeService;
import com.emdeveloper.embeaconlib.database.EMBluetoothDatabase;
import com.emdeveloper.embeaconlib.database.EMSQL;
import com.emdeveloper.embeaconlib.database.IEMBluetoothDatabase;
import com.emdeveloper.embeaconlib.embeaconspecific.Utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/*import com.emdeveloper.embeaconlib.data.AltBeaconTestData;
import com.emdeveloper.embeaconlib.data.Data;
import com.emdeveloper.embeaconlib.data.EDTLMTestData;
import com.emdeveloper.embeaconlib.data.EDUIDTestData;
import com.emdeveloper.embeaconlib.data.EDURLTestData;
import com.emdeveloper.embeaconlib.data.EMBeaconTestData;
import com.emdeveloper.embeaconlib.data.IDDataTestData;
*/

/**
 * An activity representing a list of Beacons. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BeaconDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link BeaconListFragment} and the item details
 * (if present) is a {@link BeaconDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link BeaconListFragment.Callbacks} interface
 * to listen for item selections.
 * <p/>
 * <p/>
 * It creates Advertisement Database and hooks them together with
 * \code{.java}
 * mSql = new EMSQL(this);
 * mSql.Clear();
 * EMBluetoothDatabase db = new EMBluetoothDatabase();
 * db.create(this,mSql);
 * \endcode
 * <p/>
 * It starts the *EMBluetoothLeService*
 * \code{.java}
 * Intent EMBluetoothLeServiceIntent = new Intent(this, EMBluetoothLeService.class);
 * bindService(EMBluetoothLeServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
 * \endcode
 * <p/>
 * It connects to the service in \ref emServiceConnection
 * It handles starting and stopping scanning for beacons via the menu and show the *scanning* indicator
 */
public class BeaconListActivity extends Activity
    implements BeaconListFragment.Callbacks, IDBCallBacks
{
    private final static String TAG = "BeaconListActivity";
    /**
     * @brief Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
	private Switch sw_log;
	private boolean logOn = false;

    /**
     * @{
     * Fields related to the embeaconlib
     */
    private EMSQL mSql;
    private IEMBluetoothDatabase mDb;
    private EMBluetoothLeService mBluetoothLeService;
    /** @} */

    /**
     * @brief 
     * @{
     * Fields to handle displaying the scanning state
     * 
     */
    private static final long SCAN_ON_PERIOD = 3000;
    private static final long SCAN_OFF_PERIOD = 2000;
    private boolean mInScan;
    private boolean mScanning;
    private Handler mmHandler;
    /** @} */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_list);
        
        BuildConfig bc ;
        Utils.context = this.getApplicationContext();

        
        /**
         *  @brief Set up the embeaconlib
         *  <a name="SetupEmbeaconlib"> Setting up the EmBeacon Lib </a>
         *
         */
        mSql = EMSQL.create(Utils.context);
        mSql.Clear();
        IEMBluetoothDatabase db = EMBluetoothDatabase.create(Utils.context, mSql);
        

        mDb =db;
        if(BuildConfig.FLAVOR.equals("testConfig") || BuildConfig.FLAVOR.equals("nohardware")) {
//            copyDataBase(EMSQL.DATABASE_NAME);
        //    mSql = createDataBaseFromAdvertisements(EMSQL.ADVERTISEMENT_FILE_NAME);
            addEMB();
            addALT();
            addID();
            addEd();
        }else {
        }
        /**
         * @brief Connect to the EMBluetoothLeService
         *
         */
        if(BuildConfig.FLAVOR != "nohardware") {
            Intent EMBluetoothLeServiceIntent = new Intent(Utils.context, EMBluetoothLeService.class); ///< intent for the EMBluetoothLeService
            bindService(EMBluetoothLeServiceIntent, mServiceConnection, BIND_AUTO_CREATE); ///< start and bind the EMBluetoothLeService \see mServiceConnection
        }
        sw_log = (Switch) this.findViewById(R.id.sw_log); 

        sw_log.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if(!isChecked) {
                    logOn = false;
                 }
                 else {
                    logOn = true;
                 }
                 getEMBluetoothDatabase().enableLog(logOn);
              }
           });

        if (findViewById(R.id.beacon_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((BeaconListFragment) getFragmentManager()
                    .findFragmentById(R.id.beacon_list))
                    .setActivateOnItemClick(true);
        }
        Log.d(TAG,"onCreate");


    }
    /**
     * @brief onDestroy called when we go away
     */
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(BuildConfig.FLAVOR != "nohardware") {
           try {
              unregisterReceiver(mEMBluetoothLeServiceUpdateReceiver);
              unbindService(mServiceConnection);
           } catch(Exception e){
           }
        }
        mDb = null;
        mBluetoothLeService = null;
        mSql = null;
        Log.d(TAG,"onDestroy");
    }

    /**
     * Callback method from {link BeaconListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String address, String time,String name,String type) {
    //    arguments.putString(BeaconDetailFragment.ARG_ADDRESS, id);

       boolean loadview = true;

       try{
          if(Integer.decode(type) != IEMBluetoothAdvertisement.EMBEACON_TYPE_ID) {
            loadview = false;
        }
       } catch (Exception e) {
          loadview = false;
       }
       if(! loadview) {
          // clear the view
          Fragment f = getFragmentManager().findFragmentById(R.id.beacon_detail_container);
          if(f != null) {
             getFragmentManager().beginTransaction().remove(f).commit();
          }
          return;
       }
        // stop scanning
       // TODO do we really want to do this
        stopLeScan();
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(BeaconDetailFragment.ARG_ADDRESS, address);
            arguments.putString(BeaconDetailFragment.ARG_TIME,time);
            arguments.putString(BeaconDetailFragment.ARG_NAME,name);
            BeaconDetailFragment fragment = new BeaconDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.beacon_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, BeaconDetailActivity.class);
            detailIntent.putExtra(BeaconDetailFragment.ARG_TIME, time);
            detailIntent.putExtra(BeaconDetailFragment.ARG_ADDRESS,address);
            detailIntent.putExtra(BeaconDetailFragment.ARG_NAME,name);
            startActivity(detailIntent);
        }
        
    }

    /**
     * 
     * @{
     * Menu handling methods
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.beacon_list_activity_menu, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setVisible(true);
            menu.findItem(R.id.menu_refresh_action).setActionView(null).setVisible(false);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setVisible(true);
            menu.findItem(R.id.menu_refresh_action).setActionView(R.layout.actionbar_indeterminate_progress).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                startLeScan();
                return true;
            case R.id.menu_stop:
                stopLeScan();
                return true;
            case R.id.menu_refresh:
               mSql.Clear();
               return true;
            case R.id.menu_info:
                Intent intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /** @} */




    /**
     *@{
     *  Routines for handling the broadcasts from the EMBluetoothLeService
     */
    /**
     * @brief makeGattUpdateIntentFilter sets up the filter for broadcasts we want
     */
    private static IntentFilter makeEMBluetoothLeServiceUpdateIntentFilter()
    {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EMBluetoothLeService.ACTION_RESET_BLUETOOTH);
        intentFilter.addAction(EMBluetoothLeService.ACTION_BLUETOOTH_ON);
        return intentFilter;
    }
    /**
     * @brief mEMBluetoothLeServiceUpdateReceiver receives broadcasts from the EMBluetoothLeService.
     */
    private final BroadcastReceiver mEMBluetoothLeServiceUpdateReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                final String action = intent.getAction();
                if (EMBluetoothLeService.ACTION_RESET_BLUETOOTH.equals(action)) {
                    ResetBluetooth();
                }
                if (EMBluetoothLeService.ACTION_BLUETOOTH_ON.equals(action)) {
                    startLeScan();
                }
            }
        };
    /** @} */
    
    /**
     * @brief mServiceConnection.onServiceConnected is called when the service is connected
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection()
        {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service)
            {
                mBluetoothLeService = ((EMBluetoothLeService.LocalBinder) service).getService();
                // tell the service to use this database.
                if (!mBluetoothLeService.initialize(mDb))
                    {
                        Log.e(TAG, "Unable to initialize Bluetooth");
                        MakeToast(R.string.ble_not_supported);
                        finish();
                    }
                // register the mEMBluetoothLeServiceUpdateReceiver to receive messages
                registerReceiver(mEMBluetoothLeServiceUpdateReceiver, makeEMBluetoothLeServiceUpdateIntentFilter());
                // Start Scanning
                startLeScan();

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName)
            {
                // register the mEMBluetoothLeServiceUpdateReceiver to receive messages
               unregisterReceiver(mEMBluetoothLeServiceUpdateReceiver);
               mBluetoothLeService = null;
            }
        };

    /**
     * @{
     * Methods to handle scanning for beacons
     *
     *
     *  On android 4.4 we stop and start the scanning
     * every SCAN_PERIOD seconds so we can get live updates.
     * 
     */    
    private boolean mscancycle;
    private Runnable scanRunnable = new Runnable() {
            @Override
            public void run() {
               if(mmHandler != null)
                  mmHandler.removeCallbacks(this);
                if(mScanning) {
                   //Log.i(TAG, "Toggling scan off and on");
                    if(mBluetoothLeService != null) {
                        if (mscancycle) {
                            mBluetoothLeService.startLeScan();
                            mscancycle = false;
                            mmHandler.postDelayed(this, SCAN_ON_PERIOD);
                        } else {
                            mBluetoothLeService.stopLeScan();
                            mscancycle = true;
                            mmHandler.postDelayed(this, SCAN_OFF_PERIOD);
                        }
                    }
                }
            }
        };
    /**
     * @brief start Scanning
     * start scanning and schedule the runnable after SCAN_PERIOD
     * set the menus and mScanning
     */
    private void startLeScan(){
        if(!mScanning) {
           if(mBluetoothLeService != null)
              mBluetoothLeService.startLeScan();
            mmHandler = new Handler();
            mscancycle=true;
            mmHandler.postDelayed(scanRunnable, SCAN_ON_PERIOD);
            mScanning = true;
        }
        invalidateOptionsMenu();
        mInScan = true;
    }
    /**
     * @brief stop Scanning
     * remove the scanRunnable callback
     * set the menus and mScanning
     */
    private void stopLeScan(){
        mScanning = false;
        mInScan = false;
        if(mmHandler != null)
           mmHandler.removeCallbacks(scanRunnable);
        if(mBluetoothLeService != null)
            mBluetoothLeService.stopLeScan();
        invalidateOptionsMenu();
    }
    /** @} */

    
    private void MakeToast(int id){
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
    private void ResetBluetooth(){
        mBluetoothLeService.ResetBluetooth(this);
    }

    @Override
    public EMSQL getEMSQL() {
        return mSql;
    }

    /**
     * @brief gets the EMBluetoothDatabase for testing
     * @return returns the database
     *
     * 
     */
    public IEMBluetoothDatabase getEMBluetoothDatabase(){
        return mDb;
    }
    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     *
     *
     * Debug Only
     * Stolen
     * */
    private void copyDataBase(String dbname) {
        try {
            // Open your local db as the input stream
            InputStream myInput = getAssets().open(dbname);
            // Path to the just created empty db
            String outFileName = "/data/data/com.emdeveloper.embcfinder/databases/" + dbname;
            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e){
            Log.d(TAG,"cant find database");
        }
    }
    private EMSQL createDataBaseFromAdvertisements(String advfilename){

        EMSQL sqldb = EMSQL.create(Utils.context);
        // read a line
        try {
            InputStream myInput = getAssets().open(advfilename);

            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput));

            String line = null;
            while ((line = br.readLine()) != null) {
                if(line.charAt(0) != '#') {
                        IEMBluetoothAdvertisement adv = EMBluetoothAdvertisement.undump(line);
                        sqldb.addAdvertisement(adv);
                }
            }

        } catch (Exception e) {
            sqldb = null;
        }
        return sqldb;
    }

    /**
     * Instantiates a new Beacon list activity.
     */
    public BeaconListActivity() {
        super();
    }

    /**
     * Called after {link #onCreate} &mdash; or after {link #onRestart} when
     * the activity had been stopped, but is now again being displayed to the
     * user.  It will be followed by {link #onResume}.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * see #onCreate
     * see #onStop
     * see #onResume
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either {link #onRestart}, {link #onDestroy}, or nothing,
     * depending on later user activity.
     * <p/>
     * <p>Note that this method may never be called, in low memory situations
     * where the system does not have enough memory to keep your activity's
     * process running after its {link #onPause} method is called.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * see #onRestart
     * see #onResume
     * see #onSaveInstanceState
     * see #onDestroy
     */
    @Override
    protected void onStop() {
        super.onStop();
        stopLeScan();
        Log.d(TAG,"onStop");
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLeScan();
        Log.d(TAG,"onPause");
    }

    /**
     * Called after {link #onRestoreInstanceState}, {link #onRestart}, or
     * {link #onPause}, for your activity to start interacting with the user.
     * This is a good place to begin animations, open exclusive-access devices
     * (such as the camera), etc.
     * <p/>
     * <p>Keep in mind that onResume is not the best indicator that your activity
     * is visible to the user; a system window such as the keyguard may be in
     * front.  Use {link #onWindowFocusChanged} to know for certain that your
     * activity is visible to the user (for example, to resume a game).
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * see #onRestoreInstanceState
     * see #onRestart
     * see #onPostResume
     * see #onPause
     */
    @Override
    protected void onResume() {
        super.onResume();
        startLeScan();
        Log.d(TAG,"onResume");
    }
    private void addALT(){
        //IEMBluetoothAdvertisement ad = AltBeaconTestData.create(3, 0x5A00, 0x354D, -10, 50);
        //mDb.addAdvertisement(ad);
        //ad = AltBeaconTestData.create(3, 0x5A00, 0x354D, -10, 50);
        // mDb.addAdvertisement(ad);
        // ad = AltBeaconTestData.create(4, 0x5A00, 0x354e, -10, 50);
        // mDb.addAdvertisement(ad);
        // ad = AltBeaconTestData.create(5, 0x5A00, 0x354f, -10, 50);
        // mDb.addAdvertisement(ad);
    }
    private void addID(){
        //IEMBluetoothAdvertisement ad = IDDataTestData.create(3, 0x5A00, 0x354D, -10);
        //mDb.addAdvertisement(ad);
        //ad = IDDataTestData.create(3, 0x5A00, 0x354D, -11);
        // mDb.addAdvertisement(ad);
        // ad = IDDataTestData.create(4, 0x5A00, 0x354D, -11);
        // mDb.addAdvertisement(ad);
        // ad = IDDataTestData.create(5, 0x5A00, 0x354D, -11);
        // mDb.addAdvertisement(ad);
    }
    private void addEMB(){
        //IEMBluetoothAdvertisement ad = EMBeaconTestData.create(3, "EMBeacon12345", 0xb011, 0x0033, 0x2233, 0x30, "02");
        //mDb.addAdvertisement(ad);
        //ad = EMBeaconTestData.create(3, "EMBeacon12345", 0xb011, 0x0033, 0x2233, 0x30, "02");
        // mDb.addAdvertisement(ad);
        // ad = EMBeaconTestData.create(4, "EMBeacon12345", 0xb011, 0x0033, 0x2233, 0x30, "02");
        // mDb.addAdvertisement(ad);
        // ad = EMBeaconTestData.create(5, "EMBeacon12345", 0xb011, 0x0033, 0x2233, 0x30, "02");
        // mDb.addAdvertisement(ad);
    }

    /**
     * Add ed.
     */
    void addEd(){
        //Data.bytes namespace = Data.bytes.create("0102030405060708090a");
        //Data.bytes id = Data.bytes.create("102030405060");
        //String url = "emdeveloper\07";
        //IEMBluetoothAdvertisement aduri = EDUIDTestData.create(3, -20, namespace.value(), id.value());
        //IEMBluetoothAdvertisement adurl = EDURLTestData.create(3, -21, 3, url);
        //IEMBluetoothAdvertisement adtlm = EDTLMTestData.create(3, 3000, 20, 1000, 10000);
        //mDb.addAdvertisement(aduri);
        //mDb.addAdvertisement(adurl);
        //mDb.addAdvertisement(adtlm);
        // mDb.addAdvertisement(aduri);
        // mDb.addAdvertisement(adurl);
        // adtlm = EDTLMTestData.create(3, 3000, 20, 1001, 10010);
        // mDb.addAdvertisement(adtlm);

        // aduri = EDUIDTestData.create(4, -20, namespace.value(), id.value());
        // adurl = EDURLTestData.create(4, -21, 3, url);
        // adtlm = EDTLMTestData.create(4, 3000, 20, 1000, 10000);
        // mDb.addAdvertisement(aduri);
        // mDb.addAdvertisement(adurl);
        // mDb.addAdvertisement(adtlm);

        // aduri = EDUIDTestData.create(5, -20, namespace.value(), id.value());
        // adurl = EDURLTestData.create(5, -21, 3, url);
        // adtlm = EDTLMTestData.create(5, 3000, 20, 1000, 10000);
        // mDb.addAdvertisement(aduri);
        // mDb.addAdvertisement(adurl);
        // mDb.addAdvertisement(adtlm);

        // aduri = EDUIDTestData.create(6, -20, namespace.value(), id.value());
        // adurl = EDURLTestData.create(6, -21, 3, url);
        // adtlm = EDTLMTestData.create(6, 3000, 20, 1000, 10000);
        // mDb.addAdvertisement(aduri);
        // mDb.addAdvertisement(adurl);
        // mDb.addAdvertisement(adtlm);


    }
}
