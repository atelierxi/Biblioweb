# Auditoría técnica - BiblioWeb - Segundo Corte

## 1. Qué exige de forma explícita la entrega

La instrucción del segundo corte pide un proyecto backend con Spring Boot, al menos un CRUD completo por integrante, sustentación y documentación comprensible para el cliente.

## 2. Qué se estudió durante el segundo corte y se integra

- Persistencia con JPA/Hibernate y MySQL.
- API REST.
- CRUD con modelo, repositorio, servicio y controlador.
- Validaciones y manejo de excepciones.
- JPQL para consultas personalizadas.
- Login de acceso con DTO y endpoint.
- Frontend HTML/CSS/JavaScript genérico.
- Peticiones HTTP asíncronas (AJAX / XMLHttpRequest).
- CORS.
- Swagger/OpenAPI y Postman.

## 3. Continuidad con el Sprint 1

Se conserva la base de datos `biblioweb` y sus cuatro entidades: `categoria`, `usuario`, `libro` y `prestamo`.
También se mantienen los perfiles BIBLIOTECARIO y LECTOR, los requisitos RF-01 a RF-12 y las reglas RN-01 a RN-08 definidas en el Sprint 1.

## 4. Referencias revisadas

### TiendaWeb
Se toma como referencia pedagógica el patrón modelo -> repositorio -> servicio -> controlador, el DTO de login, JPQL y CORS. No se copian sus versiones antiguas ni sus credenciales.

### FrontEndProveedores
Se conserva la idea de separar `request.js` de los scripts de cada funcionalidad y consumir endpoints REST mediante JavaScript y JSON.

### Microservicio de Login
Se adapta la idea de un DTO de login y consulta de usuario, pero BiblioWeb inicia sesión con `correo` y contraseña. La contraseña se compara con BCrypt en vez de almacenarse en texto plano.

### Proyecto monitoreo-servidores
Solo se toma como referencia estructural la separación `backend/`, `frontend/`, `database/` y `docs/`, así como la idea de sesión por token. No se copia su temática ni su código.

## 5. Decisiones técnicas

- Spring Boot 4.1.1 y Java 17, combinación ya usada con éxito en el entorno del estudiante.
- MySQL en `localhost:3307`.
- `JpaRepository` para CRUD básico.
- JPQL para búsqueda de usuario activo por correo, catálogo y préstamos por usuario.
- BCrypt mediante `spring-security-crypto`, sin incorporar Spring Security completo.
- Token de sesión opaco (UUID) guardado temporalmente en memoria por 8 horas.
- Interceptor sencillo para exigir autenticación en `/api/**`, excepto `/api/auth/login`.
- Validación de rol BIBLIOTECARIO también en backend para operaciones administrativas.
- Frontend sin React/Angular/Vue: HTML + CSS + JavaScript genérico y `XMLHttpRequest`.
- CORS global para permitir el frontend servido con Live Server.

## 6. Alcance implementado

### Bibliotecario
- Login/logout.
- CRUD categorías.
- CRUD administrativo de libros (DELETE = retiro/inactivación).
- CRUD administrativo de lectores (DELETE = inactivación).
- Registro y consulta de préstamos.
- Registro de devolución.
- Reglas de negocio para disponibilidad y usuarios activos.

### Lector
- Login/logout.
- Consulta de catálogo y disponibilidad.
- Consulta de sus propios préstamos.

## 7. Qué NO se implementa

- JWT.
- OAuth.
- Spring Security completo.
- React/Angular/Vue.
- Docker/Kubernetes.
- Pagos, reservas, correo, chat, IA o funcionalidades fuera del alcance original.

Estas exclusiones mantienen el proyecto estable, explicable y coherente con la asignatura.
