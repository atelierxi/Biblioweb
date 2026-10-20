package uis.edu.entornos.biblioweb.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String mensaje;
    private String detalles;
    private Map<String, String> erroresValidacion;

    public ApiError() {
    }

    public ApiError(LocalDateTime timestamp, int status, String mensaje, String detalles,
                    Map<String, String> erroresValidacion) {
        this.timestamp = timestamp;
        this.status = status;
        this.mensaje = mensaje;
        this.detalles = detalles;
        this.erroresValidacion = erroresValidacion;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public Map<String, String> getErroresValidacion() {
        return erroresValidacion;
    }

    public void setErroresValidacion(Map<String, String> erroresValidacion) {
        this.erroresValidacion = erroresValidacion;
    }
}
