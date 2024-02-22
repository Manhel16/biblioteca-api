package com.biblioteca.service;

import com.biblioteca.exceptions.DepartamentoNotFoundException;
import com.biblioteca.model.Biblioteca;
import com.biblioteca.repository.BibliotecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BibliotecaService {
    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    public List<Biblioteca> getAllBibliotecas() {
        return bibliotecaRepository.findAll();
    }

    public Biblioteca createBiblioteca(Biblioteca biblioteca) {
        return bibliotecaRepository.save(biblioteca);
    }

    public Optional<Biblioteca> getBibliotecaById(long id) {

        return Optional.ofNullable(bibliotecaRepository.findById(id).orElseThrow(() -> new DepartamentoNotFoundException("No se ha encontrado a la Biblioteca con id:" + id)));
    }

    public Biblioteca updateBiblioteca(Biblioteca biblioteca) {

        return bibliotecaRepository.save(biblioteca);
    }

    public void deleteBibliotecaById(long id) {

        bibliotecaRepository.deleteById(id);
    }

    public List<Biblioteca> getEjemplosByNombre(String nombre) {
        return bibliotecaRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
