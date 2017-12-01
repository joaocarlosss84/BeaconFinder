/*
 ** ############################################################################
 **
 ** file    EMBeaconManufacturerData.java
 ** brief   processing the EMBeacon specific advertisement data
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
package com.emdeveloper.embeaconlib.database;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.emdeveloper.embeaconlib.EMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.EMBluetoothDevice;
import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.IEMBluetoothDevice;
import com.emdeveloper.embeaconlib.embeaconspecific.ConversionRoutine;
import com.emdeveloper.embeaconlib.embeaconspecific.Sensors;
import com.emdeveloper.embeaconlib.embeaconspecific.Events;
import com.emdeveloper.embeaconlib.embeaconspecific.Utils;

import java.util.Arrays;

/**
 * @brief the EMBeaconManufacturerData class deals with the manufacturer specific advertising data that an EMBeacon uses.
 *
 * It contains the
 *  - sqlite database table schema
 *  - extraction methods
 *  - insertion methods
 *
 */
public class EMBeaconManufacturerData {
    private final static String TAG = "EMBeaconManufacturerData";
    /*
     * @defgroup EMBeaconManufacturerDatavariables  variables holding column data
     * @name EMBeaconManufacturerDatavariables  variables holding column data
     * @{
     */
    public Integer mCompanyCode;
    public Integer mOpenSenseType;
    public Integer mOpenSenseValue;
    public Integer mModelID;
    public Float   mBattery;
    public Integer mPacketCount;
    public Integer mEventType;
    public Integer mEventValue;
    public String  mName;
    public Integer mRssi;
    public String  mAddress;
    public Long    mTime;


   public IEMBluetoothAdvertisement mAdvertisement;
/**
     * @}
     */
    /**
     *  @brief Table name
     */
    public static final String Tablename       = "EMData";
    /*
     * @defgroup EMBeaconManufacturerDataColumnNames Column Names
     * @name EMBeaconManufacturerDataColumnNames Column Names
     * @{
     */
    public static final String ADVERTISEMENTID = "ADVERTISEMENTID"; // _id of associated advertisement
    public static final String COMPANYCODE     = "COMPANYCODE";
    public static final String OPENSENSETYPE   = "OPENSENSETYPE";
    public static final String OPENSENSEVALUE  = "OPENSENSEVALUE";
    public static final String MODELID         = "MODELID";
    public static final String BATTERY         = "BATTERY";
    public static final String PACKETS         = "PACKETS";
    public static final String EVENTTYPE       = "EVENTTYPE";
    public static final String EVENTVALUE      = "EVENTVALUE";
    public static final String NAME            = "NAME";
    public static final String RSSI            = "RSSI";
    public static final String ADDRESS         = "ADDRESS";
    public static final String TIME            = "TIME";
    /**
     *  @}
     */
    /**
     * URI for the content provider
     */
    public static  Uri TABLE_CONTENT_URI(){return  EMContentProvider.Constants.EMBEACON_MANUF_CONTENT_URI;}
/**
 * @brief Columns in the ManufacturerData table
 */
    public static final String Columns[] = {
        "_id",
        ADVERTISEMENTID, // _id of associated advertisement
        COMPANYCODE,
        OPENSENSETYPE,
        OPENSENSEVALUE,
        MODELID,
        BATTERY,
        PACKETS,
        EVENTTYPE,
        EVENTVALUE,
        NAME,
        RSSI,
        ADDRESS,
        TIME
    };
    /** @}
     */
/**
 * @brief Types of the Columns in the ManufacturerData table
 */
    public static final String ColumnTypes[] = {
        "INTEGER PRIMARY KEY",  //"_id",
        "INTEGER",              //"ADVERTISEMENTID",
        "INTEGER",              //"COMPANYCODE",
        "INTEGER",              //"OPENSENSETYPE",
        "INTEGER",              //"OPENSENSEVALUE",
        "TEXT",                 //"MODELID",
        "FLOAT",                //"BATTERY",
        "INTEGER",              //"PACKETS",
        "INTEGER",              //"EVENTTYPE",
        "INTEGER",              //"EVENTVALUE"
        "TEXT",                 //"NAME",
        "INTEGER",              //"RSSI",
        "TEXT",                 //"ADDRESS",
        "INTEGER",              //"TIME",
    };
    /** @}
     */
    // for initialization
/**
 * @brief returns the table name of  ManufacturerData table
 */
    public static String getTableName() {return Tablename;}
/**
 * @brief returns the columns in the ManufacturerData table
 */
    public static String[] getTableColumns() {return Columns;}
/**
 * @brief returns the column types ManufacturerData table
 */
    public static String[] getTableColumnTypes() {return ColumnTypes;}

