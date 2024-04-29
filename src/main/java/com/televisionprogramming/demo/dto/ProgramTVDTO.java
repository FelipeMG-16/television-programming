package com.televisionprogramming.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class ProgramTVDTO {
	
	private String name;
	private String schedule;
	private String day;
	private int duration;
	private int channelId;
	
	
	
	//Getters
	public String getName() {
		return name;
	}
	public String getSchedule() {
		return schedule;
	}
	public String getDay() {
		return day;
	}
	public int getDuration() {
		return duration;
	}
	public int getChannelId() {
		return channelId;
	}
	
	

}//programDTO
