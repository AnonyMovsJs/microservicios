package org.villacorta.springcloud.msvc.usuarios.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.villacorta.springcloud.msvc.usuarios.clients.CursoClientRest;
import org.villacorta.springcloud.msvc.usuarios.models.entity.Usuario;
import org.villacorta.springcloud.msvc.usuarios.repositories.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoClientRest cursoClient;

    @Transactional(readOnly = true)
    @Override
    public List<Usuario> listar() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Usuario> porId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }


    @Transactional
    @Override
    public void eliminar(Long id) {

        usuarioRepository.deleteById(id);
        cursoClient.eliminarCursoUsuarioPorId(id);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public List<Usuario> listarUsuariosPorIds(Iterable<Long> ids) {
        return usuarioRepository.findAllById(ids);
    }


}
