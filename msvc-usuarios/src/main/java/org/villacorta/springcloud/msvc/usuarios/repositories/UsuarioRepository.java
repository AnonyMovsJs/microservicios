package org.villacorta.springcloud.msvc.usuarios.repositories;

import org.springframework.data.repository.CrudRepository;
import org.villacorta.springcloud.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    List<Usuario> findAllById(Iterable<Long> ids);
}
