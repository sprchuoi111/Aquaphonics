#ifndef _LDR_H
#define _LDR_H
#include "stm32f1xx_hal.h"
typedef struct{ 
	uint16_t AD_RES; 
	uint16_t Vamb;
	uint16_t DC_Multiplier;
} LDR_DataTypedef;

void LDR_Init(ADC_HandleTypeDef *hadc1);
void LDR_GetData(ADC_HandleTypeDef *hadc1, LDR_DataTypedef *LRD_Data); 

#endif /*_ LDR_H*/ 