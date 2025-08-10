#include "HV507Driver.h"

void HV507Init(void) {
  pinMode(DIR, OUTPUT);
  pinMode(POL, OUTPUT);
  pinMode(BL, OUTPUT);
  SPI.begin();//SPI通信
  //引脚设置为输出模式

  digitalWrite(DIR, LOW);//向输出方向写入数据
  digitalWrite(POL, HIGH);//正极性输出
  digitalWrite(BL, HIGH);//使能输出
  digitalWrite(SS, HIGH);//禁用SPI通信

  SPI.beginTransaction(SPISettings(4000000, LSBFIRST, SPI_MODE0));
  //时钟频率为4MHz、数据传输顺序为最低有效位（LSB）、SPI模式为0
  HV507Update();
};

void HV507Write(uint8_t pin, uint8_t voltage) {
  uint8_t arr_idx, bit_idx;
  //pin ^= 0x1F;
  bit_idx = pin & 0x07;
  arr_idx = pin >> 3;
  if (voltage == HIGH)
  {
    bytes[arr_idx] |= (1 << bit_idx);
  }
  else
  {
    bytes[arr_idx] &= (~(1 << bit_idx));
  }
};

void HV507Update(void) {
  digitalWrite(SS, LOW);
  SPI.transfer(bytes[3]);
  SPI.transfer(bytes[2]);
  SPI.transfer(bytes[1]);
  SPI.transfer(bytes[0]);
  SPI.transfer(bytes[7]);
  SPI.transfer(bytes[6]);
  SPI.transfer(bytes[5]);
  SPI.transfer(bytes[4]);
  digitalWrite(SS, HIGH);
};

void HV507SetToAllZero(void){
  bytes[0] = 0;
  bytes[1] = 0;
  bytes[2] = 0;
  bytes[3] = 0;
  bytes[4] = 0;
  bytes[5] = 0;
  bytes[6] = 0;
  bytes[7] = 0;
}

void HV507AllLow(void){  
  digitalWrite(SS, LOW);
  SPI.transfer((byte)0);
  SPI.transfer((byte)0);
  SPI.transfer((byte)0);
  SPI.transfer((byte)0);
  SPI.transfer((byte)0);
  SPI.transfer((byte)0);
  SPI.transfer((byte)0);
  SPI.transfer((byte)0);  
  digitalWrite(SS, HIGH);
}

void HV507AllHigh(void){  
  digitalWrite(SS, LOW);
  SPI.transfer((byte)0xFF);
  SPI.transfer((byte)0xFF);
  SPI.transfer((byte)0xFF);
  SPI.transfer((byte)0xFF);
  SPI.transfer((byte)0xFF);
  SPI.transfer((byte)0xFF);
  SPI.transfer((byte)0xFF);
  SPI.transfer((byte)0xFF);  
  digitalWrite(SS, HIGH);
}
