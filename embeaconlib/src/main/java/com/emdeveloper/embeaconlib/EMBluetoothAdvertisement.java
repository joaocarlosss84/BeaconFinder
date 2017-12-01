/*
 ** ############################################################################
 **
 ** file    EMBluetoothAdvertisement.java
 ** brief   Represents a bluetooth advertisement
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

package com.emdeveloper.embeaconlib;

import android.bluetooth.BluetoothDevice;
//import android.bluetooth.le.ScanRecord;
import android.database.Cursor;
import android.os.Parcel;
import android.util.SparseArray;
import android.os.Parcelable;

import com.emdeveloper.embeaconlib.database.EMALTManufacturerData;
import com.emdeveloper.embeaconlib.database.EMBeaconManufacturerData;
import com.emdeveloper.embeaconlib.database.EMEDTLMManufacturerData;
import com.emdeveloper.embeaconlib.database.EMEDUIDManufacturerData;
import com.emdeveloper.embeaconlib.database.EMEDURLManufacturerData;
import com.emdeveloper.embeaconlib.database.EMIDManufacturerData;

import java.util.Arrays;

/**
 *  @brief A representation of a bluetooth advertisement
 * 
 * This is the basic unit passed into the database when an advertisement is received
 *
 * It has fields
 * - Rssi 
 * - time
 * - remote bluetooth address
 * - raw data
 * - an array of Advertisement Data Structures
 *
 * It is created in EMBluetoothLeService added to the database(IEMBluetoothDatabase) and also broadcast.
 *
 * The advertisement is parsed into a sparse array of advertisementData structures on demand.
 * This class is unaware of the content of the Advertisement Data Structures
 *
 * Also handles serialization
 * 
 */
public class  EMBluetoothAdvertisement implements IEMBluetoothAdvertisement
{
    private Integer mRssi;
    private Long    mTimeStamp;
    private IEMBluetoothDevice mBluetoothDevice;
    private byte[]  mRawData;
    private String mName;
    private Integer mType;   

    /// array of Advertisement Data structures keyed by Advertisement Data Type see @BluetoothAdvertisementNumbers.
    private SparseArray<byte[]> mAdvertisementData;

    /**
     * @brief create the advertisement from the cursor
     */ 
    public static EMBluetoothAdvertisement create(Cursor advertsCursor) {
        return null;
    }

    /**
     * @brief return the Rssi
     */
    public Integer getRSSI(){
        return mRssi ;
    }
    /**
     * @brief returns time stamp of the advertisement.
     * use the time the EMBluetoothAdvertisement is created if the api < 21
     */
    public Long    getTime(){
        return mTimeStamp ;
    }
    /**
     * @brief returns the EMBluetoothDevice of this advertisement
     */
    public IEMBluetoothDevice getDevice(){
        return mBluetoothDevice ;
    }
    /**
     * @brief returns the raw data of the advertisement
     */
    public byte[] getData(){
        return mRawData ;
    }
    /**
     * @brief returns the device address as a string
     */
    public String getAddress(){
        return mBluetoothDevice.getAddress();
    }

