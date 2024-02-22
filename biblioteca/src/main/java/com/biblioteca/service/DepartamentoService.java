package com.biblioteca.service;
import com.biblioteca.exceptions.DepartamentoBadRequestException;
import com.biblioteca.exceptions.DepartamentoException;
import com.biblioteca.exceptions.DepartamentoNotFoundException;
import com.biblioteca.model.Departamento;
import com.biblioteca.repository.BibliotecaRepository;
import com.biblioteca.repository.DepartamentoRepository;
import com.biblioteca.util.ImageUtils;
import com.biblioteca.model.Biblioteca;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
@Service
public class DepartamentoService {
    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private BibliotecaRepository bibliotecaRepository;
    public List<Departamento> getAllDepartamentos() {
        return departamentoRepository.findAll();
    }

    public Departamento createDepartamento(String nombre, String descripcion, Long id_biblioteca, MultipartFile file) throws IOException, DepartamentoException {
        // Verificar si los campos obligatorios están presentes
        if (nombre == null || nombre.isEmpty())
            throw new DepartamentoBadRequestException("Debe introducirse el nombre");
        if (descripcion == null || descripcion.isEmpty())
            throw new DepartamentoBadRequestException("Debe introducirse la descripción");

        // Verificar si el ID de la biblioteca es válido
        if (id_biblioteca == null || id_biblioteca <= 0) {
            throw new DepartamentoException("El ID de la biblioteca proporcionado no es válido: " + id_biblioteca);
        }

        // Verificar si el ID de la biblioteca existe en la base de datos
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(id_biblioteca);
        if (!bibliotecaOptional.isPresent()) {
            throw new DepartamentoException("El ID de la biblioteca proporcionado no existe en la base de datos: " + id_biblioteca);
        }

        // Crear una instancia de Departamento con los datos proporcionados y el ID de la biblioteca
        Departamento departamento = new Departamento(nombre, descripcion, null, null, id_biblioteca);

        // Verificar si se proporcionó una imagen
        if (file != null && !file.isEmpty()) {
            departamento.setImagenLocal(file.getOriginalFilename());
            departamento.setImagenBlob(file.getBytes()); // Guardar los bytes de la imagen directamente

            // Opcionalmente, también puedes guardar la imagen en el sistema de archivos
            Path dirImg = Paths.get("src//main//resources//static//img");
            String rutaAbsoluta = dirImg.toFile().getAbsolutePath();

            try {
                // Guardar la imagen en el sistema de archivos
                byte[] bytesImg = file.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + file.getOriginalFilename());
                Files.write(rutaCompleta, bytesImg);
            } catch (IOException e) {
                // Manejar cualquier error de escritura
                throw new DepartamentoException("Error al escribir la imagen");
            }
        }

        // Guardar el departamento en la base de datos
        return departamentoRepository.save(departamento);
    }

    public Optional<Departamento> getDepartamentoById(long id) {
        return Optional.ofNullable(departamentoRepository.findById(id).orElseThrow(()-> new DepartamentoNotFoundException("No se ha encontrado el departamento con id:" + id)));
    }

    public Departamento updateDepartamento(Departamento departamento, MultipartFile file) throws IOException, DepartamentoException {
        if (!file.isEmpty()) {
            departamento.setImagenLocal(file.getOriginalFilename());
            departamento.setImagenBlob(ImageUtils.compressImage(file.getBytes()));

            // Ruta donde se guardará la imagen
            Path dirImg = Paths.get("src//main//resources//static//img");
            String rutaAbsoluta = dirImg.toFile().getAbsolutePath();

            try {
                // Guardar la imagen en el sistema de archivos
                byte[] bytesImg = file.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + file.getOriginalFilename());
                Files.write(rutaCompleta, bytesImg);
            } catch (IOException e) {
                // Manejar cualquier error de escritura
                throw new DepartamentoException("Error de escritura en el archivo");
            }
        }

        return departamentoRepository.save(departamento);
    }

    public void deleteDepartamentoById(long id) {
        departamentoRepository.deleteById(id);
    }
    public byte[] descargarFoto(long id) throws DataFormatException, IOException {
        Departamento departamento = departamentoRepository.findById(id).orElse(null);
        return departamento != null ? ImageUtils.decompressImage(departamento.getImagenBlob()) : null;
    }
}
