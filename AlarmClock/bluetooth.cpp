#include "Bluetooth.h"
#include <arduino.h>

BluetoothHC05::BluetoothHC05(int pinRx, int pinTx) : SoftwareSerial(pinRx, pinTx)
{
  pinMode(pinRx, INPUT);
  pinMode(pinTx, OUTPUT);
  begin(9600);
}
