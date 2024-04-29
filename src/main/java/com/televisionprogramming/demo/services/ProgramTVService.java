package com.televisionprogramming.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.televisionprogramming.demo.dto.ProgramTVDTO;
import com.televisionprogramming.demo.entities.ProgramTV;
import com.televisionprogramming.demo.repositories.ProgramTVRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramTVService {

	@Autowired
	private ProgramTVRepository programTVRepository;

	public void addProgram(ProgramTVDTO programDTO) {
		if (programTVRepository.existsByScheduleAndChannelId(programDTO.getSchedule(), programDTO.getChannelId())) {
			throw new IllegalArgumentException("The program cannot be added because ther is a schedule overlap in the same channel");
		}

		ProgramTV program = new ProgramTV();
		program.setName(programDTO.getName());
		program.setSchedule(programDTO.getSchedule());
		program.setDay(programDTO.getDay());
		program.setDuration(programDTO.getDuration());
		program.setChannelId(programDTO.getChannelId());

		programTVRepository.save(program);
	}

	public void modifyProgram(Long id, ProgramTVDTO programDTO) {
		// Verificar si el programa existe
		Optional<ProgramTV> existingProgramOpt = programTVRepository.findById(id);
		if (!existingProgramOpt.isPresent()) {
			throw new IllegalArgumentException("The program cannot be modified because it does not exist");
		}

		ProgramTV existingProgram = existingProgramOpt.get();

		// Verificar si el horario del programa se solapa con otro programa del mismo canal
		if (isScheduleOverlapping(existingProgram.getChannelId(), programDTO.getSchedule())) {
			throw new IllegalArgumentException("The program cannot be modified because there is a schedule overlap on the same channel.");
		}

		// Verificar si el programa que se está modificando se encuentra justo en el horario que se está ejecutando
		if (isProgramRunning(existingProgram)) {
			throw new IllegalArgumentException("The program cannot be modified because it is currently running.");
		}

		// Actualizar los atributos del programa
		existingProgram.setName(programDTO.getName());
		existingProgram.setSchedule(programDTO.getSchedule());
		existingProgram.setDay(programDTO.getDay());
		existingProgram.setDuration(programDTO.getDuration());

		programTVRepository.save(existingProgram);
	}

	private boolean isScheduleOverlapping(Long channelId, String newSchedule) {
		// Get all programs for the given channel
		List<ProgramTV> programs = programTVRepository.findByChannelId(channelId);

		// Parse the new schedule string to LocalDateTime
		LocalDateTime newStartTime = LocalDateTime.parse(newSchedule, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

		for (ProgramTV program : programs) {
			// Parse the existing program's schedule string to LocalDateTime
			LocalDateTime existingStartTime = LocalDateTime.parse(program.getSchedule(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

			// Check if the new schedule overlaps with any existing program's schedule
			if (newStartTime.toLocalTime().isBefore(existingStartTime.plusHours(program.getDuration()).toLocalTime()) &&
					existingStartTime.toLocalTime().isBefore(newStartTime.plusHours(1).toLocalTime())) {
				return true; // There is an overlap
			}
		}

		return false; // No overlap found
	}




	private boolean isProgramRunning(ProgramTV program) {
		// Parse the program's schedule string to LocalDateTime
		LocalDateTime programStartTime = LocalDateTime.parse(program.getSchedule(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
		LocalDateTime programEndTime = programStartTime.plusHours(program.getDuration());

		// Get the current time
		LocalDateTime currentTime = LocalDateTime.now();

		// Check if the current time falls within the program's schedule
		return (currentTime.isAfter(programStartTime) || currentTime.isEqual(programStartTime)) &&
				currentTime.isBefore(programEndTime);
	}

	public void deleteProgram(Long id) {
		if (!programTVRepository.existsById(id)) {
			throw new IllegalArgumentException("The program cannot be deleted because it does not exist");
		}

		programTVRepository.deleteById(id);
	}

	public List<ProgramTV> consultPrograms() {
		return programTVRepository.findAll();
	}

	public List<ProgramTV> consultProgramsByChannel(Long channelId) {
		return programTVRepository.findByChannelId(channelId);
	}

	public List<ProgramTV> consultOverlapsByChannel(Long channelId, String startTime, String endTime) {
		return programTVRepository.findProgramsOverlappingByChannel(channelId, startTime, endTime);
	}


	public boolean checkProgramScheduleExists(Long channelId, String schedule) {
		return programTVRepository.existsByChannelIdAndSchedule(channelId, schedule);
	}



	public Optional<ProgramTV> consultProgramByNameAndChannel(Long channelId, String name) {
		return programTVRepository.findByChannelIdAndName(channelId, name);
	}


	private boolean isScheduleOverlapping(Long id, ProgramTVDTO programDTO) {

		Optional<ProgramTV> existingProgramOpt = programTVRepository.findById(id);
		if (!existingProgramOpt.isPresent()) {
			throw new IllegalArgumentException("The program with the provided ID does exist");
		}
		ProgramTV existingProgram = existingProgramOpt.get();

		// Convertir las cadenas de fecha y hora en objetos LocalDateTime
		LocalDateTime existingStartDateTime = LocalDateTime.parse(existingProgram.getSchedule(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
		LocalDateTime existingEndDateTime = existingStartDateTime.plusHours(existingProgram.getDuration());

		LocalDateTime newStartDateTime = LocalDateTime.parse(programDTO.getSchedule(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
		LocalDateTime newEndDateTime = newStartDateTime.plusHours(programDTO.getDuration());

		// Excluir el programa que se está modificando de la verificación de solapamiento
		if (existingProgram.getId().equals(id)) {
			// Si el ID del programa existente es igual al ID proporcionado, se excluye de la verificación
			return false;
		}

		// Verificar si hay solapamiento de horarios
		boolean isOverlapping = existingStartDateTime.isBefore(newEndDateTime) && newStartDateTime.isBefore(existingEndDateTime);
		return isOverlapping;
	}
}
