package com.persona.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.persona.modelo.Persona;
import com.persona.repository.PersonaRepository;

@Service
public class PersonaServiceImpl implements IPersonaService {

	@Autowired
	private PersonaRepository dao;

	@Override
	public List<Persona> listPersonas() {
		return (List<Persona>) dao.findAll();
	}

	@Override
	public Optional<Persona> getPersonaById(int id) {
		return dao.findById(id);
	}

	@Override
	public Persona savePersona(Persona persona) {
		dao.save(persona);
		return persona;
	}

	@Override
	public void deletePersona(int id) {
		dao.deleteById(id);
	}
}