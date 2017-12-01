/*
 ** ############################################################################
 **
 ** file    BeaconListAdapter.java
 ** brief   adapter for displaying the list of beacons
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

import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.content.Context;
import android.widget.TextView;

import com.emdeveloper.embeaconlib.EMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.database.EMBeaconDeviceDisplayData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.text.TextUtils.join;

/**
 *  @brief A list adapter for display of the discovered beacons.
 *
 *  This is just a SimpleCursorAdapter, duplicated here for reference, debug and customization.
 *
 *
 *  The two methods to overload are getView and bindView.
 *
 *  getView is called to display a row in the table and
 *
 *  bindView is called for each item in the beacon list view item
 *  Visible items:
 *  - Icon
 *  - Type Name
 *  - Name
 *  - Rssi text
 *  - Rssi Icon
 */

public class BeaconListAdapter extends SimpleCursorAdapter {
// TODO move to the right place
   /// low rssi threshold
		private static final int RSSI_LOW = 35;
   /// medium rssi threshold
		private static final int RSSI_MED= 50;
   /// high rssi threshold   
		private static final int RSSI_HIGH = 75;
    private static final String TAG = "BeaconListAdapter" ;

    ///@brief  returns id of rss icon
   int getrssi(int v){
      int w = -v;
      if(w <= RSSI_LOW) return R.drawable.ic_rssi_four;
      if(w <= RSSI_MED) return R.drawable.ic_rssi_three;
      if(w <= RSSI_HIGH ) return R.drawable.ic_rssi_two;
      return R.drawable.ic_rssi_one;            
   }
   Resources r;
   /**
    * @brief the resources for the type icon
    * these are links to the real icon
    * see IEMBluetoothAdvertisement
    */
   int[] iconresources = new int[] {
      R.drawable.embeacon,  //  EMBEACON_TYPE_ID = 0;   
      R.drawable.alt,       //  ALTBEACON_TYPE_ID = 1;  
      R.drawable.idata,     //  IDBEACON_TYPE_ID = 2;   
      R.drawable.eddyurl,  //  EDURLBEACON_TYPE_ID = 3;
      R.drawable.eddyuid,  //  EDURIBEACON_TYPE_ID = 4;
      R.drawable.eddytlm,  //  EDTLMBEACON_TYPE_ID = 5;
   };
   /**
    * @brief the resources for the initials that overlay the icon
    */
   int[] TypeNamesResources = new int[] {
      R.string.embc_name,       //  EMBEACON_TYPE_ID = 0;       
      R.string.altbeacon_name,  //  ALTBEACON_TYPE_ID = 1;      
      R.string.ibeacon_name,    //  IDBEACON_TYPE_ID = 2;       
      R.string.eddystone_url,   //  EDURLBEACON_TYPE_ID = 3;    
      R.string.eddystone_uid,   //  EDURIBEACON_TYPE_ID = 4;
      R.string.eddystone_tlm,   //  EDTLMBEACON_TYPE_ID = 5;             
   };

