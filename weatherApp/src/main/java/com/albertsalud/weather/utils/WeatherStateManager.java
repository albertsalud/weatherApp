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
	
	private Queue<WeatherStateEnum> weatherQueue = new ConcurrentLinkedQueue<>();
	private WeatherStateEnum currentState;
	
	public WeatherStateEnum getNextState() {
		this.currentState = this.weatherQueue.poll();	
		return this.currentState;
	}
	
	public void addState(WeatherStateEnum newState) {
		log.info("Adding weather state: {}", newState);
		this.weatherQueue.add(newState);
	}
	
	public Iterator<WeatherStateEnum> getQueueValues() {
		return this.weatherQueue.iterator();
	}

	public WeatherStateEnum getCurrentState() {
		return this.currentState;
	}

}
