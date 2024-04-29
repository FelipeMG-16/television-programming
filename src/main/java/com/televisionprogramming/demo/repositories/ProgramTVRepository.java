package com.televisionprogramming.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.televisionprogramming.demo.entities.ProgramTV;

import java.util.List;

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
}
