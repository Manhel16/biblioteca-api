package com.biblioteca.controller;

import com.biblioteca.exceptions.DepartamentoException;
import com.biblioteca.model.Biblioteca;
import com.biblioteca.model.Departamento;
import com.biblioteca.service.DepartamentoService;
import com.biblioteca.util.ImageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api")
public class DepartamentoController {
    @Autowired
    private DepartamentoService departamentoService;


    @GetMapping("/departamentos")
    public List<Departamento> getAllDepartamentos() {
        return departamentoService.getAllDepartamentos();
    }

    @PostMapping(value = "/departamentos", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Departamento> createDepartamento(@RequestParam String nombre, @RequestParam String descripcion, @RequestParam Long id_biblioteca, @RequestPart(name = "imagenLocal", required = false) MultipartFile imagenLocal) throws IOException, DepartamentoException {
        // Verificar si el ID de la biblioteca es nulo o no válido
        if (id_biblioteca == null || id_biblioteca <= 0) {
            throw new DepartamentoException("Debe proporcionarse un ID de biblioteca válido");
        }

        // Llama al método createDepartamento del servicio pasando el ID de la biblioteca
        Departamento createdDepartamento = departamentoService.createDepartamento(nombre, descripcion, id_biblioteca, imagenLocal);

        return new ResponseEntity<>(createdDepartamento, HttpStatus.CREATED);
    }
    @Operation(summary = "obtienes un departamento",description = "obtienes un departamento con el id", tags = {"departamentos"})
    @Parameter(name = "id",description = "ID del departamento",required = true,example = "1")
    @ApiResponse(responseCode = "200",description = "departamento encontrado")
    @ApiResponse(responseCode = "404",description = "departamento no encontrado")
    @GetMapping("/departamentos/{id}")
    public ResponseEntity<Departamento> getDepartamentoById(@PathVariable long id) {
        Optional<Departamento> optionalDepartamento = departamentoService.getDepartamentoById(id);

        if (optionalDepartamento.isPresent()) {
            return new ResponseEntity<>(optionalDepartamento.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(value = "/departamentos/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Departamento> updateDepartamento(@PathVariable long id, @RequestParam String nombre, @RequestParam String descripcion, @RequestPart(name = "imagenLocal", required = false) MultipartFile imagenLocal) throws IOException, DepartamentoException {
        Optional<Departamento> optionalDepartamento = departamentoService.getDepartamentoById(id);
        if (((Optional<?>) optionalDepartamento).isPresent()) {
            Departamento existingDepartamento = optionalDepartamento.get();
            existingDepartamento.setNombre(nombre);
            existingDepartamento.setDescripcion(descripcion);
            existingDepartamento.setImagenBlob(ImageUtils.compressImage(imagenLocal.getBytes()));

            Departamento updateDepartamento = departamentoService.updateDepartamento(existingDepartamento, imagenLocal);
            return new ResponseEntity<>(updateDepartamento, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/departamentos/{id}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable long id) {
        departamentoService.deleteDepartamentoById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping(value = "/{id}/foto",produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> descargarFoto(@PathVariable Long id) throws DataFormatException, IOException {
        byte[] foto = departamentoService.descargarFoto(id);
        if (foto != null){
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(foto);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
