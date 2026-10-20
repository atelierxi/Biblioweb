package uis.edu.entornos.biblioweb.servicio;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uis.edu.entornos.biblioweb.exception.RecursoNoEncontradoException;
import uis.edu.entornos.biblioweb.exception.ReglaNegocioException;
import uis.edu.entornos.biblioweb.exception.SolicitudInvalidaException;
import uis.edu.entornos.biblioweb.modelo.EstadoUsuario;
import uis.edu.entornos.biblioweb.modelo.RolUsuario;
import uis.edu.entornos.biblioweb.modelo.Usuario;
import uis.edu.entornos.biblioweb.repositorio.UsuarioRepositorio;

@Service
@Transactional
public class UsuarioServicio implements IUsuarioServicio {
    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepositorio.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarLectores() {
        return usuarioRepositorio.findByRolOrderByApellidosAscNombresAsc(RolUsuario.LECTOR);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscar(Long id) {
        return usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario con id " + id));
    }

    @Override
    public Usuario crear(Usuario usuario) {
        String correo = usuario.getCorreo().trim().toLowerCase();
        if (usuarioRepositorio.existsByCorreoIgnoreCase(correo)) {
            throw new ReglaNegocioException("Ya existe un usuario registrado con el correo " + correo);
        }
        if (!StringUtils.hasText(usuario.getPassword())) {
            throw new SolicitudInvalidaException("La contraseña es obligatoria al crear un usuario");
        }
        usuario.setId(null);
        usuario.setCorreo(correo);
        usuario.setRol(usuario.getRol() == null ? RolUsuario.LECTOR : usuario.getRol());
        usuario.setEstado(usuario.getEstado() == null ? EstadoUsuario.ACTIVO : usuario.getEstado());
        usuario.setClaveHash(passwordEncoder.encode(usuario.getPassword()));
        usuario.setPassword(null);
        return usuarioRepositorio.save(usuario);
    }

    @Override
    public Usuario actualizar(Long id, Usuario datos) {
        Usuario actual = buscar(id);
        String correo = datos.getCorreo().trim().toLowerCase();
        if (usuarioRepositorio.existsByCorreoIgnoreCaseAndIdNot(correo, id)) {
            throw new ReglaNegocioException("Ya existe otro usuario con el correo " + correo);
        }
        actual.setNombres(datos.getNombres());
        actual.setApellidos(datos.getApellidos());
        actual.setCorreo(correo);
        actual.setRol(datos.getRol() == null ? actual.getRol() : datos.getRol());
        actual.setEstado(datos.getEstado() == null ? actual.getEstado() : datos.getEstado());
        if (StringUtils.hasText(datos.getPassword())) {
            actual.setClaveHash(passwordEncoder.encode(datos.getPassword()));
        }
        return usuarioRepositorio.save(actual);
    }

    @Override
    public Usuario inactivar(Long id) {
        Usuario usuario = buscar(id);
        usuario.setEstado(EstadoUsuario.INACTIVO);
        return usuarioRepositorio.save(usuario);
    }
}
