package uis.edu.entornos.biblioweb.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uis.edu.entornos.biblioweb.modelo.EstadoLibro;
import uis.edu.entornos.biblioweb.modelo.Libro;

public interface LibroRepositorio extends JpaRepository<Libro, Long> {
    boolean existsByIsbnIgnoreCase(String isbn);
    boolean existsByIsbnIgnoreCaseAndIdNot(String isbn, Long id);
    boolean existsByCategoriaId(Long categoriaId);
    List<Libro> findByEstadoOrderByTituloAsc(EstadoLibro estado);

    @Query("""
        select l from Libro l
        where lower(l.titulo) like lower(concat('%', :texto, '%'))
           or lower(l.autor) like lower(concat('%', :texto, '%'))
           or lower(l.categoria.nombre) like lower(concat('%', :texto, '%'))
        order by l.titulo asc
        """)
    List<Libro> buscarCatalogo(@Param("texto") String texto);
}
