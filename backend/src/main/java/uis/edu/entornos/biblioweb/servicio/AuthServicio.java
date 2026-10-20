package uis.edu.entornos.biblioweb.servicio;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uis.edu.entornos.biblioweb.dto.LoginRequest;
import uis.edu.entornos.biblioweb.dto.LoginResponse;
import uis.edu.entornos.biblioweb.dto.SesionUsuario;
import uis.edu.entornos.biblioweb.exception.CredencialesInvalidasException;
import uis.edu.entornos.biblioweb.modelo.EstadoUsuario;
import uis.edu.entornos.biblioweb.modelo.Usuario;
import uis.edu.entornos.biblioweb.repositorio.UsuarioRepositorio;
import uis.edu.entornos.biblioweb.security.SesionServicio;

@Service
public class AuthServicio {
    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final SesionServicio sesionServicio;

    public AuthServicio(UsuarioRepositorio usuarioRepositorio,
                        PasswordEncoder passwordEncoder,
                        SesionServicio sesionServicio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
        this.sesionServicio = sesionServicio;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepositorio
                .buscarActivoPorCorreo(request.getCorreo().trim(), EstadoUsuario.ACTIVO)
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getClaveHash())) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
        }

        SesionUsuario sesion = sesionServicio.crear(usuario);
        return new LoginResponse(
                sesion.getToken(),
                sesion.getExpiraEn(),
                sesion.getUsuarioId(),
                sesion.getNombre(),
                sesion.getCorreo(),
                sesion.getRol()
        );
    }
}
