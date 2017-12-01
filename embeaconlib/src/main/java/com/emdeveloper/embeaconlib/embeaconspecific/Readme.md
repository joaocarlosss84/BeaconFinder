# stuff

This package contains routines to translate and display the manufacturer specific data for the EMBeacon product.

The manufacturer specific data contains
- company code
- model id
- sensor type
- sensor value
- event type
- event value
- battery voltage
- packet count
In addition we store
- name
- rssi
- time
- address

This package translates the sensor type, converts the sensor value and gives the proper icon, value string, and unit string.

There are 16 different sensor types
```
   private static final int TYPE_LIGHT   		= 0x0000;
	private static final int TYPE_FWREV  		= 0x1000;
	private static final int TYPE_AUTOCAL 		= 0x2000;
	private static final int TYPE_GENERIC 		= 0x3000;

	private static final int TYPE_TEMP 			= 0x4000;
	private static final int TYPE_PRES 			= 0x5000;
	private static final int TYPE_HUMID 		= 0x6000;
	private static final int TYPE_TIME 			= 0x7000;
	private static final int TYPE_DATE 			= 0x8000;
	private static final int TYPE_MAGNET 		= 0x9000;
	private static final int TYPE_DAY 			= 0xA000;
	private static final int TYPE_ACCEL 		= 0xB000;
	private static final int TYPE_GENERIC		= 0xC000;
   private static final int TYPE_GENERIC		= 0xD000;
   private static final int TYPE_GENERIC		= 0xE000;
   private static final int TYPE_GENERIC		= 0xF000;
```

There are 16 different event types.
```
  eventType = "Button Presses";  0
  eventType = "Low Battery";
  eventType = "Calibrations";
  eventType = "Low Temperature";	
  eventType = "High Temperature";
  eventType = "Low Pressure";
  eventType = "High Pressure";
  eventType = "Low Humidity";
  eventType = "High Humidity";
  eventType = "Close Magnet";
  eventType = "Far Magnet";
  eventType = "Movement";
  eventType = "Tap";
  eventType = "Fall";
  eventType = "Alarm";
  eventType = "Buzzer";      0xf

```

The icons for sensors

```
 ic_apple
 ic_autocal
 ic_date
 ic_day
 ic_dew
 ic_firmware;
 ic_generic
 ic_gyro
 ic_lightbulb
 ic_magnetic_field
 ic_pressure
 ic_thermo_outline
 ic_time
```
The icons for events
```
 ic_alarm_clock
 ic_any_movement
 ic_autocal_vco_cal
 ic_battery_low
 ic_buzzer
 ic_fall
 ic_lights_off
 ic_lights_on
 ic_magnetic_field_high
 ic_magnetic_field_low
 ic_pressure_high
 ic_pressure_low
 ic_push_button
 ic_tap
 ic_temperature_high
 ic_temperature_low
```

