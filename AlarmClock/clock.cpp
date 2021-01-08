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

String Clock::ToString(){
    return String(_RTC->hours) + ':' + String(_RTC->minutes);
}