    // create from a EMBluetoothAdvertisement
/**
 * @brief Creates an EMBeaconManufacturerData from a IEMBluetoothAdvertisement
 * @return a populated EMBeaconManufacturerData instance
 */
    public static EMBeaconManufacturerData create(IEMBluetoothAdvertisement adv)
    {
        if(adv.getType() != IEMBluetoothAdvertisement.EMBEACON_TYPE_ID) return null;
        byte[] d = adv.getAdvertisementData(IEMBluetoothAdvertisement.MANUFACTURER_SPECIFIC_TYPE);
        if(d == null) return null;
        EMBeaconManufacturerData retv =  new EMBeaconManufacturerData(d);
        retv.mName = adv.getName();
        retv.mRssi = adv.getRSSI();
        retv.mTime = adv.getTime();
        retv.mAddress = adv.getAddress();
        retv.mAdvertisement = adv;

        return retv;
    }

    /**
     * @brief creates an EMBeaconManufacturerData instance from the raw data;
       \code.{C}
       struct AD_manufdata_s
       {
           HCI_LE_Advertising_Data_Structure_header_t hdr; // UINT8 length, UINT8 AD_Type
           UINT16  companyCode;        // 0,1
           UINT16  openSense;          // 2,3  (content defined by first nibble)
           UINT16  modelID;            // 4,5
           UINT8   battery;            // 6
           unsigned long long packet_count;    // 7..10
           UINT16  event_count;        // 11,12
       };
       \endcode
     */
    public EMBeaconManufacturerData(byte[] data){
        mCompanyCode = EMBeaconAdvertisement.MyInteger(data[1], data[0]);
        Integer OpenSense = EMBeaconAdvertisement.MyInteger(data[2], data[3]);
        mOpenSenseType = (OpenSense >> 12) & 0xf;
        mOpenSenseValue = OpenSense & 0xfff;
        // sign extend
        if((mOpenSenseValue & 0x800) == 0x800) {
            mOpenSenseValue |= 0xfffff000;
        }

       // byte[] dd = Arrays.copyOfRange(data, 4, 6);
        mModelID = EMBeaconAdvertisement.MyInteger(data[4],data[5]);
        Integer b1 = data[6]>>4;
        Integer b2 = data[6]&0xf;
        Float b = b1.floatValue()+b2.floatValue()/10;
        //  b = b + Float.intBitsToFloat(data[6]&0xf)/10;
        mBattery = b;
        mPacketCount = EMBeaconAdvertisement.MyInteger(data[7], data[8], data[9], data[10]);
        Integer Event = EMBeaconAdvertisement.MyInteger(data[11], data[12]);
        mEventType = (Event >> 12) & 0xf;
        mEventValue = Event & 0xfff;
        if((mEventValue & 0x800) != 0){
           mEventValue |= 0xfffff000;
        }


    }
   /**
   * @brief create empty one
   */
   public EMBeaconManufacturerData(){};

   /**
   * @brief Sets the class data from a cursor
   */
   public void setFromCursor(Cursor c){
      int idx__ix               = c.getColumnIndex("_id");
      int idx_advo               = c.getColumnIndex(ADVERTISEMENTID);
      int idx_CompanyCode       = c.getColumnIndex(COMPANYCODE);
      int idx_OpenSenseType     = c.getColumnIndex(OPENSENSETYPE);
      int idx_OpenSenseValue    = c.getColumnIndex(OPENSENSEVALUE);
      int idx_ModelID           = c.getColumnIndex(MODELID);
      int idx_Battery           = c.getColumnIndex(BATTERY);
      int idx_PacketCount       = c.getColumnIndex(PACKETS);
      int idx_EventType         = c.getColumnIndex(EVENTTYPE);
      int idx_EventValue        = c.getColumnIndex(EVENTVALUE);
      int idx_Name              = c.getColumnIndex(NAME);
      int idx_Rssi              = c.getColumnIndex(RSSI);
      int idx_Address           = c.getColumnIndex(ADDRESS);
      int idx_Time              = c.getColumnIndex(TIME);
      if(idx_CompanyCode > 0) mCompanyCode = c.getInt(idx_CompanyCode);
      if(idx_OpenSenseType > 0) mOpenSenseType = c.getInt(idx_OpenSenseType);
      if(idx_OpenSenseValue > 0) mOpenSenseValue = c.getInt(idx_OpenSenseValue);
      if(idx_ModelID > 0) mModelID = c.getInt(idx_ModelID);
      if(idx_Battery > 0) mBattery = c.getFloat(idx_Battery);
      if(idx_PacketCount > 0) mPacketCount = c.getInt(idx_PacketCount);
      if(idx_EventType > 0) mEventType = c.getInt(idx_EventType);
      if(idx_EventValue > 0) mEventValue = c.getInt(idx_EventValue);
      if(idx_Name > 0) mName = c.getString(idx_Name);
      if(idx_Rssi > 0) mRssi = c.getInt(idx_Rssi);
      if(idx_Address > 0) mAddress = c.getString(idx_Address);
      if(idx_Time     > 0) mTime     = c.getLong(idx_Time    );

   }
    // for parsing the beacon name
/**
 * @brief The string to look for in an EMBeacon advertisement name
 */
    public static String BeaconNameStart = "EMB";

