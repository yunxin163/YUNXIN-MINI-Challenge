#include "MPU9250.h"
#include "MsTimer2.h"
#include <MadgwickAHRS.h>
Madgwick filter1;
Madgwick filter2;
MPU9250 IMU1(Wire, 0x68);
MPU9250 IMU2(Wire, 0x69);

void Send(int x);

int roll1, pitch1, yaw1;
int roll2, pitch2, yaw2;

void setup() {
  Serial.begin(115200);
  IMU1.begin();
  IMU2.begin();
  delay(100);
  //校准IMU1
  IMU1.setAccelCalZ(-2.283, 1.0);
  IMU1.setMagCalX(1.66, 1.16);
  IMU1.setMagCalY(48.38, 0.93);
  IMU1.setMagCalZ(-45.42, 0.94);
  //校准IMU2
  IMU2.setAccelCalZ(0.7875, 1.0);
  IMU2.setAccelCalX(0.237, 1.0);
  IMU2.setMagCalX(-1.98, 0.86);
  IMU2.setMagCalY(14.85, 1.04);
  IMU2.setMagCalZ(6.78, 1.14);

  MsTimer2::set(100, pr);
  MsTimer2::start();
}

void loop() {
  IMU1.readSensor();
  IMU2.readSensor();
  float ax1, ay1, az1;
  float gx1, gy1, gz1;
  float mx1, my1, mz1;
  float ax2, ay2, az2;
  float gx2, gy2, gz2;
  float mx2, my2, mz2;
  ax1 = IMU1.getAccelX_mss();
  ay1 = IMU1.getAccelY_mss();
  az1 = IMU1.getAccelZ_mss();
  gx1 = IMU1.getGyroX_rads();
  gy1 = IMU1.getGyroY_rads();
  gz1 = IMU1.getGyroZ_rads();
  mx1 = IMU1.getMagX_uT();
  my1 = IMU1.getMagY_uT();
  mz1 = IMU1.getMagZ_uT();

  ax2 = IMU2.getAccelX_mss();
  ay2 = IMU2.getAccelY_mss();
  az2 = IMU2.getAccelZ_mss();
  gx2 = IMU2.getGyroX_rads();
  gy2 = IMU2.getGyroY_rads();
  gz2 = IMU2.getGyroZ_rads();
  mx2 = IMU2.getMagX_uT();
  my2 = IMU2.getMagY_uT();
  mz2 = IMU2.getMagZ_uT();


  //filter.updateIMU(gx, gy, gz, ax, ay, az);
  filter1.update(gx1, gy1, gz1, ax1, ay1, az1, mx1, my1, mz1);
  filter2.update(gx2, gy2, gz2, ax2, ay2, az2, mx2, my2, mz2);

  roll1 = filter1.getRoll();
  pitch1 = filter1.getPitch();
  yaw1 = filter1.getYaw();

  roll2 = filter2.getRoll();
  pitch2 = filter2.getPitch();
  yaw2 = filter2.getYaw();
  //
  //    Serial.print('#');
  //    Serial.print(roll1);
  //    Serial.print(" ");
  //    Serial.print(pitch1);
  //    Serial.print(" ");
  //    Serial.println(yaw1);
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
  if (pitch1 <= 0) Send(-pitch1);
  else Send(360 - pitch1);
  if (roll1 < 0) Send(180 + roll1);
  else Send(360 - 180 + roll1);
  if (yaw1 <= 270) Send(270 - yaw1);
  else Send(360 - yaw1 + 270);


  if (pitch2 <= 0) Send(-pitch2);
  else Send(360 - pitch2);
  if (roll2 < 0) Send(180 + roll2);
  else Send(360 - 180 + roll2);
  if (yaw2 <= 270) Send(270 - yaw2);
  else Send(360 - yaw2 + 270);

}
