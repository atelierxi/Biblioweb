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
import uis.edu.entornos.biblioweb.modelo.Categoria;
import uis.edu.entornos.biblioweb.security.SesionServicio;
import uis.edu.entornos.biblioweb.servicio.ICategoriaServicio;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/categorias")
@Tag(name = "Categorías", description = "CRUD de categorías del catálogo")
public class CategoriaControlador {
    private final ICategoriaServicio categoriaServicio;
    private final SesionServicio sesionServicio;

    public CategoriaControlador(ICategoriaServicio categoriaServicio, SesionServicio sesionServicio) {
        this.categoriaServicio = categoriaServicio;
        this.sesionServicio = sesionServicio;
    }

    @GetMapping
    @Operation(summary = "Listar categorías")
    public List<Categoria> listar() {
        return categoriaServicio.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoría por id")
    public Categoria buscar(@PathVariable Long id) {
        return categoriaServicio.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Crear categoría")
    public ResponseEntity<Categoria> crear(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody Categoria categoria) {
        sesionServicio.exigirBibliotecario(authorization);
        Categoria creada = categoriaServicio.crear(categoria);
        return ResponseEntity.created(URI.create("/api/categorias/" + creada.getId())).body(creada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría")
    public Categoria actualizar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @Valid @RequestBody Categoria categoria) {
        sesionServicio.exigirBibliotecario(authorization);
        return categoriaServicio.actualizar(id, categoria);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Solo se elimina si no tiene libros asociados")
    public ResponseEntity<Void> eliminar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization,
            @PathVariable Long id) {
        sesionServicio.exigirBibliotecario(authorization);
        categoriaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
