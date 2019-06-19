void setup() {
  Serial.begin(115200);
}

void loop() {
  Serial.print("#");
  Serial.print(360);
  Serial.print('!');
  delay(100);
}
