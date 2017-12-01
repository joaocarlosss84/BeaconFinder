/*
 ** ############################################################################
 **
 ** file    BeaconDetailFragment.java
 ** brief   fragment for displaying the detail view of a beacon
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

import android.app.ListFragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;

import com.emdeveloper.embeaconlib.database.EMBeaconManufacturerData;
import com.emdeveloper.embeaconlib.database.EMBluetoothDatabase;
import com.emdeveloper.embeaconlib.database.EMContentProvider;
import com.emdeveloper.embeaconlib.database.EMSQL;
import com.emdeveloper.embeaconlib.database.IEMBluetoothDatabase;


/**
 * A fragment representing a single Beacon detail screen.
 * This fragment is either contained in a {@link BeaconListActivity}
 * in two-pane mode (on tablets) or a {@link BeaconDetailActivity}
 * on handsets.
 * 
 * This screen is a list view 
|  left(icon) | right(text)  | Unit    |
|---------------|--------------|--------|
| model icon    | model number | empty|
| sensor icon   | sensor value | Units|
| event icon    | event value  | Units| 
| battery  icon | ery voltage  | Volts|
| count icon    | packet count | Packets|
| rssi icon     | rssi value   | dBm|
 * 
 * There is a button to change the units
 *
 * This selects the most recent (highest time) advertisement with the given address
 *
 */
public class BeaconDetailFragment extends ListFragment {
    private final String TAG =  "BeaconDetailFragment";
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_NAME = "item_name";
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ADDRESS = "item_address";
    public static final String ARG_TIME = "item_time";
    private IEMBluetoothDatabase mDb;
    private EMSQL mSql;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BeaconDetailFragment() {
    }

    String mAddress;
    String mTime;
   String mName;
   // row in the EMBeaconManufacturer table of the latest advertisement with the given address
    int mIndex;

   public String [] mBeaconDeviceColumns = BeaconDetailAdapter.COLUMNS;

   int [] mBeaconListItemViewIds = {
       R.id.id,       
       R.id.iconName,
       R.id.Value,
       R.id.Units
    };

    private BeaconDetailAdapter mAdapter;
    private BeaconDetailLoader mLoader;
    private EMBeaconManufacturerData mManufacturerData;
   /**
    * @brief called on fragment creation
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IDBCallBacks id = (IDBCallBacks) getActivity();
        if(id != null) {
            mDb = id.getEMBluetoothDatabase();
            mSql = id.getEMSQL();
        }
        if (getArguments().containsKey(ARG_ADDRESS)) {
           mAddress = getArguments().getString(ARG_ADDRESS);
           mTime = getArguments().getString(ARG_TIME);
           mName = getArguments().getString(ARG_NAME);
           // get the correct row from the correct table.
           mIndex = getEMBeaconIndex(mAddress,mTime);
           //Log.i(TAG, String.format("onCreate %s %s %d", mAddress, mTime, mIndex));
           mAdapter = new BeaconDetailAdapter(getActivity(),
                                              R.layout.activity_beacon_detail_item,
                                              null,
                                              mBeaconDeviceColumns,
                                              mBeaconListItemViewIds,
                                              0);
           mLoader = new BeaconDetailLoader(getActivity(),
                                            mAddress,mTime,
                                            mAdapter);
           setListAdapter(mAdapter);
           getLoaderManager().initLoader(1,null,mLoader);

        }
    }

    private int getEMBeaconIndex(String mAddress, String mTime) {
        return 0;
    }
   /**
    * @brief called when view is created
    * 
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_beacon_detail, container, false);

        if (mAddress != null) {
            // initialize all the crud for this address.
        }

        return rootView;
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    

    /**
     * Android Infrastructure
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * @brief called when an item is selected
     */
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
       mAdapter.changeUnits(position,!mAdapter.getUnits(position));
        Uri noteUri = ContentUris.withAppendedId(EMContentProvider.Constants.EMBEACON_MANUF_CONTENT_URI, 0);
        ContentResolver cr = this.getActivity().getContentResolver();
        cr.notifyChange(noteUri, null);
    }
    
}
