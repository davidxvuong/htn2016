#include <SoftwareSerial.h>
#define FAN_1 '1'
#define FAN_2_PELTER '2'
#define HUMIDIFIER '3'
#define LIGHT '4'
#define FAN_1_PIN 2
#define FAN_2_PIN 3
#define HUMIDIFIER_PIN 4
#define LIGHT_PIN 5

char op; // stores incoming character from other device
bool fan_1_toggle = false;
bool fan_2_toggle = false;
bool humidifier_toggle = false;
bool light_toggle = false;

SoftwareSerial BT(10, 11); 
// creates a "virtual" serial port/UART
// connect BT module TX to D10
// connect BT module RX to D11
// connect BT Vcc to 5V, GND to GND


void setup()   {
  // set the data rate for the SoftwareSerial port
  BT.begin(9600);
  Serial.begin(9600);
  pinMode(FAN_1_PIN, OUTPUT);
  pinMode(FAN_2_PIN, OUTPUT);
  pinMode(HUMIDIFIER_PIN, OUTPUT);
  pinMode(LIGHT_PIN, OUTPUT);
}

void loop() {
  if (BT.available()) {
    op = BT.read();
    Serial.println(op);
    if (op == '1') {
      if (fan_1_toggle == false) {
        fan_1_toggle = true;
        
        Serial.println("fan 1 on");
        pinMode(FAN_1_PIN, OUTPUT);
        digitalWrite(FAN_1_PIN, HIGH);
      }
      else {
        fan_1_toggle = false;
        Serial.println("fan 1 off");
        digitalWrite(FAN_1_PIN, LOW);
        pinMode(FAN_1_PIN, INPUT);
        
      }
    }
    else if (op == '2') {
      if (fan_2_toggle == false) {
        Serial.println("fan 2 on");
        
        fan_2_toggle = true;
        digitalWrite(FAN_2_PIN, HIGH);
      }
      else {
        Serial.println("fan 2 off");
        
        fan_2_toggle= false;
        digitalWrite(FAN_2_PIN, LOW);
      }
    }
    else if (op == '3') {

      Serial.println("humidifier on");
      digitalWrite(HUMIDIFIER_PIN, HIGH);
      delay(1000);
      Serial.println("humidifier off");
      digitalWrite(HUMIDIFIER_PIN, LOW);
      delay(1000);
    }
    else if (op == '4') {
      if (light_toggle == false) {
        Serial.println("light on");
        
        light_toggle = true;
        digitalWrite(LIGHT_PIN, HIGH);
      }
      else {
        Serial.println("light off");
        
        light_toggle= false;
        digitalWrite(LIGHT_PIN, LOW);
      }
    }
  }

}
