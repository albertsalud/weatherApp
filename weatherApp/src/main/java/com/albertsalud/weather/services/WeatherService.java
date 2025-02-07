package com.albertsalud.weather.services;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsalud.weather.controllers.dtos.WeatherChangeDTO;
import com.albertsalud.weather.model.WeatherStateEnum;
import com.albertsalud.weather.utils.SseEmitterManager;
import com.albertsalud.weather.utils.WeatherStateManager;
import com.albertsalud.weather.utils.WeatherStateRandomGenerator;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherService {
	private static final int QUEUE_CAPACITY = 3;
	private static final int DAY_START_HOUR = 7;
	private static final int DAY_END_HOUR = 21;
	private static final int EVERY_MINUTE = 60000;
	
	private final WeatherStateRandomGenerator weatherGenerator;
	private final WeatherStateManager stateManager;
	private final SseEmitterManager emitterManager;
	
	private Date timeToNextChange;
	
	public WeatherService(WeatherStateRandomGenerator weatherGenerator,
			WeatherStateManager stateManager,
			SseEmitterManager emitterManager) {
		this.weatherGenerator = weatherGenerator;
		this.stateManager = stateManager;
		this.emitterManager = emitterManager;
		
		setTimeToNextChange();
	}
		
	@PostConstruct
	public void initQueue() {
		for(int i = 0; i <= QUEUE_CAPACITY; i++) {
			WeatherStateEnum newWeatherState = this.weatherGenerator.generateWeatherState();
			this.stateManager.addState(newWeatherState);
		}
		this.stateManager.getNextState();
	}
	
	@Scheduled(fixedRate = EVERY_MINUTE)
	public void changeWeather() throws IOException {
		WeatherStateEnum nextWeatherState = this.stateManager.getNextState();
		
		WeatherStateEnum newWeatherState = this.weatherGenerator.generateWeatherState();
		this.stateManager.addState(newWeatherState);
		
		emitterManager.send(new WeatherChangeDTO(nextWeatherState, newWeatherState, this.isDay()));
		
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
	
	public boolean isDay() {
		Calendar now = Calendar.getInstance();
		int hourOfDay = now.get(Calendar.HOUR_OF_DAY);
		
		return hourOfDay >= DAY_START_HOUR && hourOfDay < DAY_END_HOUR;   
	}
	
	public Iterator<WeatherStateEnum> getQueuedWeatherStates() {
		return this.stateManager.getQueueValues();
	}
	
}
