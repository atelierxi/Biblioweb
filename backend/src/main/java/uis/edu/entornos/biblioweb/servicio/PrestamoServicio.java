package uis.edu.entornos.biblioweb.servicio;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uis.edu.entornos.biblioweb.exception.RecursoNoEncontradoException;
import uis.edu.entornos.biblioweb.exception.ReglaNegocioException;
import uis.edu.entornos.biblioweb.modelo.EstadoLibro;
import uis.edu.entornos.biblioweb.modelo.EstadoPrestamo;
import uis.edu.entornos.biblioweb.modelo.EstadoUsuario;
import uis.edu.entornos.biblioweb.modelo.Libro;
import uis.edu.entornos.biblioweb.modelo.Prestamo;
import uis.edu.entornos.biblioweb.modelo.RolUsuario;
import uis.edu.entornos.biblioweb.modelo.Usuario;
import uis.edu.entornos.biblioweb.repositorio.LibroRepositorio;
import uis.edu.entornos.biblioweb.repositorio.PrestamoRepositorio;
import uis.edu.entornos.biblioweb.repositorio.UsuarioRepositorio;

@Service
@Transactional
public class PrestamoServicio implements IPrestamoServicio {
    private final PrestamoRepositorio prestamoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final LibroRepositorio libroRepositorio;

    public PrestamoServicio(PrestamoRepositorio prestamoRepositorio,
                            UsuarioRepositorio usuarioRepositorio,
                            LibroRepositorio libroRepositorio) {
        this.prestamoRepositorio = prestamoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.libroRepositorio = libroRepositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prestamo> listar() {
        return prestamoRepositorio.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prestamo> listarActivos() {
        return prestamoRepositorio.findByEstadoOrderByFechaLimiteAsc(EstadoPrestamo.ACTIVO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prestamo> listarPorUsuario(Long usuarioId) {
        if (!usuarioRepositorio.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("No se encontró el usuario con id " + usuarioId);
        }
        return prestamoRepositorio.buscarPorUsuario(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Prestamo buscar(Long id) {
        return prestamoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el préstamo con id " + id));
    }

    @Override
    public Prestamo crear(Prestamo prestamo) {
        Usuario lector = obtenerLector(prestamo);
        Libro libro = obtenerLibro(prestamo);

        if (lector.getRol() != RolUsuario.LECTOR || lector.getEstado() != EstadoUsuario.ACTIVO) {
            throw new ReglaNegocioException("Solo un lector ACTIVO puede recibir préstamos");
        }
        if (libro.getEstado() != EstadoLibro.DISPONIBLE) {
            throw new ReglaNegocioException("El libro no está disponible para préstamo");
        }
        if (prestamoRepositorio.existsByLibroIdAndEstado(libro.getId(), EstadoPrestamo.ACTIVO)) {
            throw new ReglaNegocioException("El libro ya tiene un préstamo activo");
        }

        LocalDate fechaPrestamo = prestamo.getFechaPrestamo() == null ? LocalDate.now() : prestamo.getFechaPrestamo();
        LocalDate fechaLimite = prestamo.getFechaLimite() == null ? fechaPrestamo.plusDays(14) : prestamo.getFechaLimite();
        validarFechas(fechaPrestamo, fechaLimite);

        prestamo.setId(null);
        prestamo.setUsuario(lector);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaLimite(fechaLimite);
        prestamo.setFechaDevolucion(null);
        prestamo.setEstado(EstadoPrestamo.ACTIVO);

        libro.setEstado(EstadoLibro.PRESTADO);
        libroRepositorio.save(libro);
        return prestamoRepositorio.save(prestamo);
    }

    @Override
    public Prestamo actualizar(Long id, Prestamo datos) {
        Prestamo actual = buscar(id);
        if (actual.getEstado() != EstadoPrestamo.ACTIVO) {
            throw new ReglaNegocioException("Solo se pueden ajustar las fechas de un préstamo ACTIVO");
        }
        LocalDate fechaPrestamo = datos.getFechaPrestamo() == null ? actual.getFechaPrestamo() : datos.getFechaPrestamo();
        LocalDate fechaLimite = datos.getFechaLimite() == null ? actual.getFechaLimite() : datos.getFechaLimite();
        validarFechas(fechaPrestamo, fechaLimite);
        actual.setFechaPrestamo(fechaPrestamo);
        actual.setFechaLimite(fechaLimite);
        return prestamoRepositorio.save(actual);
    }

    @Override
    public Prestamo devolver(Long id) {
        Prestamo prestamo = buscar(id);
        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new ReglaNegocioException("El préstamo ya fue devuelto");
        }
        prestamo.setFechaDevolucion(LocalDate.now());
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        Libro libro = prestamo.getLibro();
        libro.setEstado(EstadoLibro.DISPONIBLE);
        libroRepositorio.save(libro);
        return prestamoRepositorio.save(prestamo);
    }

    @Override
    public void eliminar(Long id) {
        Prestamo prestamo = buscar(id);
        if (prestamo.getEstado() == EstadoPrestamo.ACTIVO) {
            throw new ReglaNegocioException("No se puede eliminar un préstamo activo; primero registre la devolución");
        }
        prestamoRepositorio.delete(prestamo);
    }

    private Usuario obtenerLector(Prestamo prestamo) {
        if (prestamo.getUsuario() == null || prestamo.getUsuario().getId() == null) {
            throw new ReglaNegocioException("Debe seleccionar un lector");
        }
        return usuarioRepositorio.findById(prestamo.getUsuario().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el lector con id " + prestamo.getUsuario().getId()));
    }

    private Libro obtenerLibro(Prestamo prestamo) {
        if (prestamo.getLibro() == null || prestamo.getLibro().getId() == null) {
            throw new ReglaNegocioException("Debe seleccionar un libro");
        }
        return libroRepositorio.findById(prestamo.getLibro().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el libro con id " + prestamo.getLibro().getId()));
    }

    private void validarFechas(LocalDate fechaPrestamo, LocalDate fechaLimite) {
        if (fechaLimite.isBefore(fechaPrestamo)) {
            throw new ReglaNegocioException("La fecha límite no puede ser anterior a la fecha de préstamo");
        }
    }
}
