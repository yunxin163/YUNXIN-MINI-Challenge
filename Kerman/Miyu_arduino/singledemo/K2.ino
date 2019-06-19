#include "MPU9250.h"
#include "MsTimer2.h"
#include <MadgwickAHRS.h>

Madgwick filter;
MPU9250 IMU(Wire, 0x68);
//MPU9250 IMU(Wire, 0x69);
void Send(int x);
int roll, pitch, yaw;

void setup() {
  Serial.begin(115200);
  IMU.begin();
  delay(100);
  IMU.setAccelCalZ(-2.283, 1.0);
  IMU.setMagCalX(1.66, 1.16);
  IMU.setMagCalY(48.38, 0.93);
  IMU.setMagCalZ(-45.42, 0.94);

  MsTimer2::set(50, pr);
  MsTimer2::start();
}

void loop() {
  IMU.readSensor();
  float ax, ay, az;
  float gx, gy, gz;
  float mx, my, mz;
  ax = IMU.getAccelX_mss();
  ay = IMU.getAccelY_mss();
  az = IMU.getAccelZ_mss();
  gx = IMU.getGyroX_rads();
  gy = IMU.getGyroY_rads();
  gz = IMU.getGyroZ_rads();
  mx = IMU.getMagX_uT();
  my = IMU.getMagY_uT();
  mz = IMU.getMagZ_uT();


  //filter.updateIMU(gx, gy, gz, ax, ay, az);
  filter.update(gx, gy, gz, ax, ay, az, mx, my, mz);

  roll = filter.getRoll();
  pitch = filter.getPitch();
  yaw = filter.getYaw();

  //  Serial.print('#');
  //  Serial.print(roll);
  //  Serial.print(" ");
  //  Serial.print(pitch);
  //  Serial.print(" ");
  //  Serial.println(yaw);
}

void Send(int x) {
  int t1, t2, t3;
  t1 = x % 10;
  x /= 10;
  t2 = x % 10;
  x /= 10;
  t3 = x % 10;
  Serial.print(t3);
  Serial.print(t2);
  Serial.print(t1);
}

void pr() {
  Serial.print('#');
  if (pitch <= 0) Send(-pitch);
  else Send(360 - pitch);
  if (roll < 0) Send(180 + roll);
  else Send(360 - 180 + roll);
  if (yaw <= 270)Send(270 - yaw);
  else Send(360 - yaw + 270);
}
