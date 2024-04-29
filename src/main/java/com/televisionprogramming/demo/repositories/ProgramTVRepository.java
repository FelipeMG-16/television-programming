package com.televisionprogramming.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.televisionprogramming.demo.entities.ProgramTV;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramTVRepository extends JpaRepository<ProgramTV, Long> {

    @Query("SELECT p FROM ProgramTV p WHERE p.channelId = :channelId " +
           "AND (p.schedule BETWEEN :startTime AND :endTime)")
    List<ProgramTV> findProgramsOverlappingByChannel(
            @Param("channelId") Long channelId,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );

    boolean existsByScheduleBetweenAndChannelIdAndIdNot(String startTime, String endTime, Long channelId, Long id);

    boolean existsByScheduleAndChannelId(String schedule, Long channelId);
    
 // Consulta para encontrar todos los programas de un canal específico
    List<ProgramTV> findByChannelId(Long channelId);

    // Consulta para encontrar un programa por ID de canal y nombre del programa
    Optional<ProgramTV> findByChannelIdAndName(Long channelId, String name);

    // Consulta para verificar si existe algún programa en un canal específico en un horario dado
    boolean existsByChannelIdAndSchedule(Long channelId, String schedule);
}
