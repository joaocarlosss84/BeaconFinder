/*
 ** ############################################################################
 **
 ** file    BeaconDetailAdapter.java
 ** brief   list view adapter for the detail view of a beacon
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

import android.database.MatrixCursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.content.Context;
import android.content.res.Resources;
import android.widget.TextView;

import com.emdeveloper.embeaconlib.database.EMBeaconManufacturerData;
import com.emdeveloper.embeaconlib.embeaconspecific.ConversionRoutine;
import com.emdeveloper.embeaconlib.embeaconspecific.Events;
import com.emdeveloper.embeaconlib.embeaconspecific.Sensors;

import java.util.Arrays;

/**
 * @brief This is the adapter to handle displaying the detail view.
 *
 * The query returns a table/cursor with 6 entries
 *
 * The cursor is a MatrixCursor
 *
 * Each entry has a display type and the associated value
 * There are: 
 *     16 OpenSensor display values, 0 - 15   for each of the opensesns type
 *     16 OpenSensor display values, 100 - 115
 *     rssi 200
 *     name 201
 *     packets 202
 *     battery 203
 *     Model Id 204
 * 
 * the query is (38 is the row number in the EMBeaconManufactururData table
 *
 *  | entry type | entry value
 *  | ---------------|--------------
 *  | sensor type | sensor value 
 *  | 100+event type | event value
 *  | 200 |  rssi 
 *  | 201 | name
 *  | 202 | packets
 *  | 203 | battery
 *  | 204 | Model Id
 *
 *
 * It is the job of this adapter to produce an Icon, a value and a unit for each row
 *
 *  
 */
public class BeaconDetailAdapter extends SimpleCursorAdapter {
   Resources r;
   EMBeaconManufacturerData mManufacturerData;
   ConversionRoutine[] mRow;
   MatrixCursor mCursor;
   public static final String ICON            = "ICON";
   public static final String VALUE            = "VALUE";
   public static final String UNITS            = "UNITS";
   public static final String ROUTINE            = "ROUTINE";   

   public static final String[] COLUMNS = new String[] {
      "_id",
      ICON,
      VALUE,
      UNITS,
    };
   String[] mFrom;
   ViewBinder viewbinder = new SimpleCursorAdapter.ViewBinder(){
        /**
         * @brief Binds the Cursor column defined by the specified index to the specified view.
         *
         [setViewValue]: @ref #setViewValue "viewbinder.setviewValue"
         * When binding is handled by this ViewBinder, this method must return true.
         * If this method returns false, SimpleCursorAdapter will attempts to handle
         * the binding on its own.
         *
         * @param view the view to bind the data to
         * @param cursor the cursor to get the data from
         * @param columnIndex the column at which the data can be found in the cursor
         *
         * @return true if the data was bound to the view, false otherwise
         */
         @Override
         public boolean setViewValue(View view, Cursor cursor, int i) {
            int icon_id = R.drawable.ic_model_number;
            String text = cursor.getString(i);
            if(i == 1 && view instanceof ImageView) {
               ImageView iview = (ImageView)view;
                  iview.setImageResource(Integer.parseInt(text));
                return true;
            } else if(i == 2 && view instanceof TextView){
               TextView iview = (TextView)view;
               iview.setText(text);
                return true;
            } else if(i == 3 && view instanceof TextView){
               TextView iview = (TextView)view;
                String itext;
                try{
                    itext = r.getString(Integer.parseInt(text));
                }catch (Exception e) {
                    itext = "";
                }
                iview.setText(itext);
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
    *                as per { CursorAdapffter#CursorAdapter(android.content.Context, android.database.Cursor, int)}.
    */
   public BeaconDetailAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
      super(context, layout, c, from, to, flags);
       mFrom = from;
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
      return super.getView(position, convertView, parent);
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
      super.bindView(view, context, mCursor);
   }
   
   /**
    * @brief creates the matrix cursor from the manufacturer data cursor row
    */  
   public void setData(Cursor cursor){
       mManufacturerData = EMBeaconManufacturerData.createFromCursor(cursor);
       mRow = new ConversionRoutine[6];
       if(mManufacturerData == null || mRow == null){
           return;
       }
       mRow[0] = Sensors.getConversion(Sensors.TYPE_MODEL);
       mRow[1] = Sensors.getConversion(mManufacturerData.mOpenSenseType);
       mRow[2] = Events.getConversion(mManufacturerData.mEventType);
       mRow[3] = Sensors.getConversion(Sensors.TYPE_BATTERY);
       mRow[4] = Sensors.getConversion(Sensors.TYPE_PACKETS);
       mRow[5] = Sensors.getConversion(Sensors.TYPE_RSSI);
       generateMatrixCursor();
       swapCursor(mCursor);
   }
   /**
    * called by the BeaconDetailLoader when the data has changed
    */
    @Override
    public Cursor swapCursor(Cursor c) {
        Cursor oldcursor = super.swapCursor(c);
       return oldcursor;
    }
   /**
    * @brief changes the units for the given row
    */
   public boolean changeUnits(int index,boolean v) {
       return mRow[index].setUnits(v);
   }
   /**
    * @brief creates the matrix cursor from mManufacturerData
    [generateMatrixCursor]: @ref #generateMatrixCursor "BeaconDetailAdapter.generateMatrixCursor"
    *
    * The resource values and value strings are generated here.
    * These values are used in [setViewValue]
    * 
    */
    private Cursor generateMatrixCursor() {
        // this is the cursor we deliver.
       mCursor = new MatrixCursor(COLUMNS);
       mCursor.addRow(Arrays.asList(1, mRow[0].icon(), mRow[0].value(mManufacturerData.mModelID), mRow[0].Units()));   //model
       mCursor.addRow(Arrays.asList(2, mRow[1].icon(), mRow[1].value(mManufacturerData.mOpenSenseValue), mRow[1].Units()));   //Sensor
       mCursor.addRow(Arrays.asList(3, mRow[2].icon(), mRow[2].value(mManufacturerData.mEventValue), mRow[2].Units()));   //event
       mCursor.addRow(Arrays.asList(4, mRow[3].icon(), mRow[3].value(mManufacturerData.mBattery), mRow[3].Units()));   //battery
       mCursor.addRow(Arrays.asList(5, mRow[4].icon(), mRow[4].value(mManufacturerData.mPacketCount), mRow[4].Units()));   //packet count
       mCursor.addRow(Arrays.asList(6, mRow[5].icon(), mRow[5].value(mManufacturerData.mRssi), mRow[5].Units()));   //rssi
       return mCursor;
    }
   /**
    * @brief returns the units from the given row
    */  
    public boolean getUnits(int position) {
        return mRow[position].getUnits();
    }

}
    
