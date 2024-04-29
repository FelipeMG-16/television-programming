package com.televisionprogramming.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table (name = "programs")

public class Program {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(nullable = false)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String horario;
	
	@Column(nullable = false)
	private String dia;
	
	@Column(nullable = false)
	private int duracion;
	
	@Column(nullable = false)
	private int canal;
	
	
}//Program