    /** @brief returns a decimal
     *
    * @param address input mac address as a string aa:bb:cc:dd:ee:ff
     * @return decimal represention of address
     */
   String decimaladdress(String address)
   {
      List<String> adds = Arrays.asList(address.split(":"));
      Collections.reverse(adds);
      String s = join("",adds);
      Long v = Long.valueOf(s, 16);
      String out = String.format("%015d",v);
      return out;
   }
   /// @brief view binder for the beacon list view
   ViewBinder viewbinder = new SimpleCursorAdapter.ViewBinder() {
         @Override
         public boolean setViewValue(View view, Cursor cursor, int i) {
            int icon_id = R.drawable.ic_model_number;
            int resourceid = view.getId();
            ViewParent parentview= view.getParent();
            int beaconType = cursor.getInt(BeaconListFragment.mTypeIndex);
            int advididx = cursor.getColumnIndex(EMBeaconDeviceDisplayData.ADVERTISEMENTID);
            int advid = cursor.getInt(advididx);
            String text = cursor.getString(i);
            byte[] blobdata = null;
            try {
                int ctype = cursor.getType(i);
                if(ctype == Cursor.FIELD_TYPE_BLOB) {
                    blobdata = cursor.getBlob(i);
                }
            } catch (Exception e){
               blobdata = null;
            }
            String columnname = cursor.getColumnName(i);
            String Column =BeaconListFragment.mBeaconDeviceColumns[i];
          //  Log.i(TAG, String.format("%d %d %s %s %s", i,beaconType, text, columnname, Column));
            TextView tview = null;
            ImageView iview = null;
            if(view instanceof ImageView) {
                iview = (ImageView)view;
            }
            if(view instanceof TextView) {
               tview = (TextView)view;
            }
            Integer typevalue;
            Integer imagetyperesvalue = R.drawable.beaconunk;
            Integer texttyperesvalue =  R.string.blank;
             boolean IDS = false;
             boolean EM= false;
             boolean ED = false;
            switch (beaconType) {
               case IEMBluetoothAdvertisement.EMBEACON_TYPE_ID:
                   EM=true;
                   break;
               case IEMBluetoothAdvertisement.ALTBEACON_TYPE_ID:
               case IEMBluetoothAdvertisement.IDBEACON_TYPE_ID:
                   IDS=true;
                   break;
               case IEMBluetoothAdvertisement.EDURLBEACON_TYPE_ID:
               case IEMBluetoothAdvertisement.EDUIDBEACON_TYPE_ID:
               case IEMBluetoothAdvertisement.EDTLMBEACON_TYPE_ID:
                   ED=true;
                   break;
               default:
                  break;
            }
            LinearLayout layoutview;
             if(view.getParent() instanceof LinearLayout) {
                 layoutview = (LinearLayout) view.getParent();
                 if (layoutview != null && layoutview.getId() == R.id.majorminorlinearLayout)
                     layoutview.setVisibility(IDS ? View.VISIBLE : View.GONE);
             }
            switch (resourceid) {
               case R.id.id:
                  // ignore
                  return false;
               case R.id.beaconName:
                  if(tview != null) {
                      tview.setTypeface(null, Typeface.NORMAL);
                      tview.setText(text);
                      // return true;

                      if (beaconType == IEMBluetoothAdvertisement.EMBEACON_TYPE_ID) {
                          tview.setVisibility(View.VISIBLE);
                      } else {
                          tview.setVisibility(View.GONE);
                      }
                      return true;
                  }
                   break;
               case R.id.beaconSerialNumber:
                  // convert mac address to an integer
                  tview.setText(decimaladdress(text));
                  return true;
               case R.id.beaconAddress:
                  tview.setText(text);
                  return true;
               case R.id.beaconRssi:
                  // rssi text
                  tview.setText(text);
                  return true;
               case R.id.beaconRssiIcon:
                  // rssi view
                  Integer rssivalue;
                  Integer rssiresvalue = R.drawable.ic_rssi_zero;               
                  try{
                     rssivalue = Integer.parseInt(text);
                     rssiresvalue = getrssi(rssivalue);
                  }catch (Exception e) {
                  }
                  iview.setImageResource(rssiresvalue);
                  return true;
               case R.id.beaconTime:
                  // ignore
                  return false;
               case R.id.beaconIcon:
                  // icon image
                  imagetyperesvalue = R.drawable.beaconunk;
                  try {
                     typevalue = Integer.parseInt(text);
                     imagetyperesvalue = iconresources[typevalue];
                  } catch (Exception e){
                  }
                  iview.setImageResource(imagetyperesvalue);
                  return true;
               case R.id.beaconType:
                  // string overlaying the icon image
                  try {
                     typevalue = Integer.parseInt(text);
                     texttyperesvalue = TypeNamesResources[typevalue];
                  } catch (Exception e){
                  }
                  tview.setText(texttyperesvalue);
                  tview.bringToFront();
                  return true;
               case R.id.beaconMinorId:
                  // minor id if it exists
                  if(text != null) {
                     tview.setText(text);
                     tview.setVisibility(View.VISIBLE);
                  } else {
                     //Log.i(TAG,String.format("%d %d\n",beaconType,advid));
                     tview.setVisibility(View.GONE);
                  }
                   return true;
               case R.id.beaconMajorId:
                  // minor id if it exists
                  if(text != null) {
                     tview.setText(text);
                     tview.setVisibility(View.VISIBLE);
                  } else {
                     tview.setVisibility(View.GONE);
                  }
                   return true;
               case R.id.beaconEddy:
                  // eddystone beaconid if exists
                  if(text != null && (beaconType == EMBluetoothAdvertisement.EDURLBEACON_TYPE_ID)) {
                     tview.setText(text);
                     tview.setVisibility(View.VISIBLE);
                  }
                  else if(text != null && (beaconType == EMBluetoothAdvertisement.EDUIDBEACON_TYPE_ID)) {
                     tview.setText(text);
                     tview.setVisibility(View.VISIBLE);
                  } else {
                     tview.setVisibility(View.GONE);
                  }
                  return true;
            }

         return false;
         }
      };
        /**
         * Standard constructor.
         *
         * @param context The context where the ListView associated with this
         *                SimpleListItemFactory is running
         * @param layout  resource identifier of a layout file that defines the views
         *                for this list item. The layout file should include at least
         *                those named views defined in "to"
         * @param c       The database cursor.  Can be null if the cursor is not available yet.
         * @param from    A list of column names representing the data to bind to the UI.  Can be null
         *                if the cursor is not available yet.
         * @param to      The views that should display column in the "from" parameter.
         *                These should all be TextViews. The first N views in this list
         *                are given the values of the first N columns in the from
         *                parameter.  Can be null if the cursor is not available yet.
         * @param flags   Flags used to determine the behavior of the adapter,
         *                as per { CursorAdapter#CursorAdapter(android.content.Context, android.database.Cursor, int)}.
         */
        public BeaconListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            r = context.getResources();
            setViewBinder(viewbinder);
        }

        /**
         * @param position index of the row
         * @param convertView view to work with
         * @param parent parent view
         * @see android.widget.ListAdapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          //  try {
                return super.getView(position, convertView, parent);
          //  }catch(Exception e) {
            //    Log.i(TAG,e.toString());
          //  }
          //  return null;
        }

        /**
         * Binds all of the field names passed into the "to" parameter of the
         * constructor with their corresponding cursor columns as specified in the
         * "from" parameter.
         * Binding occurs in two phases. First, if a
         * { android.widget.SimpleCursorAdapter.ViewBinder} is available,
         * { android.widget.SimpleCursorAdapter.ViewBinder#setViewValue(android.view.View, android.database.Cursor, int)}
         * is invoked. If the returned value is true, binding has occured. If the
         * returned value is false and the view to bind is a TextView,
         * {setViewText(TextView, String)} is invoked. If the returned value is
         * false and the view to bind is an ImageView,
         * {setViewImage(ImageView, String)} is invoked. If no appropriate
         * binding can be found, an { IllegalStateException} is thrown.
         *
         * @param view view to bind to
         * @param context context
         * @param cursor  cursor to work with
         * @throws IllegalStateException if binding cannot occur
         * @see android.widget.CursorAdapter#bindView(android.view.View,
         * android.content.Context, android.database.Cursor)
         * @see getViewBinder()
         * @see setViewBinder(android.widget.SimpleCursorAdapter.ViewBinder)
         * setViewImage(ImageView, String)
         * setViewText(TextView, String)
         */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);
        }
    }
    
