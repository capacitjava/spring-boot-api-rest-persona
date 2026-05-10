package com.persona.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persona.modelo.Persona;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PersonaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ========================
    // LISTAR PERSONAS
    // ========================
    @Test
    void listarPersonas() throws Exception {
        mockMvc.perform(get("/personas/listar"))
                .andExpect(status().isOk());
    }

    // ========================
    // CREAR PERSONA
    // ========================
    @Test
    void crearPersona() throws Exception {
        Persona persona = new Persona();
        persona.setNombre("Juan");
        persona.setTelefono("5551234567");

        mockMvc.perform(post("/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(persona)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.telefono").value("5551234567"));
    }

    // ========================
    // OBTENER PERSONA POR ID
    // ========================
    @Test
    void obtenerPersonaPorId() throws Exception {
        Persona persona = new Persona();
        persona.setNombre("Ana");
        persona.setTelefono("5559876543");

        String response = mockMvc.perform(post("/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(persona)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Persona creada = objectMapper.readValue(response, Persona.class);

        mockMvc.perform(get("/personas/{id}", creada.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.telefono").value("5559876543"));
    }

    // ========================
    // MODIFICAR PERSONA
    // ========================
    @Test
    void modificarPersona() throws Exception {
        Persona persona = new Persona();
        persona.setNombre("Carlos");
        persona.setTelefono("5551112222");

        String response = mockMvc.perform(post("/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(persona)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Persona creada = objectMapper.readValue(response, Persona.class);

        creada.setNombre("Carlos Modificado");
        creada.setTelefono("5553334444");

        mockMvc.perform(put("/personas/{id}", creada.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Carlos Modificado"))
                .andExpect(jsonPath("$.telefono").value("5553334444"));
    }

    // ========================
    // ELIMINAR PERSONA
    // ========================
    @Test
    void eliminarPersona() throws Exception {
        Persona persona = new Persona();
        persona.setNombre("Luis");
        persona.setTelefono("5550009999");

        String response = mockMvc.perform(post("/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(persona)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Persona creada = objectMapper.readValue(response, Persona.class);

        mockMvc.perform(delete("/personas/{id}", creada.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/personas/{id}", creada.getId()))
                .andExpect(status().isNotFound());
    }
}
