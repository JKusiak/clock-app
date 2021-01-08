#include "Weather.h"
#include "Alarm.h"
#include "clock.h"
#include "bluetooth.h"
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <AltSoftSerial.h>

LiquidCrystal_I2C lcd(0x27, 16, 2);
Weather weatherStation(2);
Clock clock(8, 7, 6);
Alarm alarm(10, 1, 9);
BTHC05 bthc05(11, 12);
String bluetoothData = " ";

void setup() {
  Serial.begin(9600);
  while (!Serial) ; // wait for serial port to connect. Needed for native USB
  Serial.println("start");
  
  bthc05.begin(9600);
  
  lcd.init();
  lcd.backlight();
  //  clock.SetTimeAndDate(sec, min, h, weekday, day, month, year);
}



void loop() {
  delay(1000);
  clock.Update();
  DisplayState();

  lcd.setCursor(15, 0);
  lcd.print("0");

  if (bthc05.available()) {
    bluetoothData = bthc05.read();
    Serial.print(bluetoothData);

    if (bluetoothData == clock.ToString()) {
      alarm.SwitchOn();
    }
  }
}














void DisplayState() {
  lcd.setCursor(0, 0);
  lcd.print(clock.ToString());
  lcd.setCursor(0, 1);
  lcd.print(weatherStation.ToString());
}

void StartAlarm() {
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("WAKE UP !!!");
  alarm.SwitchOn();
}
