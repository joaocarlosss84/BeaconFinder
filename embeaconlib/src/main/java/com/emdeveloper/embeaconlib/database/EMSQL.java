/*
 ** ############################################################################
 **
 ** file    EMSQL.java
 ** brief   makes an sql interface use the content providers
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
/*
 * Copyright (c) 2014-2015 EM Microelectronic-Marin SA. All rights reserved.
 * Developed by Glacier River Design, LLC.
 */

package com.emdeveloper.embeaconlib.database;

import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;


import com.emdeveloper.embeaconlib.EMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @brief a class to handle the interface with SQLite.
 *
 * Creates the database and tables.
 * Updates the database and tables.
 *
 * Connects to the database.
 *
 * handles queries.
 *
 *
 * Singleton class?
 *
 */
public class EMSQL {
    // Used for debugging and logging
    private static final String TAG = "EMSQL";
    private static EMSQL theEMSQL;
    LinkedBlockingQueue<IEMBluetoothAdvertisement> mQueue;
    Thread workerThread;
   Runnable workerRunner;
    public class SQLRunner implements Runnable {
       public boolean workerrunning;
        Collection<IEMBluetoothAdvertisement> collection;
        LinkedBlockingQueue<IEMBluetoothAdvertisement> mQueue;
        SQLRunner(LinkedBlockingQueue<IEMBluetoothAdvertisement> queue){
            mQueue = queue;
            workerrunning = true;
            collection = new LinkedList<IEMBluetoothAdvertisement>();
        }
      public void run() {
         while(workerrunning) {
             IEMBluetoothAdvertisement pair;
             try {
                 pair = mQueue.take();
                 collection.add(pair);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             mQueue.drainTo(collection,20);
            dobulkinsert(collection);
             collection.clear();
         }
          Log.d(TAG,"worker exit");
      }
   }
    Context mContext;
    /**
     * @brief creates the database
     */
    public static EMSQL create(Context c){
        if((theEMSQL == null) ||(theEMSQL.mContext != c) ) {
            theEMSQL = new EMSQL(c);
        }
        return theEMSQL;
    }
   boolean queuefull = false;
    /**
     * @brief Constructor
     */
    private EMSQL(Context c){
        mContext = c;
        theEMSQL = this;
        getOpenHelper(c);
        mQueue = new LinkedBlockingQueue<IEMBluetoothAdvertisement>();
        // create the thread to handle the queue
        workerRunner = new SQLRunner(mQueue);
        workerThread = new Thread(workerRunner);
        workerThread.start();
        queuefull = false;        
    }

