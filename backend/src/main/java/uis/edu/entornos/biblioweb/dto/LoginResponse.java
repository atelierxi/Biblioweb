package uis.edu.entornos.biblioweb.dto;

import java.time.LocalDateTime;
import uis.edu.entornos.biblioweb.modelo.RolUsuario;

public class LoginResponse {
    private String token;
    private LocalDateTime expiraEn;
    private Long usuarioId;
    private String nombre;
    private String correo;
    private RolUsuario rol;

    public LoginResponse() {
    }

    public LoginResponse(String token, LocalDateTime expiraEn, Long usuarioId,
                         String nombre, String correo, RolUsuario rol) {
        this.token = token;
        this.expiraEn = expiraEn;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(LocalDateTime expiraEn) {
        this.expiraEn = expiraEn;
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
}
