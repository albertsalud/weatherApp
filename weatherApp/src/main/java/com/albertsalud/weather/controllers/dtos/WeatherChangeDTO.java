package com.albertsalud.weather.controllers.dtos;

import com.albertsalud.weather.model.WeatherStateEnum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class WeatherChangeDTO {

	private WeatherStateEnum nextWeatherState;
	private WeatherStateEnum newWeatherState;
	private boolean day;
}
