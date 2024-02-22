package com.biblioteca.controller;
import com.biblioteca.model.Biblioteca;
import com.biblioteca.service.BibliotecaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BibliotecaController {
    @Autowired
    private BibliotecaService bibliotecaService;
    @GetMapping("/biblioteca")
    public List<Biblioteca> getAllBibliotecas(){
        return bibliotecaService.getAllBibliotecas();
    }
    @PostMapping("/biblioteca")
    public ResponseEntity<Biblioteca> createBiblioteca(@RequestBody Biblioteca biblioteca){
        Biblioteca createdBiblioteca = bibliotecaService.createBiblioteca(biblioteca);
        return new ResponseEntity<>(createdBiblioteca, HttpStatus.CREATED);
    }
    @Operation(summary = "obtienes una biblioteca",description = "obtienes una biblioteca con el id", tags = {"bibliotecas"})
    @Parameter(name = "id",description = "ID del biblioteca",required = true,example = "1")
    @ApiResponse(responseCode = "200",description = "biblioteca encontrado")
    @ApiResponse(responseCode = "404",description = "biblioteca no encontrado")
    @GetMapping("/biblioteca/{id}")
    public ResponseEntity<Biblioteca> getBibliotecaById(@PathVariable long id){
        Optional<Biblioteca> optionalBiblioteca = bibliotecaService.getBibliotecaById(id);

        if (((Optional<?>) optionalBiblioteca).isPresent()){
            optionalBiblioteca = bibliotecaService.getBibliotecaById(id);
            return new ResponseEntity<>(optionalBiblioteca.get(),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/biblioteca/{id}")
    public ResponseEntity<Biblioteca> UpdateBiblioteca(@PathVariable long id, @RequestBody Biblioteca biblioteca){
        Optional<Biblioteca> optionalBiblioteca = bibliotecaService.getBibliotecaById(id);
        if (((Optional<?>) optionalBiblioteca).isPresent()){
            Biblioteca existingBiblioteca = optionalBiblioteca.get();
            existingBiblioteca.setNombre(biblioteca.getNombre());
            existingBiblioteca.setDireccion(biblioteca.getDireccion());
            existingBiblioteca.setAnioApertura(biblioteca.getAnioApertura());

            Biblioteca updateBiblioteca = bibliotecaService.updateBiblioteca(existingBiblioteca);
            return new ResponseEntity<>(updateBiblioteca,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/biblioteca/{id}")
    public ResponseEntity<Void> deleteBiblioteca(@PathVariable long id){
        Optional<Biblioteca> optionalBiblioteca = bibliotecaService.getBibliotecaById(id);
        if (optionalBiblioteca.isPresent()){
            bibliotecaService.deleteBibliotecaById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/biblioteca/nom")
    public ResponseEntity<List<Biblioteca>> getEjemplosPorNombre(@RequestParam String nombre){
        List<Biblioteca> bibliotecas = bibliotecaService.getEjemplosByNombre(nombre);
        if (!bibliotecas.isEmpty()){
            return new ResponseEntity<>(bibliotecas,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
