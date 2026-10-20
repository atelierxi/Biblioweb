package uis.edu.entornos.biblioweb.servicio;

import java.util.List;
import uis.edu.entornos.biblioweb.modelo.Categoria;

public interface ICategoriaServicio {
    List<Categoria> listar();
    Categoria buscar(Long id);
    Categoria crear(Categoria categoria);
    Categoria actualizar(Long id, Categoria categoria);
    void eliminar(Long id);
}
