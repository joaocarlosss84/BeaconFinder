/*
 ** ############################################################################
 **
 ** file    EMContentProvider.java
 ** brief   content provider for the database
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
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.Set;

/**
 *  The Content Provider for accessing the sql database
 *
 *  Handles the uri values and access via query
 */
public class EMContentProvider extends ContentProvider {
    private final String TAG = "EMContentProvider";
    SQLiteOpenHelper mHelper;
    private final Object mLock = new Object();
    public EMContentProvider() {
    }
   /** @brief These are "Constants" used to connect the ContentProvider, the ContentResolver and the AndroidManifest.xml
    */
    public static class Constants {

        /**
         * @brief the scheme string "content"
         */
        public static final String SCHEME = "content";
        /**
         * @brief default authority
         */
        public static  String AUTHORITY = "com.emdeveloper.embeaconlib.xxxxx";
        /**
         * The DataProvider content URI
         */
        public static  Uri CONTENT_URI;
        /**
         * EMBeacon table content URI
       */
         public static Uri EMALTBEACON_MANUF_CONTENT_URI;
        /**
         * EMBeacon table content URI
       */
         public static Uri EMIDBEACON_MANUF_CONTENT_URI;
        /**
         * EMBeacon table content URI
       */
         public static Uri EMEDTLMBEACON_MANUF_CONTENT_URI;
         public static Uri EMEDURIBEACON_MANUF_CONTENT_URI;
         public static Uri EMEDURLBEACON_MANUF_CONTENT_URI;
        /**
         * EMBeacon table content URI
         */
        public static  Uri EMBEACON_MANUF_CONTENT_URI;
        /**
         *  Advertisements table content URI
         */
        public static  Uri ADVERTISEMENTS_TABLE_CONTENT_URI;
        /**
         * Devices table content URI
         */
        public static  Uri DEVICES_TABLE_CONTENT_URI;

       /* Display table content URI
         */
        public static  Uri DISPLAY_TABLE_CONTENT_URI;


       public static  Uri ALL_CONTENT_URI;

