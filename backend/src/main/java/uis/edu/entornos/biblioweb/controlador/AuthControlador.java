package uis.edu.entornos.biblioweb.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uis.edu.entornos.biblioweb.dto.LoginRequest;
import uis.edu.entornos.biblioweb.dto.LoginResponse;
import uis.edu.entornos.biblioweb.dto.SesionUsuario;
import uis.edu.entornos.biblioweb.security.SesionServicio;
import uis.edu.entornos.biblioweb.servicio.AuthServicio;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Inicio, consulta y cierre de sesión")
public class AuthControlador {
    private final AuthServicio authServicio;
    private final SesionServicio sesionServicio;

    public AuthControlador(AuthServicio authServicio, SesionServicio sesionServicio) {
        this.authServicio = authServicio;
        this.sesionServicio = sesionServicio;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida correo y contraseña y devuelve un token de sesión")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authServicio.login(request));
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Consultar sesión actual")
    public ResponseEntity<SesionUsuario> me(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(sesionServicio.obtenerDesdeHeader(authorization));
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Cerrar sesión")
    public ResponseEntity<Void> logout(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorization) {
        sesionServicio.cerrar(authorization);
        return ResponseEntity.noContent().build();
    }
}
