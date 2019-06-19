#include "MPU9250.h"
//MPU9250 IMU(Wire, 0x68);
MPU9250 IMU(Wire, 0x69);

void setup() {
  Serial.begin(115200);
  IMU.begin();
  delay(100);


  float max = 0.0;
  float min = 0.0;
  for (int i = 0; i < 10000; ++i) {
    IMU.readSensor();
    float t = IMU.getAccelX_mss();
    if (t < min) min = t;
    if (t > max) max = t;
  }
  Serial.print(max);
  Serial.print(" ");
  Serial.print(min);
  Serial.print(" ");
  Serial.print((min + max) / 2);

  max = 0.0;
  min = 0.0;
  for (int i = 0; i < 10000; ++i) {
    IMU.readSensor();
    float t = IMU.getAccelY_mss();
    if (t < min) min = t;
    if (t > max) max = t;
  }
  Serial.print(max);
  Serial.print(" ");
  Serial.print(min);
  Serial.print(" ");
  Serial.print((min + max) / 2);

  max = 0.0;
  min = 0.0;
  for (int i = 0; i < 10000; ++i) {
    IMU.readSensor();
    float t = IMU.getAccelZ_mss();
    if (t < min) min = t;
    if (t > max) max = t;
  }
  Serial.print(max);
  Serial.print(" ");
  Serial.print(min);
  Serial.print(" ");
  Serial.print((min + max) / 2);

  
  IMU.calibrateMag();
  float hxb;
  hxb = IMU.getMagBiasX_uT();
  float hxs;
  hxs = IMU.getMagScaleFactorX();

  float hyb;
  hyb = IMU.getMagBiasY_uT();
  float hys;
  hys = IMU.getMagScaleFactorY();

  float hzb;
  hzb = IMU.getMagBiasZ_uT();
  float hzs;
  hzs = IMU.getMagScaleFactorZ();

  Serial.print(hxb);
  Serial.print(" ");
  Serial.println(hxs);

  Serial.print(hyb);
  Serial.print(" ");
  Serial.println(hys);

  Serial.print(hzb);
  Serial.print(" ");
  Serial.println(hzs);
}

void loop() {
  
}
