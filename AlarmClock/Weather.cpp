#include "Weather.h"


Weather::Weather(int pin) {
  _pin = pin;
  pinMode(_pin, OUTPUT);
  _dht = new DHT(_pin, DHT11);
  _dht->begin();
}

Weather::~Weather() {
  delete _dht;
}

float Weather::GetTemperature() {
  float t = _dht->readTemperature();
  if (isnan(t)) {
    Serial.println(F("Failed to read temperature from DHT sensor!"));
    return 0.0;
  }
  return t;
}

float Weather::GetHumidity() {
  float h = _dht->readHumidity();
  if (isnan(h)) {
    Serial.println(F("Failed to read humidity from DHT sensor!"));
    return 0.0;
  }
  return h;
}

String Weather::ToStringTemperature(){
  return String(GetTemperature()) + "C";
}

String Weather::ToStringHumidity(){
  return String(GetHumidity()) + "% ";
}

char* Weather::getTemperature(){
  int temp = GetTemperature();
  char* toReturn = new char[3];
  
  if (temp < 0) {
    toReturn[0] = 45;
   } else {
    toReturn[0] = 32;
  }

  int absTemp = abs(temp);
   
  if (absTemp < 10) {
    toReturn[1] = 48;
    toReturn[2] = temp + 48;
   } else {
    int tens = absTemp/10;
    int units = absTemp%10;
    toReturn[1] = tens + 48;
    toReturn[2] = units + 48;
  }
   return toReturn;
}

char* Weather::getHumidity(){
  int weather = GetHumidity();
  char* toReturn = new char[2];
   if (weather < 10) {
    toReturn[0] = 48;
    toReturn[1] = weather + 48;
   } else {
    int tens = weather/10;
    int units = weather%10;
    toReturn[0] = tens + 48;
    toReturn[1] = units + 48;
   }
   return toReturn;
  }
