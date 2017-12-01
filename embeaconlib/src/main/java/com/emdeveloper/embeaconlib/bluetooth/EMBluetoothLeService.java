/*
 ** ############################################################################
 **
 ** file    EMBluetoothLeService.java
 ** brief   The interface to the Android bluetooth system
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
package com.emdeveloper.embeaconlib.bluetooth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
// these are for android 5.0 and above 
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.app.Service;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.Choreographer;

import com.emdeveloper.embeaconlib.EMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.EMBluetoothDevice;
import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.IEMBluetoothDevice;
import com.emdeveloper.embeaconlib.database.IEMBluetoothDatabase;


/**
 *  @brief This class interacts with the Android bluetooth system
 *
 * It uses the following classes and methods:
 *   - BluetoothManager
 *   - BluetoothAdapter
 *      - enable
 *      - disable
 *      - startLeScan
 *      - stopLeScan
 *      - BluetoothAdapter.LeScanCallback onLeScan for scan notifications
 *   - BroadcastReceiver for changes in the Bluetooth device state
 *
 * It communicates with the application via
 *   service binding
 *   enable,disable Bluetooth
 *   startLeScan
 *   stopLeScan
 *
 *   it broadcasts the ACTION_BLUETOOTH_* messages
 *
 *   it listens for BluetoothAdapter.ACTION_STATE_CHANGED broadcasts
 *   
 */
public class EMBluetoothLeService extends Service implements IEMBluetoothLeService {
    private final static String TAG = EMBluetoothLeService.class.getSimpleName();


    Choreographer mcor;

    private final IBinder mBinder = new LocalBinder();
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private ServiceState mState;
    private IEMAddAdvertisement mAddAdvertisement;
   private BluetoothLeScanner mScanner;

   private ScanSettings mScanSettings;
   private List<ScanFilter> mScanFilters;
   
    /**
     * uuids to scan for
     */
    private UUID[] mUUIDS;

    public EMBluetoothLeService() {
    }
/**
 * @brief the service onCreate callback
 */
    public void onCreate(){
        super.onCreate();
    }
    /**
     *  @brief binder returned to the application for the service connection.
     *
     * 
     */
    public class LocalBinder extends Binder {
        /** @brief returns the service */
        public EMBluetoothLeService getService() {
            return EMBluetoothLeService.this;
        }
    }

