package org.villacorta.springcloud.msvc.usuarios.controllers;

import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.villacorta.springcloud.msvc.usuarios.clients.CursoClientRest;
import org.villacorta.springcloud.msvc.usuarios.models.entity.Usuario;
import org.villacorta.springcloud.msvc.usuarios.services.UsuarioService;

import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {

        Usuario usuario = usuarioService.porId(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id: " + id));
        return ResponseEntity.ok().body(usuario);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Usuario usuario, BindingResult result) {

        if (result.hasErrors()) {
            return validar(result);
        }

        if (usuario.getEmail() != null && usuarioService.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Collections.singleton(Map.of("error", "Ya existe un usuario con el email: " + usuario.getEmail())));
        }



        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable Long id, BindingResult result, @RequestBody Usuario usuario) {

        if (result.hasErrors()) {
            return validar(result);
        }

        Optional<Usuario> usuarioOptional = usuarioService.porId(id);

        if (usuarioOptional.isPresent()) {



            Usuario usuarioUpdate = usuarioOptional.get();

            if (!usuarioUpdate.getEmail().isEmpty() && usuarioService.findByEmail(usuarioUpdate.getEmail()).isPresent() && !usuarioUpdate.getEmail().equalsIgnoreCase(usuario.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Ya existe un usuario con el email: " + usuario.getEmail()));
            }

            usuarioUpdate.setNombre(usuario.getNombre());
            usuarioUpdate.setEmail(usuario.getEmail());
            usuarioUpdate.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuarioUpdate));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> delete(@PathVariable Long id) {
        Optional<Usuario> o = usuarioService.porId(id);

        if (o.isPresent()) {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-curso")
    public List<Usuario> listarUsuariosPorIds(@RequestParam List<Long> ids) {
        return usuarioService.listarUsuariosPorIds(ids);
    }

    private static @NonNull ResponseEntity<?> validar(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo: " + err.getField() + " " + err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

}