    /**
     * @brief Gets the name from the advertisement packet
     * @return the COMPLETE_LOCAL_NAME for the advertisement
     */
    public String getName(){
       if(mName != null) return mName;
       byte[] nameb = getAdvertisementData(IEMBluetoothAdvertisement.COMPLETE_LOCAL_NAME_TYPE);
       if(nameb == null) return null;
       String name = new String(nameb);
       name = name.trim();
       return name;
    }

/**
 * @brief parses the adertisement
 * @return a sparse array of the data indexed by the AD type
 * 
 */
    @Override
    public SparseArray<byte[]> getAdvertisementData() {
        if(mAdvertisementData == null) {
            parseAdvertisement(mRawData);
        }
        return mAdvertisementData;
    }

/**
 * @brief Parcelable
 */
    @Override
    public int describeContents() {
        return 0;
    }

/**
 * @brief Parcelable interface
 */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mRssi);
        dest.writeLong(mTimeStamp);
        dest.writeParcelable(mBluetoothDevice.getDevice(),0);
        dest.writeByteArray(mRawData);
    }

    /**
 * @brief Parcelable interface
 */
    public static final Parcelable.Creator<EMBluetoothAdvertisement> CREATOR =
            new Parcelable.Creator<EMBluetoothAdvertisement>() {
        public EMBluetoothAdvertisement createFromParcel(Parcel in) {
            EMBluetoothAdvertisement adv = new EMBluetoothAdvertisement();
            adv.mName = in.readString();            
            adv.mRssi = in.readInt();
            adv.mTimeStamp = in.readLong();
            //String address = in.readString();
            BluetoothDevice btd=
            in.readParcelable(null);
            adv.mBluetoothDevice = EMBluetoothDevice.create(btd);
            adv.mRawData = new byte[62];
            in.readByteArray(adv.mRawData);
            return adv ;
        }
        public EMBluetoothAdvertisement[] newArray(int size) {
            return new EMBluetoothAdvertisement[size];
        }
    };
    /**
     * @brief Figure out what the name is
     *  if it is a beacon we recognize and not an EMBeacon then the name is the address
     *  Otherwise return a blank name
     *

     */
   private void MakeName(){
      mName = getName();
      if(mType > EMBEACON_TYPE_ID) {
         mName = getAddress();
      }
      if(mName == null && mBluetoothDevice != null && mBluetoothDevice.getDevice() != null) {
         try {
            mName = mBluetoothDevice.getDevice().getName();
         }catch(Exception e){
            mName = null;
         }
      }
      if(mName == null) {
         mName = "";
      }
   }
    /**
     * @brief Create a new advertisement. 
     * The EMBluetoothDevice is created here.
     *
     * These are the parameters from the onLeScan callback. The time is different on api 21 and above.
     *
     * @param device
     * @param rssi
     * @param time
     * @param data
     */
    static public IEMBluetoothAdvertisement create(IEMBluetoothDevice device, Integer rssi, Long time, byte[] data){
        EMBluetoothAdvertisement adv = new EMBluetoothAdvertisement();
        adv.mBluetoothDevice = device;
        adv.mRssi = rssi;
        adv.mTimeStamp = time;
        adv.mRawData = data;
        adv.mType = getType(adv);
        adv.MakeName();
        return adv ;
    }
    /**
     *  @brief retruns the type of the advertisement. Currently EMBeacon supported
     * 
     */
    public int getType() {
       if (mType == null){
          mType = getType(this);
       }
       return mType;
    }

    public static int getType(EMBluetoothAdvertisement adv) {
        if(EMBeaconManufacturerData.isMyAdvertisement(adv)) {
            return EMBEACON_TYPE_ID;
        }
        if(EMALTManufacturerData.isMyAdvertisement(adv)) {
            return ALTBEACON_TYPE_ID;
        }
        if(EMIDManufacturerData.isMyAdvertisement(adv)) {
            return IDBEACON_TYPE_ID;
        }
        if(EMEDUIDManufacturerData.isMyAdvertisement(adv)) {
            return EDUIDBEACON_TYPE_ID;
        }
        if(EMEDURLManufacturerData.isMyAdvertisement(adv)) {
            return EDURLBEACON_TYPE_ID;
        }
        if(EMEDTLMManufacturerData.isMyAdvertisement(adv)) {
            return EDTLMBEACON_TYPE_ID;
        }
        return -1;
    }

    
   /** @brief parses the advertisement
    *  @param data the advertiseent
    *  
    *  fills out a sparse array of the AD data parts of the advertisement
    */
    private void parseAdvertisement(byte[] data){
        // ad data consists of <len> <type> <len bytes of data>
        mAdvertisementData = new SparseArray<byte[]>();
        int len;
        int offset = 0;
        while(offset < data.length && data[offset] > 0){
            len = data[offset];
            if(len > 0) {
                int type = 0xff & data[offset+1];
                byte[] d = Arrays.copyOfRange(data, offset+2, offset + len + 1);
                mAdvertisementData.put(type,d);
                offset = offset + len + 1;
            }
        }
    }
   /**
    * @brief Access the AD data in the advertisment
    * @param i the type of the AD data
    * @return returns the advertisement data of type i
    */
    @Override
    public byte[] getAdvertisementData(int i){
        return getAdvertisementData().get(i);
    }
   /**
    * @brief creates an EMBeacon advertisement from byte array parameters
    * @param iName
    * @param SensorValue
    * @param EventValue
    * @param ModelId
    * @param Battery
    * @param CompanyCode
    * @param PacketCount
    * 
    */
   public static byte[] makeAdvertisementData(String iName,
                                              byte[] SensorValue,
                                              byte[] EventValue,
                                              byte[] ModelId,
                                              byte[] Battery,
                                              byte[] CompanyCode,
                                              byte[] PacketCount){
      byte[] advertisement = new byte[31];

        /// copy the name
        int index = 0;
        // name must be < 13 bytes
        String Name = (iName.length()> 13 ? iName.substring(0,12) : iName);
        advertisement[index++] = (byte) (Name.length()+2);
        advertisement[index++] = (byte)IEMBluetoothAdvertisement.COMPLETE_LOCAL_NAME_TYPE;
        byte[] nameb = Name.getBytes();
        for (byte n :nameb){
            advertisement[index++] = n;
        }
        advertisement[index++] = (byte)0;
        /// make manufacturer data
        int lenindex = index;
        advertisement[index++] = (byte)0; // replaced later
        advertisement[index++] = (byte) IEMBluetoothAdvertisement.MANUFACTURER_SPECIFIC_TYPE;
        // company code
        advertisement[index++] = CompanyCode[0];
        advertisement[index++] = CompanyCode[1];
        // open sense
        advertisement[index++] = SensorValue[0];
        advertisement[index++] = SensorValue[1];
        // model ID
        //byte[] model = ModelId.getBytes();
        advertisement[index++] = ModelId[0] ;
        advertisement[index++] = ModelId[1];
        // battery FIXME
        advertisement[index++] = Battery[0];
        // count
        advertisement[index++] = PacketCount[0];
        advertisement[index++] = PacketCount[1];
        advertisement[index++] = PacketCount[2];
        advertisement[index++] = PacketCount[3];
        // event
        advertisement[index++] = EventValue[0];
        advertisement[index++] = EventValue[1];
        advertisement[lenindex] =(byte)(index - lenindex-1);
       return advertisement;
   }
    
   /**
    * @brief creates an EMBeacon advertisement from numeric parameters
    * @param iName
    * @param SensorType
    * @param SensorValue
    * @param EventType
    * @param EventValue
    * @param ModelId
    * @param Battery
    * @param CompanyCode
    * @param PacketCount
    * 
    */
   public static byte[] makeAdvertisementData(String iName,
                                                    Integer SensorType, Integer SensorValue,
                                                    Integer EventType, Integer EventValue,
                                                    String ModelId,
                                                    Float Battery,
                                                    Integer CompanyCode,
                                              Integer PacketCount){
    byte[] advertisement = new byte[32];

        /// copy the name
        int index = 0;
        // name must be < 13 bytes
        String Name = (iName.length()> 13 ? iName.substring(0,12) : iName);
        advertisement[index++] = (byte) (Name.length()+2);
        advertisement[index++] = (byte)IEMBluetoothAdvertisement.COMPLETE_LOCAL_NAME_TYPE;
        byte[] nameb = Name.getBytes();
        for (byte n :nameb){
            advertisement[index++] = n;
        }
        advertisement[index++] = (byte)0;
        /// make manufacturer data
        int lenindex = index;
        advertisement[index++] = (byte)0; // replaced later
        advertisement[index++] = (byte) IEMBluetoothAdvertisement.MANUFACTURER_SPECIFIC_TYPE;
        // company code
        advertisement[index++] = (byte)(CompanyCode & 0xff);
        advertisement[index++] = (byte)((CompanyCode >> 8) & 0xff);
        // open sense
        advertisement[index++] = (byte) (((SensorType << 4) & 0xf0) | ((SensorValue >>8 ) & 0xf));
        advertisement[index++] = (byte)(SensorValue & 0xff);
        // model ID
        byte[] model = ModelId.getBytes();
        advertisement[index++] = model[0] ;
        advertisement[index++] = model[1];
        // battery FIXME
        advertisement[index++] = (byte) Battery.intValue();
        // count
        advertisement[index++] = (byte) (PacketCount >> (8*3)  & 0xff);
        advertisement[index++] = (byte) (PacketCount >> (8*2)  & 0xff);
        advertisement[index++] = (byte) (PacketCount >> (8*1)  & 0xff);
        advertisement[index++] = (byte) (PacketCount >> (8*0)  & 0xff);
        // event
        advertisement[index++] = (byte) (((EventType << 4 ) & 0xf0) | ((EventValue >>8 ) & 0xf));
        advertisement[index++] = (byte)(EventValue & 0xff);
        advertisement[lenindex] =(byte)(index - lenindex-1);
       return advertisement;
   }
   /**
    * @brief Debug utility for generating an EMBeacon advertisement
    *
    * @param address
    * @param Rssi
    * @param Time
    * @param Name
    * @param SensorType
    * @param SensorValue
    * @param EventType
    * @param EventValue
    * @param ModelId
    * @param Battery
    * @param CompanyCode
    * @param PacketCount
    */
    public static IEMBluetoothAdvertisement generateAdvertisement(String address,Integer Rssi, Long Time, String Name,
                                                    Integer SensorType, Integer SensorValue,
                                                    Integer EventType, Integer EventValue,
                                                    String ModelId,
                                                    Float Battery,
                                                    Integer CompanyCode,
                                                    Integer PacketCount)   {
        IEMBluetoothAdvertisement result=null;
        byte[] advertisement = makeAdvertisementData(Name,SensorType,SensorValue,EventType,EventValue,ModelId,Battery,CompanyCode,PacketCount);
        IEMBluetoothDevice dev = EMBluetoothDevice.create(address);
        result = EMBluetoothAdvertisement.create(dev,Rssi,Time,advertisement);
        return result;
    }

   /**
    * @brief Debug utility for generating an EMBeacon advertisement
    *
    * @param address
    * @param Rssi
    * @param Time
    * @param Name
    * @param SensorValue
    * @param EventValue
    * @param ModelId
    * @param Battery
    * @param CompanyCode
    * @param PacketCount
    */
    public static IEMBluetoothAdvertisement generateAdvertisement(String address,Integer Rssi, Long Time, String Name,
                                              byte[] SensorValue,
                                              byte[] EventValue,
                                              byte[] ModelId,
                                              byte[] Battery,
                                              byte[] CompanyCode,
                                              byte[]PacketCount){
        IEMBluetoothAdvertisement result=null;
        byte[] advertisement = makeAdvertisementData(Name,SensorValue,EventValue,ModelId,Battery,CompanyCode,PacketCount);
        IEMBluetoothDevice dev = EMBluetoothDevice.create(address);
        result = EMBluetoothAdvertisement.create(dev,Rssi,Time,advertisement);
        return result;
    }
   /**
    * @brief debug utility for dumping an advertisement
    *
    * 
    */
   public String dump(){
      // format
//      # Device rssi time record
//00:00:00:00:00:01  -1 1000 0F09454D426561636F6E3132383737000EFF5A00312330313400000293312
      StringBuilder sb = new StringBuilder();
      sb.append(getAddress());
      sb.append(String.format(" %d ",getRSSI()));
      sb.append(String.format(" %d ",getTime()));
      sb.append(byteArrayToHexString(getData()));
      return sb.toString();
      
   };
   /**
    * @brief converts a string to a byte array
    * @param s input string
    * @return array of bytes
    */

   private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
           data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                  + Character.digit(s.charAt(i+1), 16));
        }
        return data;
   }
   /**
    * @brief converts a byte aray to a string 
    * @param data array of bytes
    * @return Hex String
    */
   private String byteArrayToHexString(byte[] data){
      final StringBuilder sb = new StringBuilder(2 * data.length);
      for(byte b : data) {
         sb.append(String.format("%02x",b));
      }
      return sb.toString();
   }
   
   /**
    * @brief creates an advertisement from a line generated by @ref dump and 
    * @param line input line
    * @return Advertisement
    */
   public static IEMBluetoothAdvertisement undump(String line){
      String device;
      Integer rssi;
      Long time;
      byte[] scanRecord;
      String[] parts = line.split("\\s+");
      if (parts.length == 4) {
         device = parts[0];
         rssi = Integer.parseInt(parts[1], 10);
         time = Long.parseLong(parts[2], 10);
         scanRecord = hexStringToByteArray(parts[3]);
         IEMBluetoothDevice iEMD = EMBluetoothDevice.create(device);
         IEMBluetoothAdvertisement adv = EMBluetoothAdvertisement.create(iEMD, rssi, time, scanRecord);
         return adv;
      }
      return null;
   }
}
