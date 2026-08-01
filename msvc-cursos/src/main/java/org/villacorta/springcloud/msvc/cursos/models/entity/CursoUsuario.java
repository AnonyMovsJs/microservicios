package org.villacorta.springcloud.msvc.cursos.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "curso_usuarios")
public class CursoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

    private Long usuarioId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof CursoUsuario)) return false; /*Pregunta si el obj es un CursoUsuario*/

        CursoUsuario o = (CursoUsuario)obj;
        return this.usuarioId != null && this.usuarioId.equals(o.usuarioId); /*Si son iguales entonces puede hacer la comparacion por id*/

    }
}
