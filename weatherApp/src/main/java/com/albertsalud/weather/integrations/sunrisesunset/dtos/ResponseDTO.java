package com.albertsalud.weather.integrations.sunrisesunset.dtos;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResponseDTO {
	
	private TimesDTO results;
	private String status;

}
