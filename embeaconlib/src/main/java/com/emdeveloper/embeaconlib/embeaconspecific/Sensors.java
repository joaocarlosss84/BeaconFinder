/*
 ** ############################################################################
 **
 ** file    Sensors.java
 ** brief   Inteface for displaying EMBeacon sensor data
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

package com.emdeveloper.embeaconlib.embeaconspecific;

import android.util.SparseArray;

import com.emdeveloper.embeaconlib.R;

/**
 * @brief This class handles the sensor field in the advertisement.
 *
 * it has a ms nibble which contains the type and 12 bits of data
 * to be interpreted in various ways.
 * 
 */
public class Sensors {
    /**
     * @brief Constants for the Sensor Type
     * @name Constants for the Sensor Type
     * @{
     */
    public static final int TYPE_LIGHT       = 0x0;
    public static final int TYPE_FWREV       = 0x1;
    public static final int TYPE_AUTOCAL     = 0x2;
    public static final int TYPE_GENERIC     = 0x3;
    public static final int TYPE_TEMP        = 0x4;
    public static final int TYPE_PRES        = 0x5;
    public static final int TYPE_HUMID       = 0x6;
    public static final int TYPE_TIME        = 0x7;
    public static final int TYPE_DATE        = 0x8;
    public static final int TYPE_MAGNET      = 0x9;
    public static final int TYPE_DAY         = 0xA;
    public static final int TYPE_ACCEL       = 0xB;
    public static final int TYPE_GYRO        = 0xC;
    public static final int TYPE_SPARED      = 0xD;
    public static final int TYPE_SPAREE      = 0xE;
    public static final int TYPE_SPAREF      = 0xF;    

    public static final int TYPE_RSSI        = 0x10;
    public static final int TYPE_BATTERY     = 0x11;
    public static final int TYPE_PACKETS     = 0x12;
    public static final int TYPE_MODEL     = 0x13;
    /**
     * @}
     */
    
    /**
     * @brief Icon resource names
     * @name Icon resource names
     * @{
     */
    public static String ic_apple = "ic_apple";
    public static String ic_autocal = "ic_autocal";
    public static String ic_date = "ic_date";
    public static String ic_day = "ic_day";
    public static String ic_dew = "ic_dew";
    public static String ic_firmware = "ic_firmware";
    public static String ic_generic = "ic_generic";
    public static String ic_gyro = "ic_gyro";
    public static String ic_lightbulb = "ic_lightbulb";
    public static String ic_magnetic_field = "ic_magnetic_field";
    public static String ic_pressure = "ic_pressure";
    public static String ic_thermo_outline = "ic_thermo_outline";
    public static String ic_time = "ic_time";
    public static String ic_rssi = "ic_signal_stack";
    public static String ic_battery = "ic_battery";
    public static String ic_packets = "ic_flip_clock";
    public static String ic_model_number = "ic_model_number";
    /**
     * @}
     */
    /**
     * @brief use a different set of units
     */
    public static boolean unitChange = false;

    /**
     * @brief Array of icon resource strings indexed by sensor type
     */
    private static SparseArray<String> iconDb = new SparseArray<String>();
    /**
     * @brief Array of ConversionRoutines indexed by sensor typep
     */
    private static SparseArray<ConversionRoutine> funcDb = new SparseArray<ConversionRoutine>();
   /// @brief adds a converson routing to the funcDb
    private static void insertFun(ConversionRoutine routine){
        funcDb.put(routine.index(),routine);
    }
    
    /**
     * @brief static initialization
     */
    static {
        iconDb.put(TYPE_RSSI,ic_rssi);
        iconDb.put(TYPE_BATTERY,ic_battery);          

        insertFun(new LuxRoutine());
        insertFun(new FWRevisionRoutine());
        insertFun(new AutoCalRoutine());
        insertFun(new GenericRoutine());
        insertFun(new TemperatureRoutine());
        insertFun(new PressureRoutine());
        insertFun(new HumidityRoutine());
        insertFun(new TimeRoutine());
        insertFun(new DateRoutine());   
        insertFun(new MagnetRoutine());
        insertFun(new DayRoutine());    
        insertFun(new AccelerationRoutine());  
        insertFun(new GyroRoutine());

        insertFun(new BatteryRoutine());
        insertFun(new PacketsRoutine());
        insertFun(new ModelRoutine());
        insertFun(new RSSIRoutine());

    }
   public static ConversionRoutine getConversion(int type){
      ConversionRoutine s = funcDb.get(type);
      if(s == null) {
         s =funcDb.get(TYPE_GENERIC);
      }
      return s;
   }
     /**
     * @brief returns an icon resource given a type
     */
    public static int getIcon(int type){
       ConversionRoutine s = getConversion(type);
       return s.icon();

    }
   /// @brief returns the conversion routine of a given type
   public static Object getRoutine(int type){
        ConversionRoutine s = getConversion(type);
        return s;
    }

