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
char bluetoothData = '0';

int timePassed = 0;

void setup() {
  Serial.begin(9600);
  lcd.init();
  lcd.backlight();
  //clock.SetTimeAndDate(0,24,20,7,24,05,2020);
}

void loop() {

  if (Serial.available()){
    bluetoothData = Serial.read();
  }

  delay(1000);
  clock.Update();

  DisplayState();
  timePassed += 1;
  if(timePassed == 10){
    StartAlarm();
  }
}

void DisplayState(){
  lcd.setCursor(0,0);
  lcd.print(clock.ToString());
  lcd.setCursor(0, 15);
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
