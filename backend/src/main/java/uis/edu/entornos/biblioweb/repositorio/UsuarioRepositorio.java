package uis.edu.entornos.biblioweb.repositorio;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uis.edu.entornos.biblioweb.modelo.EstadoUsuario;
import uis.edu.entornos.biblioweb.modelo.RolUsuario;
import uis.edu.entornos.biblioweb.modelo.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    boolean existsByCorreoIgnoreCase(String correo);
    boolean existsByCorreoIgnoreCaseAndIdNot(String correo, Long id);
    List<Usuario> findByRolOrderByApellidosAscNombresAsc(RolUsuario rol);

    @Query("select u from Usuario u where lower(u.correo) = lower(:correo) and u.estado = :estado")
    Optional<Usuario> buscarActivoPorCorreo(@Param("correo") String correo,
                                             @Param("estado") EstadoUsuario estado);
}
