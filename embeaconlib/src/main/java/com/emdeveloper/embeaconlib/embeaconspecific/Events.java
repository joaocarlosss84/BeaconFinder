/*
 ** ############################################################################
 **
 ** file  Events.java
 ** brief   Inteface for displaying EMBeacon Events data
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

package com.emdeveloper.embeaconlib.embeaconspecific;

import android.util.SparseArray;

import com.emdeveloper.embeaconlib.R;

/**
 * @brief deals with displaying the events portion of the EMBeacon Manufacturer data
 *
 * The extenstions of Conversion routine only need to implement the Icon and the Units since they are
 * displayed as integers.
 *
 *
 *
 * Index |variable | icon     | unit
 * --------|----------|--------|-----------
 *  0x0|    ButtonPresses   |ic_push_button        | "Button Presses"
 *  0x1|    LowBattery      |ic_battery_low        | "Low Events"
 *  0x2|    Calibrations    |ic_autocal_vco_cal    | "Calibrations"
 *  0x3|    LowTemperature  |ic_temperature_low    | "Low Events"
 *  0x4|    HighTemperature |ic_temperature_high   |"High Events"
 *  0x5|    LowPressure     |ic_pressure_low       |"Low Events"
 *  0x6|    HighPressure    |ic_pressure_high      |"High Events"
 *  0x7|    HighLight       |ic_lights_off        |"Lights Off"
 *  0x8|    LowLight        |ic_lights_on          | "Lights On"
 *  0x8|    LOWHUMIDITY     |ic_low_humidity        | "Low Events"
 *  0x7|    HIGHHUMIDITY    |ic_high_humididy        |"High Events"
 *  0x9|    CloseMagnet     |ic_magnetic_field_high|"Near Events"
 *  0xa|    FarMagnet       |ic_magnetic_field_low |"Far Events"
 *  0xb|    Movement        |ic_any_movement       |" Moves"
 *  0xc|    Tap             |ic_tap                | "Taps"
 *  0xd|    Fall            |ic_fall               | "Falls"
 *  0xe|    Alarm           |ic_alarm              |"Alarms"
 *  0xf|    Buzzer          |ic_buzzer             |"Buzzer Events"
 *
 * 
 */
public class Events {

    /**
     * @brief Event types
     * @name Event Types
     * @{
     */
    public final static int BUTTONPRESS   = 0x0;
    public final static int LOWBATTERY      = 0x1;
    public final static int CALIBRATIONS    = 0x2;
    public final static int LOWTEMPERATURE  = 0x3;	
    public final static int HIGHTEMPERATURE = 0x4;
    public final static int LOWPRESSURE     = 0x5;
    public final static int HIGHPRESSURE    = 0x6;

    public final static int LOWHUMIDITY     = 0x7;
    public final static int HIGHHUMIDITY    = 0x8;
    public final static int LOWLIGHT        = 0x7;
    public final static int HIGHLIGHT       = 0x8;

    public final static int CLOSEMAGNET     = 0x9;
    public final static int FARMAGNET       = 0xA;
    public final static int MOVEMENT        = 0xB;
    public final static int TAP             = 0xC;
    public final static int FALL            = 0xD;
    public final static int ALARM           = 0xE;
    public final static int BUZZER          = 0xF;
    public final static int GENERIC         = 0x10;   
    /**
     * @}
     */


    /**
     * @brief Event Iconso
     * @name Button Icon names
     * @{
     */
    public static String ic_alarm_clock         = "ic_alarm_clock";
    public static String ic_any_movement        = "ic_any_movement";
    public static String ic_autocal_vco_cal     = "ic_autocal_vco_cal";
    public static String ic_battery_low         = "ic_battery_low";
    public static String ic_buzzer              = "ic_buzzer";
    public static String ic_fall                = "ic_fall";
    public static String ic_humidity_high       = "ic_humidity_high";
    public static String ic_humidity_low        = "ic_humidity_low";
    public static String ic_lights_off          = "ic_lights_off";
    public static String ic_lights_on           = "ic_lights_on";
    public static String ic_magnetic_field_high = "ic_magnetic_field_high";
    public static String ic_magnetic_field_low  = "ic_magnetic_field_low";
    public static String ic_pressure_high       = "ic_pressure_high";
    public static String ic_pressure_low        = "ic_pressure_low";
    public static String ic_push_button         = "ic_push_button";
    public static String ic_tap                 = "ic_tap";
    public static String ic_temperature_high    = "ic_temperature_high";
    public static String ic_temperature_low     = "ic_temperature_low";
   public static String ic_generic              = "ic_generic";
   /**
     * @}
     */
    /**
     * @brief Array of icon resource strings indexed by sensor type
     */
    private static SparseArray<String> iconDb = new SparseArray<String>();
    /**
     * @brief Array of ConversionRoutines indexed by sensor typep
     */
    private static SparseArray<ConversionRoutine> funcDb = new SparseArray<ConversionRoutine>();
    /**
     * @brief adds a conversion routine to the fundDb
     */
    private static void insertFunc(ConversionRoutine routine){
        funcDb.put(routine.index(),routine);
    }
/**
 * @brief initialzes the array of Event converstion routines
 */ 
    static {
        insertFunc(new ButtonPressesRoutine());
        insertFunc(new LowBatteryRoutine());
        insertFunc(new CalibrationsRoutine());
        insertFunc(new LowTemperatureRoutine());
        insertFunc(new HighTemperatureRoutine());
        insertFunc(new LowPressureRoutine());
        insertFunc(new HighPressureRoutine());
//        insertFunc(new LowLightRoutine());
//        insertFunc(new HighLightRoutine());
        insertFunc(new LowHumidityRoutine());
        insertFunc(new HighHumidityRoutine());
        insertFunc(new CloseMagnetRoutine());
        insertFunc(new FarMagnetRoutine());
        insertFunc(new MovementRoutine());
        insertFunc(new TapRoutine());
        insertFunc(new FallRoutine());
        insertFunc(new AlarmRoutine());
        insertFunc(new BuzzerRoutine());
    }


