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
char bluetoothData = 0;

int timePassed = 0;

void setup() {
  Serial.begin(9600);
  BTSerial.begin(9600);
 
  lcd.init();
  lcd.backlight();
}



void loop() {
  delay(1000);
  clock.Update();
  DisplayState();

  if (BTSerial.available()) {
    bluetoothData = BTSerial.read();
    
    if (bluetoothData == '1') {
      lcd.setCursor(15, 0);
      lcd.print(bluetoothData);
      Serial.print(bluetoothData);  
    }
    else {
      lcd.setCursor(15, 0);
      lcd.print("0");
    }
  }
  
//  timePassed += 1;
//  if(timePassed == 10){
//    StartAlarm();
//  }
}














void DisplayState(){
  lcd.setCursor(0,0);
  lcd.print(clock.ToString());
  lcd.setCursor(0,1);
  lcd.print(weatherStation.ToString());
}

void StartAlarm(){
  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print("WAKE UP !!!");
  alarm.SwitchOn();
}
