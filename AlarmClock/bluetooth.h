#ifndef Bluetooth_h
#define Bluetooth_h

#include <SoftwareSerial.h>


class BluetoothHC05 : public SoftwareSerial
{
  public:
    BluetoothHC05(int pinRx, int pinTx);


};
#endif  //_BTHC05_H
