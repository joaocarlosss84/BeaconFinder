/*
 ** ############################################################################
 **
 ** file    BeaconListFragment.java
 ** brief   fragment for displaying the list of beacons
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
import android.database.Cursor;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;



import com.emdeveloper.embeaconlib.database.EMBeaconDevice;
import com.emdeveloper.embeaconlib.database.EMBeaconDeviceDisplayData;
import com.emdeveloper.embeaconlib.database.EMContentProvider;

/**
 * @brief A list fragment representing a list of Beacons. 
 * This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link BeaconDetailFragment}.
 * 
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 *
 * Uses the BeaconListLoader to load the items and the BeaconListAdapter to display
 */
public class BeaconListFragment extends ListFragment {

    BeaconListLoader mLoader;
    BeaconListAdapter mAdapter;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    public String[] getCursorColumns() {
        return mBeaconDeviceColumns;
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
       public void onItemSelected(String id, String time,String name,String type);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id, String time,String name,String type) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BeaconListFragment() {
    }
    public static String [] mBeaconDeviceColumns = {
        "_id",
        EMBeaconDeviceDisplayData.NAME,
        EMBeaconDeviceDisplayData.ADDRESS,
        EMBeaconDeviceDisplayData.ADDRESS,
        EMBeaconDeviceDisplayData.RSSI,
        EMBeaconDeviceDisplayData.RSSI,
        EMBeaconDeviceDisplayData.TIME,
        EMBeaconDeviceDisplayData.TYPE,
        EMBeaconDeviceDisplayData.TYPE,
        EMBeaconDeviceDisplayData.MAJORID,
        EMBeaconDeviceDisplayData.MINORID,
        EMBeaconDeviceDisplayData.EDDY,
            EMBeaconDeviceDisplayData.ADVERTISEMENTID,
    };
   public static int mTypeIndex = 7;
    public static int [] mBeaconListItemViewIds = {
       R.id.id,
       R.id.beaconName,             // EMBeaconDeviceDisplayData.NAME,
       R.id.beaconSerialNumber,     // EMBeaconDeviceDisplayData.ADDRESS,
       R.id.beaconAddress,          // EMBeaconDeviceDisplayData.ADDRESS,
       R.id.beaconRssi,             // EMBeaconDeviceDisplayData.RSSI,
       R.id.beaconRssiIcon,         // EMBeaconDeviceDisplayData.RSSI,
       R.id.beaconTime,             // EMBeaconDeviceDisplayData.TIME,
       R.id.beaconIcon,             // EMBeaconDeviceDisplayData.TYPE,
       R.id.beaconType,             // EMBeaconDeviceDisplayData.TYPE,
       R.id.beaconMajorId,          // EMBeaconDeviceDisplayData.MAJORID,
       R.id.beaconMinorId,          // EMBeaconDeviceDisplayData.MINORID,
       R.id.beaconEddy              // EMBeaconDeviceDisplayData.EDDY,
       };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new BeaconListAdapter(getActivity(),
                                         R.layout.activity_beacon_list_item,
                                         null,
                                         mBeaconDeviceColumns,
                                         mBeaconListItemViewIds,
                                         0);
        mLoader = new BeaconListLoader(getActivity(),
                EMContentProvider.Constants.DISPLAY_TABLE_CONTENT_URI,
                mAdapter,this);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(0,null,mLoader);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoader = null;
        mAdapter = null;
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Cursor c = (Cursor) mAdapter.getItem(position);
        int idx = c.getColumnIndex(EMBeaconDevice.ADDRESS);
        String address = c.getString(idx);
        idx = c.getColumnIndex(EMBeaconDevice.TIME);
        String time = c.getString(idx);
        idx = c.getColumnIndex(EMBeaconDevice.NAME);
        String name = c.getString(idx);
        idx = c.getColumnIndex(EMBeaconDevice.TYPE);
        String type = c.getString(idx);
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(address,time,name,type);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
