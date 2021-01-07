#include "Weather.h"
#include "Alarm.h"
#include "clock.h"
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <AltSoftSerial.h>

LiquidCrystal_I2C lcd(0x27, 16, 2);
Weather weatherStation(2);
Clock clock(8, 7, 6);
Alarm alarm(10, 1, 9);
AltSoftSerial BTSerial(11, 12); // RX | TX
char bluetoothData = '0';

int timePassed = 0;

void setup() {
  Serial.begin(9600);
  Serial.println("Arduino is connected to 9600 baud");
  BTSerial.begin(38400); //HC-05 default baud rate for AT mode

  lcd.init();
  lcd.backlight();
//  clock.SetTimeAndDate(0,24,20,7,24,05,2020);
}

void loop() {
  delay(1000);
  clock.Update();
//  if (BTSerial.available()) {
//    Serial.println("Module HC-05 is connected at 38400 baud");
//  }
  DisplayState();
//
//  if (BTSerial.available()) {   // read from HC-05 and send to Arduino Serial Monitor
//    Serial.write(BTSerial.read());
//  }
//  
//  if (Serial.available()) {   // read from Arduino Serial Monitor and send to HC-05
//    BTSerial.write(Serial.read());
//  }
  
//  timePassed += 1;
//  if(timePassed == 10){
//    StartAlarm();
//  }
}

void DisplayState(){
  lcd.setCursor(0,0);
  lcd.print(clock.ToString());
  lcd.setCursor(15, 0);
//  lcd.print(bluetoothData);
  lcd.setCursor(0,1);
  lcd.print(weatherStation.ToString());
}

void StartAlarm(){
  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print("WAKE UP !!!");
  alarm.SwitchOn();
}
