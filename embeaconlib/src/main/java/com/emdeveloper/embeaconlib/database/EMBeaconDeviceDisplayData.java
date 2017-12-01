/*
 ** ############################################################################
 **
 ** file    EMIDManufacturerData.java
 ** brief   handling alt beacons
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

import android.content.ContentValues;
import android.net.Uri;
import android.util.SparseArray;

import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;
/**
 * @brief EMIDManufacturerData deals with the advertisements from an Alt Beacon
 */
public class EMBeaconDeviceDisplayData {
  /**
   * URI Scheme maps a byte code into the scheme and an optional scheme specific prefix.
   */
  private static final SparseArray<String> URI_SCHEMES = new SparseArray<String>() {{
    put((byte) 0, "http://www.");
    put((byte) 1, "https://www.");
    put((byte) 2, "http://");
    put((byte) 3, "https://");
    put((byte) 4, "urn:uuid:");    // RFC 2141 and RFC 4122};
  }};
  /**
   * Expansion strings for "http" and "https" schemes. These contain strings appearing anywhere in a
   * URL. Restricted to Generic TLDs. <p/> Note: this is a scheme specific encoding.
   */
  private static final SparseArray<String> URL_CODES = new SparseArray<String>() {{
    put((byte) 0, ".com/");
    put((byte) 1, ".org/");
    put((byte) 2, ".edu/");
    put((byte) 3, ".net/");
    put((byte) 4, ".info/");
    put((byte) 5, ".biz/");
    put((byte) 6, ".gov/");
    put((byte) 7, ".com");
    put((byte) 8, ".org");
    put((byte) 9, ".edu");
    put((byte) 10, ".net");
    put((byte) 11, ".info");
    put((byte) 12, ".biz");
    put((byte) 13, ".gov");
  }};

   static String parseURL(int scheme,byte[] blobdata){
      byte urlt = blobdata[0];
      urlt -=0x30;
      //Log.i(TAG,"url " + blobdata.length + " " + urlt);
      StringBuilder s = new StringBuilder(31);
      // decode the url
      s.append(URI_SCHEMES.get(scheme));
      // there is an extra null at the end of the data?
      for(int ii = 0; ii < blobdata.length; ii++) {
         byte b = blobdata[ii];
         String code = URL_CODES.get(b);
         if (code != null) {
            s.append(code);
         }else {
            s.append((char) b);
         }
      }
      return s.toString();
   }
   static String parseUID(byte[] namespace, byte[] id){
      String bns = EMBeaconAdvertisement.byteToHex(namespace,0,namespace.length);
      String bid = EMBeaconAdvertisement.byteToHex(id,0,id.length);
      return bns+"-"+bid;
   }

/**
 * @brief deals with the advertisements from an Alt Beacon
 */
        public static final String Tablename = "FINDERDISPLAY";
/**
 * @brief Columns strings
 * @{
 */
        public static final String ADVERTISEMENTID = "ADVERTISEMENTID"; // _id of associated advertisement
        public static final String MAJORID = "MAJORID";
        public static final String MINORID = "MINORID";
    public static final String TYPE = "TYPE";
    public static final String RSSI = "RSSI";
    public static final String SERIALNUMBER = "SERIALNUMBER";   
    public static final String NAME = "NAME";
    public static final String EDDY = "EDDY";
   public static final String TIME = "TIME";
   public static final String ADDRESS = "ADDRESS";
    /*
 * @}
 */
    
/**
 * @brief AltBeacon table uri
 */
    static public Uri TABLE_CONTENT_URI() {return EMContentProvider.Constants.DISPLAY_TABLE_CONTENT_URI;}
/**
 * @brief Columns in the altBeacon table
 */
        public static final String Columns[] = {
            "_id",
      TYPE,
      NAME,
      TIME,
      SERIALNUMBER,
      ADDRESS,
      RSSI,
      MINORID,
      MAJORID,
      EDDY,
            ADVERTISEMENTID
        };
   Integer mType;
   String mName;
   Long   mTime;
   String mAddress;
   Integer mRSSI;
   Integer mMinorId;
   Integer mMajorId;
   Integer mAdvertisement;
   String mEddyStone;
   
/**
 * @brief Column types in the Alt Beacon table
 */
        public static final String ColumnTypes[] = {
            "INTEGER PRIMARY KEY", //"_id",
            "INTEGER", // type
            "TEXT", //name
            "TIME", //time
            "INTEGER", //serial number
            "TEXT",//address
            "INTEGER",//RSSI
            "INTEGER",//"MAJORID
            "INTEGER",//"MINORID",
            "TEXT",//EDDY
            "INTEGER",//Adv id
        };

/**
 * @brief creates an EMBeaconDeviceDisplayData from a row advertisement
 */
    // public EMBeaconDeviceDisplayData(byte[] data){
    //     mCompanyCode = EMBeaconAdvertisement.MyInteger(data[1], data[0]);
    //     mBeaconCode = EMBeaconAdvertisement.MyInteger(data[3], data[4]);
    //     mGuid = EMBeaconAdvertisement.byteToHex(data, 5, 16);
    //     mMajorId = EMBeaconAdvertisement.MyInteger(data[20], data[21]);
    //     mMinorId = EMBeaconAdvertisement.MyInteger(data[22], data[23]);
    //     mPower = EMBeaconAdvertisement.MyInteger(data[24]);
    //    // mBatteryPct = EMBeaconAdvertisement.MyInteger(data[25]);
    // }
/**
 * @brief returns the content values for use in the database
 */
    public ContentValues getContentValues()
    {        ContentValues v = new ContentValues();
        // add to advertisement table
        v.put(TYPE, mType);
        v.put(NAME, mName);
        v.put(TIME, mTime);
        v.put(ADDRESS, mAddress);
        v.put(RSSI, mRSSI);
        v.put(MAJORID, mMajorId);
        v.put(MINORID, mMinorId);
        v.put(ADVERTISEMENTID,mAdvertisement);
        v.put(EDDY,mEddyStone);
        return v;

    }
/**
 * @brief returns the Alt Beacon table name
 */
    public static String getTableName() {return Tablename;}
/**
 * @brief returns the altBeacon table columns
 */
    public static String[] getTableColumns() {return Columns;}
/**
 * @brief returns the altBeacon table column types
 */
    public static String[] getTableColumnTypes() {return ColumnTypes;}

