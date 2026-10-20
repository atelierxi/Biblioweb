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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uis.edu.entornos.biblioweb.modelo.Libro;
import uis.edu.entornos.biblioweb.security.SesionServicio;
import uis.edu.entornos.biblioweb.servicio.ILibroServicio;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/libros")
@Tag(name = "Libros", description = "Catálogo y administración de libros")
public class LibroControlador {
    private final ILibroServicio libroServicio;
    private final SesionServicio sesionServicio;

    public LibroControlador(ILibroServicio libroServicio, SesionServicio sesionServicio) {
        this.libroServicio = libroServicio;
        this.sesionServicio = sesionServicio;
    }

    @GetMapping
    @Operation(summary = "Listar libros")
    public List<Libro> listar() {
        return libroServicio.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar libro por id")
    public Libro buscar(@PathVariable Long id) {
        return libroServicio.buscar(id);
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Listar libros disponibles")
    public List<Libro> disponibles() {
        return libroServicio.disponibles();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar catálogo", description = "Busca por título, autor o categoría")
    public List<Libro> buscarCatalogo(@RequestParam(defaultValue = "") String q) {
        return libroServicio.buscarCatalogo(q);
    }

    @PostMapping
    @Operation(summary = "Crear libro")
    public ResponseEntity<Libro> crear(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody Libro libro) {
        sesionServicio.exigirBibliotecario(authorization);
        Libro creado = libroServicio.crear(libro);
        return ResponseEntity.created(URI.create("/api/libros/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar libro")
    public Libro actualizar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @Valid @RequestBody Libro libro) {
        sesionServicio.exigirBibliotecario(authorization);
        return libroServicio.actualizar(id, libro);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Retirar libro", description = "No borra el historial; cambia el libro a INACTIVO")
    public Libro retirar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @PathVariable Long id) {
        sesionServicio.exigirBibliotecario(authorization);
        return libroServicio.inactivar(id);
    }
}
