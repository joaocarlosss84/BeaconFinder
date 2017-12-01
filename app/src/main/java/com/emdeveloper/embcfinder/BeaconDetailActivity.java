/*
 ** ############################################################################
 **
 ** file    BeaconDetailActivity.java
 ** brief   activity for displaying the detail view of a beacon
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

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Activity;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.emdeveloper.embeaconlib.bluetooth.EMBluetoothLeService;
import com.emdeveloper.embeaconlib.database.EMBeaconAdvertisement;
import com.emdeveloper.embeaconlib.database.EMBluetoothDatabase;
import com.emdeveloper.embeaconlib.database.EMSQL;
import com.emdeveloper.embeaconlib.database.IEMBluetoothDatabase;


/**
 * An activity representing a single Beacon detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {link BeaconListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {link BeaconDetailFragment}.
 *
 * It also manages the database connections as in the {link BeaconListActivity}.
 *
 */
public class BeaconDetailActivity extends Activity implements IDBCallBacks {
   private final static String TAG = "BeaconDetailActivity";
   /**
    * @brief 
    * @{
    * Fields to handle displaying the scanning state
    * 
    */
   private static final long SCAN_PERIOD = 5000;
   private boolean mInScan;
   private boolean mScanning;
   private Handler mmHandler;
   /** @} */

   /**
    * @{
    * Fields related to the embeaconlib
    */
   private IEMBluetoothDatabase mEMBluetoothDatabase;
   private EMSQL mSql;
   private EMBluetoothLeService mBluetoothLeService;
   /** @} */

