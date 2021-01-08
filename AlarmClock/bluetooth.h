#ifndef Bluetooth_h
#define Bluetooth_h

#include <SoftwareSerial.h>


class BTHC05 : public SoftwareSerial
{
  public:
    BTHC05(int pinRx, int pinTx);


};
#endif  //_BTHC05_H
