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
String bluetoothData;
int wait = 0;
char toSend[8];



void setup() {
  attachInterrupt(digitalPinToInterrupt(interruptPin), StopAlarm, CHANGE); 
  
  Serial.begin(9600);
  while (!Serial) ; // wait for serial port to connect. Needed for native USB
  Serial.println("start");
  
  bthc05.begin(9600);
  
  lcd.init();
  lcd.backlight();
  //  clock.SetTimeAndDate(sec, min, h, weekday, day, month, year);
}



void loop() {
  interrupts();
  clock.Update();
  bthc05.write('a');
  DisplayTime();
  DisplayWeather();


if (bthc05.available()) {
    bluetoothData = bthc05.read();
    Serial.print(bluetoothData);
    
//    lcd.setCursor(14, 0);
//    lcd.print(bluetoothData);
//
//    if (bluetoothData == "49") {
//      lcd.setCursor(14, 0);
//      lcd.print("1");
//    }
//
//    if (bluetoothData == "48") {
//      alarm.SwitchOff();  
//    }
  }
  
//  wait++;

//  if (wait == 10) {
//    StartAlarm();
//  }
  
 
  
}


void DisplayTime() {
  hour = clock.getHour();
  minute = clock.getMin();
  second = clock.getSec();
  
  bthc05.write(hour[0]);
  bthc05.write(hour[1]);
  bthc05.write(':');
  bthc05.write(minute[0]);
  bthc05.write(minute[1]);
  bthc05.write(':');
  bthc05.write(second[0]);
  bthc05.write(second[1]);

  lcd.setCursor(0, 0);
  lcd.print(hour[0]);
  lcd.setCursor(1, 0);
  lcd.print(hour[1]);
  lcd.setCursor(2, 0);
  lcd.print(':');
  lcd.setCursor(3, 0);
  lcd.print(minute[0]);
  lcd.setCursor(4, 0);
  lcd.print(minute[1]);
  lcd.setCursor(5, 0);
  lcd.print(':');
  lcd.setCursor(6, 0);
  lcd.print(second[0]);
  lcd.setCursor(7, 0);
  lcd.print(second[1]);

  delete[] hour;
  delete[] minute;
  delete[] second;
}

void DisplayWeather() {
  lcd.setCursor(0, 1);
  //lcd.print(weatherStation.ToString());

  temperature = weatherStation.getTemperature();
  humidity = weatherStation.getHumidity();

  bthc05.write(temperature[0]);
  bthc05.write(temperature[1]);
  bthc05.write(temperature[2]);

  bthc05.write(humidity[0]);
  bthc05.write(humidity[1]);



  lcd.setCursor(0, 1);
  lcd.print(temperature[0]);
  lcd.setCursor(1, 1);
  lcd.print(temperature[1]);
  lcd.setCursor(2, 1);
  lcd.print(temperature[2]);
  lcd.setCursor(3, 1);
  lcd.print(' ');
  lcd.setCursor(4, 1);
  lcd.print(humidity[0]);
  lcd.setCursor(5, 1);
  lcd.print(humidity[1]);
  
    


  delete[] temperature;
  delete[] humidity;
}

void StartAlarm() {
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("WAKE UP !!!");
  alarm.SwitchOn();
}

void StopAlarm() {
  alarm.SwitchOff();
}
