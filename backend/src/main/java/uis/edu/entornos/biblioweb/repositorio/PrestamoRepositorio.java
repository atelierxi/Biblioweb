package uis.edu.entornos.biblioweb.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uis.edu.entornos.biblioweb.modelo.EstadoPrestamo;
import uis.edu.entornos.biblioweb.modelo.Prestamo;

public interface PrestamoRepositorio extends JpaRepository<Prestamo, Long> {
    boolean existsByLibroIdAndEstado(Long libroId, EstadoPrestamo estado);
    boolean existsByUsuarioId(Long usuarioId);
    boolean existsByLibroId(Long libroId);
    List<Prestamo> findByEstadoOrderByFechaLimiteAsc(EstadoPrestamo estado);

    @Query("select p from Prestamo p where p.usuario.id = :usuarioId order by p.fechaPrestamo desc, p.id desc")
    List<Prestamo> buscarPorUsuario(@Param("usuarioId") Long usuarioId);
}
