#ifndef Clock_h
#define Clock_h

#include "virtuabotixRTC.h"

class Clock {
  public:
    Clock(int _rst, int _dat, int _clk);
    ~Clock();
    void SetTimeAndDate(int _seconds, int _minutes, int _hours, int _dayOfWeek, int _day, int _month, int _year);
    void Update();
    String ToString();
  private:
    int _pinRST;
    int _pinDAT;
    int _pinCLK;
    virtuabotixRTC* _RTC;
};

#endif
