package com.emdeveloper.embeaconlib.data;

import java.util.ArrayList;
import java.util.Arrays;
import android.os.SystemClock;
import com.emdeveloper.embeaconlib.EMBluetoothDevice;
import com.emdeveloper.embeaconlib.IEMBluetoothDevice;
import com.emdeveloper.embeaconlib.EMBluetoothAdvertisement;
import com.emdeveloper.embeaconlib.IEMBluetoothAdvertisement;
/// test class for creating advertisements.
/// data scanned via hcitool -i hi0 lescan
// addhex("0F09454D426561636F6E3132383737000EFF5A0000003031340000003C0000");
/**                                            0 1 2 3 4 5 6 7 8 9 a b c
 * addhex("0F09454D426561636F6E3132383737000EFF5A0000003031340000003C0000");
 *         ^ ^ ^                           ^ ^ ^   ^   ^   ^ ^       ^
 *         | | |                           | | |   |   |   | |       | Event  
 *         | | |                           | | |   |   |   | | Packet Count
 *         | | |                           | | |   |   |   | Battery voltage
 *         | | |                           | | |   |   |Model Id
 *         | | |                           | | |   |Open Sensor 
 *         | | |                           | | |Manuf ID
 *         | | |                           | | Manuf Id Type_ID
 *         | | |                           |Manuf data length
 *         | | |Beacon Name
 *         | |Beacon Name Type ID
 *         | Name Length
 * 
 */
public class Data {
   /// @brief an array of bytes
   public static class bytes {
      byte[] d;
      public bytes(byte[] b) {
         d=b;
      }
      static bytes create(byte[] b){
         return new bytes(b);
      }
      public static bytes create(String s){
         return new bytes(hexStringToByteArray(s));
      }
      public static bytes createv(int... in){
         byte[]dd = new byte[in.length];
         int i;
         for(i = 0; i < in.length; i++){
            dd[i] = (byte)in[i];
         }
         return new bytes(dd);
      }
      public byte[] value() { return d;}
      public int length(){return (d == null ? 0 : d.length);}
      
   }
   /// @brief one piece of an advertisement
   public static class ADData{
      public byte atype;
      public byte[] data;
      public int datalength() { return (data==null ? 0 :data.length);};
      public byte[] getADData() {
         byte[] out = new byte[1+1+data.length];
         out[0] = (byte)(datalength()+1);
         out[1] = atype;
         System.arraycopy(data,0,out,2,data.length);
         return out;
      }
      public void add(byte[] d) {
         byte[] odata = new byte[datalength()+d.length];
         if(data != null) System.arraycopy(data,0,odata,0,datalength());
         System.arraycopy(d,0,data,datalength(),d.length);
         data = odata;
      }
      public ADData(byte t, bytes... datas){
         int i ;
         atype = t;
         int n = datas.length;
         int totallength = 0;
         for(i = 0; i < n; i++){
            totallength += datas[i].length();
         }
         data = new byte[totallength];
         int p = 0;
         for(i = 0; i < n; i++){
            System.arraycopy(datas[i].value(),0,data,p,datas[i].length());
            p += datas[i].length();
         }
      }

      public static ADData createv(byte type,bytes... datas) {
         return new ADData(type,datas);

      }
      public static ADData create(byte type,bytes datas) {
         return new ADData(type,datas);

      }
   }
   ///@brief an advertisement
   public static class Advert {
      public ADData[] addata;
      public byte[] getdata(){
         int i;
         int len = 0;
         for(i = 0; i < addata.length; i++){
            len += addata[i].datalength()+2;
         }
         byte[] out = new byte[len];
         int p = 0;
         for(i = 0; i < addata.length; i++){
            byte[] dd = addata[i].getADData();
            System.arraycopy(dd,0,out,p,dd.length);
            p += dd.length;
         }
         return out;
      }
      public Advert(ADData... addda){
         addata = new ADData[addda.length];
         int i;
         for(i = 0; i < addda.length; i++){
            addata[i] = addda[i];
         }
      }
   }
   public static ArrayList<byte[]> data ;
   public static int datasize = 0;
   public static ArrayList<IEMBluetoothAdvertisement>  adverts;
   static void addhex(String s) {
      byte[] b = hexStringToByteArray(s);
      long currenttime = SystemClock.elapsedRealtimeNanos();
      long st = System.currentTimeMillis();
      while(st == System.currentTimeMillis());
      currenttime  = System.currentTimeMillis();
      Long ct = Long.valueOf(currenttime);
      String address = String.format("01:02:03:04:05:%02x",datasize%10);
      IEMBluetoothDevice iemd = EMBluetoothDevice.create(address);
      IEMBluetoothAdvertisement adv = EMBluetoothAdvertisement.create(iemd, -datasize*10, ct , b);
      adverts.add(datasize,adv);
      data.add(datasize, b);
      datasize++;
   }
/**
 * @brief converts a string of hex bytes to an array
 */
   public static byte[] hexStringToByteArray(String s) {
      int len = s.length();
      byte[] data = new byte[len / 2];
      for (int i = 0; i < len; i += 2) {
         data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                               + Character.digit(s.charAt(i+1), 16));
      }
      return data;
   }
   /// @brief returns the byte rep of the string
   public static byte[] StringToByteArray(String s){
      byte[] b = new byte[s.length()];
      b = s.getBytes();
    //  s.getBytes(0,s.length(),b,0);
      return b;
   }
   static {
      data = new ArrayList<byte[]>(1000);
      adverts = new ArrayList<IEMBluetoothAdvertisement>(1000);
      datasize = 0;
      Advert a = new Advert(ADData.create((byte) 0x9,bytes.create("454D426561636F6E313238373700")),
                            ADData.create((byte) 0xff,bytes.create("5A0000003031340000003C0000")));
      byte[] x=  a.getdata();
      byte[] y = hexStringToByteArray("0F09454D426561636F6E3132383737000EFF5A0000003031340000003C0000");
      boolean pass = java.util.Arrays.equals(x, y);
      datasize = 1;
      
   }

   
   public static IEMBluetoothAdvertisement createIEMBluetoothAdvertisement(String address,Integer rssi, Long time, Advert a){
      IEMBluetoothDevice iemd = EMBluetoothDevice.create(address);
      IEMBluetoothAdvertisement adv = EMBluetoothAdvertisement.create(iemd, rssi, time , a.getdata());
      return adv;
   }
   
}
