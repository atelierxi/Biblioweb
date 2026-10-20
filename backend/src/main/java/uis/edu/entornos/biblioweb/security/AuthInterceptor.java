package uis.edu.entornos.biblioweb.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final SesionServicio sesionServicio;

    public AuthInterceptor(SesionServicio sesionServicio) {
        this.sesionServicio = sesionServicio;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        sesionServicio.obtenerDesdeHeader(request.getHeader("Authorization"));
        return true;
    }
}
