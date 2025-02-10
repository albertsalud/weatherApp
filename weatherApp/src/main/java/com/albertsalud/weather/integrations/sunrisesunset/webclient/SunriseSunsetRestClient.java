package com.albertsalud.weather.integrations.sunrisesunset.webclient;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.albertsalud.weather.integrations.sunrisesunset.dtos.RequestDTO;
import com.albertsalud.weather.integrations.sunrisesunset.dtos.ResponseDTO;
import com.albertsalud.weather.integrations.sunrisesunset.dtos.TimesDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SunriseSunsetRestClient {
	
	private RestClient restClient;
	
	public SunriseSunsetRestClient() {
		this.restClient = RestClient.create();
	}
	
	public TimesDTO getAPIData() {
		RequestDTO requestData = new RequestDTO();
		
		try {
			log.info("Conntecting to https://api.sunrisesunset.io...");
			ResponseEntity<ResponseDTO> apiResponse = restClient.get()
				.uri("https://api.sunrisesunset.io/json?lat={lat}&lng={long}&time_format={format}", requestData.getLatitude(),
					requestData.getLongitude(), requestData.getTimeFormat())
				.retrieve()
				.toEntity(ResponseDTO.class);
			
			log.info("Response retrieved: {}", apiResponse);
			return apiResponse.getBody().getResults();
		
		} catch (RestClientException e) {
			log.error("Unable to get response from API: {}", e.getMessage());
			return new TimesDTO();
		}
	}

}
