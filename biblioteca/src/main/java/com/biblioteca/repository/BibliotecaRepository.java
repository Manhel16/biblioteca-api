package com.biblioteca.repository;

import com.biblioteca.model.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BibliotecaRepository extends JpaRepository<Biblioteca, Long> {
    List<Biblioteca> findByNombreContainingIgnoreCase(String nombre);
}
