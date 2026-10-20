# BiblioWeb

**Sistema web para la gestión de libros y préstamos de una biblioteca**  
Proyecto de clase - Desarrollo de Aplicaciones Empresariales - UTS  
**Estudiante:** Martín Enrique Bernal Gómez  
**Profesor:** Carlos Adolfo Beltrán Castro  
**Periodo:** II semestre de 2026

## 1. Arquitectura

BiblioWeb utiliza una arquitectura web de tres capas:

```text
Frontend HTML/CSS/JavaScript
        ↓ HTTP + JSON
API REST Spring Boot
        ↓ JPA / Hibernate
MySQL - biblioweb
```

El backend se organiza en:

```text
modelo -> repositorio -> servicio -> controlador
```

Además incluye DTO de login, manejo global de excepciones, CORS y una sesión sencilla mediante token opaco.

## 2. Tecnologías

- Java 17
- Spring Boot 4.1.1
- Maven
- Spring Web MVC
- Spring Data JPA / Hibernate
- MySQL 8.x
- Jakarta Validation
- JPQL
- SpringDoc OpenAPI / Swagger
- BCrypt (`spring-security-crypto`)
- HTML5, CSS3 y JavaScript genérico
- XMLHttpRequest (AJAX)
- Bootstrap CDN como apoyo visual

## 3. Estructura

```text
BiblioWeb/
├── backend/                 Proyecto Maven Spring Boot
├── frontend/                HTML/CSS/JavaScript
├── database/                Script BiblioWeb.sql
├── docs/
│   ├── Auditoria_Tecnica.md
│   ├── evidencias/
│   └── diagramas/           Diagramas del Sprint 1
├── BiblioWeb.postman_collection.json
├── Guion_Sustentacion_BiblioWeb.md
└── README.md
```

## 4. Base de datos

La aplicación utiliza:

- Host: `localhost`
- Puerto: `3307`
- Base: `biblioweb`
- Usuario local esperado: `root`

### Crear la base

1. Abra HeidiSQL.
2. Conéctese a su instancia MySQL.
3. Abra `database/BiblioWeb.sql`.
4. Ejecute el script completo.
5. Compruebe que existan las tablas:
   - `categoria`
   - `usuario`
   - `libro`
   - `prestamo`

El script reinicia la base `biblioweb`, por lo que debe ejecutarse solo cuando desee restaurar los datos de demostración.

## 5. Contraseña local de MySQL

El proyecto se entrega sin contraseña personal. Abra:

```text
backend/src/main/resources/application.properties
```

y complete únicamente esta línea:

```properties
spring.datasource.password=SU_CLAVE_LOCAL
```

No suba su contraseña al repositorio ni la incluya en la entrega.

## 6. Ejecutar backend

Desde CMD o PowerShell:

```bat
cd backend
mvn clean package
mvn spring-boot:run
```

También puede importar `backend` en Spring Tools como proyecto Maven y ejecutar `BiblioWebApplication.java` con **Run As -> Spring Boot App**.

La API queda en:

```text
http://localhost:8094
```

Swagger:

```text
http://localhost:8094/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8094/api-docs
```

## 7. Ejecutar frontend

1. Abra la carpeta `frontend` en Visual Studio Code.
2. Abra `index.html` con **Live Server**.
3. El navegador abrirá normalmente una dirección como:

```text
http://127.0.0.1:5500/index.html
```

El backend permite CORS para este escenario académico.

## 8. Usuarios de demostración

| Perfil | Correo | Contraseña |
|---|---|---|
| Bibliotecario | `admin@biblioweb.test` | `Admin123` |
| Lector | `lector@biblioweb.test` | `Lector123` |

Son credenciales ficticias para demostración académica.

## 9. Funcionalidades

### Bibliotecario

- Gestionar categorías.
- Gestionar libros.
- Registrar y actualizar lectores.
- Inactivar lectores conservando el historial.
- Registrar préstamos.
- Registrar devoluciones.
- Consultar todos los préstamos.
- Consultar catálogo.

### Lector

- Consultar catálogo.
- Consultar disponibilidad.
- Consultar únicamente sus propios préstamos.

## 10. Reglas de negocio principales

- Solo un usuario `LECTOR` y `ACTIVO` puede recibir un préstamo.
- Solo un libro `DISPONIBLE` puede prestarse.
- Un mismo libro no puede tener dos préstamos activos simultáneos.
- Al prestar un libro, cambia a `PRESTADO`.
- Al devolverlo, el préstamo cambia a `DEVUELTO` y el libro a `DISPONIBLE`.
- La fecha límite no puede ser anterior a la fecha de préstamo.
- Una categoría con libros asociados no se elimina.
- Los lectores y libros se inactivan en vez de borrarse cuando se desea conservar historial.

## 11. Login y sesión

El frontend envía correo y contraseña a:

```text
POST /api/auth/login
```

El backend:

1. Busca el usuario activo mediante JPA/JPQL.
2. Compara la contraseña con el hash BCrypt almacenado.
3. Crea un token UUID de sesión con duración de 8 horas.
4. Devuelve token, usuario y rol.
5. El frontend guarda el token en `sessionStorage`.
6. Las demás peticiones envían:

```text
Authorization: Bearer <token>
```

No se utiliza JWT ni Spring Security completo porque el objetivo académico se cumple con una solución más sencilla.

## 12. Endpoints principales

### Autenticación

```text
POST /api/auth/login
GET  /api/auth/me
POST /api/auth/logout
```

### Categorías

```text
GET    /api/categorias
GET    /api/categorias/{id}
POST   /api/categorias
PUT    /api/categorias/{id}
DELETE /api/categorias/{id}
```

### Libros

```text
GET    /api/libros
GET    /api/libros/{id}
GET    /api/libros/disponibles
GET    /api/libros/buscar?q=texto
POST   /api/libros
PUT    /api/libros/{id}
DELETE /api/libros/{id}
```

### Usuarios

```text
GET    /api/usuarios
GET    /api/usuarios/lectores
GET    /api/usuarios/{id}
POST   /api/usuarios
PUT    /api/usuarios/{id}
DELETE /api/usuarios/{id}
```

### Préstamos

```text
GET    /api/prestamos
GET    /api/prestamos/activos
GET    /api/prestamos/{id}
GET    /api/prestamos/usuario/{usuarioId}
GET    /api/prestamos/mis-prestamos
POST   /api/prestamos
PUT    /api/prestamos/{id}
PATCH  /api/prestamos/{id}/devolver
DELETE /api/prestamos/{id}
```

## 13. Postman

Importe:

```text
BiblioWeb.postman_collection.json
```

La colección incluye login y pruebas de los recursos principales. El login de bibliotecario guarda automáticamente el token en la variable `token` de la colección.

## 14. Códigos HTTP usados

- `200 OK`: consulta o actualización correcta.
- `201 Created`: recurso creado.
- `204 No Content`: eliminación/logout sin cuerpo.
- `400 Bad Request`: validación incorrecta.
- `401 Unauthorized`: no hay sesión o credenciales incorrectas.
- `403 Forbidden`: el usuario autenticado no tiene rol permitido.
- `404 Not Found`: recurso inexistente.
- `409 Conflict`: regla de negocio o integridad impide la operación.

