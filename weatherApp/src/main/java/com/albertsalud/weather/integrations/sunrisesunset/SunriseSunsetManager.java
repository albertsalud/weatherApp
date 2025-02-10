package com.albertsalud.weather.integrations.sunrisesunset;

import java.time.LocalTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.albertsalud.weather.integrations.sunrisesunset.dtos.TimesDTO;
import com.albertsalud.weather.integrations.sunrisesunset.webclient.SunriseSunsetRestClient;

@Component
public class SunriseSunsetManager {
	
	private static final LocalTime DEFAULT_SUNRISE = LocalTime.of(7, 0);
	private static final LocalTime DEFAULT_SUNSET = LocalTime.of(21, 0);
		
	private LocalTime sunrise;
	private LocalTime sunset;
	
	private final SunriseSunsetRestClient restClient;
	
	public SunriseSunsetManager(SunriseSunsetRestClient restClient) {
		this.restClient = restClient;
		this.setCurrentSunriseAndSunsetData();
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void setCurrentSunriseAndSunsetData() {
		TimesDTO apiResponse = this.restClient.getAPIData();
		if(apiResponse != null) {
			sunrise = apiResponse.getSunrise();
			sunset = apiResponse.getSunset();
		}
	}
	
	public boolean isDayYet() {
		LocalTime now = LocalTime.now();
		if(sunrise == null || sunset == null) {
			return now.isAfter(DEFAULT_SUNRISE) && now.isBefore(DEFAULT_SUNSET);
		}
		
		return now.isAfter(sunrise) && now.isBefore(sunset);
		
	}
}