    /**
     * @brief adds an advertisement using the content resolver
     */
    public void doaddAdvertisement(IEMBluetoothAdvertisement adv){

        //       public void addAdvertisement(String address, int rssi, Long time, byte[] record){
        // add to advertisement table
        ContentValues acv = EMBeaconAdvertisement.getContentValues(adv);
        ContentResolver contentResolver = mContext.getContentResolver();
        ContentProviderClient cp =  contentResolver.acquireContentProviderClient(EMBeaconAdvertisement.TABLE_CONTENT_URI());
        Uri aUri = mContext.getContentResolver().insert(EMBeaconAdvertisement.TABLE_CONTENT_URI(), acv);
        long arow = ContentUris.parseId(aUri);
        // add to device table
        ContentValues dcv = EMBeaconDevice.getContentValues(adv)      ;
        Integer advtype = adv.getType();
        Uri dUri =  mContext.getContentResolver().insert(EMBeaconDevice.TABLE_CONTENT_URI(), dcv);
        // add the special values
        if(advtype == EMBluetoothAdvertisement.EMBEACON_TYPE_ID){
            EMBeaconManufacturerData embd = EMBeaconManufacturerData.create(adv);
            ContentValues bcv = embd.getContentValues();
            // set the row id in the advertisement table
            bcv.put(EMBeaconManufacturerData.ADVERTISEMENTID,arow);
            Uri bUri = mContext.getContentResolver().insert(EMBeaconManufacturerData.TABLE_CONTENT_URI(), bcv);
            EMBeaconDeviceDisplayData dispdata = EMBeaconDeviceDisplayData.add(adv, embd);
            bcv = dispdata.getContentValues();
            bcv.put(EMBeaconManufacturerData.ADVERTISEMENTID, arow);
            Uri ccUri =  mContext.getContentResolver().insert(EMBeaconDeviceDisplayData.TABLE_CONTENT_URI(), bcv);
            // logging
            embd.log();
        }else
        if(advtype == EMBluetoothAdvertisement.ALTBEACON_TYPE_ID){
            EMALTManufacturerData emaltd = EMALTManufacturerData.create(adv);
            ContentValues bcv = emaltd.getContentValues();
            bcv.put(EMBeaconManufacturerData.ADVERTISEMENTID,arow);
            Uri alUri =  mContext.getContentResolver().insert(EMALTManufacturerData.TABLE_CONTENT_URI(), bcv);
            EMBeaconDeviceDisplayData dispdata = EMBeaconDeviceDisplayData.add(adv, emaltd);
            bcv = dispdata.getContentValues();
            bcv.put(EMBeaconManufacturerData.ADVERTISEMENTID, arow);
            Uri ccUri =  mContext.getContentResolver().insert(EMBeaconDeviceDisplayData.TABLE_CONTENT_URI(), bcv);
        }else
        if(advtype == EMBluetoothAdvertisement.IDBEACON_TYPE_ID){
            EMIDManufacturerData emaltd = EMIDManufacturerData.create(adv);
            ContentValues bcv = emaltd.getContentValues()           ;
            bcv.put(EMBeaconManufacturerData.ADVERTISEMENTID,arow);
            Uri idUri =  mContext.getContentResolver().insert(EMIDManufacturerData.TABLE_CONTENT_URI(), bcv);
            EMBeaconDeviceDisplayData dispdata = EMBeaconDeviceDisplayData.add(adv, emaltd);
            bcv = dispdata.getContentValues();
            bcv.put(EMBeaconManufacturerData.ADVERTISEMENTID,arow);
            Uri dccUri =  mContext.getContentResolver().insert(EMBeaconDeviceDisplayData.TABLE_CONTENT_URI(), bcv);
        }else
        if(advtype == EMBluetoothAdvertisement.EDURLBEACON_TYPE_ID){
            EMEDURLManufacturerData emaltd = EMEDURLManufacturerData.create(adv);
            ContentValues bcv = emaltd.getContentValues()           ;
            bcv.put(EMBeaconManufacturerData.ADVERTISEMENTID,arow);
            Uri ccUri =  mContext.getContentResolver().insert(EMEDURLManufacturerData.TABLE_CONTENT_URI(), bcv);
            EMBeaconDeviceDisplayData dispdata = EMBeaconDeviceDisplayData.add(adv, emaltd);
            bcv = dispdata.getContentValues();
            bcv.put(EMBeaconManufacturerData.ADVERTISEMENTID,arow);
            Uri dccUri =  mContext.getContentResolver().insert(EMBeaconDeviceDisplayData.TABLE_CONTENT_URI(), bcv);
        }else
        if(advtype == EMBluetoothAdvertisement.EDUIDBEACON_TYPE_ID){
            EMEDUIDManufacturerData emaltd = EMEDUIDManufacturerData.create(adv);
            ContentValues bcv = emaltd.getContentValues()           ;
            bcv.put(EMBeaconManufacturerData.ADVERTISEMENTID,arow);
            Uri ccUri =  mContext.getContentResolver().insert(EMEDUIDManufacturerData.TABLE_CONTENT_URI(), bcv);
            EMBeaconDeviceDisplayData dispdata = EMBeaconDeviceDisplayData.add(adv, emaltd);
            bcv = dispdata.getContentValues();
            bcv.put(EMBeaconManufacturerData.ADVERTISEMENTID,arow);
            Uri dccUri =  mContext.getContentResolver().insert(EMBeaconDeviceDisplayData.TABLE_CONTENT_URI(), bcv);
        }else
        if(advtype == EMBluetoothAdvertisement.EDTLMBEACON_TYPE_ID){
            EMEDTLMManufacturerData emaltd = EMEDTLMManufacturerData.create(adv);
            ContentValues bcv = emaltd.getContentValues()           ;
            bcv.put(EMBeaconManufacturerData.ADVERTISEMENTID,arow);
            Uri ccUri =  mContext.getContentResolver().insert(EMEDTLMManufacturerData.TABLE_CONTENT_URI(), bcv);
            EMBeaconDeviceDisplayData dispdata = EMBeaconDeviceDisplayData.add(adv, emaltd);
            bcv = dispdata.getContentValues();
            bcv.put(EMBeaconManufacturerData.ADVERTISEMENTID,arow);
            Uri dccUri =  mContext.getContentResolver().insert(EMBeaconDeviceDisplayData.TABLE_CONTENT_URI(), bcv);
        }
    }
   /**
    * @brief clears the database
    */
    public void Clear() {
        mContext.getContentResolver().delete(EMContentProvider.Constants.ALL_CONTENT_URI,null,null);
    }

