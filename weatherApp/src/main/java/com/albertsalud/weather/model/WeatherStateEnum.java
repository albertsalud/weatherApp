package com.albertsalud.weather.model;

import lombok.Getter;

@Getter
public enum WeatherStateEnum {
	
	SUNNY(1, 65), CLOUDY(66, 85), RAINY(86, 94), LIGHTING(95, 100);
	
	private int minPercentage;
	private int maxPercentage;
	
	private WeatherStateEnum(int minPercentage, int maxPercentage) {
		this.minPercentage = minPercentage;
		this.maxPercentage = maxPercentage;
	}

}
