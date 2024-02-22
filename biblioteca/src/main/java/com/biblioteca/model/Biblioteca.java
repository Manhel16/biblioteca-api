package com.biblioteca.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name="biblioteca")
public class Biblioteca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;
    @Column(name = "anioApertura", nullable = false)
    private Integer anioApertura;
    //@OneToMany(targetEntity = Departamento.class, fetch = FetchType.LAZY,mappedBy = "biblioteca")
     @OneToMany (mappedBy = "biblioteca", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Departamento> departamentos;

    public Biblioteca() {

    }

    public Biblioteca(String nombre, String direccion, Integer anioApertura){
        this.nombre = nombre;
        this.direccion = direccion;
        this.anioApertura = anioApertura;
    }
    @Override
    public String toString(){
        return "Biblioteca{" + "id" + id +
                ", nombre='" + nombre +'\''
                + ",direccion" + direccion
                + "anioApertura" + anioApertura +'}';
    }

}