   /**
    * The database that the provider uses as its underlying data store
    */
    public static final String DATABASE_NAME = "EMBluetoothScan.db";
    public static final String ADVERTISEMENT_FILE_NAME = "EMBAdvertisements";   

    /**
     * The database version
     */
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper mOpenHelper;

    /**
     * @brief Helper class to open and create the SQL database
     * 
     */
    static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            // calls the super constructor, requesting the default cursor factory.
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

       
        /**
         * Called when the database is created for the first time. This is where the
         * creation of tables and the initial population of the tables should happen.
         *
         * @param db The database.
         */
        public void onCreate(SQLiteDatabase db){
           String devices =createTableString(EMBeaconDevice.getTableName(),EMBeaconDevice.getTableColumns(), EMBeaconDevice.getTableColumnTypes());
           final StringBuilder stringBuilder = new StringBuilder(devices);
           int index = devices.length() - 2;
           // strip off last ');' add ', UNIQUE(ADDRESS,TYPE) ON CONFLICT REPLACE);'
           stringBuilder.insert(index,", UNIQUE(ADDRESS,TYPE) ON CONFLICT REPLACE");
           devices = stringBuilder.toString();
           db.execSQL(devices);

           // add display table
           String dispdevices =createTableString(EMBeaconDeviceDisplayData.getTableName(),EMBeaconDeviceDisplayData.getTableColumns(), EMBeaconDeviceDisplayData.getTableColumnTypes());
           final StringBuilder dispstringBuilder = new StringBuilder(dispdevices);
           index = dispdevices.length() - 2;
           // strip off last ');' add ', UNIQUE(ADDRESS,TYPE) ON CONFLICT REPLACE);'
           dispstringBuilder.insert(index,", UNIQUE(ADDRESS,TYPE) ON CONFLICT REPLACE");
           dispdevices = dispstringBuilder.toString();
           db.execSQL(dispdevices);

           
            db.execSQL(createTableString(EMBeaconAdvertisement.Tablename, EMBeaconAdvertisement.Columns, EMBeaconAdvertisement.ColumnTypes));
            db.execSQL(createTableString(EMBeaconManufacturerData.Tablename, EMBeaconManufacturerData.Columns, EMBeaconManufacturerData.ColumnTypes));

            db.execSQL(createTableString(EMALTManufacturerData.Tablename, EMALTManufacturerData.Columns, EMALTManufacturerData.ColumnTypes));
            db.execSQL(createTableString(EMIDManufacturerData.Tablename, EMIDManufacturerData.Columns, EMIDManufacturerData.ColumnTypes));

            db.execSQL(createTableString(EMEDURLManufacturerData.Tablename, EMEDURLManufacturerData.Columns, EMEDURLManufacturerData.ColumnTypes));
            db.execSQL(createTableString(EMEDUIDManufacturerData.Tablename, EMEDUIDManufacturerData.Columns, EMEDUIDManufacturerData.ColumnTypes));
            db.execSQL(createTableString(EMEDTLMManufacturerData.Tablename, EMEDTLMManufacturerData.Columns, EMEDTLMManufacturerData.ColumnTypes));
        }

