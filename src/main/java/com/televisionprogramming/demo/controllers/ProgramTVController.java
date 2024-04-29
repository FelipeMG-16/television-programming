package com.televisionprogramming.demo.controllers;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.televisionprogramming.demo.dto.ProgramTVDTO;
import com.televisionprogramming.demo.entities.ProgramTV;
import com.televisionprogramming.demo.services.ProgramTVService;


@RestController
@RequestMapping("/api/program")
public class ProgramTVController {

	
	@Autowired
	private ProgramTVService programTVService;
	
	
	//Endpoint para agregar un programa
	@PostMapping
	public ResponseEntity<String> addProgram (@RequestBody ProgramTVDTO program) {
		try {
			programTVService.addProgram(program);
			return ResponseEntity.status(HttpStatus.CREATED).body("Program succesfully added");	
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding program");
		}//try-catch
	}//postMapping
	
	
	
	
	//Endpoint para modificar un programa
	@PutMapping("/{id}")
	public ResponseEntity<String> modifyProgram (@PathVariable Long id, @RequestBody ProgramTVDTO program){
		try {
			programTVService.modifyProgram(id, program);
			return ResponseEntity.ok("Program succesfully modified");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error modifying program");
		}//try-catch
	}//putMapping
	
	
	
	// Endpoint para eliminar un programa
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProgram(@PathVariable Long id) {
        try {
            programTVService.deleteProgram(id);
            return ResponseEntity.ok("Program succesfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting program: " + e.getMessage());
        }//try-catch
    }//deleteMapping

    
 

    // Endpoint para consultar todos los programas
    @GetMapping
    public ResponseEntity<List<ProgramTV>> consultPrograms() {
        List<ProgramTV> programs = programTVService.consultPrograms();
        return ResponseEntity.ok(programs);
    }//getMapping

    
    
    // Endpoint para consultar programas de un canal específico
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<ProgramTV>> consultarProgramasPorCanal(@PathVariable Long channelId) {
        List<ProgramTV> programs = programTVService.consultProgramsByChannel(channelId);
        return ResponseEntity.ok(programs);
    }//getMapping

    
    
    // Endpoint para consultar si hay solapamientos en los programas de un canal específico
    @GetMapping("/channel/{channelId}/overlaps")
    public ResponseEntity<String> consultOverlapsByChannel(
            @PathVariable Long canalId,
            @RequestParam String inicio,
            @RequestParam String fin
    ) {
        try {
            List<ProgramTV> overlappingPrograms = programTVService.consultOverlapsByChannel(canalId, inicio, fin);
            if (!overlappingPrograms.isEmpty()) {
                return ResponseEntity.badRequest().body("Existen programas solapados en el canal.");
            }
            return ResponseEntity.ok("No existen programas solapados en el canal.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al consultar solapamientos: " + e.getMessage());
        }//try-catch
    }//getMapping
}//programTVController