   /**
    * Called when the activity is starting.  This is where most initialization
    * should go: calling {link #setContentView(int)} to inflate the
    * activity's UI, using {link #findViewById} to programmatically interact
    * with widgets in the UI, calling
    * {link #managedQuery(android.net.Uri , String[], String, String[], String)} to retrieve
    * cursors for data being displayed, etc.
    * 
    * <p>You can call {link #finish} from within this function, in
    * which case onDestroy() will be immediately called without any of the rest
    * of the activity lifecycle ({link #onStart}, {link #onResume},
    * {link #onPause}, etc) executing.
    * 
    * <p><em>Derived classes must call through to the super class's
    * implementation of this method.  If they do not, an exception will be
    * thrown.</em></p>
    * 
    * @param savedInstanceState If the activity is being re-initialized after
    *     previously being shut down then this Bundle contains the data it most
    *     recently supplied in {link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
    * 
    * @see #onStart
    * @see #onSaveInstanceState
    * @see #onRestoreInstanceState
    * @see #onPostCreate
    */
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_beacon_detail);

      // Show the Up button in the action bar.
      getActionBar().setDisplayHomeAsUpEnabled(true);
      mSql = EMSQL.create(this);
      mEMBluetoothDatabase = EMBluetoothDatabase.create(this, mSql);
      // savedInstanceState is non-null when there is fragment state
      // saved from previous configurations of this activity
      // (e.g. when rotating the screen from portrait to landscape).
      // In this case, the fragment will automatically be re-added
      // to its container so we don't need to manually add it.
      // For more information, see the Fragments API guide at:
      //
      // http://developer.android.com/guide/components/fragments.html
      //
      if(BuildConfig.FLAVOR != "nohardware") {
         Intent EMBluetoothLeServiceIntent = new Intent(this, EMBluetoothLeService.class); ///< intent for the EMBluetoothLeService
         bindService(EMBluetoothLeServiceIntent, mServiceConnection, BIND_AUTO_CREATE); ///< start and bind the EMBluetoothLeService \see mServiceConnection
      }
      if (savedInstanceState == null) {
         // Create the detail fragment and add it to the activity
         // using a fragment transaction.
         Bundle arguments = new Bundle();
         arguments.putString(BeaconDetailFragment.ARG_TIME,
                             getIntent().getStringExtra(BeaconDetailFragment.ARG_TIME));
         arguments.putString(BeaconDetailFragment.ARG_ADDRESS,
                             getIntent().getStringExtra(BeaconDetailFragment.ARG_ADDRESS));
         String name = getIntent().getStringExtra(BeaconDetailFragment.ARG_NAME);
         arguments.putString(BeaconDetailFragment.ARG_NAME,name);
         getActionBar().setTitle(name);
         BeaconDetailFragment fragment = new BeaconDetailFragment();
         fragment.setArguments(arguments);
         getFragmentManager().beginTransaction()
            .add(R.id.beacon_detail_container, fragment)
            .commit();
      }
   }
   /**
    * Perform any final cleanup before an activity is destroyed.  This can
    * happen either because the activity is finishing (someone called
    * {link #finish} on it, or because the system is temporarily destroying
    * this instance of the activity to save space.  You can distinguish
    * between these two scenarios with the {link #isFinishing} method.
    * 
    * <p><em>Note: do not count on this method being called as a place for
    * saving data! For example, if an activity is editing data in a content
    * provider, those edits should be committed in either {link #onPause} or
    * {link #onSaveInstanceState}, not here.</em> This method is usually implemented to
    * free resources like threads that are associated with an activity, so
    * that a destroyed activity does not leave such things around while the
    * rest of its application is still running.  There are situations where
    * the system will simply kill the activity's hosting process without
    * calling this method (or any others) in it, so it should not be used to
    * do things that are intended to remain around after the process goes
    * away.
    * 
    * <p><em>Derived classes must call through to the super class's
    * implementation of this method.  If they do not, an exception will be
    * thrown.</em></p>
    * 
    * @see #onPause
    * @see #onStop
    * @see #finish
    * @see #isFinishing
    */
   @Override
   protected void onDestroy()
   {
      super.onDestroy();
      if(BuildConfig.FLAVOR != "nohardware") {
         try {
            unregisterReceiver(mEMBluetoothLeServiceUpdateReceiver);
            unbindService(mServiceConnection);
         } catch (Exception e){

         }
      }
   }
   /**
    *
    * @{
    * Menu handling methods
    */
   /**
    * Initialize the contents of the Activity's standard options menu.  You
    * should place your menu items in to <var>menu</var>.
    * 
    * <p>This is only called once, the first time the options menu is
    * displayed.  To update the menu every time it is displayed, see
    * {link #onPrepareOptionsMenu}.
    * 
    * <p>The default implementation populates the menu with standard system
    * menu items.  These are placed in the {link Menu#CATEGORY_SYSTEM} group so that 
    * they will be correctly ordered with application-defined menu items. 
    * Deriving classes should always call through to the base implementation. 
    * 
    * <p>You can safely hold on to <var>menu</var> (and any items created
    * from it), making modifications to it as desired, until the next
    * time onCreateOptionsMenu() is called.
    * 
    * <p>When you add items to the menu, you can implement the Activity's
    * {link #onOptionsItemSelected} method to handle them there.
    * 
    * @param menu The options menu in which you place your items.
    * 
    * @return You must return true for the menu to be displayed;
    *         if you return false it will not be shown.
    * 
    * @see #onPrepareOptionsMenu
    * @see #onOptionsItemSelected
    */
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.beacon_list_activity_menu, menu);
      if (!mScanning) {
         menu.findItem(R.id.menu_stop).setVisible(false);
         menu.findItem(R.id.menu_scan).setVisible(true);
         menu.findItem(R.id.menu_refresh).setActionView(null);
      } else {
         menu.findItem(R.id.menu_stop).setVisible(true);
         menu.findItem(R.id.menu_scan).setVisible(false);
         menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_indeterminate_progress);
      }
      return true;
   }
   /**
    * This hook is called whenever an item in your options menu is selected.
    * The default implementation simply returns false to have the normal
    * processing happen (calling the item's Runnable or sending a message to
    * its Handler as appropriate).  You can use this method for any items
    * for which you would like to do processing without those other
    * facilities.
    * 
    * <p>Derived classes should call through to the base class for it to
    * perform the default menu handling.</p>
    * 
    * @param item The menu item that was selected.
    * 
    * @return boolean Return false to allow normal menu processing to
    *         proceed, true to consume it here.
    * 
    * @see #onCreateOptionsMenu
    */
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.menu_scan:
            startLeScan();
            return true;
            // break;
         case R.id.menu_stop:

            stopLeScan();
            return true;
            // break;
         case android.R.id.home:
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, BeaconListActivity.class));
            return true;
            //  break;
         case R.id.menu_info:
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }

   }
   /**
    * @{
    * Methods to handle scanning for beacons
    *
    *
    *  On android 4.4 we stop and start the scanning
    * every SCAN_PERIOD seconds so we can get live updates.
    * 
    */    
   private Runnable scanRunnable = new Runnable() {
         @Override
         public void run() {
            if(mmHandler != null)
               mmHandler.removeCallbacks(this);
            if(mScanning) {
//               Log.i(TAG, "Toggling scan off and on");
               if(mBluetoothLeService != null)
                  mBluetoothLeService.startLeScan();
               mmHandler.postDelayed(this, SCAN_PERIOD);    
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
         mmHandler.postDelayed(scanRunnable, SCAN_PERIOD);
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
   /**
    * @brief resets the bluetooth service
    *
    */

   private void ResetBluetooth(){
      mBluetoothLeService.ResetBluetooth(this);
   }

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
               //mMyBluetoothDevices.clear();
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
            if (!mBluetoothLeService.initialize(mEMBluetoothDatabase))
            {
               //Log.e(TAG, "Unable to initialize Bluetooth");
               //   MakeToast(R.string.ble_not_supported);
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
    * @brief returns the EMSQL instance
    */
   @Override
   public EMSQL getEMSQL() {
      return mSql;
   }

   /**
    * @brief returns the EMBluetoothDatabase interface
    */
   @Override
   public IEMBluetoothDatabase getEMBluetoothDatabase() {
      return mEMBluetoothDatabase;
   }

   /**
    * Called as part of the activity lifecycle when an activity is going into
    * the background, but has not (yet) been killed.  The counterpart to
    * {link #onResume}.
    * <p/>
    * <p>When activity B is launched in front of activity A, this callback will
    * be invoked on A.  B will not be created until A's {link #onPause} returns,
    * so be sure to not do anything lengthy here.
    * <p/>
    * <p>This callback is mostly used for saving any persistent state the
    * activity is editing, to present a "edit in place" model to the user and
    * making sure nothing is lost if there are not enough resources to start
    * the new activity without first killing this one.  This is also a good
    * place to do things like stop animations and other things that consume a
    * noticeable amount of CPU in order to make the switch to the next activity
    * as fast as possible, or to close resources that are exclusive access
    * such as the camera.
    * <p/>
    * <p>In situations where the system needs more memory it may kill paused
    * processes to reclaim resources.  Because of this, you should be sure
    * that all of your state is saved by the time you return from
    * this function.  In general {link #onSaveInstanceState} is used to save
    * per-instance state in the activity and this method is used to store
    * global persistent data (in content providers, files, etc.)
    * <p/>
    * <p>After receiving this call you will usually receive a following call
    * to {link #onStop} (after the next activity has been resumed and
    * displayed), however in some cases there will be a direct call back to
    * {link #onResume} without going through the stopped state.
    * <p/>
    * <p><em>Derived classes must call through to the super class's
    * implementation of this method.  If they do not, an exception will be
    * thrown.</em></p>
    *
    * @see #onResume
    * @see #onSaveInstanceState
    * @see #onStop
    */
   @Override
   protected void onPause() {
      super.onPause();
      stopLeScan();
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
    * @see #onCreate
    * @see #onStop
    * @see #onResume
    */
   @Override
   protected void onStart() {
      super.onStart();
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
    * @see #onRestart
    * @see #onResume
    * @see #onSaveInstanceState
    * @see #onDestroy
    */
   @Override
   protected void onStop() {
      super.onStop();
   }

   /**
    * Called after  #onRestoreInstanceState},  #onRestart}, or
    *  #onPause}, for your activity to start interacting with the user.
    * This is a good place to begin animations, open exclusive-access devices
    * (such as the camera), etc.
    * <p/>
    * <p>Keep in mind that onResume is not the best indicator that your activity
    * is visible to the user; a system window such as the keyguard may be in
    * front.  Use  #onWindowFocusChanged} to know for certain that your
    * activity is visible to the user (for example, to resume a game).
    * <p/>
    * <p><em>Derived classes must call through to the super class's
    * implementation of this method.  If they do not, an exception will be
    * thrown.</em></p>
    *
    * @see #onRestoreInstanceState
    * @see #onRestart
    * @see #onPostResume
    * @see #onPause
    */
   @Override
   protected void onResume() {
      super.onResume();
   }

}