    /**  @brief the onBind callback
     *  @return the binder
     *
     */
    @Override
    public IBinder onBind(Intent intent) {
        mState = ServiceState.BOUND;
        
        return mBinder;
    }
    /**  @brief onUnbind callback,
     *
     */
    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
      //  close();
        mState = ServiceState.UNBOUND;
        unregisterReceiver(mReceiver);
        return super.onUnbind(intent);
    }
    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     * @param mDb database to add advertisements
     */
    public boolean initialize(IEMAddAdvertisement mDb) {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.e(TAG, "BLE not supported.");
            return false;
        }
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        registerReceiver(mReceiver, makeBluetoothIntentFilter());
        mAddAdvertisement = mDb;
        mScanner = mBluetoothAdapter.getBluetoothLeScanner();
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        //builder.setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES);
       // builder.setScanResultType(ScanSettings.SCAN_RESULT_TYPE_FULL);

        /// delay only works on api > 23
        builder.setReportDelay(0); // 1 second?
        mScanSettings = builder.build();

        mScanFilters = new ArrayList<ScanFilter>();
        
        startLeScan();
        return true;
    }
    /** @brief starts a scan
    */
    public void startLeScan(){
       new Thread(new Runnable() {
             public void run() {
                stopLeScan();
                //noinspection deprecation
                //Log.i(TAG, "startLeScan");
                // mBluetoothAdapter.startLeScan(mUUIDS,mLeScanCallback);
                mScanner.startScan(mScanFilters,mScanSettings,mLeScanCallback);
                mScanning = true;
        }
          }).start();
    }
    /** @brief stops a scan
    */
    public void stopLeScan(){
        //noinspection deprecation
       //Log.i(TAG, "stopLeScan");
       //mBluetoothAdapter.stopLeScan(mLeScanCallback);
       mScanner.stopScan(mLeScanCallback);
    }
    /** @brief enables the bluetooth interface
     */
    public void enable()
    {
        mBluetoothAdapter.enable();
    }
    /**  @brief disables the bluetooth interface
     */
    public void disable()
    {
        mBluetoothAdapter.disable(); 
    }
    /**
     * @brief resets the bluetooth interface
     * 
     */
    @Override
    public void ResetBluetooth(Context c) {
       /// not implemented
    }
   private void addAdvertisement(final BluetoothDevice device, final int rssi, final long time, final byte[] scanRecord){
      if(mAddAdvertisement != null) {
         new Thread(new Runnable() {
               public void run() {
                  mAddAdvertisement.addAdvertisement(device, rssi, time , scanRecord);
               }
            }).start();
      }
   }

    /** @brief Scan Callback
     * called when a bluetooth advertisement is received
     *
     * creates an EMBluetoothAdvertisement instance
     * adds it to the database
     * broadcasts the data
    */
    private ScanCallback mLeScanCallback =
        new ScanCallback() {
            /* 
             *  * @see BluetoothLeScanner#startScan
             */
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                final int rssi = result.getRssi();
                final byte[] scanRecord = result.getScanRecord().getBytes();
                final BluetoothDevice device = result.getDevice();
                    //final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                Log.i(TAG,String.format("Le Scan Callback %s %d", device.getAddress(),rssi));
                long currentTime;
                currentTime  = System.currentTimeMillis();
                final Long ct = currentTime;
                // This runs on the BluetoothThread, don't block
                // run the addAdvertisement on a different thread 
                addAdvertisement(device, rssi, ct, scanRecord);
            }
            @Override
           public void onScanFailed(int errorCode) {
               Log.i(TAG,String.format("onScanFailed %d",errorCode));
           }
            @Override
           public void onBatchScanResults(List<ScanResult> results) {
               Log.i(TAG,String.format("onBatchScanResults"));
           }
        };
    /**
     * @brief Broadcast Receiver for getting changes in the bluetooth interface
    */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
        {
           @Override
            public void onReceive(Context context, Intent intent)
            {
                final String action = intent.getAction();
                Log.i(TAG, String.format("Broadcast receiver action %s",action));

                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    final int state = intent.getExtras().getInt(BluetoothAdapter.EXTRA_STATE);
                    Log.i(TAG, String.format("Broadcast receiver action %s state %d",action,state));
                    if(state == BluetoothAdapter.STATE_OFF){
                    	mBluetoothAdapter.enable();
                    }

                    if(state == BluetoothAdapter.STATE_TURNING_OFF){
                        //noinspection deprecation
                        mScanner.stopScan(mLeScanCallback);
 //                       mScanning = true;
                    }
                    if(state == BluetoothAdapter.STATE_ON){
                        // reinitialize?
//                        broadcastUpdate(ACTION_BLUETOOTH_ON);
                    }
                }
            }
        };
    /** @brief creates the intent filter for the 
    */
    private IntentFilter makeBluetoothIntentFilter()
    {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        //  intentFilter.addAction(BluetoothAdapter.ACTION_BLE_STATE_CHANGED);
        return intentFilter;
    }
    /** @brief sets the UUIDS to scan for
     */
    @Override
	public void setScanUUIDS(UUID[] uuids) {
       mUUIDS = uuids;
	}
    /** @brief gets the UUIDS to filter
     */
	@Override
	public UUID[] getScanUUIDS() {
       return mUUIDS;
	}
    /** @brief gets the state of the bluetooth service
     */
	@Override
	public ServiceState getState() {
		return mState;
	}
    /** @brief gets the state of the bluetooth service
     */
    @Override
    public boolean getScanning() {
        return mScanning;
    }

}
