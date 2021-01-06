#include "Weather.h"
#include "Arduino.h"

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

String Weather::ToString(){
  return String(GetHumidity()) + "% " + String(GetTemperature()) + " C";
}
