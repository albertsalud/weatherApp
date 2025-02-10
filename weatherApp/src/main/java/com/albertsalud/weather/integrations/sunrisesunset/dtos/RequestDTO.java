package com.albertsalud.weather.integrations.sunrisesunset.dtos;

import lombok.Data;

@Data
public class RequestDTO {
	
	private Double latitude = 41.30605d;
	private Double longitude = 2.00123d;
	private String timeFormat = "24";

}
