package com.emdeveloper.embeaconlib.data;

import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;

/**
 * Created by chris on 8/27/15.
 */
public class AltBeaconTestData {
   static Integer  advcnt = 0;
   public static IEMBluetoothAdvertisement create(Integer id,Data.ADData mandata){
      Data.ADData flags = Data.ADData.create((byte)0x01, Data.bytes.createv(0x02|0x04));
      Data.Advert adv = new Data.Advert(flags,mandata);
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
   public static Data.ADData makemandata(Integer majorId,Integer minorId,Integer power,Integer pct){
      Data.bytes b0 = Data.bytes.createv(0x5a, 0x00);  // company
      Data.bytes b1 = Data.bytes.createv(0xBE, 0xAC);  // beacon codeest
      Data.bytes b2 = Data.bytes.createv(0x69, 0x9E, 0xBC, 0x80, 0xE1, 0xF3, 0x11, 0xE3,
                                         0x9A, 0x0F, 0x0C, 0xF3, 0xEE, 0x3B, 0xC0, 0x12); // guid
      Data.bytes b3 = Data.bytes.createv(majorId >> 8 & 0xff, majorId & 0xff);
      Data.bytes b4 = Data.bytes.createv(minorId >> 8 & 0xff, minorId & 0xff);
      Data.bytes b5 = Data.bytes.createv(power);
      Data.bytes b6 = Data.bytes.createv(pct);

      Data.ADData x = Data.ADData.createv((byte) (0xff),b0,b1,b2,b3,b4,b5,b6);
      return x;
   }
   public static IEMBluetoothAdvertisement create(Integer id,Integer majorId,Integer minorId,Integer power,Integer pct){
      return create(id,makemandata(majorId,minorId,power,pct));
   }
}