This code is from the Beacon firmware for updating the packet
``` C
/**
 ******************************************************************************
 * @brief Add dynamic values to EMBeacon and AltBeacon Advertising Packets
 *
 * @n -inserts event count in open event field
 * @n -schedules sensor readings
 * @n -inserts sensor data in open sensor field
 *
 * @param  powerIndex, EM9301 power level (0..7)
 * @param  batteryLevel unsigned value representing battery life
 * @return nothing
 *******************************************************************************/

void UpdateDynamicPacketData(const UINT8 powerIndex, const UINT8 batteryLevel)
{

   // This defines when and how often data is read.  This can
   // be customized to suit product needs.  Warning: temperature is
   // averaged and will move very slowly if the beacon interval is
   // very long.
   switch (EMAdvertisingPacket.data.packet_count % 4)
   {
      case 1:
         // Insert a call to run once every 4 advertising cycles
         break;
      case 2:
         #if CAPABILITY_BATTERY_SENSE
            EMAdvertisingPacket.data.battery = MeasureBattery();
         #endif
         break;
      case 3:
         #if CAPABILITY_LIGHT_SENSE
            lightReading = MeasureLight();
         #endif
         break;
      case 0:
      default:
         // Still used for 9301 calibration purposes even if we use the EM4325
         // Could consider changing this, though.
         temperatureReading = MeasureTemperature() + AdvParams.temperatureOffset;
         break;
   }
   // Let the product code decide for itself
   PRODUCT_ACQUIRE_DATA(beaconModeIdx, beaconMode, EMAdvertisingPacket.data.packet_count);

   UINT8 eventType = OpenEventSpecifier(AdvParams.openSensor[0]);
   UINT8 dataType  = OpenSensorSpecifier(AdvParams.openSensor[0]);

   // This defines what goes in the open sensor field
   switch (dataType)
   {
      // UNIVERSAL CASES

      case SENSOR_FIELD_TYPE_FIRMWARE_REV:
         EMAdvertisingPacket.data.openSense = SENSOR_FW_REV();
         break;
      case SENSOR_FIELD_TYPE_UNSPECIFIED:
         EMAdvertisingPacket.data.openSense = SENSOR_UNSPEC(GetEM9301Rev());
         // Special tweak for MM - delete this line and the next to restore original code
         EMAdvertisingPacket.data.openSense = SENSOR_UNSPEC(temperatureReading>>4);
         break;

      // CAPABILITY-DEPENDENT CASES

      #if !CAPABILITY_EXTERNAL_TEMP_SENSE
      case SENSOR_FIELD_TYPE_TEMPERATURE:
         // rely on the 6819's on-board sensor when there's no external source
         EMAdvertisingPacket.data.openSense = SENSOR_TEMPERATURE(temperatureReading);
         break;
      #endif

      #if CAPABILITY_LIGHT_SENSE
      case SENSOR_FIELD_TYPE_LIGHT_SENSE:
         EMAdvertisingPacket.data.openSense = SENSOR_LIGHT(lightReading);
         break;
      #endif

      #if CAPABILITY_EM9301_ONESHOT_CAL
      case SENSOR_FIELD_TYPE_AUTOCAL_RESULT:
         EMAdvertisingPacket.data.openSense = SENSOR_AUTOCAL(GetRFCal_vco(),
                                                             GetRFCal_modulator());
         break;
      #endif

      // PRODUCT-DEPENDENT CASES:

      default:
         // Let the product-specific function determine what to provide
         EMAdvertisingPacket.data.openSense = (dataType << 12) |
                                              (PRODUCT_GET_SAMPLE_VALUE(dataType) & 0x0FFF);
   }

   // This defines what goes in the open event counter field
   switch (eventType)
   {
      // UNIVERSAL CASES

      case EVENT_FIELD_TYPE_LOW_BATTERY:
           EMAdvertisingPacket.data.event_count = (EVENT_FIELD_TYPE_LOW_BATTERY      << 12) |
                                                  0x00;
           break;
      case EVENT_FIELD_TYPE_RF_VCO_CAL:
           EMAdvertisingPacket.data.event_count = (EVENT_FIELD_TYPE_RF_VCO_CAL       << 12) |
                                                  GetRFCal_EventCounter();
           break;

      // CAPABILITY- and PRODUCT-DEPENDENT CASES

      default:
         EMAdvertisingPacket.data.event_count = (eventType << 12) |
                                                (PRODUCT_GET_EVENT_COUNT(eventType) & 0x0FFF);


   }

   #if CAPABILITY_ALT_BEACON
      UpdateDynamicAltBcnData(powerIndex, batteryLevel);
   #endif
}
```


From the spec
Sensors
Description | ms-nibble | data format |  Units  | Formula
------------|-----------|-------------|----------------
Light       | 0x0       |12-bit unsigned | xxx Lux |    %d, 0xfff&v
Firmware Revision | 0x01 | BCD 4msb.4nsb.4lsb | none | %d.%d.%d ((v>>8)&0xf),((v>>4)&0xf),((v>>0)&0xf)
Autocal | 0x02 | BCD 4msb/8msb | xx/xxxx VCO/Mod | %d/%d ((v>>8)&0xf),((v>>0)&0xff)
Generic | 0x3  | 12 bit unsigned | none | %d
Temperature | 0x4 | 12 bit fixed-point 4 fractional bits 2's complement | degC -> degF | ((float) v) / 16.0
Pressure    | 0x05| 12 bit unsigned | hPa -> m(meters?) | %d
Humitity    | 6 | 12 bit fixed point 4 fractional 2's complement|   xxx.xx % | ((float) v) / 16.0
Time        | 7 | BCD HH:MM| hh:mm | %x:%02x ,(v >> 7) & 0x1f, (v & 0x7f)
Date        | 8 | BCD MM,-,DD | month = ```(((v >> 11) & 1) *10)+((v >> 7) & 0xf)```, day = ```((v>>4) & 0x3)*10 + (v & 0xf)```
Magnetic field | 9 |12 bit fixed 4 fractional 2's complement | microT | ((float) v) / 16.0
Day | A | BCD A/P,DD,YY | pm = ((v>>10) & 0x1), dayOfWeek = ((v>>7) & 0x7), year = v & 0x7f
Acceleration | B | 12bit fixed 6 fractional 2's complement | -xx.xxx g | ((float) v) / 64.0
Gyro         | C | 12-bit 2's complement | -xxxx degrees/second | %d


RSSI    |0x10 | 12 bit signed | dBm | %d
Battery |0x11 | bcd 4 4 | v | %d.%d (v>>4) & 0xf , v & 0xf


Events
Description | ms-nibble | count |  Units  | Formula
------------|-----------|-------------|----------------
Button Press| 0x0 |12-bit unsigned | Button Presses |    %d, 0xfff&v
Low Battery | 0x1 |Low events
VCO Cal     | 0x2 |Calibrations
Low Temp    | 0x3 |Low Events
High Temp   | 0x4 |High Events
Low Pressure| 0x5 |Low Events
High Pressure| 0x6 |High Events
Lights On | 0x7 |Lights Off
Lights Off| 0x8 |Lights Off
Close Mag | 0x9 |Close Events
Far Mag | 0xA| Far Events
Movement | 0xB|Moves
Tap | 0xC|Taps
Fall | 0xD|Falls
Alarm | 0xE|Alarms
Buzzer | 0xF|Buzzer Events
