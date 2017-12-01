/*
 ** ############################################################################
 **
 ** file    EMBeaconAdvertisement
 ** brief   process the embeacon advertisement
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


import android.database.Cursor;
import android.content.ContentValues;
import android.net.Uri;

import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;

/**
 *  @brief a class for dealing with a bluetooth advertisement.
 *
 * It defines the Advertisements table
 *
 */
public class EMBeaconAdvertisement{
/*
 * @defgroup EMBeaconAdvertisementColumnNames Column Names
 * @brief Columns names for a raw advertisement
 * @name EMBeaconAdvertisementColumnNames Column Names
 * @{
 */
    public static final String ADDRESS_COLUMN = "ADDRESS";
    public static final String TIME_COLUMN = "TIME";
    public static final String RSSI_COLUMN = "RSSI";
    public static final String DATA_COLUMN = "DATA";
    public static final String NAME_COLUMN = "NAME";    
    public static final String TYPE_COLUMN = "TYPE";
    /** @} */
    /**
     * @brief table name for the advertisements
     */
    public static final String Tablename = "Advertisements";
    /**
     * @brief The uri for the advertisement table
     */
   static public Uri TABLE_CONTENT_URI() {return EMContentProvider.Constants.ADVERTISEMENTS_TABLE_CONTENT_URI;}
/**
 * @brief columns in the advertisement table
 * @{
 */
    public static final String[] Columns = new String[] {
        "_id",
        ADDRESS_COLUMN,
        TIME_COLUMN,
        RSSI_COLUMN,
        DATA_COLUMN,
        NAME_COLUMN,
        TYPE_COLUMN
    };
    /** @} */
    /**
     * @brief column types in the advertisement table
     */
    public static final String[] ColumnTypes = new String[]{
        "INTEGER PRIMARY KEY",
        "TEXT",
        "TIME",
        "INTEGER",
        "BLOB",
        "TEXT",
        "INTEGER" // could be the table name?
    };
    /** @} */
/**
 * @brief gets the advertisement table name
 * @return the table name
 */
    public static String getTableName() {return Tablename;}
/**
 * @brief gets the columns in the advertisement table
 * @return an array of table columns
 */
    public static String[] getTableColumns() {return Columns;}
/**
 * @brief gets the column types in the advertisement table
 * @return an array of table columns types
 */
    public static String[] getTableColumnTypes() {return ColumnTypes;}    
    
    private IEMBluetoothAdvertisement mAdvertisementData;
/**
 * @brief gets the advertisement data of the given type
 * @param adv_type type of the data to get see @BluetoothAdvertisementNumbers.
 * @return raw data
 */
    public byte[] getAdvertisementData(Integer adv_type){
        return mAdvertisementData.getAdvertisementData(adv_type);
    }
/**
 * @brief creates a beacon advertisement from a bluetooth advertisement
 */
    public EMBeaconAdvertisement(IEMBluetoothAdvertisement ema){
        mAdvertisementData = ema;
    }

    String mAddress;
    Long mTime;
    Integer mRSSI;
    byte[] mData;
    String mName;
    Integer mType;
    Long mId;
        
/**
 * @brief creates a beacon advertisement from a cursor
 */
    public static EMBeaconAdvertisement createAdvertisementFromCursor(Cursor c){
        return new EMBeaconAdvertisement(c);
    }
    
/**
 * @brief creates a beacon advertisement from a cursor
 */
    public EMBeaconAdvertisement(Cursor c){
        int addressIndex = c.getColumnIndex(ADDRESS_COLUMN);
        int dataIndex = c.getColumnIndex(DATA_COLUMN);
        int idIndex = c.getColumnIndex("_id");
        int nameIndex = c.getColumnIndex(NAME_COLUMN);
        int rssiIndex = c.getColumnIndex(RSSI_COLUMN);        
        int timeIndex = c.getColumnIndex(TIME_COLUMN);
        int typeIndex = c.getColumnIndex(TYPE_COLUMN);
        // all these should be > 0
        if(addressIndex > 0) mAddress = c.getString(addressIndex);
        if(dataIndex > 0) mData = c.getBlob(dataIndex);
        if(idIndex > 0) mId = c.getLong(idIndex);
        if(nameIndex > 0) mName = c.getString(nameIndex);
        if(rssiIndex > 0) mRSSI = c.getInt(rssiIndex);
        if(timeIndex > 0) mTime = c.getLong(timeIndex);
        if(typeIndex > 0) mType = c.getInt(typeIndex);
    }

/**
 * @brief returns the integer between 0 and 255 from a byte
 */
    public static Integer MyInteger(byte d){
        return 0xff & d;
    }
/**
 * @brief returns an integer between 0 and 2^16 from two bytes
 *  d0 is msb
 *  d1 is lsb
 */
    public static Integer MyInteger(byte d0, byte d1){
        Integer v = MyInteger(d0);
        v = (v << 8) + MyInteger(d1);
        return v;
    }
/**
 * @brief return an integer between 0 and 2^32 from 4 bytes
 *  d0 is big, d3 is little
 */
    public static Integer MyInteger(byte d0, byte d1, byte d2, byte d3){
        Integer v = MyInteger(d0);
        v = (v << 8) + MyInteger(d1);
        v = (v << 8) + MyInteger(d2);
        v = (v << 8) + MyInteger(d3);
        return v;
    }
/**
 * @brief returns a string of hex digits from a byte array
 */
    public static String byteToHex(byte[] data, int start, int len){
        if(data == null) return "";
        final StringBuilder stringBuilder = new StringBuilder(len*2);
        int i;
        for(i = start ;i < start+len; i++){
            stringBuilder.append(String.format("%02X", data[i]));
        }
        return stringBuilder.toString();
    }
    /**
     * @brief A single piece advertisement data consisting of a type and data
     */
    public static class EMAdvertisementData {
        public EMAdvertisementData(int type, byte[] data){
            mType = type;
            mData = data;
        }
        public Integer mType;
        public byte[] mData;
    }

/**
 * @brief returns the ContetValues from an advertisement for use with the SQL database
 * 
 */
    public static ContentValues getContentValues(IEMBluetoothAdvertisement adv){
        ContentValues v = new ContentValues();
        v.put(DATA_COLUMN, adv.getData());
        v.put(ADDRESS_COLUMN, adv.getAddress());
        v.put(TIME_COLUMN, adv.getTime());
        v.put(RSSI_COLUMN, adv.getRSSI());
        v.put(TYPE_COLUMN, adv.getType());
        v.put(NAME_COLUMN, adv.getName());
        return v;
    }
}
