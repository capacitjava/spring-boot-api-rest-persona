package com.persona.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persona.modelo.Persona;
import com.persona.service.IPersonaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias del controlador PersonaController
 * Compatibles con Spring Boot 3.5.6
 */
@WebMvcTest(PersonaController.class)
public class PersonaControllerPruebaUnitariaTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private IPersonaService personaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Persona persona1;
    private Persona persona2;

    @BeforeEach
    void init() {
        persona1 = new Persona(1, "Mario", "5551234");
        persona2 = new Persona(2, "Ana", "5555678");
    }

    @Test
    @DisplayName("Debe listar todas las personas (GET /personas/listar)")
    void listarPersonas() throws Exception {
        List<Persona> personas = Arrays.asList(persona1, persona2);
        when(personaService.listPersonas()).thenReturn(personas);

        mockMvc.perform(get("/personas/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Mario"))
                .andExpect(jsonPath("$[1].telefono").value("5555678"));
    }

    @Test
    @DisplayName("Debe agregar una nueva persona (POST /personas)")
    void agregarPersona() throws Exception {
        Persona nueva = new Persona(0, "Laura", "5550000");
        Persona guardada = new Persona(3, "Laura", "5550000");

        when(personaService.savePersona(any(Persona.class))).thenReturn(guardada);

        mockMvc.perform(post("/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombre").value("Laura"));
    }
  
    @Test
    @DisplayName("Debe devolver una persona existente (GET /personas/{id})")
    void obtenerPersonaExistente() throws Exception {
        when(personaService.getPersonaById(1)).thenReturn(Optional.of(persona1));

        mockMvc.perform(get("/personas/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Mario"));
    }
  
    @Test
    @DisplayName("Debe devolver 404 si no se encuentra la persona (GET /personas/{id})")
    void obtenerPersonaNoExistente() throws Exception {
        when(personaService.getPersonaById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/personas/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe modificar una persona existente (PUT /personas/{id})")
    void modificarPersona() throws Exception {
        Persona modificada = new Persona(1, "Mario Editado", "5559999");
        when(personaService.savePersona(any(Persona.class))).thenReturn(modificada);

        mockMvc.perform(put("/personas/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Mario Editado"))
                .andExpect(jsonPath("$.telefono").value("5559999"));
    }
 
    @Test
    @DisplayName("Debe eliminar una persona existente (DELETE /personas/{id})")
    void eliminarPersonaExistente() throws Exception {
        when(personaService.getPersonaById(1)).thenReturn(Optional.of(persona1));
        doNothing().when(personaService).deletePersona(1);

        mockMvc.perform(delete("/personas/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe devolver 404 si intenta eliminar una persona inexistente")
    void eliminarPersonaNoExistente() throws Exception {
        when(personaService.getPersonaById(100)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/personas/{id}", 100))
                .andExpect(status().isNotFound());
    }
}