    /**
     * @brief returns true if this is an alt beacon
     */
   public static boolean isMyAdvertisement(IEMBluetoothAdvertisement advert){
      return false;
    }
   public EMBeaconDeviceDisplayData(IEMBluetoothAdvertisement adv){
      mRSSI=adv.getRSSI();
      mAddress=adv.getAddress();
      mTime=adv.getTime();
       mType = adv.getType();
       mEddyStone = null;
       mName = null;
       mMajorId = null;
       mMinorId = null;

   }
   public static EMBeaconDeviceDisplayData add(IEMBluetoothAdvertisement adv,EMBeaconManufacturerData emaltd){
       EMBeaconDeviceDisplayData dd = new EMBeaconDeviceDisplayData(adv);
      dd.mType = IEMBluetoothAdvertisement.EMBEACON_TYPE_ID;
      dd.mName= adv.getName();
      return dd;
   }
   public static EMBeaconDeviceDisplayData add(IEMBluetoothAdvertisement adv,EMALTManufacturerData emaltd){
      EMBeaconDeviceDisplayData dd = new EMBeaconDeviceDisplayData(adv);
      dd.mType = IEMBluetoothAdvertisement.ALTBEACON_TYPE_ID;
      dd.mMajorId=emaltd.mMajorId;
      dd.mMinorId=emaltd.mMinorId;      
      return dd;
   }
   public static EMBeaconDeviceDisplayData add(IEMBluetoothAdvertisement adv,EMIDManufacturerData emaltd){
      EMBeaconDeviceDisplayData dd = new EMBeaconDeviceDisplayData(adv);
      dd.mType = IEMBluetoothAdvertisement.IDBEACON_TYPE_ID;
      dd.mMajorId=emaltd.mMajorId;
      dd.mMinorId=emaltd.mMinorId;      
      return dd;
   }
   public static EMBeaconDeviceDisplayData add(IEMBluetoothAdvertisement adv,EMEDURLManufacturerData emaltd){
      EMBeaconDeviceDisplayData dd = new EMBeaconDeviceDisplayData(adv);
      dd.mType = IEMBluetoothAdvertisement.EDURLBEACON_TYPE_ID;
      dd.mEddyStone = parseURL(emaltd.mEncoding,emaltd.mURL);
      return dd;
   }
   public static EMBeaconDeviceDisplayData add(IEMBluetoothAdvertisement adv,EMEDUIDManufacturerData emaltd){
      EMBeaconDeviceDisplayData dd = new EMBeaconDeviceDisplayData(adv);
      dd.mType = IEMBluetoothAdvertisement.EDUIDBEACON_TYPE_ID;
      dd.mEddyStone = parseUID(emaltd.mNamespace,emaltd.mBeaconId);
      return dd;
   }
   public static EMBeaconDeviceDisplayData add(IEMBluetoothAdvertisement adv,EMEDTLMManufacturerData emaltd){
      EMBeaconDeviceDisplayData dd = new EMBeaconDeviceDisplayData(adv);
      dd.mType = IEMBluetoothAdvertisement.EDTLMBEACON_TYPE_ID;
      dd.mEddyStone = "";
      return dd;
   }
}

