package uis.edu.entornos.biblioweb.servicio;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uis.edu.entornos.biblioweb.exception.RecursoNoEncontradoException;
import uis.edu.entornos.biblioweb.exception.ReglaNegocioException;
import uis.edu.entornos.biblioweb.modelo.Categoria;
import uis.edu.entornos.biblioweb.modelo.EstadoLibro;
import uis.edu.entornos.biblioweb.modelo.EstadoPrestamo;
import uis.edu.entornos.biblioweb.modelo.Libro;
import uis.edu.entornos.biblioweb.repositorio.CategoriaRepositorio;
import uis.edu.entornos.biblioweb.repositorio.LibroRepositorio;
import uis.edu.entornos.biblioweb.repositorio.PrestamoRepositorio;

@Service
@Transactional
public class LibroServicio implements ILibroServicio {
    private final LibroRepositorio libroRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final PrestamoRepositorio prestamoRepositorio;

    public LibroServicio(LibroRepositorio libroRepositorio,
                         CategoriaRepositorio categoriaRepositorio,
                         PrestamoRepositorio prestamoRepositorio) {
        this.libroRepositorio = libroRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
        this.prestamoRepositorio = prestamoRepositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> listar() {
        return libroRepositorio.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> disponibles() {
        return libroRepositorio.findByEstadoOrderByTituloAsc(EstadoLibro.DISPONIBLE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> buscarCatalogo(String texto) {
        if (!StringUtils.hasText(texto)) {
            return listar();
        }
        return libroRepositorio.buscarCatalogo(texto.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public Libro buscar(Long id) {
        return libroRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el libro con id " + id));
    }

    @Override
    public Libro crear(Libro libro) {
        if (libroRepositorio.existsByIsbnIgnoreCase(libro.getIsbn().trim())) {
            throw new ReglaNegocioException("Ya existe un libro con el ISBN " + libro.getIsbn());
        }
        libro.setId(null);
        libro.setIsbn(libro.getIsbn().trim());
        libro.setEstado(libro.getEstado() == null ? EstadoLibro.DISPONIBLE : libro.getEstado());
        libro.setCategoria(obtenerCategoria(libro));
        return libroRepositorio.save(libro);
    }

    @Override
    public Libro actualizar(Long id, Libro datos) {
        Libro actual = buscar(id);
        String isbn = datos.getIsbn().trim();
        if (libroRepositorio.existsByIsbnIgnoreCaseAndIdNot(isbn, id)) {
            throw new ReglaNegocioException("Ya existe otro libro con el ISBN " + isbn);
        }
        actual.setTitulo(datos.getTitulo());
        actual.setAutor(datos.getAutor());
        actual.setIsbn(isbn);
        actual.setAnioPublicacion(datos.getAnioPublicacion());
        actual.setCategoria(obtenerCategoria(datos));
        if (datos.getEstado() != null && datos.getEstado() != EstadoLibro.PRESTADO) {
            if (prestamoRepositorio.existsByLibroIdAndEstado(id, EstadoPrestamo.ACTIVO)) {
                actual.setEstado(EstadoLibro.PRESTADO);
            } else {
                actual.setEstado(datos.getEstado());
            }
        }
        return libroRepositorio.save(actual);
    }

    @Override
    public Libro inactivar(Long id) {
        Libro libro = buscar(id);
        if (prestamoRepositorio.existsByLibroIdAndEstado(id, EstadoPrestamo.ACTIVO)) {
            throw new ReglaNegocioException("No se puede retirar el libro porque tiene un préstamo activo");
        }
        libro.setEstado(EstadoLibro.INACTIVO);
        return libroRepositorio.save(libro);
    }

    private Categoria obtenerCategoria(Libro libro) {
        if (libro.getCategoria() == null || libro.getCategoria().getId() == null) {
            throw new ReglaNegocioException("Debe seleccionar una categoría existente");
        }
        return categoriaRepositorio.findById(libro.getCategoria().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la categoría con id " + libro.getCategoria().getId()));
    }
}
