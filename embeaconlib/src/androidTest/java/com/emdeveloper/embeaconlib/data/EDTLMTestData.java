package com.emdeveloper.embeaconlib.data;

import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;

/**
 * Created by chris on 8/27/15.
 */
public class EDTLMTestData {
   static Integer  advcnt = 0;
   public static IEMBluetoothAdvertisement create(Integer id,Data.ADData mandata){
      Data.ADData flags = Data.ADData.create((byte)0x01, Data.bytes.createv(0x02|0x04));
      Data.ADData services = Data.ADData.create((byte)0x03, Data.bytes.createv(0xAA,0xFE));        Data.Advert adv = new Data.Advert(flags,services,mandata);
      long st = System.currentTimeMillis();
      while(st == System.currentTimeMillis());
      long currenttime  = System.currentTimeMillis();
      Long ct = Long.valueOf(currenttime);
      advcnt++;
      String address = String.format("01:02:03:04:05:%02x",id & 0xff);
      return Data.createIEMBluetoothAdvertisement(address,-advcnt,ct,adv);
   }
   /**
    *  fixed beacon code,
    *  fixed company code
    *  fixed guid
    */
   public static Data.ADData makemandata(Integer battery, Integer temperature, Integer packets, Integer uptime) {
      Data.bytes b0 = Data.bytes.createv(0xAA, 0xFE);  // serviceuuid
      Data.bytes b1 = Data.bytes.createv(0x20);  // frame type
      Data.bytes b2 = Data.bytes.createv(0x0);
      // check lengths
      Data.bytes b3 = Data.bytes.createv((battery >> 8) & 0xff,battery & 0xff);
      Data.bytes b4 = Data.bytes.createv((temperature >> 8) & 0xff,temperature & 0xff);
      Data.bytes b5 = Data.bytes.createv((packets >> 24) & 0xff, (packets >> 16) & 0xff, (packets >> 8) & 0xff, (packets) & 0xff);
      Data.bytes b6 = Data.bytes.createv((uptime >> 24) & 0xff, (uptime >> 16) & 0xff, (uptime >> 8) & 0xff, (uptime) & 0xff);

      Data.ADData x = Data.ADData.createv((byte) (0x16),b0,b1,b2,b3,b4,b5,b6);
      return x;
   }
   public static IEMBluetoothAdvertisement create(Integer id, Integer battery, Integer temperature, Integer count, Integer uptime) {
      return create(id,makemandata(battery,temperature,count,uptime));
   }
}
