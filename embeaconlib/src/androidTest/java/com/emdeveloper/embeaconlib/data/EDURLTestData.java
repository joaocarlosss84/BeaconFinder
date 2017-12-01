package com.emdeveloper.embeaconlib.data;

import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;

/**
 * Created by chris on 8/27/15.
 */
public class EDURLTestData {
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
   public static Data.ADData makemandata(Integer power,Integer scheme,String url){
      Data.bytes b0 = Data.bytes.createv(0xAA, 0xFE);  // serviceuuid
      Data.bytes b1 = Data.bytes.createv(0x10);  // frame type
      Data.bytes b2 = Data.bytes.createv(power);
      Data.bytes b3= Data.bytes.createv(scheme);
      // truncate name
      byte[]urlb = url.getBytes();
      Data.bytes b4 = Data.bytes.create(urlb);

      Data.ADData x = Data.ADData.createv((byte) (0x16),b0,b1,b2,b3,b4);
      return x;
   }
   public static IEMBluetoothAdvertisement create(Integer id, Integer power,Integer scheme,String url) {
      return create(id,makemandata(power,scheme,url));
   }
}
