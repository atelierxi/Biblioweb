package uis.edu.entornos.biblioweb.security;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import uis.edu.entornos.biblioweb.dto.SesionUsuario;
import uis.edu.entornos.biblioweb.exception.AccesoNoAutorizadoException;
import uis.edu.entornos.biblioweb.exception.AccesoProhibidoException;
import uis.edu.entornos.biblioweb.modelo.RolUsuario;
import uis.edu.entornos.biblioweb.modelo.Usuario;

@Service
public class SesionServicio {
    private static final int HORAS_SESION = 8;
    private final Map<String, SesionUsuario> sesiones = new ConcurrentHashMap<>();

    public SesionUsuario crear(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiraEn = LocalDateTime.now().plusHours(HORAS_SESION);
        SesionUsuario sesion = new SesionUsuario(
                token,
                usuario.getId(),
                usuario.getNombres() + " " + usuario.getApellidos(),
                usuario.getCorreo(),
                usuario.getRol(),
                expiraEn
        );
        sesiones.put(token, sesion);
        return sesion;
    }

    public SesionUsuario obtenerDesdeHeader(String authorization) {
        String token = extraerToken(authorization);
        SesionUsuario sesion = sesiones.get(token);
        if (sesion == null) {
            throw new AccesoNoAutorizadoException("La sesión no existe o ya fue cerrada");
        }
        if (sesion.getExpiraEn().isBefore(LocalDateTime.now())) {
            sesiones.remove(token);
            throw new AccesoNoAutorizadoException("La sesión ha expirado");
        }
        return sesion;
    }

    public SesionUsuario exigirBibliotecario(String authorization) {
        SesionUsuario sesion = obtenerDesdeHeader(authorization);
        if (sesion.getRol() != RolUsuario.BIBLIOTECARIO) {
            throw new AccesoProhibidoException("Esta operación requiere rol BIBLIOTECARIO");
        }
        return sesion;
    }

    public void cerrar(String authorization) {
        String token = extraerToken(authorization);
        sesiones.remove(token);
    }

    private String extraerToken(String authorization) {
        if (authorization == null || authorization.isBlank() || !authorization.startsWith("Bearer ")) {
            throw new AccesoNoAutorizadoException("Debe enviar el encabezado Authorization: Bearer <token>");
        }
        String token = authorization.substring(7).trim();
        if (token.isBlank()) {
            throw new AccesoNoAutorizadoException("El token de sesión está vacío");
        }
        return token;
    }
}
