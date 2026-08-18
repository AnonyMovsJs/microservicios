package org.villacorta.springcloud.msvc.cursos.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.villacorta.springcloud.msvc.cursos.models.Usuario;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url = "${msvc.usuarios.url}")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario detalle(@PathVariable Long id);

    @PostMapping
    Usuario save(@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> obtenerUsuariosPorIds(@RequestParam Iterable<Long> ids);
}
