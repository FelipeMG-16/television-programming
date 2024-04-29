package com.televisionprogramming.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.televisionprogramming.demo.dto.ProgramTVDTO;
import com.televisionprogramming.demo.entities.ProgramTV;
import com.televisionprogramming.demo.repositories.ProgramTVRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramTVService {

    @Autowired
    private ProgramTVRepository programTVRepository;

    public void addProgram(ProgramTVDTO programDTO) {
        // Verificar si el programa ya existe
        if (programTVRepository.existsByScheduleAndChannelId(programDTO.getSchedule(), programDTO.getChannelId())) {
            throw new IllegalArgumentException("No se puede agregar el programa porque hay un solapamiento de horario en el mismo canal");
        }

        // Convertir ProgramTVDTO a ProgramTV
        ProgramTV program = new ProgramTV();
        program.setName(programDTO.getName());
        program.setSchedule(programDTO.getSchedule());
        program.setDay(programDTO.getDay());
        program.setDuration(programDTO.getDuration());
        program.setChannelId(programDTO.getChannelId()); // Establecer el ID del canal

        programTVRepository.save(program);
    }

    public void modifyProgram(Long id, ProgramTVDTO programDTO) {
        // Verificar si el programa existe
        Optional<ProgramTV> existingProgramOpt = programTVRepository.findById(id);
        if (!existingProgramOpt.isPresent()) {
            throw new IllegalArgumentException("No se puede modificar el programa porque no existe");
        }

        ProgramTV existingProgram = existingProgramOpt.get();

        // Verificar si el horario del programa se solapa con otro programa del mismo canal
        if (programTVRepository.existsByHorarioAndCanalIdAndIdNot(programDTO.getSchedule(), programDTO.getChannelId(), id)) {
            throw new IllegalArgumentException("No se puede modificar el programa porque hay un solapamiento de horario en el mismo canal");
        }

        // Actualizar los atributos del programa
        existingProgram.setName(programDTO.getName());
        existingProgram.setSchedule(programDTO.getSchedule());
        existingProgram.setDay(programDTO.getDay());
        existingProgram.setDuration(programDTO.getDuration());

        programTVRepository.save(existingProgram);
    }

    public void deleteProgram(Long id) {
        // Verificar si el programa existe
        Optional<ProgramTV> existingProgramOpt = programTVRepository.findById(id);
        if (!existingProgramOpt.isPresent()) {
            throw new IllegalArgumentException("No se puede eliminar el programa porque no existe");
        }

        // Eliminar el programa
        programTVRepository.deleteById(id);
    }

    public List<ProgramTV> consultPrograms() {
        return programTVRepository.findAll();
    }

    public List<ProgramTV> consultProgramsByChannel(Long channelId) {
        return programTVRepository.findByChannelId(channelId);
    }

    public List<ProgramTV> consultOverlapsByChannel(Long channelId, String inicio, String fin) {
        return programTVRepository.findProgramsOverlappingByChannel(channelId, inicio, fin);
    }
}
