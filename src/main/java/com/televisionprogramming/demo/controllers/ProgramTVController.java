package com.televisionprogramming.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.televisionprogramming.demo.dto.ProgramTVDTO;
import com.televisionprogramming.demo.entities.ProgramTV;
import com.televisionprogramming.demo.services.ProgramTVService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/program")


public class ProgramTVController {

    @Autowired
    private ProgramTVService programTVService;
    
    
    @PostMapping
    public ResponseEntity<String> addProgram(@RequestBody ProgramTVDTO program) {
        try {
            programTVService.addProgram(program);
            return ResponseEntity.status(HttpStatus.CREATED).body("Program successfully added");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<String> modifyProgram(@PathVariable Long id, @RequestBody ProgramTVDTO program) {
        try {
            programTVService.modifyProgram(id, program);
            return ResponseEntity.ok("Program successfully modified");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProgram(@PathVariable Long id) {
        try {
            programTVService.deleteProgram(id);
            return ResponseEntity.ok("Program successfully deleted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    
    @GetMapping
    public ResponseEntity<List<ProgramTV>> consultPrograms() {
        List<ProgramTV> programs = programTVService.consultPrograms();
        return ResponseEntity.ok(programs);
    }
    
    
    
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<ProgramTV>> consultProgramsByChannel(@PathVariable Long channelId) {
        List<ProgramTV> programs = programTVService.consultProgramsByChannel(channelId);
        return ResponseEntity.ok(programs);
    }

    
    
    @GetMapping("/channel/{channelId}/overlaps")
    public ResponseEntity<String> consultOverlapsByChannel(
            @PathVariable Long channelId,
            @RequestParam String inicio,
            @RequestParam String fin
    ) {
        try {
            List<ProgramTV> overlappingPrograms = programTVService.consultOverlapsByChannel(channelId, inicio, fin);
            if (!overlappingPrograms.isEmpty()) {
                return ResponseEntity.badRequest().body("There are overlapping programs in the channel.");
            }
            return ResponseEntity.ok("There are no overlapping programs in the channel.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when querying overlaps: " + e.getMessage());
        }
    }
}
