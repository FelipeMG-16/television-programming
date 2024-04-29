package com.televisionprogramming.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "programs")

public class ProgramTV {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(nullable = false)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String schedule;
	
	@Column(nullable = false)
	private String day;
	
	@Column(nullable = false)
	private int duration;
	
	@Column(nullable = false)
	private Long channelId;


	
	


	//Setters
	public void setId(Long id) {
		this.id = id;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}


	public void setDay(String day) {
		this.day = day;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setChannelId(long id) {
		this.channelId = id;
	}

	
}//Program
