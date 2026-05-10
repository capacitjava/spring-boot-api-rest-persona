package com.persona.controller;

import org.springframework.web.bind.annotation.*;

import com.persona.dto.PersonaRequestDTO;
import com.persona.dto.PersonaResponseDTO;
import com.persona.mapper.PersonaMapper;
import com.persona.modelo.Persona;
import com.persona.service.IPersonaService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/personas/api/v1")
public class PersonaController {

    @Autowired
    private IPersonaService service;

    @Autowired
    private PersonaMapper mapper;

    // 🔹 LISTAR
    @GetMapping("/listar")
    public ResponseEntity<List<PersonaResponseDTO>> listar() {
        List<PersonaResponseDTO> lista = service.listPersonas()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // 🔹 CREAR
    @PostMapping
    public ResponseEntity<PersonaResponseDTO> crear(@RequestBody PersonaRequestDTO dto) {

        Persona persona = mapper.toEntity(dto);
        persona = service.savePersona(persona);

        return new ResponseEntity<>(mapper.toDTO(persona), HttpStatus.CREATED);
    }

    // 🔹 OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<PersonaResponseDTO> obtener(@PathVariable int id) {

        Optional<Persona> persona = service.getPersonaById(id);

        return persona
                .map(p -> ResponseEntity.ok(mapper.toDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<PersonaResponseDTO> actualizar(
            @RequestBody PersonaRequestDTO dto,
            @PathVariable int id) {

        Optional<Persona> existente = service.getPersonaById(id);

        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Persona persona = mapper.toEntity(dto);
        persona.setId(id);

        persona = service.savePersona(persona);

        return ResponseEntity.ok(mapper.toDTO(persona));
    }

    // 🔹 ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {

        Optional<Persona> persona = service.getPersonaById(id);

        if (persona.isPresent()) {
            service.deletePersona(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}



/*
import org.springframework.web.bind.annotation.RestController;

import com.persona.modelo.Persona;
import com.persona.service.IPersonaService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping({ "personas" })
public class PersonaController {

	@Autowired
	private IPersonaService repository;

	@GetMapping("/listar")
	public ResponseEntity<List<Persona>> listAllPersona() {
	    List<Persona> personas = repository.listPersonas();

	    // Siempre devuelve 200
	    return ResponseEntity.ok(personas);
	}


	@PostMapping
	public ResponseEntity<Persona> agregarPersona(@RequestBody Persona persona) {
		persona = repository.savePersona(persona);
		return new ResponseEntity<>(persona, HttpStatus.CREATED); // <--- IMPORTANTE
	}

	@GetMapping("/{id}")
	public ResponseEntity<Persona> getPersonaById(@PathVariable int id) {
		Optional<Persona> persona = repository.getPersonaById(id);
		if (persona.isPresent()) {
			// Retorna la persona encontrada con estado HTTP 200 OK
			return ResponseEntity.ok(persona.get());
		} else {
			// Retorna estado HTTP 404 Not Found si no se encuentra
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Persona> modificarPersona(@RequestBody Persona persona, @PathVariable int id) {
		persona.setId(id);
		persona = repository.savePersona(persona);
		return new ResponseEntity<>(persona, HttpStatus.CREATED); // <--- IMPORTANTE
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePersona(@PathVariable int id) {
		Optional<Persona> persona = repository.getPersonaById(id); // Usar findById si estás usando JPA o un método similar
		if (persona.isPresent()) {
			repository.deletePersona(id); // Elimina la persona de la base de datos
			return ResponseEntity.ok().build(); // Responde con 200 OK
		} else {
			return ResponseEntity.notFound().build(); // Si no se encuentra la persona, responde con 404 Not Found
		}
	}

}*/