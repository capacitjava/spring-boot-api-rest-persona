package com.persona.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.persona.modelo.Persona;

@Repository
public interface PersonaRepository extends CrudRepository<Persona, Integer>{

	//CRUD: delete, deleteById, deleteAll, find, findById, findAll, update.... etc..
	
}