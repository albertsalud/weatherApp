package com.albertsalud.weather.exceptions;

public class WeatherStateException extends RuntimeException {

	private static final long serialVersionUID = 1665790020546615668L;
	
	public WeatherStateException(int percent) {
		super("Unable to find a weather state with percent " + percent);
	}

}
