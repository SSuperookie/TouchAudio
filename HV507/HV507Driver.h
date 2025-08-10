#ifndef _HV507_DRIVER_H_
#define _HV507_DRIVER_H_

//引脚号赋予有意义的名字
#define DIR 42
#define POL 44
#define BL  46
#define SS  53
//HV507的高压模拟开关阵列驱动器
uint8_t bytes[8] = {0, 0, 0, 0, 0, 0, 0, 0};

void HV507Init(void);
void HV507Write(uint8_t, uint8_t);
void HV507SetToAllZero(void);
void HV507Update(void);
void HV507AllLow(void);
void HV507AllHigh(void);

#endif
