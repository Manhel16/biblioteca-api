package com.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "departamento")
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "imagen_local", nullable = false,length = 100)
    private String imagenLocal;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "imagen_blob", columnDefinition = "LONGBLOB")
    private byte[] imagenBlob;

    @ManyToOne(targetEntity = Biblioteca.class)
    @JoinColumn (name = "id_biblioteca")
    @JsonBackReference
    private Biblioteca biblioteca;
    public Departamento() {
        // Puedes dejar este constructor vacío o inicializar algunos valores por defecto si es necesario
    }
    // Constructor sin el id del Departamento
    public Departamento(String nombre, String descripcion, String imagenLocal, byte[] imagenBlob, Long id_biblioteca) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenLocal = imagenLocal;
        this.imagenBlob = imagenBlob;
        if (biblioteca == null) {
            this.biblioteca = new Biblioteca();
        }
        this.biblioteca.setId(id_biblioteca);
    }

    // Getters y setters (no incluidos aquí por brevedad)

    // Método toString()
    @Override
    public String toString() {
        return "Departamento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagenLocal='" + imagenLocal + '\'' +
                // No se incluye imagenBlob en toString() por simplicidad
                '}';
    }
}
