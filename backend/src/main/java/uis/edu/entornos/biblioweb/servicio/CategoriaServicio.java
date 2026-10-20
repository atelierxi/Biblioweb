package uis.edu.entornos.biblioweb.servicio;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uis.edu.entornos.biblioweb.exception.RecursoNoEncontradoException;
import uis.edu.entornos.biblioweb.exception.ReglaNegocioException;
import uis.edu.entornos.biblioweb.modelo.Categoria;
import uis.edu.entornos.biblioweb.repositorio.CategoriaRepositorio;
import uis.edu.entornos.biblioweb.repositorio.LibroRepositorio;

@Service
@Transactional
public class CategoriaServicio implements ICategoriaServicio {
    private final CategoriaRepositorio categoriaRepositorio;
    private final LibroRepositorio libroRepositorio;

    public CategoriaServicio(CategoriaRepositorio categoriaRepositorio, LibroRepositorio libroRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
        this.libroRepositorio = libroRepositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listar() {
        return categoriaRepositorio.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria buscar(Long id) {
        return categoriaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la categoría con id " + id));
    }

    @Override
    public Categoria crear(Categoria categoria) {
        String nombre = categoria.getNombre().trim();
        if (categoriaRepositorio.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException("Ya existe una categoría con el nombre " + nombre);
        }
        categoria.setId(null);
        categoria.setNombre(nombre);
        return categoriaRepositorio.save(categoria);
    }

    @Override
    public Categoria actualizar(Long id, Categoria datos) {
        Categoria actual = buscar(id);
        String nombre = datos.getNombre().trim();
        if (categoriaRepositorio.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new ReglaNegocioException("Ya existe otra categoría con el nombre " + nombre);
        }
        actual.setNombre(nombre);
        actual.setDescripcion(datos.getDescripcion());
        return categoriaRepositorio.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        Categoria categoria = buscar(id);
        if (libroRepositorio.existsByCategoriaId(id)) {
            throw new ReglaNegocioException("No se puede eliminar la categoría porque tiene libros asociados");
        }
        categoriaRepositorio.delete(categoria);
    }
}
