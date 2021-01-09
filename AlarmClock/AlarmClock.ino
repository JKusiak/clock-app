#include "Weather.h"
#include "Alarm.h"
#include "clock.h"
#include "bluetooth.h"
#include <Wire.h>
#include <LiquidCrystal_I2C.h>

LiquidCrystal_I2C lcd(0x27, 16, 2);
Weather weatherStation(2);
Clock clock(8, 7, 6);
Alarm alarm(10, 3, 9);
BTHC05 bthc05(11, 12);

char* hour;
char* minute;
char* second;

char* temperature;
char* humidity;

const byte interruptPin = 3;
String alarmDataString = " 1 ";
String currentTime = "don't start";
char rc;
char toSend[8];



void setup() {
//  attachInterrupt(digitalPinToInterrupt(interruptPin), StopAlarm, CHANGE); 

  Serial.begin(9600);
  bthc05.begin(9600);
  
  lcd.init();
  lcd.backlight();
  //  clock.SetTimeAndDate(sec, min, h, weekday, day, month, year);
}


void loop() {
  interrupts();
  clock.Update();
  
  DisplayTime();
  DisplayWeather();

  
  if (bthc05.available()) {
    alarmDataString = "";
    while (bthc05.available() > 0) {
      rc = bthc05.read();
      alarmDataString += rc;
      Serial.write(rc);
    }
  }
  
  lcd.setCursor(0, 0);
  lcd.print(alarmDataString);

  lcd.setCursor(0, 1);
  lcd.print(currentTime);
  
  
  if (currentTime == alarmDataString) {
      StartAlarm();
  }
}


void DisplayTime() {
  hour = clock.getHour();
  minute = clock.getMin();
  second = clock.getSec();
  
  bthc05.write('a');
  bthc05.write(hour[0]);
  bthc05.write(hour[1]);
  bthc05.write(':');
  bthc05.write(minute[0]);
  bthc05.write(minute[1]);
  bthc05.write(':');
  bthc05.write(second[0]);
  bthc05.write(second[1]);

//  lcd.setCursor(0, 0);
//  lcd.print(clock.ToStringTime());
//  lcd.setCursor(0, 1);
//  lcd.print(clock.ToStringDate());

  currentTime = clock.ToStringAlarm();
  
  delete[] hour;
  delete[] minute;
  delete[] second;
}


void DisplayWeather() {
  temperature = weatherStation.getTemperature();
  humidity = weatherStation.getHumidity();

  bthc05.write(temperature[0]);
  bthc05.write(temperature[1]);
  bthc05.write(temperature[2]);
  bthc05.write(humidity[0]);
  bthc05.write(humidity[1]);

//  lcd.setCursor(10, 0);
//  lcd.print(weatherStation.ToStringTemperature());
//
//  lcd.setCursor(10, 1);
//  lcd.print(weatherStation.ToStringHumidity());

  delete[] temperature;
  delete[] humidity;
}

void StartAlarm() {
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("WAKE UP !!!");
  alarm.SwitchOn();
}
