package uis.edu.entornos.biblioweb.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uis.edu.entornos.biblioweb.modelo.Usuario;
import uis.edu.entornos.biblioweb.security.SesionServicio;
import uis.edu.entornos.biblioweb.servicio.IUsuarioServicio;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Administración de usuarios y lectores")
public class UsuarioControlador {
    private final IUsuarioServicio usuarioServicio;
    private final SesionServicio sesionServicio;

    public UsuarioControlador(IUsuarioServicio usuarioServicio, SesionServicio sesionServicio) {
        this.usuarioServicio = usuarioServicio;
        this.sesionServicio = sesionServicio;
    }

    @GetMapping
    @Operation(summary = "Listar usuarios")
    public List<Usuario> listar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization) {
        sesionServicio.exigirBibliotecario(authorization);
        return usuarioServicio.listar();
    }

    @GetMapping("/lectores")
    @Operation(summary = "Listar lectores")
    public List<Usuario> listarLectores(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization) {
        sesionServicio.exigirBibliotecario(authorization);
        return usuarioServicio.listarLectores();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por id")
    public Usuario buscar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @PathVariable Long id) {
        sesionServicio.exigirBibliotecario(authorization);
        return usuarioServicio.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Crear usuario")
    public ResponseEntity<Usuario> crear(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody Usuario usuario) {
        sesionServicio.exigirBibliotecario(authorization);
        Usuario creado = usuarioServicio.crear(usuario);
        return ResponseEntity.created(URI.create("/api/usuarios/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public Usuario actualizar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {
        sesionServicio.exigirBibliotecario(authorization);
        return usuarioServicio.actualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inactivar usuario", description = "Conserva el historial de préstamos")
    public Usuario inactivar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @PathVariable Long id) {
        sesionServicio.exigirBibliotecario(authorization);
        return usuarioServicio.inactivar(id);
    }
}
