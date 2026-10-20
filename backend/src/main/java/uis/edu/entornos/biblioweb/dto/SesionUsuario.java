package uis.edu.entornos.biblioweb.dto;

import java.time.LocalDateTime;
import uis.edu.entornos.biblioweb.modelo.RolUsuario;

public class SesionUsuario {
    private String token;
    private Long usuarioId;
    private String nombre;
    private String correo;
    private RolUsuario rol;
    private LocalDateTime expiraEn;

    public SesionUsuario() {
    }

    public SesionUsuario(String token, Long usuarioId, String nombre, String correo,
                         RolUsuario rol, LocalDateTime expiraEn) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.expiraEn = expiraEn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public LocalDateTime getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(LocalDateTime expiraEn) {
        this.expiraEn = expiraEn;
    }
}
