package org.villacorta.springcloud.msvc.cursos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.villacorta.springcloud.msvc.cursos.clients.UsuarioClientRest;
import org.villacorta.springcloud.msvc.cursos.models.Usuario;
import org.villacorta.springcloud.msvc.cursos.models.entity.Curso;
import org.villacorta.springcloud.msvc.cursos.models.entity.CursoUsuario;
import org.villacorta.springcloud.msvc.cursos.repositories.CursoRepository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioClientRest usuarioClient;

    @Transactional(readOnly = true)
    @Override
    public List<Curso> listar() {
        return (List<Curso>) cursoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Curso> porId(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    public Optional<Curso> porIdConUsuarios(Long id) {

        Optional<Curso> cursoOptional = cursoRepository.findById(id);

        if (cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
             if (!curso.getCursoUsuarios().isEmpty()) {
                 List<Long> idsUsuarios = curso.getCursoUsuarios().stream().map(CursoUsuario::getUsuarioId).toList();
                 List<Usuario> usuarios =  usuarioClient.obtenerUsuariosPorIds(idsUsuarios);
                 curso.setUsuarios(usuarios);
             }

            return Optional.of(curso);
        }

        return Optional.empty();
    }

    @Transactional
    @Override
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        cursoRepository.eliminarCursoUsuarioPorId(id);
    }

    @Transactional
    @Override
    public Optional<Usuario> asignarUsuarioCurso(Usuario usuario, Long idCurso) {

        Optional<Curso> cursoOptional = cursoRepository.findById(idCurso);

        if(cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
            Usuario usuarioMsvc = usuarioClient.detalle(usuario.getId());

            CursoUsuario cursoUsuario = new  CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario); //Preguntar esto, guarda un cursoUsuario en curso que al mismo time se guarda enla list de entity de CursoUsuario.
            cursoRepository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Usuario> crearUsuario(Usuario usuario, Long idCurso) {

        Optional<Curso> cursoOptional = cursoRepository.findById(idCurso);
        if(cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
            Usuario usuarioNuevoMsvc = usuarioClient.save(usuario);
            CursoUsuario cursoUsuario = new  CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioNuevoMsvc);
        }

        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Usuario> eliminarUsuarioCurso(Usuario usuario, Long idCurso) {

        Optional<Curso> cursoOptional = cursoRepository.findById(idCurso);

        if(cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
            Usuario usuarioMsvc = usuarioClient.detalle(usuario.getId());

            CursoUsuario cursoUsuario = new  CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.removeCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }


}