       /** @brief setConstants is called with the desired authority.
        * The authority must be declared in a \<provider\> section of the AndroidManifest.xml 
        */
       static public void setConstants(String authority) {
            AUTHORITY = authority;
            CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);
            EMALTBEACON_MANUF_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, EMALTBEACON_MANUF_TABLE_NAME);
            EMIDBEACON_MANUF_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, EMIDBEACON_MANUF_TABLE_NAME);
            EMBEACON_MANUF_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, EMBEACON_MANUF_TABLE_NAME);
            ADVERTISEMENTS_TABLE_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, ADVERTISEMENTS_TABLE_NAME);
            DEVICES_TABLE_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, DEVICES_TABLE_NAME);
            DISPLAY_TABLE_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, DISPLAY_TABLE_NAME);
            ALL_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, ALL_NAME);

            EMEDTLMBEACON_MANUF_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, EMEDTLMBEACON_MANUF_TABLE_NAME);
            EMEDURIBEACON_MANUF_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, EMEDURIBEACON_MANUF_TABLE_NAME);
            EMEDURLBEACON_MANUF_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, EMEDURLBEACON_MANUF_TABLE_NAME);                                    

            CONTENT_URIS = new Uri[10];
            CONTENT_URIS[ALL_CONTENT_URI_N]                       =                ALL_CONTENT_URI ;            
            CONTENT_URIS[DEVICES_TABLE_CONTENT_URI_N]             =                DEVICES_TABLE_CONTENT_URI ;
            CONTENT_URIS[ADVERTISEMENTS_TABLE_CONTENT_URI_N]      =                ADVERTISEMENTS_TABLE_CONTENT_URI;
            CONTENT_URIS[EMALTBEACON_MANUF_CONTENT_URI_N]   =                EMBEACON_MANUF_CONTENT_URI ;
            CONTENT_URIS[EMIDBEACON_MANUF_CONTENT_URI_N]    =                EMALTBEACON_MANUF_CONTENT_URI ;
            CONTENT_URIS[EMBEACON_MANUF_CONTENT_URI_N]      =                EMIDBEACON_MANUF_CONTENT_URI ;   
            CONTENT_URIS[EMEDTLMBEACON_MANUF_CONTENT_URI_N] =                EMEDTLMBEACON_MANUF_CONTENT_URI ;
            CONTENT_URIS[EMEDURIBEACON_MANUF_CONTENT_URI_N] =                EMEDURIBEACON_MANUF_CONTENT_URI ;
            CONTENT_URIS[EMEDURLBEACON_MANUF_CONTENT_URI_N] =                EMEDURLBEACON_MANUF_CONTENT_URI ;
            CONTENT_URIS[DISPLAY_TABLE_CONTENT_URI_N] = DISPLAY_TABLE_CONTENT_URI;

            TABLE_NAMES = new String[10];
            TABLE_NAMES[ALL_CONTENT_URI_N]= ALL_NAME;
            TABLE_NAMES[DEVICES_TABLE_CONTENT_URI_N] = DEVICES_TABLE_NAME;
            TABLE_NAMES[ADVERTISEMENTS_TABLE_CONTENT_URI_N] = ADVERTISEMENTS_TABLE_NAME;
            TABLE_NAMES[EMALTBEACON_MANUF_CONTENT_URI_N] = EMALTBEACON_MANUF_TABLE_NAME;
            TABLE_NAMES[EMIDBEACON_MANUF_CONTENT_URI_N] = EMIDBEACON_MANUF_TABLE_NAME;
            TABLE_NAMES[EMBEACON_MANUF_CONTENT_URI_N] = EMBEACON_MANUF_TABLE_NAME;
            TABLE_NAMES[EMEDTLMBEACON_MANUF_CONTENT_URI_N] = EMEDTLMBEACON_MANUF_TABLE_NAME;
            TABLE_NAMES[EMEDURIBEACON_MANUF_CONTENT_URI_N] = EMEDURIBEACON_MANUF_TABLE_NAME;
            TABLE_NAMES[EMEDURLBEACON_MANUF_CONTENT_URI_N] = EMEDURLBEACON_MANUF_TABLE_NAME;
            TABLE_NAMES[DISPLAY_TABLE_CONTENT_URI_N]        = DISPLAY_TABLE_NAME ;                        
        }
    }
    /**
     * @brief sets the database helper
     */
    public void setOpenHelper(SQLiteOpenHelper Helper) {
        mHelper = Helper;
    }
    /**
     * @brief gets the database helper
     */
    public SQLiteOpenHelper getOpenHelper(){
        return mHelper;
    }
    // The URI scheme used for content URIs
    String mAuthority;

    /**
     *  The URI name for all the tables
     */
   public static final String ALL_NAME = "ALL";
    /**
     *  All tables uri matcher
     */
   public static final int ALL_CONTENT_URI_N = 0;

   
    /**
     * Devices table name
     */
    public static final String DEVICES_TABLE_NAME = EMBeaconDevice.Tablename;
    /**
     *  Devices table content matcher
     */
    public static final int DEVICES_TABLE_CONTENT_URI_N = 1;

    /**
     * Advertisements table name
     */
    public static final String ADVERTISEMENTS_TABLE_NAME = EMBeaconAdvertisement.Tablename;

    /**
     *  Advertisements table content matcher
     */
    public static final int ADVERTISEMENTS_TABLE_CONTENT_URI_N = 2;

    /**
     * @brief EMBeacon table name
     */
    public static final String EMBEACON_MANUF_TABLE_NAME = EMBeaconManufacturerData.Tablename;

    /**
     *  @brief EMBeacon table content matcher
     */
    public static final int EMBEACON_MANUF_CONTENT_URI_N = 3;

    /**
     * @brief AltBeacon table name
     */
    public static final String EMALTBEACON_MANUF_TABLE_NAME = EMALTManufacturerData.Tablename;

    /**
     *  @brief AltBeacon table content matcher
     */
    public static final int EMALTBEACON_MANUF_CONTENT_URI_N = 4;

    /**
     * @brief AltBeacon table name
     */
    public static final String EMIDBEACON_MANUF_TABLE_NAME = EMIDManufacturerData.Tablename;
    /**
     * @brief IDBeacon table content matcher
     */
    public static final int EMIDBEACON_MANUF_CONTENT_URI_N = 5;

   /**
     * @brief EddyStone Beacon table name
     */
    public static final String EMEDTLMBEACON_MANUF_TABLE_NAME = EMEDTLMManufacturerData.Tablename;
    /**
     * @brief IDBeacon table content matcher
     */
    public static final int EMEDTLMBEACON_MANUF_CONTENT_URI_N = 6;
   /**
     * @brief EddyStone Beacon table name
     */
    public static final String EMEDURLBEACON_MANUF_TABLE_NAME = EMEDURLManufacturerData.Tablename;
    /**
     * @brief IDBeacon table content matcher
     */
    public static final int EMEDURLBEACON_MANUF_CONTENT_URI_N = 7;
   /**
     * @brief EddyStone Beacon table name
     */
    public static final String EMEDURIBEACON_MANUF_TABLE_NAME = EMEDUIDManufacturerData.Tablename;
    /**
     * @brief IDBeacon table content matcher
     */
    public static final int EMEDURIBEACON_MANUF_CONTENT_URI_N = 8;

   /**
     * @brief EddyStone Beacon table name
     */
    public static final String DISPLAY_TABLE_NAME = EMBeaconDeviceDisplayData.Tablename;
    /**
     * @brief IDBeacon table content matcher
     */
    public static final int DISPLAY_TABLE_CONTENT_URI_N = 9;
   

   public static String[] TABLE_NAMES;
   public static Uri[] CONTENT_URIS;
    /**
     *  Indicates an invalid content URI
     */
    public static final int INVALID_URI = -1;

    // Defines a helper object that matches content URIs to table-specific parameters
    private static UriMatcher sUriMatcher;
    // Creates an object that associates content URIs with numeric codes
    private static void seturimatcher() {
        sUriMatcher = new UriMatcher(0);
        int i;
        for(i = 0; i < TABLE_NAMES.length; i++){
           sUriMatcher.addURI(Constants.AUTHORITY, TABLE_NAMES[i], i);
        }
    }

    /**
     * @brief delete something from the database
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        //throw new UnsupportedOperationException("Not yet implemented");
        int uidmatch = sUriMatcher.match(uri);
        synchronized(mLock) {
            switch (uidmatch) {
                case ALL_CONTENT_URI_N:
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    db.execSQL("DROP TABLE IF EXISTS " + EMBeaconDevice.Tablename + " ;");
                    db.execSQL("DROP TABLE IF EXISTS " + EMBeaconDeviceDisplayData.Tablename + " ;");
                    db.execSQL("DROP TABLE IF EXISTS " + EMBeaconAdvertisement.Tablename + " ;");

                    db.execSQL("DROP TABLE IF EXISTS " + EMBeaconManufacturerData.Tablename + " ;");

                    db.execSQL("DROP TABLE IF EXISTS " + EMALTManufacturerData.Tablename + " ;");
                    db.execSQL("DROP TABLE IF EXISTS " + EMIDManufacturerData.Tablename + " ;");

                    db.execSQL("DROP TABLE IF EXISTS " + EMEDURLManufacturerData.Tablename + " ;");
                    db.execSQL("DROP TABLE IF EXISTS " + EMEDUIDManufacturerData.Tablename + " ;");
                    db.execSQL("DROP TABLE IF EXISTS " + EMEDTLMManufacturerData.Tablename + " ;");

                    mHelper.onCreate(db);

                    ContentResolver cr = getContext().getContentResolver();
                    Uri noteUri = ContentUris.withAppendedId(EMBeaconDevice.TABLE_CONTENT_URI(), 0);
                    cr.notifyChange(noteUri, null);
                    noteUri = ContentUris.withAppendedId(EMBeaconDeviceDisplayData.TABLE_CONTENT_URI(), 0);
                    cr.notifyChange(noteUri, null);
                    noteUri = ContentUris.withAppendedId(EMBeaconAdvertisement.TABLE_CONTENT_URI(), 0);
                    cr.notifyChange(noteUri, null);

                    noteUri = ContentUris.withAppendedId(EMBeaconManufacturerData.TABLE_CONTENT_URI(), 0);
                    cr.notifyChange(noteUri, null);
                    noteUri = ContentUris.withAppendedId(EMALTManufacturerData.TABLE_CONTENT_URI(), 0);
                    cr.notifyChange(noteUri, null);
                    noteUri = ContentUris.withAppendedId(EMIDManufacturerData.TABLE_CONTENT_URI(), 0);
                    cr.notifyChange(noteUri, null);

                    noteUri = ContentUris.withAppendedId(EMEDURLManufacturerData.TABLE_CONTENT_URI(), 0);
                    cr.notifyChange(noteUri, null);
                    noteUri = ContentUris.withAppendedId(EMEDUIDManufacturerData.TABLE_CONTENT_URI(), 0);
                    cr.notifyChange(noteUri, null);
                    noteUri = ContentUris.withAppendedId(EMEDTLMManufacturerData.TABLE_CONTENT_URI(), 0);
                    cr.notifyChange(noteUri, null);

                    break;
                default:
                    break;
            }
        }
        return 0;
    }

    /**
     * @brief  gets the MIME type of the data
     */
    @Override
    public String getType(Uri uri) {
        // Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }
   /**
    * @brief  deletes the oldest rows from table 'table' leaving limit
    * @param table name of the table to trim
    * @param limit number of rows to keep
    */
   public void trimTable( String table, int limit){
      // delete from table  order by _id desc limit  (select count(*) from table) - limit
      // or if delete doesn't have a limit
      // delete from Advertisements where _id < (select _id from (select * from 'Advertisements'  order by _id asc limit  1 offset (select count(*) from advertisements) - 10));
       SQLiteDatabase db = mHelper.getWritableDatabase();
      int numberdeleted = 0;
      String sql = String.format("delete from %s where _id < (select _id from (select * from %s  order by _id asc limit  1 offset (select count(*) from %s) - %d))", table, table, table, limit);
        db.acquireReference();
        try {
           SQLiteStatement statement = db.compileStatement(sql);
            try {
                numberdeleted = statement.executeUpdateDelete();
            } catch (Exception e){
               Log.i(TAG,String.format("delete error %s %d ",table,limit));
            }  finally {
                statement.close();
            }
        }  catch (Exception e){
           Log.i(TAG,String.format("delete statement error %s %d ",table,limit));
        } finally {
            db.releaseReference();
        }
       if(numberdeleted > 0) {
          // Log.i(TAG,String.format("deleted %d from %s",numberdeleted,table));
       }
   }
   private String deviceInsertString(String tablename,ContentValues values){
             String address = values.getAsString(EMBeaconDevice.ADDRESS);
             Integer beacontype  = values.getAsInteger(EMBeaconDevice.TYPE);
             Set<String> keys = values.keySet();
             StringBuilder sql = new StringBuilder();
             sql.append("INSERT OR REPLACE INTO ");
             sql.append(tablename);
             sql.append(" ( _id");
             for (String colName : keys) {
                sql.append(" , ");
                sql.append(colName);
             }
             sql.append(")");
             sql.append(" VALUES (");
             // This select returns null if the device hasn't been added
             sql.append(String.format("(Select _id from \"%s\" where ADDRESS = '%s' and TYPE = '%d' )", tablename, address, beacontype));
             for (String colName : keys) {
                sql.append(" , ");
                sql.append(String.format("\"%s\"",values.getAsString(colName)));
             }
             sql.append(")");
             
             return sql.toString();
   }
    /**
     * @brief method for inserting data into the database
     *
     * INSERT OR REPLACE INTO "Devices" VALUES((Select _id from "Devices" where ADDRESS = '01:02:03:04:05:09'),'01:02:03:04:05:09',1116991367931158,-299);
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        synchronized (mLock) {
            SQLiteDatabase db = null;
            String sqlstring;
            long rowId = -1;
            int uidmatch = sUriMatcher.match(uri);
            switch (uidmatch) {
                case DEVICES_TABLE_CONTENT_URI_N:
                    // This adds a device to the Devices table.
                    // If the device is already there, it replaces the row
                    //            values.put("_id",String.format("(Select _id from \"Devices\" where ADDRESS = '%s')",address));
                    //            rowId = db.insertWithOnConflict(DEVICES_TABLE_NAME,EMBeaconAdvertisement.TIME_COLUMN,values,SQLiteDatabase.CONFLICT_REPLACE);
                    sqlstring = deviceInsertString(DEVICES_TABLE_NAME, values);
                    db = mHelper.getWritableDatabase();
                    // execute the statement
                    try {
                        SQLiteStatement statement = db.compileStatement(sqlstring);
                        rowId = statement.executeInsert();
                    } catch (Exception e) {
                        //Log.i(TAG,String.format("%d add errror row %d:%s", uidmatch, rowId,sql.toString()));
                    }
                    //Log.i(TAG,String.format("%d %s add row %d: %s", uidmatch, TABLE_NAMES[uidmatch],rowId,sqlstring));
                    break;
                case DISPLAY_TABLE_CONTENT_URI_N:
                    sqlstring = deviceInsertString(TABLE_NAMES[uidmatch], values);
                    db = mHelper.getWritableDatabase();
                    try {
                        SQLiteStatement statement = db.compileStatement(sqlstring);
                        rowId = statement.executeInsert();
                    } catch (Exception e) {
                        //Log.i(TAG,String.format("%d add errror row %d:%s", uidmatch, rowId,sql.toString()));
                    }
                    // Log.i(TAG,String.format("%d %s add row %d: %s", uidmatch, TABLE_NAMES[uidmatch],rowId,sqlstring));
                    break;
                case ADVERTISEMENTS_TABLE_CONTENT_URI_N:
                    db = mHelper.getWritableDatabase();
                    rowId = db.insert(TABLE_NAMES[uidmatch], EMBeaconAdvertisement.DATA_COLUMN, values);
                    trimTable(ADVERTISEMENTS_TABLE_NAME, 50);
                    //Log.i(TAG,String.format("%d %s add row %d: %s", uidmatch, TABLE_NAMES[uidmatch],rowId,values.toString()));
                    break;
                case EMBEACON_MANUF_CONTENT_URI_N:
                    db = mHelper.getWritableDatabase();
                    rowId = db.insert(TABLE_NAMES[uidmatch], EMBeaconManufacturerData.COMPANYCODE, values);
                    trimTable(EMBEACON_MANUF_TABLE_NAME, 50);
                    //Log.i(TAG,String.format("%d %s add row %d: %s", uidmatch, TABLE_NAMES[uidmatch],rowId,values.toString()));
                    break;
                case EMALTBEACON_MANUF_CONTENT_URI_N:
                    db = mHelper.getWritableDatabase();
                    rowId = db.insert(TABLE_NAMES[uidmatch], EMALTManufacturerData.COMPANYCODE, values);
                    trimTable(EMALTBEACON_MANUF_TABLE_NAME, 50);
                    //Log.i(TAG,String.format("%d %s add row %d: %s", uidmatch, TABLE_NAMES[uidmatch],rowId,values.toString()));
                    break;
                case EMIDBEACON_MANUF_CONTENT_URI_N:
                    db = mHelper.getWritableDatabase();
                    rowId = db.insert(TABLE_NAMES[uidmatch], EMIDManufacturerData.COMPANYCODE, values);
                    //Log.i(TAG,String.format("%d %s add row %d: %s", uidmatch, TABLE_NAMES[uidmatch],rowId,values.toString()));
                    trimTable(EMIDBEACON_MANUF_TABLE_NAME, 50);
                    break;
                case EMEDURIBEACON_MANUF_CONTENT_URI_N:
                    db = mHelper.getWritableDatabase();
                    rowId = db.insert(TABLE_NAMES[uidmatch], EMEDUIDManufacturerData.UUID, values);
                    trimTable(EMEDURIBEACON_MANUF_TABLE_NAME, 50);
                    //Log.i(TAG,String.format("%d %s add row %d: %s", uidmatch, TABLE_NAMES[uidmatch],rowId,values.toString()));
                    break;
                case EMEDURLBEACON_MANUF_CONTENT_URI_N:
                    db = mHelper.getWritableDatabase();
                    rowId = db.insert(TABLE_NAMES[uidmatch], EMEDURLManufacturerData.UUID, values);
                    trimTable(EMEDURLBEACON_MANUF_TABLE_NAME, 50);
                    //Log.i(TAG,String.format("%d %s add row %d: %s", uidmatch, TABLE_NAMES[uidmatch],rowId,values.toString()));
                    break;
                case EMEDTLMBEACON_MANUF_CONTENT_URI_N:
                    db = mHelper.getWritableDatabase();
                    rowId = db.insert(TABLE_NAMES[uidmatch], EMEDTLMManufacturerData.UUID, values);
                    trimTable(EMEDTLMBEACON_MANUF_TABLE_NAME, 50);
                    //Log.i(TAG,String.format("%d %s add row %d: %s", uidmatch, TABLE_NAMES[uidmatch],rowId,values.toString()));
                    break;
                default:
                    break;
            }
            Uri noteUri = ContentUris.withAppendedId(uri, rowId);
            //Log.i(TAG, String.format("%d add %s %d row %d:%s", uidmatch, values.getAsString(EMBeaconDevice.ADDRESS),values.getAsInteger(EMBeaconDevice.RSSI),rowId, noteUri.toString()));
            ContentResolver cr = getContext().getContentResolver();
            cr.notifyChange(noteUri, null);
//       notifyChange(noteUri, null);
            try {
                //           db.endTransaction();
            } catch (Exception e) {

            }
            if (db != null) {
                //  db.close();
            }
            return noteUri;
        }
       
    }
   private Handler notifyHandler = new Handler();
   private Runnable notifyRunnable = new Runnable() {
         @Override
         public void run() {
            ContentResolver cr = getContext().getContentResolver();
            cr.notifyChange(Constants.CONTENT_URI, null);
            notifypending = false;
         }
      };
   
   private boolean notifypending = false;
    private void notifyChange(Uri contentUri, ContentObserver o) {
        // throttle notifications. delay this notification for 100ms;
       if(!notifypending) {
        notifypending = true;
        notifyHandler.postDelayed(notifyRunnable,100);
       }
    }

    /**
     * @brief onCreate callback
     */
    @Override
    public boolean onCreate() {
        mHelper = EMSQL.getOpenHelper(getContext());
        return true;
    }

    /**
     * @brief ContentProivider query
     * @param uri Uri of the query, gives the table
     * @param projection What columns to give
     * @param selection What rows to give
     * @param selectionArgs Parameterizatio for the selection, replaces ?s in the selection
     * @param sortOrder How to sort
     * @return The cursor
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

     synchronized (mLock) {
         SQLiteDatabase db = mHelper.getReadableDatabase();

         Cursor returnCursor;
         // Decodes the content URI and maps it to a code
         //Uri uri1 = uri;
         int match = sUriMatcher.match(uri);
         switch (match) {

             // If the query is for a picture URL
             case DEVICES_TABLE_CONTENT_URI_N:
                 returnCursor = db.query(DEVICES_TABLE_NAME,
                         projection,
                         selection,
                         selectionArgs,
                         null,
                         null,
                         sortOrder);
                 //Log.i(TAG, String.format("query %d", returnCursor.getCount()));

                 // // Sets the ContentResolver to watch this content URI for data changes
                 returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
                 return returnCursor;

             // If the query is for a modification date URL
             case ADVERTISEMENTS_TABLE_CONTENT_URI_N:
                 returnCursor = db.query(ADVERTISEMENTS_TABLE_NAME,
                         projection,
                         selection,
                         selectionArgs,
                         null,
                         null,
                         sortOrder);


                 // // Sets the ContentResolver to watch this content URI for data changes
                 //Log.i(TAG,String.format("query %d",returnCursor.getCount()));
                 returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
                 return returnCursor;
         }
         if ((match > 0) && (match < TABLE_NAMES.length)) {
             String limit = uri.getQueryParameter("limit");
             returnCursor = db.query(TABLE_NAMES[match],
                     projection,
                     selection,
                     selectionArgs,
                     null,    //  groupBy
                     null,    //  having
                     sortOrder,
                     limit);
             Log.i(TAG, String.format("query %d(%s) %s %d", match, TABLE_NAMES[match], limit, returnCursor.getCount()));
             returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
             return returnCursor;
         }
     }
       return null;
    }
    /**
     * @brief updates a row
     * not supported
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * After being instantiated, this is called to tell the content provider
     * about itself.
     *
     * @param context The context this provider is running in
     * @param info    Registered information about this content provider
     */
    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        if(info != null) {
           mAuthority = info.authority;
        } else {
           mAuthority = Constants.AUTHORITY;
        }
        Constants.setConstants(mAuthority);

        seturimatcher();
        super.attachInfo(context, info);
    }

    /**
     * Implement this to shut down the ContentProvider instance. You can then
     * invoke this method in unit tests.
     * <p/>
     * <p>
     * Android normally handles ContentProvider startup and shutdown
     * automatically. You do not need to start up or shut down a
     * ContentProvider. When you invoke a test method on a ContentProvider,
     * however, a ContentProvider instance is started and keeps running after
     * the test finishes, even if a succeeding test instantiates another
     * ContentProvider. A conflict develops because the two instances are
     * usually running against the same underlying data source (for example, an
     * sqlite database).
     * </p>
     * <p>
     * Implementing shutDown() avoids this conflict by providing a way to
     * terminate the ContentProvider. This method can also prevent memory leaks
     * from multiple instantiations of the ContentProvider, and it can ensure
     * unit test isolation by allowing you to completely clean up the test
     * fixture before moving on to the next test.
     * </p>
     */
    @Override
    public void shutdown() {

       //Log.i(TAG,"shutdown");
        mHelper.close();
    }

    /**
     * A test package can call this to get a handle to the database underlying NotePadProvider,
     * so it can insert test data into the database. The test case class is responsible for
     * instantiating the provider in a test context; {android.test.ProviderTestCase2} does
     * this during the call to setUp()
     *
     * @return a handle to the database helper object for the provider's data.
     */
    public SQLiteOpenHelper getOpenHelperForTest(Context context) {
        if(mHelper == null)
            mHelper = new EMSQL.DatabaseHelper(context);
        return mHelper;
    }

}