     /**
     * @brief returns an value given a type and input
     */
    public static String getValue(int type,Number input){
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
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class LuxRoutine extends ConversionRoutine {
        LuxRoutine(){
            idx= TYPE_LIGHT;
            icon_name = ic_lightbulb;
            name = "Light";
        }
        public String value(Number i ) {
            // 12 bit unsigned 0 - 4095
            return String.format("%4d",i.intValue()&0xfff);
        }
        public int Units(){
            return R.string.Lux;
        }
       public byte[] generate(Object... args){
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

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class FWRevisionRoutine extends ConversionRoutine {

        public FWRevisionRoutine() {
            idx= TYPE_FWREV;
            icon_name = ic_firmware;
            name = "Rev";
        }
        public int icon(){
            return super.icon();
        }
        public String value(Number i ) {
            //mmmm.nnnn.llll
            int lsb = i.intValue() & 0xf;
            int nsb = (i.intValue() & 0xf0) >> 4;
            int msb = (i.intValue() & 0xf00) >> 8;
            return String.format("%d.%d.%d",msb,nsb,lsb);
        }
        public int Units(){
            return R.string.Blank;
        }
       public byte[] generate(Object... args){
          if(args != null && args.length >= 3 &&
             args[0] instanceof Integer &&
             args[1] instanceof Integer &&
             args[2] instanceof Integer ){
             Integer msb = (Integer)args[0];
             Integer nsb = (Integer)args[1];
             Integer lsb = (Integer)args[2];                          
             
             byte[] ret = new byte[2];
             byte type = (byte)((idx & 0xf) << 4);
             byte low = (byte)((0xf0 & (nsb<< 4)) | (0xf&(lsb)));;
             byte hi  = (byte)(msb & 0xf);
             ret[0] = (byte)(type | hi);
             ret[1] = low;
             return ret;
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class AutoCalRoutine extends ConversionRoutine {
        public AutoCalRoutine() {
        idx= TYPE_AUTOCAL;        
        icon_name = ic_autocal;
        name = "Autocal Results";
        }
        public String value(Number i ) {
            //mmmm/llllllll
            int msb = (0xf & ((i.intValue() & 0xf00) >> 8));
            int lsb = i.intValue() & 0xff;
            return String.format("%d/%d",msb,lsb);
        }
        public int Units(){
            return R.string.VCOMOD;
        }
       public byte[] generate(Object... args){
          if(args != null && args.length >= 2 &&
             args[0] instanceof Integer &&
             args[1] instanceof Integer ){
              byte[] ret = new byte[2];
             Integer msb = (Integer)args[0];
             Integer lsb = (Integer)args[1];
             byte type = (byte)((idx & 0xf) << 4);
             byte low = (byte)(lsb & 0xff);
             byte hi  = (byte)(msb & 0xf);
             ret[0] = (byte)(type | hi);
             ret[1] = low;
             return ret;
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class GenericRoutine extends ConversionRoutine {
        public GenericRoutine() {
        idx= TYPE_GENERIC;        
        icon_name = ic_generic;
        name = "Generic";
        }
        public String value(Number i ) {
            // 12 bit unsigned
            return String.format("%d",i.intValue()&0xfff);
        }
        public int Units(){
            return R.string.Blank;
        }
       public byte[] generate(Object... args){
          if(args != null && args[0] instanceof Integer){
             Integer v = (Integer)args[0];
             byte[] ret = new byte[2];
             byte type = (byte)((idx & 0xf) << 4);
             byte low = (byte)(v & 0xff);
             byte hi  = (byte)((v>>8)&0xf0);
             ret[0] = (byte)(type | hi);
             ret[1] = low;
             return ret;
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class TemperatureRoutine extends ConversionRoutine {
        public TemperatureRoutine() {
        idx= TYPE_TEMP;        
        icon_name = ic_thermo_outline;
        name = "Temperature";
        }
        public String value(Number i ) {
            // 12 bit fixed point 4 fractional bits 2's complement
           float value =  i.floatValue();
            // 4 fractional bits
            value /= 16.0f;
            if (alternateunits) {
                value = 32.0f + (value * 9.0f) / 5.0f;
            }
            return String.format("%6.2f", value);
        }
         public int Units(){
            if(alternateunits) {
                return R.string.DegreesF;
            }
            return R.string.DegreesC;
        }
       public byte[] generate(Object... args){
          if(args != null && args[0] instanceof Float){
             byte[] r = fixed12_4((Float)args[0]);
             r[0] = (byte) (r[0] | (idx << 4));
             return r;
                
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class PressureRoutine extends ConversionRoutine {
        public PressureRoutine() {
        idx= TYPE_PRES;        
        icon_name = ic_pressure;
        name = "Pressure";
        }
        public String value(Number i ) {
            //12 bit unsigned            
            if(alternateunits) {
               float v = i.floatValue();
               double w = (44330.0f * (1.0f - Math.pow( v /1013.25f, (1.0f / 5.255f) )));
               return String.format("%5.1f",w);
            }
            return String.format("%5.1f",i.floatValue());
        }
        public int Units(){
            if(!alternateunits) return R.string.hPa;
            return R.string.Meters;
        }
       public byte[] generate(Object... args){
          if(args != null && args[0] instanceof String){
             byte[] ret = new byte[2];
             byte type = (byte)((idx & 0xf) << 4);
             byte low = (byte)(0);
             byte hi  = (byte)(0);
             ret[0] = (byte)(type | hi);
             ret[1] = low;
             return ret;
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class HumidityRoutine extends ConversionRoutine {
        public HumidityRoutine() {
        idx= TYPE_HUMID;        
        icon_name = ic_dew;
        name = "Humidity";
        }
        public String value(Number i ) {
            //12 bit fixed point 4 fractional 2's complement
           float value =  i.floatValue();
            value /= 16.0f;
            return String.format("%6.2f",value);
        }
        public int Units(){
            return R.string.Percent;
        }
       public byte[] generate(Object... args){
          // 12 bit fixed 4 fract
          if(args != null && args[0] instanceof Float){
             byte[] r = fixed12_4((Float)args[0]);
             r[0] = (byte) (r[0] | (idx << 4));
             return r;
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class TimeRoutine extends ConversionRoutine {
        public TimeRoutine() {
        idx= TYPE_TIME;        
        icon_name = ic_time;
        name = "Time";
        }
        public String value(Number i ) {
            // ba9876543210
            // hiiiijjjkkkk
            // h*10+i:j*10+k   HH:MM
            int hten = (i.intValue() >> 11) & 1;
            int hunit = (i.intValue() >> 7) & 0xf;            
            int mten = (i.intValue() >> 4) & 0x7;
            int munit =i.intValue() & 0xf;            
            return String.format("%d%d:%d%d",hten,hunit,mten,munit);
        }
        public int Units(){
            return R.string.Blank;
        }
       public byte[] generate(Object... args){
          if(args != null &&  args.length >= 2 &&
             args[0] instanceof Integer &&
             args[1] instanceof Integer){
             Integer hours = (Integer)(args[0]);
             Integer hours10s = hours/10;
             Integer hours1s = hours%10;             
             
             Integer minutes = (Integer)(args[1]);
             Integer minutes10s = minutes/10;
             Integer minutes1s = minutes%10;             

             byte type = (byte)((idx & 0xf) << 4);

             Integer hrs = 0x1f &((hours10s << 3) | (hours1s & 0xf));
             Integer mins = 0x7f &((minutes10s << 3) | (minutes1s & 0xf));
             Integer ov = (hrs << 7) |( mins & 0x7f);
             byte low = (byte)(ov & 0xff);
             byte hi  = (byte)((ov >> 8) & 0xf);

             byte[] ret = new byte[2];
             ret[0] = (byte)(type | hi);
             ret[1] = low;
             return ret;
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class DateRoutine extends ConversionRoutine {
        public DateRoutine() {
        idx= TYPE_DATE;        
        icon_name = ic_date;
        name = "Date";
        }
        public String value(Number i ) {
            // ba9876543210
            // mnnnnxoopppp
            // hours = m*10+n;
            // minutes = o*10+p;
            int mten = (i.intValue() >> 11) & 1;
            int munit = (i.intValue() >> 7) & 0xf;            
            int dten = (i.intValue() >> 4) & 0x3;
            int dunit =i.intValue() & 0xf;            
            return String.format("%d%d/%d%d",mten,munit,dten,dunit);

        }
        public int Units(){
            return R.string.Blank;
        }
       public byte[] generate(Object... args){
          if(args != null &&  args.length >= 2 &&
             args[0] instanceof Integer &&
             args[1] instanceof Integer){
             byte type = (byte)((idx & 0xf) << 4);

             Integer month = (Integer)(args[0]);
             Integer month10s = month/10;
             Integer month1s = month%10;             
             Integer ms = 0x1f & ((month10s << 3) | (month1s & 0xf));
                
             Integer day = (Integer)(args[1]);
             Integer day10s = day/10;
             Integer day1s = day%10;             
             Integer ds = 0x3f & ((day10s << 3) | (day1s & 0xf));
             
             Integer v = (ms << 7)| (ds & 0x3f);
                
             byte[] ret = new byte[2];
             ret[0] = (byte)(type | ((v>> 4)&0xf));
             ret[1] = (byte)(v & 0xff);
             return ret;
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class MagnetRoutine extends ConversionRoutine {
        public MagnetRoutine() {
        idx= TYPE_MAGNET;        
        icon_name = ic_magnetic_field;
        name = "Magnetic Field";
        }
        public String value(Number i ) {
            // 12 bit fixed point 4 fractional bits 2's complement
            float value =  i.floatValue();
            // 4 fractional bits
            value /= 16.0f;
            return String.format("%6.2f",value);
        }
        public int Units(){
            return R.string.MicroTesla;
        }
       public byte[] generate(Object... args){
          // 12 bit fixed 4 fract
          if(args != null && args[0] instanceof Float){
             byte[] r = fixed12_4((Float)args[0]);
             r[0] = (byte) (r[0] | (idx << 4));
             return r;

          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class DayRoutine extends ConversionRoutine {
        public DayRoutine() {
        idx= TYPE_DAY;        
        icon_name = ic_day;
        name = "Day";
        }
        public String value(Number i ) {
            // ba9876543210
            // mnnnnxoopppp
            // hours = m*10+n;
            // minutes = o*10+p;
            int ampm = (i.intValue() >> 10) & 1;
            int day = (i.intValue() >> 7) & 0x7;            
            int yten = (i.intValue() >> 4) & 0x7;
            int yunit =i.intValue() & 0xf;            
            return String.format("%d %d %d%d",ampm,day,yten,yunit);

        }
        public int Units(){
            return R.string.Blank;
        }
       public byte[] generate(Object... args){
          if(args != null && args.length >= 3 &&
             args[0] instanceof Integer &&
             args[1] instanceof Integer &&
             args[2] instanceof Integer){
             Integer year = ((Integer)args[0]).intValue();
             Integer day= ((Integer)args[1]).intValue();
             Integer ampm= ((Integer)args[2]).intValue();
             Integer year10s = year / 10;
             Integer year1s = year %10;
             Integer yrs = (year1s & 0xf) | ((0x7 & year10s) << 4);
             Integer dys = day & 0x7;
             byte[] ret = new byte[2];
             byte type = (byte)((idx & 0xf) << 4);
             
             byte low = (byte)(yrs & 0x7f);
             low = (byte)(low | (byte)((dys & 1) << 7));
             byte hi  = (byte)((dys >> 1)&0x3);
             hi = (byte) (hi | (byte) (ampm == 0 ? 0 : 1 << 3));
             ret[0] = (byte)(type | hi);
             ret[1] = low;
             return ret;
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class AccelerationRoutine extends ConversionRoutine {
        public AccelerationRoutine() {
        idx= TYPE_ACCEL;        
        icon_name = ic_apple;
        name = "Acceleration";
        }
        public String value(Number i ) {
            //12bit fixed 6 fractional 2's complement
            float value =  i.floatValue();
            // 4 fractional bits
            value /= 64.0f;
            if(alternateunits) {
               // m/sec/sec
               return String.format("%7.3f",value*9.80665f);
            }
            return String.format("%7.3f",value);
        }
       public int Units(){
            if(alternateunits) {
               return R.string.mpsps;
            }
            return R.string.Gees;
        }
       public byte[] generate(Object... args){
          if(args != null && args[0] instanceof Float){
             byte[] r =  fixed12_6((Float)args[0]);
             r[0] = (byte) (r[0] | (idx << 4));
             return r;
          }
          return null;
       }
    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class GyroRoutine extends ConversionRoutine {
       /**
           twos complement [-2000 2000]
        */
        public GyroRoutine() {
        idx= TYPE_GYRO;        
        icon_name = ic_gyro;
        name = "Gyro";
        }
        public String value(Number i ) {
            // 12-bit 2's complement
            int value = i.intValue() % 0x2000;
            return String.format("%4d",value);
        }
        public int Units(){
            return R.string.Degrees_per_second;
        }
       public byte[] generate(Object... args){
          if(args != null && args[0] instanceof Integer){
             Integer v = (Integer) args[0];
             byte[] ret = new byte[2];
             byte low = (byte)(v & 0xff);
             byte hi  = (byte)((v >> 8) & 0xf);
             byte type = (byte)((idx & 0xf) << 4);
             ret[0] = (byte)(type | hi);
             ret[1] = low;
             return ret;
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class BatteryRoutine extends ConversionRoutine {
        public BatteryRoutine() {
        idx= TYPE_BATTERY;        
        icon_name = ic_battery;
        name = "";
        }
        public String value(Number i ) {
            // uglyugly
            Float v = i.floatValue();
          //  int valuev = (i>>4) & 0xf;
           // int valued = i & 0xf;
            return String.format("%4.2f",v);
        }
        public int Units(){
            return R.string.Volts;
        }
       public byte[] generate(Object... args){
          if(args != null && args[0] instanceof Float){
             Float v = (Float)(args[0]);
             byte[] ret = new byte[1];
             v *= 10;
             Integer vi = v.intValue();
             vi = vi %100; // 00 to 99
              Integer vV = vi/10;
              Integer vt = vi%10;
             byte res = (byte)((vV ) << 4 | (vt & 0xf));
             ret[0] = res;
             return ret;
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class RSSIRoutine extends ConversionRoutine {
        public RSSIRoutine() {
        idx= TYPE_RSSI;        
        icon_name = ic_rssi;
        name ="";
        }
        public String value(Number i ) {
            // 12 bit signed
            int value = i.intValue() ;
            return String.format("%3d",value);
        }
         public int Units(){
            return R.string.dBm;
        }
       public byte[] generate(Object... args){
          if(args != null && args[0] instanceof Integer){
             Integer v = (Integer) args[0];
             return null;
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class PacketsRoutine extends ConversionRoutine {
        public PacketsRoutine() {
        idx= TYPE_PACKETS;        
        icon_name = ic_packets;
        name="";
        }
        public String value(Number i ) {
            // 24 bit signed
           int value = i.intValue();
            return String.format("%8d",value);
        }
        public int Units(){
            return R.string.Packets;
        }
       public byte[] generate(Object... args){
          if(args != null && args[0] instanceof Long){
             byte[] ret = new byte[4];
             Long v = (Long)args[0];
             ret[3] = (byte)(v & 0xff);
             v = v >> 8;
             ret[2] = (byte)(v & 0xff);
             v = v >> 8;
             ret[1] = (byte)(v & 0xff);
             v = v >> 8;
             ret[0] = (byte)(v & 0xff);
              return ret;
          }
          return null;
       }

    }
    /**
     * @brief an class for calculating sensor values, icons and units
     * 
     */
    public static class ModelRoutine extends ConversionRoutine {
        public ModelRoutine() {
        idx= TYPE_MODEL;        
        icon_name = ic_model_number;
        name="";
        }
        public int icon(){
            return super.icon();
        }

        public String value(Number i ) {
           // 16 bit signed
           // This is really 2 characters
           char low = (char)(i.intValue() & 0xff);
           char high = (char)((i.intValue() >> 8) & 0xff)
                   ;
           return String.format("EMBC%c%c",high,low);
        }
        public int Units(){
            return R.string.Blank;
        }
       public byte[] generate(Object... args){
          if(args != null && args[0] instanceof String){
             byte[] ret = new byte[2];
             String v = (String) args[0];
             ret[0] = (byte)v.charAt(0);
             ret[1] = (byte)v.charAt(1);
             return ret;
          }
          return null;
       }

    }

    /**
     * @brief an function for converting a float into a 8.4 integer
     * into 12 bits
     * 
     */
   private static byte[] fixed12_4(Float v){
      byte[] ret = new byte[2];
      Integer iv = v.intValue();
      // 4 bits of fraction
      Float   ffv = ((v - iv) * 16.0f);
      Integer fv = ffv.intValue();
      Integer ov = (iv << 4) | (fv & 0xf);
      byte low = (byte)(ov & 0xff);
      byte hi  = (byte)((ov >> 8) & 0xf);
      ret[0] = hi;
      ret[1] = low;
      return ret;
   }
    /**
     * @brief an function for converting a float into a 6.6 integer
     * into 12 bits
     * 
     */
   private static byte[] fixed12_6(Float v){
      byte[] ret = new byte[2];
      Integer iv = v.intValue();
      // 6 bits of fraction
      Float   ffv = ((v - iv) * 64.0f);
      Integer fv = ffv.intValue();
      Integer ov = iv << 6 | (fv & 0x3f);
      byte low = (byte)(ov & 0xff);
      byte hi  = (byte)((ov >> 8) & 0xf);
      ret[0] = hi;
      ret[1] = low;
      return ret;
   }
}
