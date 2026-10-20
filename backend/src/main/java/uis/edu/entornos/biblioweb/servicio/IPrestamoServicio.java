package uis.edu.entornos.biblioweb.servicio;

import java.util.List;
import uis.edu.entornos.biblioweb.modelo.Prestamo;

public interface IPrestamoServicio {
    List<Prestamo> listar();
    List<Prestamo> listarActivos();
    List<Prestamo> listarPorUsuario(Long usuarioId);
    Prestamo buscar(Long id);
    Prestamo crear(Prestamo prestamo);
    Prestamo actualizar(Long id, Prestamo prestamo);
    Prestamo devolver(Long id);
    void eliminar(Long id);
}
