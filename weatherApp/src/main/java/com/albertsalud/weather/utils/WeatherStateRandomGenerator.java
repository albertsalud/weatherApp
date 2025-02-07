package com.albertsalud.weather.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.albertsalud.weather.exceptions.WeatherStateException;
import com.albertsalud.weather.model.WeatherStateEnum;

@Component
public class WeatherStateRandomGenerator {
	
	public WeatherStateEnum generateWeatherState() {
		int percent = new Random().nextInt(1, 100);
		
		for (WeatherStateEnum currentWeatherState : WeatherStateEnum.values()) {
			if(percent >= currentWeatherState.getMinPercentage() &&
					percent <= currentWeatherState.getMaxPercentage()) {
				return currentWeatherState;
			}
		}
		
		throw new WeatherStateException(percent);
	}

}
