package uis.edu.entornos.biblioweb.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import uis.edu.entornos.biblioweb.modelo.Categoria;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
