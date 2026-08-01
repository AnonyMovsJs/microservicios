package org.villacorta.springcloud.msvc.cursos.services;

import org.villacorta.springcloud.msvc.cursos.models.Usuario;
import org.villacorta.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Optional<Curso> porIdConUsuarios(Long id);
    Curso guardar(Curso curso);
    void eliminar(Long id);

    void eliminarCursoUsuarioPorId(Long id);


    //Microservicio

    Optional<Usuario> asignarUsuarioCurso(Usuario usuario, Long id);
    Optional<Usuario> crearUsuario(Usuario usuario, Long id);
    Optional<Usuario> eliminarUsuarioCurso(Usuario usuario, Long id);


}
