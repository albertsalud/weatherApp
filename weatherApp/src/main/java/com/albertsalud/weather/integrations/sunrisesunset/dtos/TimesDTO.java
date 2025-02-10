package com.albertsalud.weather.integrations.sunrisesunset.dtos;

import java.time.LocalTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = {"date", "sunrise", "sunset", "timezone"} )
public class TimesDTO {
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date; 
	
	private LocalTime sunrise;
	private LocalTime sunset;
	
	@JsonProperty("first_light")
	private LocalTime firstLight;
	
	@JsonProperty("last_light")
	private LocalTime lastLight;
	private LocalTime dawn;
	private LocalTime dusk;
	
	@JsonProperty("solar_noon")
	private LocalTime solarNoon;
	
	@JsonProperty("golden_hour")
	private LocalTime goldenHour;
	
	@JsonProperty("day_length")
	private LocalTime dayLength;
	
	private String timezone;
	private int utcOffset;
	
	
	
}

