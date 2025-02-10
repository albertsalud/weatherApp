package com.albertsalud.weather.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.albertsalud.weather.integrations.sunrisesunset.SunriseSunsetManager;
import com.albertsalud.weather.services.WeatherService;
import com.albertsalud.weather.utils.SseEmitterManager;

@Controller
@RequestMapping(value = {"", "/"})
public class HomeController {
	
	private final WeatherService weatherService;
	private final SseEmitterManager emitterManager;
	private final SunriseSunsetManager sunriseSunsetManager;
	
	public HomeController(WeatherService weatherService,
			SseEmitterManager emitterManager,
			SunriseSunsetManager sunriseSunsetManager) {
		this.weatherService = weatherService;
		this.emitterManager = emitterManager;
		this.sunriseSunsetManager = sunriseSunsetManager;
	}
	
	@GetMapping
	public String goHome(Model model) {
		model.addAttribute("currentWeather", this.weatherService.getCurrentWeatherState());
		model.addAttribute("queuedWeather", weatherService.getQueuedWeatherStates());
		model.addAttribute("timeToNextChange", this.weatherService.getTimeToNextChange());
		model.addAttribute("day", this.sunriseSunsetManager.isDayYet());
		
		return "home";
	}
	
	@ResponseBody
	@GetMapping("/events/stateChanges")
	public SseEmitter serverMessages() {
		SseEmitter emitter = new SseEmitter(0L);
		emitterManager.addEmitter(emitter);
		
		return emitter;
	}

}
