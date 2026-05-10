package com.persona.mapper;

import org.mapstruct.Mapper;

import com.persona.dto.PersonaRequestDTO;
import com.persona.dto.PersonaResponseDTO;
import com.persona.modelo.Persona;

//MapStruct generará la implementación automáticamente
/*
 * Estás diciendo:

“Convierte un PersonaRequestDTO → Persona”
“Convierte un Persona → PersonaResponseDTO”

  		Persona p = new Persona();
        p.setNombre(dto.getNombre());
        p.setTelefono(dto.getTelefono());
        
        PersonaResponseDTO dto = new PersonaResponseDTO();
        dto.setId(persona.getId());
        dto.setNombre(persona.getNombre());
        dto.setTelefono(persona.getTelefono());

 */
@Mapper(componentModel = "spring")
public interface PersonaMapper {

    // Request -> Entity
    Persona toEntity(PersonaRequestDTO dto);

    // Entity -> Response
    PersonaResponseDTO toDTO(Persona persona);
}