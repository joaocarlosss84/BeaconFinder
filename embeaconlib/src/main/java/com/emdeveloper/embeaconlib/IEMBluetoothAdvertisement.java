/*
 ** ############################################################################
 **
 ** file    IEMBluetoothAdvertisement.java
 ** brief   interface definition for EMBluetoothAdvertisement
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
 * Copyright (c) 2013-2015 EM Microelectronic-Marin SA. All rights reserved.
 * Developed by Glacier River Design, LLC
 */

package com.emdeveloper.embeaconlib;

import android.os.Parcelable;
import android.util.SparseArray;

/**
 * @brief An interface to a  bluetooth advertisement
 * This is the basic unit passed into the database when an advertisement is received
 *
 * It has methods
 * - getRSSI()
 * - getTime()
 * - getAddress()
 * - getData()
 * - getDevice()
 * - getAdvertisementData()
 * - getAdvertisementData(i)
 * - getType()
 *
 * It is created in EMBluetoothLeService and broadcast.
 *
 * The advertisement is parsed into a sparse array of byte[] for each advertisement Data element in the advertisement
 * This class is unaware of the content of the individual advertisement Data elements
 * 
 */
@SuppressWarnings("UnusedDeclaration")
public interface IEMBluetoothAdvertisement extends Parcelable
{/* from external/bluetooth/bluedroid/stack/include/bt_types.h
#define BT_EIR_FLAGS_TYPE                   0x01
#define BT_EIR_MORE_16BITS_UUID_TYPE        0x02
#define BT_EIR_COMPLETE_16BITS_UUID_TYPE    0x03
#define BT_EIR_MORE_32BITS_UUID_TYPE        0x04
#define BT_EIR_COMPLETE_32BITS_UUID_TYPE    0x05
#define BT_EIR_MORE_128BITS_UUID_TYPE       0x06
#define BT_EIR_COMPLETE_128BITS_UUID_TYPE   0x07
#define BT_EIR_SHORTENED_LOCAL_NAME_TYPE    0x08
#define BT_EIR_COMPLETE_LOCAL_NAME_TYPE     0x09
#define BT_EIR_TX_POWER_LEVEL_TYPE          0x0A
#define BT_EIR_OOB_BD_ADDR_TYPE             0x0C
#define BT_EIR_OOB_COD_TYPE                 0x0D
#define BT_EIR_OOB_SSP_HASH_C_TYPE          0x0E
#define BT_EIR_OOB_SSP_RAND_R_TYPE          0x0F
#define BT_EIR_MANUFACTURER_SPECIFIC_TYPE   0xFF
 */
    /**
     * @brief the values AD Type field for the Advertising data structure
     *
     * Bluetooth 4.1 Volume 3, Part C section 1
     * https://www.bluetooth.org/en-us/specification/assigned-numbers/generic-access-profile
     *
     * the beacons use FLAGS_TYPE,COMPLETE_LOCAL_NAME_TYPE,MANUFACTURER_SPECIFIC_TYPE
     * @name AD Advertising data type
     * @{
     */
    public static final int FLAGS_TYPE                   = 0x01;
    public static final int MORE_16BITS_UUID_TYPE        = 0x02;
    public static final int COMPLETE_16BITS_UUID_TYPE    = 0x03;
    public static final int MORE_32BITS_UUID_TYPE        = 0x04;
    public static final int COMPLETE_32BITS_UUID_TYPE    = 0x05;
    public static final int MORE_128BITS_UUID_TYPE       = 0x06;
    public static final int COMPLETE_128BITS_UUID_TYPE   = 0x07;
    public static final int SHORTENED_LOCAL_NAME_TYPE    = 0x08;
    public static final int COMPLETE_LOCAL_NAME_TYPE     = 0x09;
    public static final int TX_POWER_LEVEL_TYPE          = 0x0A;
    public static final int OOB_BD_ADDR_TYPE             = 0x0C;
    public static final int OOB_COD_TYPE                 = 0x0D;
    public static final int OOB_SSP_HASH_C_TYPE          = 0x0E;
    public static final int OOB_SSP_RAND_R_TYPE          = 0x0F;
    public static final int SERVICE_DATA_16BITS_UUID     = 0x16;
   
    public static final int MANUFACTURER_SPECIFIC_TYPE   = 0xFF;
    /**
     * @}
     */


    /**
     * @brief Values returned by getType()
     * @name the type of the advertisement
     *  EMBeacon, AltBeacon or IDBeacon or UnKnown
     * @{
     */
    public static final int UNKNOWN_TYPE_ID = -1;
    public static final int EMBEACON_TYPE_ID = 0;
    public static final int ALTBEACON_TYPE_ID = 1;    
    public static final int IDBEACON_TYPE_ID = 2;
    public static final int EDURLBEACON_TYPE_ID = 3;       
    public static final int EDUIDBEACON_TYPE_ID = 4;       
    public static final int EDTLMBEACON_TYPE_ID = 5;       
    /**
     * @}
     */

    /**
     * @return Rssi of the advertisement
     */
    public Integer getRSSI();
    /**
     * time stamp of the advertisement.
     * use the time the EMBluetoothAdvertisement is created if the api < 21
     * @return advertisement time
     */
    public Long    getTime();

    /**
     * Returns the EMBluetoothDevice of this advertisement
     */
    public IEMBluetoothDevice getDevice();

    /**
     * @return the raw data of the advertisement
     */
    public byte[] getData();

    /**
     * @return the address of the device advertising
     */
    public String getAddress();

    /**
     * @return the Name 
     */
    public String getName();

    /**
     * Advertisement data keyed by Advertisement Data Type (AD_Type)
     * @return array of advertisement Data bytes
     */
    public SparseArray<byte[]> getAdvertisementData();

    /**
     * @return the advertisement data of type AD_Type
     */
    public byte[] getAdvertisementData(int AD_Type);


    /**
     * @return the advertisement type
     */
    public int getType();

    public String dump();

}
