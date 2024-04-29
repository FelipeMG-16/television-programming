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
            throw new IllegalArgumentException("No se puede agregar el programa porque hay un solapamiento de horario en el mismo canal");
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
        if (!programTVRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede modificar el programa porque no existe");
        }

        if (isScheduleOverlapping(id, programDTO)) {
            throw new IllegalArgumentException("No se puede modificar el programa porque hay un solapamiento de horario en el mismo canal");
        }

        ProgramTV existingProgram = programTVRepository.findById(id).orElse(null);
        if (existingProgram != null) {
            existingProgram.setName(programDTO.getName());
            existingProgram.setSchedule(programDTO.getSchedule());
            existingProgram.setDay(programDTO.getDay());
            existingProgram.setDuration(programDTO.getDuration());

            programTVRepository.save(existingProgram);
        }
    }

    public void deleteProgram(Long id) {
        if (!programTVRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar el programa porque no existe");
        }

        programTVRepository.deleteById(id);
    }

    public List<ProgramTV> consultPrograms() {
        return programTVRepository.findAll();
    }

    public Optional<ProgramTV> consultProgramsByChannel(Long channelId) {
        return programTVRepository.findById(channelId);
    }

    public List<ProgramTV> consultOverlapsByChannel(Long channelId, String startTime, String endTime) {
        return programTVRepository.findProgramsOverlappingByChannel(channelId, startTime, endTime);
    }

    private boolean isScheduleOverlapping(Long id, ProgramTVDTO programDTO) {
        // Obtener el programa existente con el ID proporcionado
        Optional<ProgramTV> existingProgramOpt = programTVRepository.findById(id);
        if (!existingProgramOpt.isPresent()) {
            throw new IllegalArgumentException("El programa con el ID proporcionado no existe");
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
