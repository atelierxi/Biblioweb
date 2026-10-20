package uis.edu.entornos.biblioweb.servicio;

import java.util.List;
import uis.edu.entornos.biblioweb.modelo.Libro;

public interface ILibroServicio {
    List<Libro> listar();
    List<Libro> disponibles();
    List<Libro> buscarCatalogo(String texto);
    Libro buscar(Long id);
    Libro crear(Libro libro);
    Libro actualizar(Long id, Libro libro);
    Libro inactivar(Long id);
}