        public String createTableString(String tablename,String[] columns, String[] types){
            final StringBuilder stringBuilder = new StringBuilder(100);
            stringBuilder.append("CREATE TABLE IF NOT EXISTS " + tablename + " ( ");
            int i = 0;
            stringBuilder.append(" " + columns[i] + " " + types[i]);
            for(i=1 ;i < columns.length; i++){
                stringBuilder.append(" , " + columns[i] + " " + types[i]);      
            }
            stringBuilder.append( ");");
            return stringBuilder.toString();
        }
        /**
         * Called when the database needs to be upgraded. The implementation
         * should use this method to drop tables, add tables, or do anything else it
         * needs to upgrade to the new schema version.
         *
         * <p>
         * The SQLite ALTER TABLE documentation can be found
         * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
         * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
         * you can use ALTER TABLE to rename the old table, then create the new table and then
         * populate the new table with the contents of the old table.
         * </p><p>
         * This method executes within a transaction.  If an exception is thrown, all changes
         * will automatically be rolled back.
         * </p>
         *
         * @param db The database.
         * @param oldVersion The old database version.
         * @param newVersion The new database version.
         *  Not Implemented
         */
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
           ///Not implemented
        }

        /**
         * Called when the database has been opened.  The implementation
         * should check {SQLiteDatabase#isReadOnly} before updating the
         * database.
         * <p>
         * This method is called after the database connection has been configured
         * and after the database schema has been created, upgraded or downgraded as necessary.
         * If the database connection must be configured in some way before the schema
         * is created, upgraded, or downgraded, do it in {onConfigure} instead.
         * </p>
         *
         * @param db The database.
         */
        public void onOpen(SQLiteDatabase db) {
//           setWriteAheadLoggingEnabled(true);
            // what to do here?
        }

    }
    /**
     * @brief local on create function
     */
    public void myonCreate(Context context){
        mOpenHelper = new DatabaseHelper(context);
    }

    /**
     * @brief returns a database helper
     * @return the SQLiteOpenHelper
     */
    public static SQLiteOpenHelper getOpenHelper(Context context) {
       if(theEMSQL == null) {
          EMSQL.create(context);
       }
       if(theEMSQL.mOpenHelper  == null){
            theEMSQL.mOpenHelper  = new DatabaseHelper(context);
       }
       return theEMSQL.mOpenHelper ;
    }

   private void dobulkinsert(Collection<IEMBluetoothAdvertisement> c){
      // start transaction database
      SQLiteDatabase db=null;
       int size = c.size();
      db = mOpenHelper.getWritableDatabase();
  //    db.beginTransaction();
      try {
         for(Iterator<IEMBluetoothAdvertisement> it = c.iterator(); it.hasNext();){
            IEMBluetoothAdvertisement ip = it.next();
            doaddAdvertisement(ip);
         }
      //   db.setTransactionSuccessful();
         Log.d(TAG, String.format("bulkinsert %d", size));
      } finally {
         // close transaction
    //     db.endTransaction();
      }
   }
   public void addAdvertisement(IEMBluetoothAdvertisement adv){
      // add uri,values to queue
      try {
         if(mQueue.size() > 200) {
            queuefull = true;
         }
         if(mQueue.size() < 20) {
            queuefull = false;
         }
         if(! queuefull) {
            mQueue.add(adv);
         }
         Log.d(TAG,String.format("mQueue.add %d",mQueue.size()));
      } catch (Exception e) {
      }
   }


}
