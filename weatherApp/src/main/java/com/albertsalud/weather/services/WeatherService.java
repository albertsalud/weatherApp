package com.albertsalud.weather.services;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsalud.weather.controllers.dtos.WeatherChangeDTO;
import com.albertsalud.weather.integrations.sunrisesunset.SunriseSunsetManager;
import com.albertsalud.weather.model.WeatherStateEnum;
import com.albertsalud.weather.utils.SseEmitterManager;
import com.albertsalud.weather.utils.WeatherStateManager;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherService {
	
	private static final int EVERY_MINUTE = 60000;
	
	private final WeatherStateManager stateManager;
	private final SseEmitterManager emitterManager;
	private final SunriseSunsetManager sunriseSunsetManager;
	
	private Date timeToNextChange;
	
	public WeatherService(WeatherStateManager stateManager,
			SseEmitterManager emitterManager,
			SunriseSunsetManager sunriseSunsetManager) {
		this.stateManager = stateManager;
		this.emitterManager = emitterManager;
		this.sunriseSunsetManager = sunriseSunsetManager;
		
		setTimeToNextChange();
	}
		
	
	@Scheduled(fixedRate = EVERY_MINUTE)
	public void changeWeather() throws IOException {
		WeatherStateEnum nextWeatherState = this.stateManager.getNextState();
		WeatherStateEnum newWeatherState = this.stateManager.addState();
		
		emitterManager.send(new WeatherChangeDTO(nextWeatherState, newWeatherState, sunriseSunsetManager.isDayYet()));
		
		setTimeToNextChange();
	}
	
	private void setTimeToNextChange() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MILLISECOND, EVERY_MINUTE);
		
		timeToNextChange = calendar.getTime();
		log.info("now: {}, time to next change: {}", new Date(), timeToNextChange);
		
	}
	
	public int getTimeToNextChange() {
		Date now = new Date();
		
		int diffInSeconds = (int) Math.abs(timeToNextChange.getTime() - now.getTime()) / 1000;
		log.info("time to next change (seconds): {}", diffInSeconds);
		
		return diffInSeconds;
	}

	public WeatherStateEnum getCurrentWeatherState() {
		return this.stateManager.getCurrentState();
	}
	
	public Iterator<WeatherStateEnum> getQueuedWeatherStates() {
		return this.stateManager.getQueueValues();
	}
	
}