   public static ConversionRoutine getConversion(int type){
      ConversionRoutine s = funcDb.get(type);
      if(s == null) {
         s = funcDb.get(GENERIC);
      }
      return s;
   }
   
     /**
     * @brief returns an icon resource given a type
     */
    public static int getIcon(int type){
        ConversionRoutine s = getConversion(type);
        if(s != null ){
            return s.icon();
        }
        return 0;
    }
   public static Object getRoutine(int type){
        ConversionRoutine s = getConversion(type);
        return s;
    }

    /**
     * @brief returns an value given a type and input
     */
    public static String getValue(int type,int input){
        ConversionRoutine r = getConversion(type);
        if(r != null) {
            return r.value(input);
        }
        return "Unknown";
    }
    /**
     * @brief returns the unit string for a given type
     */
    public static int getUnits(int type){
        ConversionRoutine r = getConversion(type);
        if(r != null) return r.Units();
        return -1;

    }
   /**
    * @brief generates an Event byte array from arguments
    * @param idx Event type
    * @param args the value(s)
    * @return array of two bytes
    */
   public static byte[] generate(int idx,Object... args){
          if(args != null && args[0] instanceof Integer){
             byte[] ret = new byte[2];
             Integer v = (Integer) args[0];
             byte type = (byte)((idx & 0xf) << 4);
             byte low = (byte)(v&0xff);
             byte hi  = (byte)((v>>8) & 0xf);
             ret[0] = (byte)(type | hi);
             ret[1] = low;
             return ret;
          }
          return null;
   }

    /**
     * @brief Handles Button Presses event records
     */
    public static class GenericRoutine extends ConversionRoutine {
        public GenericRoutine() {
           idx= GENERIC;
           icon_name = ic_generic;
           name = "Generic";
        }
        @Override
        public int Units() {
            return (R.string.Events);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }
    }
    /**
     * @brief Handles Button Presses event records
     */
    public static class ButtonPressesRoutine extends ConversionRoutine {
        public ButtonPressesRoutine() {
           idx= BUTTONPRESS;
           icon_name = ic_push_button;
        }
        /**
         * @brief returns the units for button presses
         */
        @Override
        public int Units() {
            return (R.string.ButtonPresses);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }
    }
    /**
     * @brief Handles  LowBatteryRoutine event records
     */
    public static class LowBatteryRoutine extends ConversionRoutine{
        LowBatteryRoutine() {
        idx= LOWBATTERY;
        icon_name = ic_battery_low;
        }

        /**
         * @brief Handles LowBatteryRoutine units
         */
        @Override
        public int Units() {
            return (R.string.LowBattery);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }
    }
    /**
     * @brief Handles Calibration event records
     */
    public static class CalibrationsRoutine extends ConversionRoutine{
        CalibrationsRoutine() {
             idx= CALIBRATIONS;
         icon_name = ic_autocal_vco_cal;
        }

