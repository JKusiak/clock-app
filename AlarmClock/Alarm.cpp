#include "Alarm.h"
#include <Arduino.h>

int melody[] = {
  N_A3, 0, N_GS3, N_G3, N_F3, N_D3, N_F3, N_G3, N_AS2, N_AS2, N_D4, N_A3, 0, N_GS3, N_G3, N_F3, N_D3, N_F3, N_G3, N_D3, N_D3, N_D4, N_A3, 0, N_GS3, N_G3, N_F3, N_D3, N_F3, N_G3, N_C3, N_C3, N_D4, N_A3, 0, N_GS3, N_G3, N_F3, N_D3, N_F3,
};

char noteDurations[320] = {
  6, 32, 8, 8, 8, 16, 16, 16, 16, 16, 8, 6, 32, 8, 8, 8, 16, 16, 16, 16, 16, 8, 6, 32, 8, 8, 8, 16, 16, 16, 16, 16, 8, 6, 32, 8, 8, 8, 16, 16,
};


Alarm::Alarm(int buzzerPin, int interruptPin, int diodePin) {
  _buzzerPin = buzzerPin;
  _interruptPin = interruptPin;
  _diodePin = diodePin;
  pinMode(_buzzerPin, OUTPUT);
  pinMode(_diodePin, OUTPUT);
  pinMode(_interruptPin, INPUT_PULLUP);
}

void Alarm::SwitchOn(){
  int melody_len = sizeof(melody)/sizeof(melody[0]);
  int brightness = 0;
  int fadeAmount = 4;
  
  for (int thisNote = 0; thisNote < melody_len; thisNote++) {
    int noteDuration = TEMPO / (int)noteDurations[thisNote];
    tone(_buzzerPin, melody[thisNote], noteDuration);
    int pauseBetweenNotes = noteDuration * 1.45;
    delay(pauseBetweenNotes);
    noTone(8);

    // diode intensity
    analogWrite(_diodePin, brightness);
    brightness = brightness + fadeAmount;
    if (brightness <= 0 || brightness >= 140) {
      fadeAmount = -fadeAmount;
    }

    // button interrupt
    if(!digitalRead(_interruptPin)){
      analogWrite(_diodePin, 0);
      break;
    };
  }
}

void Alarm::SwitchOff() {
  analogWrite(_diodePin, 0);
}
