package com.albertsalud.weather.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.albertsalud.weather.controllers.dtos.WeatherChangeDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SseEmitterManager {
	
	private List<SseEmitter> activeEmitters = new ArrayList<>();
	
	public void addEmitter(SseEmitter emitter) {
		log.info("Adding new emitter {}", emitter);
		activeEmitters.add(emitter);
	}
	
	public void send(WeatherChangeDTO weatherChangeDTO) {
		Iterator<SseEmitter> emitterIterator = activeEmitters.iterator();
		log.info("Active emitters: {}", activeEmitters.size());
		
		List<SseEmitter> emittersToBeRemoved = new ArrayList<>();
		while(emitterIterator.hasNext()) {
			SseEmitter currentEmitter = emitterIterator.next();
			try {
				log.info("Sending to emitter {} ...", currentEmitter);
				currentEmitter.send(weatherChangeDTO);
				
			} catch (Throwable e) {
				log.error("Error sending message to emmiter {}. Adding into emitters to be removed list...", currentEmitter);
				currentEmitter.completeWithError(e);
				emittersToBeRemoved.add(currentEmitter);
			}
		}
		
		if(!emittersToBeRemoved.isEmpty()) {
			log.warn("{} emitters will be removed", emittersToBeRemoved.size());
			this.activeEmitters.removeAll(emittersToBeRemoved);
			log.info("Active emitters after sending message: {}", activeEmitters.size());
		}
	}
	
}
