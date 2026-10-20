# Manual de usuario - BiblioWeb

> BORRADOR PREPARADO PARA INSERTAR CAPTURAS REALES DESPUÉS DE LA PRUEBA LOCAL.

## 1. ¿Qué es BiblioWeb?

BiblioWeb es una aplicación web para administrar una biblioteca pequeña. Permite organizar categorías y libros, registrar lectores y controlar los préstamos y devoluciones.

Existen dos perfiles:

- **Bibliotecario:** administra la información y los préstamos.
- **Lector:** consulta el catálogo y sus propios préstamos.

## 2. Requisitos

- Java 17.
- Maven.
- MySQL 8.
- Backend BiblioWeb iniciado en `http://localhost:8094`.
- Un navegador moderno.
- Frontend abierto mediante Live Server.

## 3. Preparar la base de datos

Ejecute `database/BiblioWeb.sql` en HeidiSQL.

[INSERTAR CAPTURA REAL - BASE BIBLIOWEB EN HEIDISQL]

## 4. Iniciar el backend

Complete temporalmente su contraseña local en:

```properties
spring.datasource.password=SU_CLAVE_LOCAL
```

Ejecute `BiblioWebApplication.java` como Spring Boot App.

[INSERTAR CAPTURA REAL - CONSOLA SPRING BOOT INICIADA]

## 5. Iniciar el frontend

Abra la carpeta `frontend` en Visual Studio Code y ejecute `index.html` con Live Server.

[INSERTAR CAPTURA REAL - LOGIN]

## 6. Iniciar sesión como bibliotecario

Credenciales académicas:

```text
admin@biblioweb.test
Admin123
```

Después del ingreso se muestra el panel administrativo.

[INSERTAR CAPTURA REAL - PANEL BIBLIOTECARIO]

## 7. Gestionar categorías

Desde el panel seleccione **Categorías**. Puede registrar, editar y eliminar categorías que no tengan libros asociados.

[INSERTAR CAPTURA REAL - CRUD CATEGORÍAS]

## 8. Gestionar libros

Desde **Libros** puede registrar título, autor, ISBN, año, categoría y estado. El botón Retirar cambia el libro a `INACTIVO` en vez de borrar el historial.

[INSERTAR CAPTURA REAL - CRUD LIBROS]

## 9. Gestionar lectores

Desde **Lectores** puede crear, actualizar o inactivar usuarios con rol LECTOR.

[INSERTAR CAPTURA REAL - CRUD LECTORES]

## 10. Registrar un préstamo

1. Abra **Préstamos**.
2. Seleccione un lector ACTIVO.
3. Seleccione un libro DISPONIBLE.
4. Revise las fechas.
5. Pulse **Registrar préstamo**.

El préstamo queda `ACTIVO` y el libro cambia automáticamente a `PRESTADO`.

[INSERTAR CAPTURA REAL - PRÉSTAMO ACTIVO]

## 11. Registrar una devolución

En la tabla de préstamos, pulse **Registrar devolución** sobre un préstamo ACTIVO.

El préstamo pasa a `DEVUELTO` y el libro vuelve a `DISPONIBLE`.

[INSERTAR CAPTURA REAL - DEVOLUCIÓN]

## 12. Iniciar sesión como lector

Credenciales académicas:

```text
lector@biblioweb.test
Lector123
```

El lector visualiza únicamente las opciones Catálogo y Mis préstamos.

[INSERTAR CAPTURA REAL - PANEL LECTOR]

## 13. Consultar catálogo

El catálogo permite buscar por título, autor o categoría y muestra la disponibilidad.

[INSERTAR CAPTURA REAL - CATÁLOGO]

## 14. Consultar mis préstamos

La opción **Mis préstamos** muestra únicamente los movimientos del lector autenticado.

[INSERTAR CAPTURA REAL - MIS PRÉSTAMOS]

## 15. Cerrar sesión

Pulse **Cerrar sesión**. El backend invalida el token y el navegador regresa al login.

## 16. Swagger

La documentación técnica de la API se encuentra en:

```text
http://localhost:8094/swagger-ui/index.html
```

[INSERTAR CAPTURA REAL - SWAGGER]

