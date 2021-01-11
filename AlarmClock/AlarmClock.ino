#include "Weather.h"
#include "Alarm.h"
#include "Clock.h"
#include "Bluetooth.h"
#include <Wire.h>
#include <LiquidCrystal_I2C.h>

LiquidCrystal_I2C lcd(0x27, 16, 2);
Weather weatherStation(2);
Clock clock(8, 7, 6);
Alarm alarm(10, 3, 9, false);
BluetoothHC05 bluetoothHC05(11, 12);

char* hour;
char* minute;
char* second;

char* temperature;
char* humidity;

const byte interruptPin = 3;
String alarmDataString = " 1 ";
String currentTime = " 2 ";
bool wasPlayed = false;
char rc;
char toSend[8];

String oldAlarm;

void setup() {
  Serial.begin(9600);
  bluetoothHC05.begin(9600);

  lcd.init();
  lcd.backlight();
  //  clock.SetTimeAndDate(sec, min, h, weekday, day, month, year);
}


void loop() {
  clock.Update();

  DisplayTime();
  DisplayWeather();
  ReceiveBluetoothData();

  if (currentTime == alarmDataString && wasPlayed == false) {
    wasPlayed = true;
    StartAlarm();
  }

  if (currentTime != alarmDataString) {
    wasPlayed == false;
  }
}


void ReceiveBluetoothData() {
  if (bluetoothHC05.available()) {
    alarmDataString = "";

    while (bluetoothHC05.available() > 0) {
      rc = bluetoothHC05.read();
      alarmDataString += rc;

      if (oldAlarm == alarmDataString) {
        wasPlayed = false;
      }
      Serial.write(rc);
    }
    delay(2000);
  }
    oldAlarm = alarmDataString;
}


void DisplayTime() {
  hour = clock.getHour();
  minute = clock.getMin();
  second = clock.getSec();

  bluetoothHC05.write('a');
  bluetoothHC05.write(hour[0]);
  bluetoothHC05.write(hour[1]);
  bluetoothHC05.write(':');
  bluetoothHC05.write(minute[0]);
  bluetoothHC05.write(minute[1]);
  bluetoothHC05.write(':');
  bluetoothHC05.write(second[0]);
  bluetoothHC05.write(second[1]);

  lcd.setCursor(0, 0);
  lcd.print(clock.ToStringTime());
  lcd.setCursor(0, 1);
  lcd.print(clock.ToStringDate());

  currentTime = clock.ToStringAlarm();
  delete[] hour;
  delete[] minute;
  delete[] second;
}


void DisplayWeather() {
  temperature = weatherStation.getTemperature();
  humidity = weatherStation.getHumidity();

  bluetoothHC05.write(temperature[0]);
  bluetoothHC05.write(temperature[1]);
  bluetoothHC05.write(temperature[2]);
  bluetoothHC05.write(humidity[0]);
  bluetoothHC05.write(humidity[1]);

  lcd.setCursor(10, 0);
  lcd.print(weatherStation.ToStringTemperature());

  lcd.setCursor(10, 1);
  lcd.print(weatherStation.ToStringHumidity());

  delete[] temperature;
  delete[] humidity;
}


void StartAlarm() {
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("WAKE UP");
  alarm.SwitchOn();
}
