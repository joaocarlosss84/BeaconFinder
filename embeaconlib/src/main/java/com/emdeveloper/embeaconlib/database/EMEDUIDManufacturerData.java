/*
 ** ############################################################################
 **
 ** file    EMEDURIManufacturerData.java
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

import java.util.Arrays;

/**
 * @brief EMEDUIDManufacturerData deals with the advertisements from an Alt Beacon
 */
public class EMEDUIDManufacturerData {

/**
 * @brief deals with the advertisements from an Alt Beacon
 */
        public static final String Tablename = "EDDYUIDData";
/**
 * @brief Columns strings
 * @{
 */
        public static final String ADVERTISEMENTID = "ADVERTISEMENTID"; // _id of associated advertisement
        public static final String UUID = "UUID";
        public static final String FRAMETYPE = "FRAMETYPE";
        public static final String UIDPOWER = "UIDPOWER";
   public static final String NAMESPACE = "NAMESPACE";
   public static final String BEACONID= "BEACONID";   
    /*
 * @}
 */
    
/**
 * @brief EddyStone URIBeacon table uri
 */
    static public Uri TABLE_CONTENT_URI() {return EMContentProvider.Constants.EMEDURIBEACON_MANUF_CONTENT_URI;}
/**
 * @brief Columns in the EddyStone URIBeacon table
 */
        public static final String Columns[] = {
            "_id",
             ADVERTISEMENTID , // _id of associated advertisement
            UUID,
            FRAMETYPE,
            UIDPOWER,
            NAMESPACE,
            BEACONID
        };
/**
 * @brief Column types in the EddyStone URI Beacon table
 */
        public static final String ColumnTypes[] = {
            "INTEGER PRIMARY KEY", //"_id",
            "ADVERTISEMENTID", // _id of associated advertisement
            "INTEGER",  // UUID
            "INTEGER",  // FRAMETYPE
            "INTEGER",  // UIDPOWER          
            "BLOB",     // NAMESPACE
            "BLOB",     // BEACONID       
        };
/**
 * @brief Creates an EMEDUIDManufacturerData instance from an IEMBluetoothAdvertisement advertisement
 */
    public static EMEDUIDManufacturerData create(IEMBluetoothAdvertisement advert){
        if(advert.getType() != IEMBluetoothAdvertisement.EDUIDBEACON_TYPE_ID) return null;
        byte[] d = advert.getAdvertisementData(IEMBluetoothAdvertisement.SERVICE_DATA_16BITS_UUID);
        if(d != null)
        {
            return new EMEDUIDManufacturerData(d);
        }
        return null;
    }
/**
 * @brief creates an EMEDUIDManufacturerData from raw ad
 */
    public EMEDUIDManufacturerData(byte[] data){
        mUUID = EMBeaconAdvertisement.MyInteger(data[0], data[1]);
        mFrameType = EMBeaconAdvertisement.MyInteger(data[2]);
        mPower = EMBeaconAdvertisement.MyInteger(data[3]);
        mNamespace = Arrays.copyOfRange(data, 4, 14);
        mBeaconId = Arrays.copyOfRange(data,14,14+6);
        // length should be 22
    }
/**
 * @brief returns the content values for use in the database
 */
    public ContentValues getContentValues()
    {        ContentValues v = new ContentValues();
        // add to advertisement table
         v.put(UUID, mUUID);
         v.put(FRAMETYPE, mFrameType);
         v.put(UIDPOWER, mPower);
         v.put(NAMESPACE, mNamespace);
         v.put(BEACONID, mBeaconId);
        return v;

    }
/**
 * @brief returns the EddyStone URI Beacon table name
 */
    public static String getTableName() {return Tablename;}
/**
 * @brief returns the EddyStone URIBeacon table columns
 */
    public static String[] getTableColumns() {return Columns;}
/**
 * @brief returns the EddyStone URIBeacon table column types
 */
    public static String[] getTableColumnTypes() {return ColumnTypes;}



    /**
     * @brief returns true if this is an EddyStone URI beacon
     */
    public static boolean isMyAdvertisement(IEMBluetoothAdvertisement advert){
        byte[] uuids = advert.getAdvertisementData(IEMBluetoothAdvertisement.COMPLETE_16BITS_UUID_TYPE);
        if(uuids == null) return false;
        if (!((uuids.length >1 ) && (uuids[0] == (byte)0xAA) && (uuids[1] == (byte)0xFE))) {
           return false;
        };
        byte[] sd = advert.getAdvertisementData(IEMBluetoothAdvertisement.SERVICE_DATA_16BITS_UUID);
        if(sd == null) return false;
        return ((sd.length > 2) && (sd[0] == (byte)0xAA) && (sd[1] == (byte)0xFE) && (sd[2] == (byte)0x00));
    }
   /**
    * @brief  the mBeaconCode field
    */
   public Integer mUUID;
   public Integer mFrameType;
   public Integer mPower;
   public byte[]  mNamespace;  // 10 bytes
   public byte[]  mBeaconId;   // 6 bytes
}

