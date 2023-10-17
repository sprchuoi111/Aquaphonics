
#ifndef __DHT_H
#define __DHT_H

#include "stm32f1xx_hal.h"
#define DHT11_STARTTIME 18000
#define DHT22_STARTTIME 12000
#define DHT11 0x01
#define DHT22 0x02
typedef struct
{	
	uint16_t Type;
	uint16_t Pin;
	TIM_HandleTypeDef* Timer;
	GPIO_TypeDef* PORT;
	uint8_t Temp1, Temp2, RH1, RH2;
	float Temp;
	float Humi;
}DHT_Name;

void DHT_Init(DHT_Name* DHT, uint8_t DHT_Type, TIM_HandleTypeDef* Timer, GPIO_TypeDef* DH_PORT, uint16_t DH_Pin);
uint8_t DHT_ReadTempHum(DHT_Name* DHT);
#endif

