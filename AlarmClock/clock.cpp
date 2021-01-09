#include "clock.h"

Clock::Clock(int _rst, int _dat, int _clk) {
  _pinRST = _rst;
  _pinDAT = _dat;
  _pinCLK = _clk;
  _RTC = new virtuabotixRTC(_pinCLK, _pinDAT, _pinRST);
  _RTC->_DS1302_start();
}

Clock::~Clock() {
  delete _RTC;
}

void Clock::Update(){
  _RTC->updateTime();
}

void Clock::SetTimeAndDate(int _seconds, int _minutes, int _hours, int _dayOfWeek, int _day, int _month, int _year){
  _RTC->setDS1302Time(_seconds, _minutes, _hours, _dayOfWeek, _day, _month, _year);
}

String Clock::ToStringAlarm() {
   return String(_RTC->hours) + ":" + String(_RTC->minutes);
}

String Clock::ToStringTime() {
  return String(_RTC->hours) + ":" + String(_RTC->minutes) + ":" + String(_RTC->seconds);
}

String Clock::ToStringDate() {
  return String(_RTC->dayofmonth) + "/" + String(_RTC->month) + "/" + String(_RTC->year);
}

char* Clock::getHour() {
   int hour = _RTC->hours;
   char* toReturn = new char[2];
   if (hour < 10) {
    toReturn[0] = 48;
    toReturn[1] = hour + 48;
   } else {
    int tens = hour/10;
    int units = hour%10;
    toReturn[0] = tens + 48;
    toReturn[1] = units + 48;
   }
   return toReturn;
}

char* Clock::getMin() {
   int minute = _RTC->minutes;
   char* toReturn = new char[2];
   if (minute < 10) {
    toReturn[0] = 48;
    toReturn[1] = minute + 48;
   } else {
    int tens = minute/10;
    int units = minute%10;
    toReturn[0] = tens + 48;
    toReturn[1] = units + 48;
   }
   return toReturn;
}

char* Clock::getSec() {
   int sec = _RTC->seconds;
   char* toReturn = new char[2];
   if (sec < 10) {
    toReturn[0] = 48;
    toReturn[1] = sec + 48;
   } else {
    int tens = sec/10;
    int units = sec%10;
    toReturn[0] = tens + 48;
    toReturn[1] = units + 48;
   }
   return toReturn;
}