    /**
     * @brief returns true if this is an em beacon
     */
    public static boolean isMyAdvertisement(IEMBluetoothAdvertisement advert){
       byte[] nameb = advert.getAdvertisementData(IEMBluetoothAdvertisement.COMPLETE_LOCAL_NAME_TYPE);
       if(nameb == null) return false;
       String name = new String(nameb);
       name = name.trim();
       return ((name != null) &&  name.startsWith(BeaconNameStart));
    }

    /**
     * @brief returns a ContentValues  instance for inserting into the database
     */
    public ContentValues getContentValues()
    {
        ContentValues v = new ContentValues();
        // add to advertisement table
        v.put(COMPANYCODE, mCompanyCode);
        v.put(OPENSENSETYPE, mOpenSenseType);
        v.put(OPENSENSEVALUE, mOpenSenseValue);
        v.put(MODELID, mModelID);
        v.put(BATTERY, mBattery);
        v.put(PACKETS, mPacketCount);
        v.put(EVENTTYPE, mEventType);
        v.put(EVENTVALUE, mEventValue);
        v.put(NAME, mName);
        v.put(RSSI, mRssi);
        v.put(TIME, mTime);
        v.put(ADDRESS, mAddress);
        return v;
    }

    /**
     * @brief for test, create one with given values
     */
    public static EMBeaconManufacturerData generate(String address,Integer Rssi, Long Time, String Name,
                                                    Integer SensorType, Integer SensorValue,
                                                    Integer EventType, Integer EventValue,
                                                    String ModelId,
                                                    Float Battery,
                                                    Integer CompanyCode,
                                                    Integer PacketCount)
    {
        EMBeaconManufacturerData result = new EMBeaconManufacturerData();
        result.mCompanyCode = CompanyCode;
        result.mOpenSenseType = SensorType;
        result.mOpenSenseValue = SensorValue;
        int imodelid = ModelId.charAt(0) << 8 + ModelId.charAt(1);
        result.mModelID = imodelid;
        result.mBattery = Battery;
        result.mPacketCount = PacketCount;
        result.mEventType = EventType;
        result.mEventValue = EventValue;
        result.mName = Name;
        result.mRssi = Rssi;
        result.mAddress = address;
        result.mTime = Time;
        return result;
    }


   public static EMBeaconManufacturerData createFromCursor(Cursor c){
     EMBeaconManufacturerData emd = new EMBeaconManufacturerData();
     if(c.getCount() <=0)
          return null;
      c.moveToFirst();
      emd.setFromCursor(c);
      return emd;
   }
   public void log(){
      if(EMBluetoothDatabase.getIEMBluetoothDatabase() != null &&
         EMBluetoothDatabase.getIEMBluetoothDatabase().isLoggingEnabled()) {
         Resources r;
         r = Utils.context.getResources();

         ConversionRoutine sens = Sensors.getConversion(mOpenSenseType);
         ConversionRoutine evt = Events.getConversion(mEventType);
         final DateFormat csvDate = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss.SSS");
         String logv[] = new String[] {
            csvDate.format(mTime),
            mName,
            Sensors.getConversion(Sensors.TYPE_MODEL).value(mModelID),
            sens.getName(),
            sens.value(mOpenSenseValue)+ " "+ r.getString(sens.Units()),
            evt.getName(),
            evt.value(mEventValue)+ " "+r.getString(evt.Units()),
            Sensors.getConversion(Sensors.TYPE_BATTERY).value(mBattery) + " " +
            r.getString(Sensors.getConversion(Sensors.TYPE_BATTERY).Units()),
            Sensors.getConversion(Sensors.TYPE_PACKETS).value(mPacketCount) + " " +
            r.getString(Sensors.getConversion(Sensors.TYPE_PACKETS).Units()),
            Sensors.getConversion(Sensors.TYPE_RSSI).value(mRssi) + " " +
            r.getString(Sensors.getConversion(Sensors.TYPE_RSSI).Units()) + "\n",
            };

         final String logData = TextUtils.join(",",logv);


         new Thread()
         {
            @Override
            public void run() {
               CSVLogger.writeLog(Utils.context, logData);
            }
         }.start();
      }
   }
}


