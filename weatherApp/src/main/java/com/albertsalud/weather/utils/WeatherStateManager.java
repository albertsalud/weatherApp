package com.albertsalud.weather.utils;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Component;

import com.albertsalud.weather.model.WeatherStateEnum;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WeatherStateManager {
	private static final int QUEUE_CAPACITY = 3;
	
	private Queue<WeatherStateEnum> weatherQueue = new ConcurrentLinkedQueue<>();
	private WeatherStateEnum currentState;
	
	private final WeatherStateRandomGenerator weatherGenerator;
	
	public WeatherStateManager(WeatherStateRandomGenerator weatherGenerator) {
		this.weatherGenerator = weatherGenerator;
		
		this.initQueue();
	}
	
	public WeatherStateEnum getNextState() {
		this.currentState = this.weatherQueue.poll();	
		return this.currentState;
	}
	
	public WeatherStateEnum addState() {
		WeatherStateEnum newState = this.weatherGenerator.generateWeatherState();
		log.info("Adding weather state: {}", newState);
		this.weatherQueue.add(newState);
		
		return newState;
	}
	
	public Iterator<WeatherStateEnum> getQueueValues() {
		return this.weatherQueue.iterator();
	}

	public WeatherStateEnum getCurrentState() {
		return this.currentState;
	}
	
	private void initQueue() {
		for(int i = 0; i <= QUEUE_CAPACITY; i++) {
			this.addState();
		}
		this.getNextState();
	}

}
