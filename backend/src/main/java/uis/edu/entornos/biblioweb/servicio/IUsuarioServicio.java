package uis.edu.entornos.biblioweb.servicio;

import java.util.List;
import uis.edu.entornos.biblioweb.modelo.Usuario;

public interface IUsuarioServicio {
    List<Usuario> listar();
    List<Usuario> listarLectores();
    Usuario buscar(Long id);
    Usuario crear(Usuario usuario);
    Usuario actualizar(Long id, Usuario usuario);
    Usuario inactivar(Long id);
}
