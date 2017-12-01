/*
 ** ############################################################################
 **
 ** file    EMBluetoothDatabase.java
 ** brief   Handles the database
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
/**
 * 
 */
package com.emdeveloper.embeaconlib.database;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.database.ContentObservable;
import android.database.Cursor;


import com.emdeveloper.embeaconlib.EMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.EMBluetoothDevice;
import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.IEMBluetoothDevice;
import com.emdeveloper.embeaconlib.bluetooth.IEMBluetoothLeService;

/**
 * 
 * Holds data for a bluetooth device.
 * 
 * The database is updated by the BluetoothLeService via
 *   addDevice
 * The database notifies its ContentObservers when data is changed.
 *
 * Classes 
 *
 *   
 *   @ref EMBluetoothDatabase EMBluetoothDatabase
 */
public class EMBluetoothDatabase extends ContentObservable implements Observer, IEMBluetoothDatabase, IEMBluetoothLeService.IEMAddAdvertisement
{

   boolean mLoggingEnabled = false;

    /**
     * @brief a list of devices indexed by address
     */
    HashMap<String,EMBluetoothDevice> mDevices;

    Context mContext;
    EMSQL mSql;
    private static EMBluetoothDatabase  theEMBluetoothDatabase;
    
    private EMBluetoothDatabase() {
    }
    
    
    /**@brief called when the Observer is updated
     */
    @Override
	public void update(Observable observable, Object data) {
       // ignored
	}
    /**
     * @brief creates "The" database
     * 
     */
    //@Override
    public static IEMBluetoothDatabase create(Context c, EMSQL Sqlf) {
        if(theEMBluetoothDatabase == null) {
            theEMBluetoothDatabase = new EMBluetoothDatabase();
            theEMBluetoothDatabase.mDevices = new HashMap<String,EMBluetoothDevice>();
            theEMBluetoothDatabase.mContext = c;
            theEMBluetoothDatabase.mSql = Sqlf;
        }
        return theEMBluetoothDatabase;
    }
   /**
    * @brief return the database
    */

   public static IEMBluetoothDatabase  getIEMBluetoothDatabase(){
      return theEMBluetoothDatabase;
   }
    /**
     * @brief removes all devices and advertisements
     */
    @Override
    public void clear() {
        mSql.Clear();
    }
    /**
     * @brief gets or creates a new EMBluetoothDevice given a BluetoothDevice
     *
     * the devices are stored by MAC address
     * 
     */
    @Override
    public IEMBluetoothDevice getOrCreateDevice(BluetoothDevice dev)
    {
       if(mDevices.containsKey(dev.getAddress()))
       {
       //    return mDevices.get(dev.getAddress());
       }
       return EMBluetoothDevice.create(dev);
    }

    @Override
    public void initialize() {

    }

    @Override
    public boolean initialized() {
        return false;
    }
    /** 
     * @brief adds an advertisement to the database. This is the implementation of the IEMBluetoothLeService.IEMAddAdvertisement
     */
    @Override
    public void addAdvertisement(BluetoothDevice device, int rssi, long time, byte[] scanRecord){
        IEMBluetoothDevice iEMD = EMBluetoothDevice.create(device);
        IEMBluetoothAdvertisement adv = EMBluetoothAdvertisement.create(iEMD, rssi, time, scanRecord);
        addAdvertisement(adv);
    };

    /**  
     * @brief adds an advertisement to the database
     */
    public void addAdvertisement(IEMBluetoothAdvertisement adv){
        mSql.addAdvertisement(adv);
    }

    /**
     *  @brief returns the advertisements for the device
     */
    public List<IEMBluetoothAdvertisement> getAdvertisementForDevice(IEMBluetoothDevice dev){
        if(dev == null) return null;
        return getAdvertisementForDevice(dev.getAddress());
    }
    /**
     *  @brief returns the advertisements for the device
     */
    public List<IEMBluetoothAdvertisement> getAdvertisementForDevice(String address){
        if(address == null) return null;
        Cursor advertsCursor = mContext.getContentResolver().query(EMContentProvider.Constants.ADVERTISEMENTS_TABLE_CONTENT_URI,
                                                                   null, // projection = all columns
                                                                   EMBeaconAdvertisement.ADDRESS_COLUMN + " =?", // selection
                                                                   new String[]{address}, // selection args
                                                                   EMBeaconAdvertisement.TIME_COLUMN,
                                                                   null
                                                                   );
        if(advertsCursor.getCount() == 0) return null;
        List<IEMBluetoothAdvertisement> aList= new LinkedList<IEMBluetoothAdvertisement>();
        advertsCursor.moveToFirst();
        while(!advertsCursor.isAfterLast()) {
            IEMBluetoothAdvertisement e = EMBluetoothAdvertisement.create(advertsCursor);
            aList.add(e);
            advertsCursor.moveToNext();
        }
        return aList;
    }

    /**
     *  @brief return the advertisements for a given time range
     */
    public List<IEMBluetoothAdvertisement> getAdvertisementForDevice(IEMBluetoothDevice dev,Long fromTime, Long toTime){
        return null;
    }

    /**
     *  @brief return the advertisements for a given time range
     */
    public List<IEMBluetoothAdvertisement> getAdvertisementForDevice(IAdvertisementFilter filter){
        return null;
    }

   public void enableLog(boolean on){
      if(on) {
         CSVLogger.setupFile(mContext);
      }
      mLoggingEnabled = on;
   }
   public boolean isLoggingEnabled(){
      return mLoggingEnabled;
   }
}
