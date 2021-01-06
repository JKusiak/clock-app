#ifndef Alarm_h
#define Alarm_h

#include "pitches.h"

class Alarm {
  public:
    Alarm(int buzzerPin, int interruptPin, int diodePin);
    void SwitchOn();
  private:
    int _buzzerPin;
    int _interruptPin;
    int _diodePin;
    int const TEMPO = 1200;
};

#endif
