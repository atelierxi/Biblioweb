package uis.edu.entornos.biblioweb.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarNoEncontrado(RecursoNoEncontradoException ex,
                                                        HttpServletRequest request) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(SolicitudInvalidaException.class)
    public ResponseEntity<ApiError> manejarSolicitudInvalida(SolicitudInvalidaException ex,
                                                              HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiError> manejarReglaNegocio(ReglaNegocioException ex,
                                                        HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ApiError> manejarCredenciales(CredencialesInvalidasException ex,
                                                        HttpServletRequest request) {
        return respuesta(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, null);
    }

    @ExceptionHandler(AccesoNoAutorizadoException.class)
    public ResponseEntity<ApiError> manejarNoAutorizado(AccesoNoAutorizadoException ex,
                                                        HttpServletRequest request) {
        return respuesta(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, null);
    }

    @ExceptionHandler(AccesoProhibidoException.class)
    public ResponseEntity<ApiError> manejarProhibido(AccesoProhibidoException ex,
                                                     HttpServletRequest request) {
        return respuesta(HttpStatus.FORBIDDEN, ex.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(MethodArgumentNotValidException ex,
                                                      HttpServletRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errores.putIfAbsent(error.getField(), error.getDefaultMessage())
        );
        return respuesta(HttpStatus.BAD_REQUEST, "La solicitud contiene datos inválidos", request, errores);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> manejarIntegridad(DataIntegrityViolationException ex,
                                                      HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT,
                "La operación no puede completarse porque existen datos relacionados o duplicados",
                request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarGeneral(Exception ex, HttpServletRequest request) {
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno no esperado", request, null);
    }

    private ResponseEntity<ApiError> respuesta(HttpStatus status, String mensaje,
                                               HttpServletRequest request,
                                               Map<String, String> erroresValidacion) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                mensaje,
                request.getRequestURI(),
                erroresValidacion
        );
        return ResponseEntity.status(status).body(error);
    }
}
