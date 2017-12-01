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

import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;
/**
 * @brief EMIDManufacturerData deals with the advertisements from an Alt Beacon
 */
public class EMIDManufacturerData {

/**
 * @brief deals with the advertisements from an Alt Beacon
 */
        public static final String Tablename = "IDData";
/**
 * @brief Columns strings
 * @{
 */
        public static final String ADVERTISEMENTID = "ADVERTISEMENTID"; // _id of associated advertisement
        public static final String COMPANYCODE = "COMPANYCODE";
        public static final String BEACONCODE = "BEACONCODE";
        public static final String GUID = "GUID";
        public static final String MAJORID = "MAJORID";
        public static final String MINORID = "MINORID";
        public static final String POWER = "POWER";
        public static final String BATTERP = "BATTERP";
    /*
 * @}
 */
    
/**
 * @brief AltBeacon table uri
 */
    static public Uri TABLE_CONTENT_URI() {return EMContentProvider.Constants.EMIDBEACON_MANUF_CONTENT_URI;}
/**
 * @brief Columns in the altBeacon table
 */
        public static final String Columns[] = {
            "_id",
             ADVERTISEMENTID , // _id of associated advertisement
             COMPANYCODE ,
             BEACONCODE ,
             GUID ,
             MAJORID ,
             MINORID ,
             POWER ,
             BATTERP
        };
/**
 * @brief Column types in the Alt Beacon table
 */
        public static final String ColumnTypes[] = {
            "INTEGER PRIMARY KEY", //"_id",
            "ADVERTISEMENTID", // _id of associated advertisement
            "INTEGER", //"COMPANYCODE",
            "TEXT",//"BEACONCODE",
            "TEXT",//"GUID",
            "INTEGER",//"MAJORID",
            "INTEGER",//"MINORID",
            "INTEGER",//"POWER",
            "INTEGER",//"BATTERP"
        };
/**
 * @brief Creates an EMIDManufacturerData instance from an IEMBluetoothAdvertisement advertisement
 */
    public static EMIDManufacturerData create(IEMBluetoothAdvertisement advert){
        Integer advtype = advert.getType();
        if(advert.getType() != IEMBluetoothAdvertisement.IDBEACON_TYPE_ID) return null;
        byte[] d = advert.getAdvertisementData(IEMBluetoothAdvertisement.MANUFACTURER_SPECIFIC_TYPE);
        if(d != null)
        {
            return new EMIDManufacturerData(d);
        }
        return null;
    }
/**
 * @brief creates an EMIDManufacturerData from a row advertisement
 */
    public EMIDManufacturerData(byte[] data){
        mCompanyCode = EMBeaconAdvertisement.MyInteger(data[1], data[0]);
        mBeaconCode = EMBeaconAdvertisement.MyInteger(data[3], data[4]);
        mGuid = EMBeaconAdvertisement.byteToHex(data, 5, 16);
        mMajorId = EMBeaconAdvertisement.MyInteger(data[20], data[21]);
        mMinorId = EMBeaconAdvertisement.MyInteger(data[22], data[23]);
        mPower = EMBeaconAdvertisement.MyInteger(data[24]);
       // mBatteryPct = EMBeaconAdvertisement.MyInteger(data[25]);
    }
/**
 * @brief returns the content values for use in the database
 */
    public ContentValues getContentValues()
    {        ContentValues v = new ContentValues();
        // add to advertisement table
        v.put(COMPANYCODE, mCompanyCode);
        v.put(BEACONCODE, mBeaconCode);
        v.put(GUID, mGuid);
        v.put(MAJORID, mMajorId);
        v.put(MINORID, mMinorId);
        v.put(POWER, mPower);                
     //   v.put(BATTERP, mBatteryPct);
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
        byte[] ad = advert.getAdvertisementData(IEMBluetoothAdvertisement.MANUFACTURER_SPECIFIC_TYPE);
        if(ad == null) return false;
        boolean mine = (ad.length > 5) && (ad[2] == (byte)0x02) && (ad[3] == (byte)0x15);
        return (mine);
    }
   Integer mCompanyCode;
   /**
    * @brief  the mBeaconCode field
    */
   Integer mBeaconCode;
   String mGuid;
   Integer mMajorId;
   Integer mMinorId;
   Integer mPower;
   //Integer mBatteryPct;
}

