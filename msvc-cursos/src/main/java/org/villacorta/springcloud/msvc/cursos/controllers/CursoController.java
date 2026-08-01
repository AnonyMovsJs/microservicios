package org.villacorta.springcloud.msvc.cursos.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.villacorta.springcloud.msvc.cursos.models.Usuario;
import org.villacorta.springcloud.msvc.cursos.models.entity.Curso;
import org.villacorta.springcloud.msvc.cursos.services.CursoService;

import java.util.*;

@RestController
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public List<Curso> listar() {
        return cursoService.listar();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Curso> porId(@PathVariable Long id) {
        Optional<Curso> cursoOptional =  cursoService.porIdConUsuarios(id);//cursoService.porId(id);

        if(cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
            return ResponseEntity.ok(curso);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Curso curso, BindingResult result) {

        if (result.hasErrors()) {
            return validar(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(curso));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @PathVariable Long id, BindingResult result, @RequestBody Curso curso) {

        if (result.hasErrors()) {
            return validar(result);
        }

        Optional<Curso> cursoOptional = cursoService.porId(id);

        if (cursoOptional.isPresent()) {
            Curso cursoEdit = cursoOptional.get();
            cursoEdit.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursoEdit));
        }

        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Curso> eliminar(@PathVariable Long id) {
        Optional<Curso> cursoOptional = cursoService.porId(id);

        if (cursoOptional.isPresent()) {
            cursoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuarioCurso(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioOptional;

        try {

            usuarioOptional = cursoService.asignarUsuarioCurso(usuario, cursoId);
        }catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por el id " +
                    "o hay un error en la comunicación: " + e.getMessage()));
        }

        if (usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioOptional.get());
        }

        return  ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioOptional;

        try {

            usuarioOptional = cursoService.crearUsuario(usuario, cursoId);
        }catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No se pudo crear el usuario " +
                            "o hay un error en la comunicación: " + e.getMessage()));
        }

        if (usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioOptional.get());
        }

        return  ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuarioCurso(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioOptional;

        try {

            usuarioOptional = cursoService.eliminarUsuarioCurso(usuario, cursoId);
        }catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por el id " +
                            "o hay un error en la comunicación: " + e.getMessage()));
        }

        if (usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
        }

        return  ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    private ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id) {
        cursoService.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    private static @NonNull ResponseEntity<?> validar(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo: " + err.getField() + " " + err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

}
