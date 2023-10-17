#include "LDR.h" 

/*------------ author Binh Nguyen ----------------*/ 
void LDR_Init(ADC_HandleTypeDef *hadc1)
{
	// Calibrate The ADC On Power-Up For Better Accuracy
    HAL_ADCEx_Calibration_Start(hadc1);
	// & Calculate The DutyCycle Multiplier
    HAL_ADC_Start(hadc1);
	// Read The Sensor Once To Get The Ambient Level
    // & Calculate The DutyCycle Multiplier

}
/* High level */

void LDR_GetData(ADC_HandleTypeDef *hadc1 , LDR_DataTypedef *LRD_Data)
{
  // Start ADC conversation 
    HAL_ADC_Start(hadc1);
  // Pool ADC1 Peripheral & Timeout =1ms
    HAL_ADC_PollForConversion(hadc1, 1);
 // Read The ADC Conversion Result & Map It To PWM DutyCycle
    LRD_Data->AD_RES = HAL_ADC_GetValue(hadc1);
    TIM2->CCR1 = (LRD_Data->AD_RES- LRD_Data->Vamb)*LRD_Data->DC_Multiplier;
    HAL_Delay(1);
	
}