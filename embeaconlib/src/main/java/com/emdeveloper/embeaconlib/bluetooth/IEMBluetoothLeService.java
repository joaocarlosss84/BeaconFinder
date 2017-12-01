/*
 ** ############################################################################
 **
 ** file    IEMBluetoothLeService.java
 ** brief   interface definition for EMBluetoothLeService
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
package com.emdeveloper.embeaconlib.bluetooth;

import android.content.Context;
import android.bluetooth.BluetoothDevice;

import com.emdeveloper.embeaconlib.database.EMBluetoothDatabase;
import com.emdeveloper.embeaconlib.database.IEMBluetoothDatabase;

import java.util.UUID;
/**
 * @brief This class is the interface to the Android bluetooth system
 *
 * It communicates with the application via
 *   - service binding
 *   - enable()
 *   - disable()
 *   - startLeScan
 *   - stopLeScan
 *
 *   it broadcasts the ACTION_BLUETOOTH_* messages
 *   it creates EMBluetoothAdvertisement instances and adds them to an IEMBluetoothDatabase.
 *
 *   Notifications
 *   - Initialize failed
 *   - Adapter failed
 *   - reset bluetooth
 *   - bluetooth off
 *   - Advertising data available
 *
 *   Advertising data available data
 *   - address
 *   - byte data
 *   - time
 *
 */
public interface IEMBluetoothLeService {
   /** @brief The callback interface used when an advertisement is received.
    */
   public static interface IEMAddAdvertisement  {
        public void addAdvertisement(BluetoothDevice device, int rssi, long time, byte[] scanRecord);
    }

    /**
     * Base package name for broadcast and intent parameters
     */
    public final static String PKG = "com.emdeveloper.embeaconlib";

    /* @defgroup EMBluetoothLeServicebroadcasts Broadcast messages
     * @{
     */
    /**
     * Broadcast Action: unable to start bluetooth
     */
    public final static String ACTION_BLUETOOTH_INITIALIZE_FAIL = PKG+".ACTION_BLUETOOTH_INITIALIZE_FAIL";
    /**
     * Broadcast Action: unable to start bluetooth
     */
    public final static String ACTION_BLUETOOTH_ADAPTER_FAIL = PKG+".ACTION_BLUETOOTH_ADAPTER_FAIL";        
    /**
     * Broadcast Action: bluetooth has been reset
     */
    public final static String ACTION_RESET_BLUETOOTH = PKG+".ACTION_RESET_BLUETOOTH";
    /**
     * Broadcast Action: bluetooth has been turned on
     */
    public final static String ACTION_BLUETOOTH_ON = PKG+".ACTION_BLUETOOTH_ON";    
    /**
     * Broadcast Action: An advertisement has been recieved
     */
    public final static String ACTION_ADVERTISING_DATA = PKG+".ACTION_ADVERTISING_DATA";
    /**@}*/

    /**
     * Used as an extra field for the ACTION_ADVERTISING_DATA
     */
    public final static String EXTRA_DATA = PKG+".EXTRA_DATA";
    /**
     * Used as an extra string field to store the device address
     */
    public final static String EXTRA_ADDRESS = PKG+".EXTRA_ADDRESS";
    /**
     * Used as an extra string field to store the AdvertisingData
     */
    public final static String EXTRA_BYTEDATA =  PKG+".EXTRA_BYTEDATA";
    /**
     * Used as an extra string field to store the time of an advertisement
     */
    public final static String EXTRA_TIME =  PKG+".EXTRA_TIME";
    /**
     * Used as an extra string field to store the RSSI of the advertisement
     */
    public final static String EXTRA_RSSI =  PKG+".EXTRA_RSSI";
    /**
     * Used as an extra string field to store the time of an advertisement
     */
    public final static String EXTRA_DEVICE =  PKG+".EXTRA_DEVICE";
    /**
     * Used as an extra string field to store the name part of an advertisement
     */
    public final static String EXTRA_NAME =  PKG+".EXTRA_NAME";


    /**
     * @brief holds the state of the bluetooth service
     */
    public enum ServiceState
    {
        UNBOUND,         //!< The service hasn't been bound
        BOUND,           //!< The service hasn't been bound
        ENABLED,         //!< bluetooth is enabled
        DISABLED,        //!< bluetooth is disabled
        SCANNING,        //!< we are scanning
        NOTSCANNING,     //!< we are not scanning
        // later with the CONNECTION stuff;
    }

    /**
     * @brief Initializes a reference to the local Bluetooth adapter.
     * @return Return true if the initialization is successful.
     * @param mDb database to add advertisements to
     */
    public boolean initialize(IEMAddAdvertisement mDb) ;
    /**
     * @brief Starts a scan
     *
     */
    public void startLeScan();
    /**
     * @brief Stops a scan
     *
     */
    public void stopLeScan();

    /**
     * @brief sets the uuids for scanning
     *
     */
    public void setScanUUIDS(UUID[] uuids);
    /**
     * @brief returns the uuids for scanning
     *
     */
    public UUID[] getScanUUIDS();

    /**
     * @brief returns the state of the service
     *
     * @return state value
     */
    public ServiceState getState();
    /**
     * @brief enables the bluetooth interface
     *
     */
    public void enable();

    /**
     * @brief disables the bluetooth interface
     *
     */
    public void disable();
    
    /**
     * @brief resets the bluetooth interface
     * @param c the context to deal with
     */
    public void ResetBluetooth(Context c);
    /**
     * @brief returns true if scanning is enabled
     */
    public boolean getScanning();

}