        /**
         * @brief Handles Calibration event records
         */
        @Override
        public int Units() {
            return (R.string.Calibrations);
        }

       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }

    }
    /**
     * @brief Handles Low Temperature event records
     */
    public static class LowTemperatureRoutine extends ConversionRoutine{
        LowTemperatureRoutine() {
        idx= LOWTEMPERATURE;
        icon_name = ic_temperature_low;
        }

        /**
         * @brief Handles Low Temperature event records
         */
        @Override
        public int Units() {
            return (R.string.LowTemperature);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }

    }
    /**
     * @brief Handles High Temperature event records
     */
    public static class HighTemperatureRoutine extends ConversionRoutine{
        HighTemperatureRoutine() {
        idx= HIGHTEMPERATURE;
        icon_name = ic_temperature_high;
        }

        /**
         * @brief Handles High Temperature event records
         */
        @Override
        public int Units() {
            return (R.string.HighTemperature);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }

    }
    /**
     * @brief Handles Low Pressuer event records
     */
    public static class LowPressureRoutine extends ConversionRoutine{
        LowPressureRoutine() {
        idx= LOWPRESSURE;
        icon_name = ic_pressure_low;
        }
        /**
         * @brief Handles Low Pressure event records
         */
        @Override
        public int Units() {
            return (R.string.LowPressure);
        }

       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }
    }
    /**
     * @brief Handles High Pressure event records
     */
    public static class HighPressureRoutine extends ConversionRoutine{
        HighPressureRoutine() {
        idx= HIGHPRESSURE;
        icon_name = ic_pressure_high;
        }
        /**
         * @brief Handles High Pressure event records
         */
        @Override
        public int Units() {
            return (R.string.HighPressure);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }

    }
    /**
     * @brief Handles Low Light event records
     */
    public static class LowLightRoutine extends ConversionRoutine{
        LowLightRoutine() {
        idx= LOWLIGHT;
        icon_name = ic_lights_off;
        }
        /**
         * @brief Handles Low Light event records
         */
        @Override
        public int Units() {
            return (R.string.LowLight);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }

    }
    /**
     * @brief Handles High Light event records
     */
    public static class HighLightRoutine extends ConversionRoutine{
        HighLightRoutine() {
        idx= HIGHLIGHT;
        icon_name = ic_lights_on;
        }
        /**
         * @brief Handles High Light  event records
         */
        @Override
        public int Units() {
            return (R.string.HighLight);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }

    }
    /**
     * @brief Handles Low Light event records
     */
    public static class LowHumidityRoutine extends ConversionRoutine{
        LowHumidityRoutine() {
        idx= LOWHUMIDITY;
        icon_name = ic_humidity_low;
        }
        /**
         * @brief Handles Low Humidity event records
         */
        @Override
        public int Units() {
            return (R.string.LowHumidity);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }

    }
    /**
     * @brief Handles High Humidity event records
     */
    public static class HighHumidityRoutine extends ConversionRoutine{
        HighHumidityRoutine() {
        idx= HIGHHUMIDITY;
        icon_name = ic_humidity_high;
        }
        /**
         * @brief Handles High Humidity  event records
         */
        @Override
        public int Units() {
            return (R.string.HighHumidity);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }

    }
    /**
     * @brief Handles Clowe Magnet event records
     */
    public static class CloseMagnetRoutine extends ConversionRoutine{
        CloseMagnetRoutine() {
        idx= CLOSEMAGNET;
        icon_name = ic_magnetic_field_high;
        }
        /**
         * @brief Handles Close Magnet event records
         */
        @Override
        public int Units() {
            return (R.string.CloseMagnet);
        }

       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }
    }
    /**
     * @brief Handles Far Magnet event records
     */
    public static class FarMagnetRoutine extends ConversionRoutine{
        FarMagnetRoutine() {
        idx= FARMAGNET;
        icon_name = ic_magnetic_field_low;
        }
        /**
         * @brief Handles Far Magnet event records
         */
        @Override
        public int Units() {
            return (R.string.FarMagnet);
        }

       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }
    }
    /**
     * @brief Handles Movement event records
     */
    public static class MovementRoutine extends ConversionRoutine{
        MovementRoutine() {
        idx= MOVEMENT;
        icon_name = ic_any_movement;
        }
        /**
         * @brief Handles Movement event records
         */
        @Override
        public int Units() {
            return (R.string.Movement);
        }

       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }
    }
    /**
     * @brief Handles Tap event records
     */
    public static class TapRoutine extends ConversionRoutine{
        TapRoutine() {
        idx= TAP;
        icon_name = ic_tap;
        }
        /**
         * @brief Handles Tap event records
         */
        @Override
        public int Units() {
            return (R.string.Tap);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }
    }
    /**
     * @brief Handles Fall event records
     */
    public static class FallRoutine extends ConversionRoutine{
        FallRoutine() {
        idx= FALL;
        icon_name = ic_fall;
        }
        /**
         * @brief Handles Fall  event records
         */
        @Override
        public int Units() {
            return (R.string.Fall);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }

    }
    /**
     * @brief Handles Alarm event records
     */
    public static class AlarmRoutine extends ConversionRoutine{
        AlarmRoutine() {
        idx= ALARM;
        icon_name = ic_alarm_clock;
        }
        /**
         * @brief Handles Alarm event records
         */
        @Override
        public int Units() {
            return (R.string.Alarm);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }

    }
    /**
     * @brief Handles Buzzer event records
     */
    public static class BuzzerRoutine extends ConversionRoutine{
        BuzzerRoutine() {
        idx= BUZZER;
        icon_name = ic_buzzer;
        }
        /**
         * @brief Handles Buzzer event units
         */
        @Override
        public int Units() {
            return (R.string.Buzzer);
        }
       public byte[] generate(Object... args){
          return Events.generate(idx,args);
       }

    }



}
