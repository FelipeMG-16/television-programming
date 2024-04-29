package com.televisionprogramming.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.televisionprogramming.demo.entities.ProgramTV;

import java.util.List;

@Repository
public interface ProgramTVRepository extends JpaRepository<ProgramTV, Long> {

    boolean existsByScheduleAndChannelId(String schedule, int channelId);

    List<ProgramTV> findByChannelId(Long channelId);

    @Query("SELECT p FROM ProgramTV p WHERE p.channelId = :channelId " +
           "AND ((p.schedule BETWEEN :inicio AND :fin) OR " +
           "(:inicio BETWEEN p.schedule AND p.scheduleEnd))")
    List<ProgramTV> findProgramsOverlappingByChannel(
            @Param("channelId") Long channelId,
            @Param("inicio") String inicio,
            @Param("fin") String fin
    );

 

	boolean existsByHorarioAndCanalIdAndIdNot(String schedule, int channelId, Long id);
}
