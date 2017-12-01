/*
 * Copyright (c) 2014-2015 EM Microelectronic-Marin SA. All rights reserved.
 * Developed by Glacier River Design, LLC.
 */

package com.emdeveloper.embeaconlib.embeaconspecific;

import android.test.InstrumentationTestCase;

import com.emdeveloper.embeaconlib.EMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.EMBluetoothDevice;
import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.IEMBluetoothDevice;
import com.emdeveloper.embeaconlib.R;

import com.emdeveloper.embeaconlib.embeaconspecific.Sensors;
import com.emdeveloper.embeaconlib.embeaconspecific.Events;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Math.max;

/**
 * @brief class to generate advertisements
 */

public class GeneratorTest extends InstrumentationTestCase {

   public void setUp() throws Exception {
      super.setUp();
   }

   public void tearDown() throws Exception {

   }
   private Integer generateit(FileWriter fs,Integer idx,test sense,test event){
      String name = String.format("EMBeacon%05d",idx);
      String address = String.format("00:00:00:00:00:%02x",idx);
      Integer rssi = -idx;
      Float voltage = idx.floatValue();
      voltage = voltage % 10;
      voltage = 2.5f + voltage/10.0f;
      Number vnumber = voltage;
      Long time = idx + 123456700l;
      ConversionRoutine packetcount= new Sensors.PacketsRoutine() ;
      ConversionRoutine battery = new Sensors.BatteryRoutine();
      ConversionRoutine modelid = new Sensors.ModelRoutine();
      byte[] bat = battery.generate(vnumber);
      byte[] pkt = packetcount.generate(idx+0x9876500l);
      byte[] ed  = event.value();
      byte[] sd  = sense.value();
      byte[] company = {0x5A,0x00};

      IEMBluetoothAdvertisement adv =  EMBluetoothAdvertisement.generateAdvertisement(
         address,rssi,time,name,
         sd,
         ed,
         modelid.generate("02"),
         bat,
         company,
         pkt);
      writeit(fs,adv);
      return idx+1;
   }
   public void writeit(FileWriter fs,IEMBluetoothAdvertisement adv){
      String s = adv.dump();
      s = s+"\n";
      try {
         fs.append(s);
         fs.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   public void testGenerateCases() throws Exception {
      // generate advertisements for each sensor and each event
      int idx = 0;
      File file = getInstrumentation().getTargetContext().getFileStreamPath("adverts");
      FileWriter fs = new FileWriter(file);
      int cnt = max(sensortests.length,eventtests.length);
      for(idx = 0; idx < cnt; idx++){
         generateit(fs,idx,sensortests[idx%sensortests.length],eventtests[idx%eventtests.length]);
      }
      fs.close();
   }
   static class test {
      int type;
      int value;
      test(int t,int v){ type=t;value=v;};
      byte[] value() {
         byte[] b = new byte[2];
         b[0] = (byte)(((type<<4)&0xf0) | ((value>>8)&0xf));
         b[1] = (byte)((value) & 0xff);
         return b;
      }
   }
   static test[] sensortests = {
      new test(Sensors.TYPE_LIGHT     ,(0x000)), // 0 Lux
      new test(Sensors.TYPE_LIGHT     ,(0x123)), // 291 Lux
      new test(Sensors.TYPE_LIGHT     ,(0xFFF)), // 4095 Lux
      new test(Sensors.TYPE_FWREV    ,(0x000)), // Firmware v0.0.0
      new test(Sensors.TYPE_FWREV    ,(0x254)), // Firmware v2.5.4
      new test(Sensors.TYPE_FWREV    ,(0x999)), // Firmware v9.9.9
      new test(Sensors.TYPE_AUTOCAL  ,(0x000)), // 00/000 VCO/Mod
      new test(Sensors.TYPE_AUTOCAL  ,(0x123)), // 1/23 VCO/Mod
      new test(Sensors.TYPE_AUTOCAL  ,(0x999)), // 9/99 VCO/Mod
      new test(Sensors.TYPE_GENERIC         ,(0x000)), // 0
      new test(Sensors.TYPE_GENERIC         ,(0x123)), // 291
      new test(Sensors.TYPE_GENERIC         ,(0xFFF)), // 4095
      new test(Sensors.TYPE_TEMP     ,(0x000)), // 0 C, 32 F
      new test(Sensors.TYPE_TEMP     ,(0x7FF)), // 127.9375 C, 262.2875 F
      new test(Sensors.TYPE_TEMP     ,(0x123)), // 18.1875 C, 64.7375 F
      new test(Sensors.TYPE_TEMP     ,(0x800)), // -128 C, -198.4 F
      new test(Sensors.TYPE_TEMP     ,(0xFFF)), // -0.0625 C, 31.8875 F
      new test(Sensors.TYPE_PRES        ,(0x000)), // 0 hPa, 44330 m
      new test(Sensors.TYPE_PRES        ,(0x123)), // 291 hPa, 9368.3897 m
      new test(Sensors.TYPE_PRES        ,(0xFFF)), // 4095 hPa, -13495.4099 m
      new test(Sensors.TYPE_HUMID        ,(0x000)), // 0% Humidity
      new test(Sensors.TYPE_HUMID        ,(0x7FF)), // 127.9375% Humidity
      new test(Sensors.TYPE_HUMID        ,(0x123)), // 18.1875% Humidity
      new test(Sensors.TYPE_HUMID        ,(0x800)), // -128% Humidity
      new test(Sensors.TYPE_HUMID        ,(0xFFF)), // -0.0625% Humidity
      new test(Sensors.TYPE_TIME            ,(0x959)), // 12:59
      new test(Sensors.TYPE_TIME            ,(0x0000)), // 00:00
      new test(Sensors.TYPE_TIME            ,(0x0A3)), // 01:23
      new test(Sensors.TYPE_DATE            ,(0x091)), // January 11 Date
      new test(Sensors.TYPE_DATE            ,(0x113)), // Feburary 13 Date
      new test(Sensors.TYPE_DATE            ,(0x1A0)), // March 20 Date
      new test(Sensors.TYPE_DATE            ,(0x269)), // April 29 Date
      new test(Sensors.TYPE_DATE            ,(0x2B1)), // May 31 Date
      new test(Sensors.TYPE_DATE            ,(0x301)), // June 1 Date
      new test(Sensors.TYPE_DATE            ,(0x381)), // July 1 Date
      new test(Sensors.TYPE_DATE            ,(0x401)), // August 1 Date
      new test(Sensors.TYPE_DATE            ,(0x481)), // Septempber 1 Date
      new test(Sensors.TYPE_DATE            ,(0x801)), // October 1 Date
      new test(Sensors.TYPE_DATE            ,(0x881)), // November 1 Date
      new test(Sensors.TYPE_DATE            ,(0x901)), // December 1 Date
      new test(Sensors.TYPE_MAGNET        ,(0x000)), // 0 uT
      new test(Sensors.TYPE_MAGNET        ,(0x7FF)), // 2047 uT
      new test(Sensors.TYPE_MAGNET        ,(0x800)), // -2048 uT
      new test(Sensors.TYPE_DAY             ,(0x080)), // Sunday, am, 2000
      new test(Sensors.TYPE_DAY             ,(0x512)), // Monday, pm, 2012
      new test(Sensors.TYPE_DAY             ,(0x1F9)), // Tuesday, am, 2079
      new test(Sensors.TYPE_DAY             ,(0x200)), // Wednesday, am, 2000
      new test(Sensors.TYPE_DAY             ,(0x280)), // Thursday, am, 2000
      new test(Sensors.TYPE_DAY             ,(0x300)), // Friday, am, 2000
      new test(Sensors.TYPE_DAY             ,(0x381)), // Saturday, am, 2001
      new test(Sensors.TYPE_ACCEL    ,(0x000)), // 0 g
      new test(Sensors.TYPE_ACCEL    ,(0x7FF)), // 31.984375 g
      new test(Sensors.TYPE_ACCEL    ,(0x123)), // 4.359375 g
      new test(Sensors.TYPE_ACCEL    ,(0x800)), // -32 g
      new test(Sensors.TYPE_ACCEL    ,(0xFFF)), // -0.015625 g
      new test(Sensors.TYPE_GYRO            ,(0x000)), // 0 rot/sec
      new test(Sensors.TYPE_GYRO            ,(0x7FF)), // 2047 rot/sec
      new test(Sensors.TYPE_GYRO            ,(0x800)), // -2048 rot/sec
      };
   static test[] eventtests = {
      new test(Events.ALARM            ,(0x000)), // 0 Alarm Events
      new test(Events.ALARM            ,(0x123)), // 291 Alarm Events
      new test(Events.ALARM            ,(0xFFF)), // 4095 Alarm Events
      new test(Events.MOVEMENT     ,(0x000)), // 0 Any Movement Events
      new test(Events.MOVEMENT     ,(0x123)), // 291 Any Movement Events
      new test(Events.MOVEMENT     ,(0xFFF)), // 4095 Any Movement Events
      new test(Events.BUTTONPRESS     ,(0x000)), // 0 Button Presses
      new test(Events.BUTTONPRESS     ,(0x123)), // 291 Button Presses
      new test(Events.BUTTONPRESS     ,(0xFFF)), // 4095 Button Presses
      new test(Events.BUZZER           ,(0x000)), // 0 Buzzer Events
      new test(Events.BUZZER           ,(0x123)), // 291 Buzzer Events
      new test(Events.BUZZER           ,(0xFFF)), // 4095 Buzzer Events
      new test(Events.CLOSEMAGNET     ,(0x000)), // 0 Close Magnet Events
      new test(Events.CLOSEMAGNET     ,(0x123)), // 291 Close Magnet Events
      new test(Events.CLOSEMAGNET     ,(0xFFF)), // 4095 Close Magnet Events
      new test(Events.FALL             ,(0x000)), // 0 Fall Events
      new test(Events.FALL             ,(0x123)), // 291 Fall Events
      new test(Events.FALL             ,(0xFFF)), // 4095 Fall Events
      new test(Events.FARMAGNET       ,(0x000)), // 0 Far Magnet Events
      new test(Events.FARMAGNET       ,(0x123)), // 291 Far Magnet Events
      new test(Events.FARMAGNET       ,(0xFFF)), // 4095 Far Magnet Events
      new test(Events.HIGHHUMIDITY    ,(0x000)), // 0 High Humidity Events
      new test(Events.HIGHHUMIDITY    ,(0x123)), // 291 High Humidity Events
      new test(Events.HIGHHUMIDITY    ,(0xFFF)), // 4095 High Humidity Events
      new test(Events.HIGHPRESSURE    ,(0x000)), // 0 High Pressure Events
      new test(Events.HIGHPRESSURE    ,(0x123)), // 291 High Pressure Events
      new test(Events.HIGHPRESSURE    ,(0xFFF)), // 4095 High Pressure Events
      new test(Events.HIGHTEMPERATURE ,(0x000)), // 0 High Temp Events
      new test(Events.HIGHTEMPERATURE ,(0x123)), // 291 High Temp Events
      new test(Events.HIGHTEMPERATURE ,(0xFFF)), // 4095 High Temp Events
      new test(Events.LOWBATTERY      ,(0x000)), // 0 Low Battery
      new test(Events.LOWBATTERY      ,(0x123)), // 291 Low Battery
      new test(Events.LOWBATTERY      ,(0xFFF)), // 4095 Low Battery
      new test(Events.LOWHUMIDITY     ,(0x000)), // 0 Low Humidity Events
      new test(Events.LOWHUMIDITY     ,(0x123)), // 291 Low Humidity Events
      new test(Events.LOWHUMIDITY     ,(0xFFF)), // 4095 Low Humidity Events
      new test(Events.LOWPRESSURE     ,(0x000)), // 0 Low Pressure Events
      new test(Events.LOWPRESSURE     ,(0x123)), // 291 Low Pressure Events
      new test(Events.LOWPRESSURE     ,(0xFFF)), // 4095 Low Pressure Events
      new test(Events.LOWTEMPERATURE  ,(0x000)), // 0 Low Temp Events
      new test(Events.LOWTEMPERATURE  ,(0x123)), // 291 Low Temp Events
      new test(Events.LOWTEMPERATURE  ,(0xFFF)), // 4095 Low Temp Events
      new test(Events.CALIBRATIONS    ,(0x000)), // 0 Calibrations
      new test(Events.CALIBRATIONS    ,(0x123)), // 291 Calibrations
      new test(Events.CALIBRATIONS    ,(0xFFF)), // 4095 Calibrations
      new test(Events.TAP              ,(0x000)), // 0 Tap Events
      new test(Events.TAP              ,(0x123)), // 291 Tap Events
      new test(Events.TAP              ,(0xFFF)), // 4095 Tap Events
      
      };
           
}
