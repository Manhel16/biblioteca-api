package com.biblioteca.repository;

import com.biblioteca.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    // Puedes agregar métodos personalizados de consulta aquí si es necesario
}
