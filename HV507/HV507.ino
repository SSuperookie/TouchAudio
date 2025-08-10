#include <SPI.h>
#include "HV507Driver.h"

int8_t pattern[][32] = {
  { -1 },
  { 0, 1, 8, 9, 16, 17, -1 },//A-1
  { 6, 7, 14, 15,  22, 23, -1 },//B-2
  { 46, 47,  54, 55,  62, 63, -1 },//C-3
  { 40, 41,  48, 49,  56, 57,  -1 },//D-4
  { 27, 28, 35, 36, -1 },//E-5
  { 0, 9, 18, 27, 36, 45, 54, 63, -1 },//F-6
  { 7, 14, 21, 28, 35, 42, 49, 56, -1 },//G-7
  { 0, 9, 18, 27, 36, 45, 54, 63, 7, 14, 21, 28, 35, 42, 49, 56, -1 },//H-8
  { 1, 9, 17, 25, 33, 41, 49, 57, -1 },//I-9
  { 8, 9, 10, 11, 12, 13, 14, 15, -1 },//J-10
  { 6, 14, 22, 30, 38, 46, 54, 62, -1 },//K-11
  { 48, 49, 50, 51, 52, 53, 54, 55, -1 }//L-12
};

uint32_t PW = 30;  //单位us，脉冲宽度
uint32_t PP = 5;    //单位ms，脉冲间隔
uint8_t PN = 2;     //(inner)，pattern内部循环的次数
uint8_t BN = 1;     //(outer),外部循环的次数
uint32_t BP = 25;   //单位ms,外部循环之间的间隔时间

volatile uint8_t currentPattern = 0;//存储当前pattern

void setup() {
  Serial.begin(9600);

  //初始化
  HV507Init();

  //全部置低，这个直接生效，不用Update
  HV507AllLow();
}

void tick() {
  uint32_t t_pw_us = micros() + PW;
  uint32_t t_pp_ms = millis() + PP;
  uint32_t t_bp_ms = millis() + BP;

  for (uint8_t bn = 0; bn < BN; bn++) {
    for (uint8_t pn = 0; pn < PN; pn++) {
      for (uint8_t i = 0; pattern[currentPattern][i] >= 0; i++) {
        HV507Write(pattern[currentPattern][i], HIGH);
      }
      // 所有电平都写完之后需要调用下面这个Update才生效
      HV507Update();
      while (micros() < t_pw_us) {}
      t_pw_us += PW;
      HV507AllLow();
      HV507SetToAllZero();
      while (millis() < t_pp_ms) {}
      t_pp_ms += PP;
    }
    while (millis() < t_bp_ms) {}
    t_bp_ms += BP;
  }
}

void loop() {//主循环函数
  if (Serial.available() > 0) {//有输入，切换pattern
    char ch = Serial.read();
    if (ch >= 'A' && ch <= 'L') {
      currentPattern = ch - 'A' + 1;
    } else {
      currentPattern = 0;
    }
    Serial.println(currentPattern);
  } else {//没有串口输入时
    tick();
  }
}
//INPUT CHARACTER A-L 
//OUTPUT 1-12
