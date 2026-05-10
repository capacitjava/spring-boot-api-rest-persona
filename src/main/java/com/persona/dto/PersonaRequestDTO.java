package com.persona.dto;

import jakarta.validation.constraints.NotBlank;

//Request (lo que recibes del cliente)
public class PersonaRequestDTO {

    @NotBlank
    private int id;
    
    @NotBlank
    private String nombre;

    @NotBlank
    private String telefono;

    
    // getters y setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}




    
    
}