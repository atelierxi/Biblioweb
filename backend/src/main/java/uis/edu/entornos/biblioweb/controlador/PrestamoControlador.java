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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uis.edu.entornos.biblioweb.dto.SesionUsuario;
import uis.edu.entornos.biblioweb.modelo.Prestamo;
import uis.edu.entornos.biblioweb.security.SesionServicio;
import uis.edu.entornos.biblioweb.servicio.IPrestamoServicio;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/prestamos")
@Tag(name = "Préstamos", description = "Registro, consulta y devolución de préstamos")
public class PrestamoControlador {
    private final IPrestamoServicio prestamoServicio;
    private final SesionServicio sesionServicio;

    public PrestamoControlador(IPrestamoServicio prestamoServicio, SesionServicio sesionServicio) {
        this.prestamoServicio = prestamoServicio;
        this.sesionServicio = sesionServicio;
    }

    @GetMapping
    @Operation(summary = "Listar todos los préstamos")
    public List<Prestamo> listar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization) {
        sesionServicio.exigirBibliotecario(authorization);
        return prestamoServicio.listar();
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar préstamos activos")
    public List<Prestamo> listarActivos(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization) {
        sesionServicio.exigirBibliotecario(authorization);
        return prestamoServicio.listarActivos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar préstamo por id")
    public Prestamo buscar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @PathVariable Long id) {
        sesionServicio.exigirBibliotecario(authorization);
        return prestamoServicio.buscar(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Consultar préstamos de un usuario")
    public List<Prestamo> porUsuario(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @PathVariable Long usuarioId) {
        sesionServicio.exigirBibliotecario(authorization);
        return prestamoServicio.listarPorUsuario(usuarioId);
    }

    @GetMapping("/mis-prestamos")
    @Operation(summary = "Consultar mis préstamos", description = "Devuelve únicamente los préstamos del usuario autenticado")
    public List<Prestamo> misPrestamos(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization) {
        SesionUsuario sesion = sesionServicio.obtenerDesdeHeader(authorization);
        return prestamoServicio.listarPorUsuario(sesion.getUsuarioId());
    }

    @PostMapping
    @Operation(summary = "Registrar préstamo")
    public ResponseEntity<Prestamo> crear(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody Prestamo prestamo) {
        sesionServicio.exigirBibliotecario(authorization);
        Prestamo creado = prestamoServicio.crear(prestamo);
        return ResponseEntity.created(URI.create("/api/prestamos/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar fechas de un préstamo activo")
    public Prestamo actualizar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody Prestamo prestamo) {
        sesionServicio.exigirBibliotecario(authorization);
        return prestamoServicio.actualizar(id, prestamo);
    }

    @PatchMapping("/{id}/devolver")
    @Operation(summary = "Registrar devolución", description = "Marca el préstamo DEVUELTO y el libro DISPONIBLE")
    public Prestamo devolver(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @PathVariable Long id) {
        sesionServicio.exigirBibliotecario(authorization);
        return prestamoServicio.devolver(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar préstamo devuelto", description = "Uso administrativo para corregir un registro; un préstamo activo no puede eliminarse")
    public ResponseEntity<Void> eliminar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @PathVariable Long id) {
        sesionServicio.exigirBibliotecario(authorization);
        prestamoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
