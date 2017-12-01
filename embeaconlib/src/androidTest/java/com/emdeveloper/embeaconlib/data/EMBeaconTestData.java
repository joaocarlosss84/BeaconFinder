package com.emdeveloper.embeaconlib.data;

import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;

/**
 * Created by chris on 8/27/15.
 *
 * class for generating EMBeacon advertisements
 */
public class EMBeaconTestData {
   static Integer  advcnt = 0;
   public static IEMBluetoothAdvertisement create(Integer id,String name, Data.ADData mandata){
      Data.ADData adname = Data.ADData.create((byte)0x9, new Data.bytes(Data.StringToByteArray(name)));
      Data.Advert adv = new Data.Advert(adname,mandata);
      long st = System.currentTimeMillis();
      while(st == System.currentTimeMillis());
      long currenttime  = System.currentTimeMillis();
      Long ct = Long.valueOf(currenttime);
      advcnt++;
      String address = String.format("01:02:03:04:05:%02x",id & 0xff);
      return Data.createIEMBluetoothAdvertisement(address,-advcnt,ct,adv);
   }
   public static Data.ADData makemandata(Integer sense,Integer event, Integer packets,Integer battery, String modelid){
      Data.ADData x = Data.ADData.createv((byte) (0xff),
              Data.bytes.createv(0x5a, 0x00),  // company
              Data.bytes.createv((sense >> 8) & 0xff, sense & 0xff),
              Data.bytes.createv(modelid.charAt(0), modelid.charAt(0)),
              Data.bytes.createv(battery),
              Data.bytes.createv((packets >> 24) & 0xff, (packets >> 16) & 0xff, (packets >> 8) & 0xff, (packets) & 0xff),
              Data.bytes.createv((event >> 8) & 0xff, event & 0xff));
      return x;
   }

   public static IEMBluetoothAdvertisement create(Integer id,String name,Integer sense,Integer event, Integer packets,Integer battery, String modelId){
      return create(id,name,makemandata(sense,event,packets,battery,modelId));
   }
}
