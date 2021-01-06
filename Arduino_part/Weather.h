#ifndef Weather_h
#define Weather_h

#include "DHT.h"

class Weather {
  public:
    Weather(int pin);
    ~Weather();
    float GetTemperature();
    float GetHumidity();
    String ToString();
  private:
    int _pin;
    DHT* _dht;
};

#endif
