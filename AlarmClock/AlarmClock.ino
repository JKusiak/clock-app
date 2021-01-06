#include "Weather.h"
#include "Alarm.h"
#include "clock.h"
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <SoftwareSerial.h>

LiquidCrystal_I2C lcd(0x27, 16, 2);
Weather weatherStation(2);
Clock clock(8, 7, 6);
Alarm alarm(10, 1, 9);
SoftwareSerial BTSerial(5,4); // RX | TX
char bluetoothData = '0';

int timePassed = 0;

void setup() {
  Serial.begin(9600);
  
  BTSerial.begin(38400); //HC-05 default speed in AT mode
  lcd.init();
  lcd.backlight();
  //clock.SetTimeAndDate(0,24,20,7,24,05,2020);
}

void loop() {
  delay(1000);
  clock.Update();
  
  if (Serial.available()){
    bluetoothData = Serial.read();
  }
  
  DisplayState();

  if (BTSerial.available())    // read from HC-05 and send to Arduino Serial Monitor
  Serial.write(BTSerial.read());

  if (Serial.available())     // Keep reading from Arduino Serial Monitor and send to HC-05
  BTSerial.write(Serial.read());
  
//  timePassed += 1;
//  if(timePassed == 10){
//    StartAlarm();
//  }
}

void DisplayState(){
  lcd.setCursor(0,0);
  lcd.print(clock.ToString());
  lcd.setCursor(15, 0);
  lcd.print(bluetoothData);
  lcd.setCursor(0,1);
  lcd.print(weatherStation.ToString());
}

void StartAlarm(){
  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print("WAKE UP !!!");
  alarm.SwitchOn();
}
