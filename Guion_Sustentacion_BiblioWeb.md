# Guion de sustentación - BiblioWeb

**Estudiante:** Martín Enrique Bernal Gómez  
**Asignatura:** Desarrollo de Aplicaciones Empresariales  
**Profesor:** Carlos Adolfo Beltrán Castro

---

# A. Sustentación completa (8-10 minutos)

## 1. Presentación del proyecto - 30 segundos

### Qué digo

> Mi proyecto se llama BiblioWeb. Es una aplicación web académica para administrar una biblioteca pequeña. Permite organizar categorías y libros, registrar lectores, controlar préstamos y devoluciones, y ofrecer acceso diferente para bibliotecarios y lectores.

### Qué muestro

Abrir `frontend/index.html` con Live Server y mostrar la pantalla de login.

### Qué significa técnicamente

BiblioWeb es una aplicación web de tres capas: presentación, lógica y datos.

### Posible pregunta

**¿Cuál era el problema que quería resolver?**  
Centralizar libros, usuarios y préstamos para evitar registros separados y facilitar la consulta de disponibilidad e historial.

---

## 2. Arquitectura - 45 segundos

### Qué digo

> La arquitectura separa frontend, backend y base de datos. El frontend está hecho con HTML, CSS y JavaScript. Se comunica por HTTP con una API REST en Spring Boot. El backend aplica las reglas de negocio, utiliza JPA e Hibernate para persistir objetos Java y finalmente almacena los datos en MySQL.

### Qué muestro

Mostrar la estructura raíz del proyecto:

```text
frontend/
backend/
database/
docs/
```

Después mostrar en Spring Tools los paquetes:

```text
modelo
repositorio
servicio
controlador
dto
exception
config
security
```

### Qué significa

```text
Frontend -> HTTP/JSON -> Controlador -> Servicio -> Repositorio -> JPA/Hibernate -> MySQL
```

- Frontend: lo que ve el usuario.
- API REST: puerta de entrada al backend.
- Servicio: reglas del negocio.
- Repositorio: acceso a datos.
- JPA/Hibernate: convierte objetos Java en filas de la BD.

### Posible pregunta

**¿Por qué separó el proyecto por capas?**  
Porque cada capa tiene una responsabilidad y así el código es más fácil de mantener, probar y explicar.

---

## 3. Base de datos - 45 segundos

### Qué digo

> La base de datos se llama biblioweb y conserva el diseño del Sprint 1. Tiene cuatro tablas principales: categoria, usuario, libro y prestamo. Categoria se relaciona uno a muchos con Libro, y Usuario y Libro se relacionan uno a muchos con Prestamo.

### Qué muestro

Abrir HeidiSQL y expandir `biblioweb`.

Mostrar tablas y, si hay tiempo, el DER en `docs/diagramas/der_biblioweb.png`.

### Qué significa

- PK: identifica una fila de forma única.
- FK: conecta tablas.
- `libro.id_categoria` referencia `categoria.id_categoria`.
- `prestamo.id_usuario` referencia usuario.
- `prestamo.id_libro` referencia libro.

### Posible pregunta

**¿Por qué préstamo tiene claves foráneas a usuario y libro?**  
Porque cada préstamo debe identificar qué lector recibió qué libro.

---

## 4. Entidades JPA - 45 segundos

### Qué digo

> En el backend cada tabla principal tiene una entidad JPA. Por ejemplo Libro está marcado con `@Entity` y `@Table`, y su relación con Categoria se implementa con `@ManyToOne` y `@JoinColumn`.

### Qué muestro

Abrir `modelo/Libro.java` y señalar:

```java
@Entity
@Table(name = "libro")
```

y:

```java
@ManyToOne
@JoinColumn(name = "id_categoria")
```

### Qué significa

`@Entity` indica que la clase se persiste. `@ManyToOne` significa que muchos libros pueden pertenecer a una categoría.

### Posible pregunta

**¿Qué diferencia hay entre una clase normal y una entidad?**  
La entidad está mapeada por JPA a una tabla y su estado puede persistirse en la base de datos.

---

## 5. Repositorio, servicio y controlador - 60 segundos

### Qué digo

> El repositorio extiende JpaRepository, que ya ofrece operaciones como guardar, buscar, listar y eliminar. El servicio contiene las reglas de negocio. El controlador expone esas operaciones como endpoints HTTP.

### Qué muestro

1. `LibroRepositorio.java`
2. `LibroServicio.java`
3. `LibroControlador.java`

### Qué significa

Ejemplo de crear un libro:

```text
Formulario HTML
-> JSON
-> POST /api/libros
-> LibroControlador
-> LibroServicio
-> LibroRepositorio
-> MySQL
```

### Posible pregunta

**¿Por qué no usa el repositorio directamente desde el controlador?**  
Porque las reglas de negocio pertenecen al servicio y así se separan responsabilidades.

---

## 6. API REST y Swagger - 45 segundos

### Qué digo

> El backend ofrece una API REST. Uso GET para consultar, POST para crear, PUT para actualizar, DELETE para retirar o inactivar y PATCH para una acción específica, que es registrar una devolución.

### Qué muestro

Abrir:

```text
http://localhost:8094/swagger-ui/index.html
```

Expandir Libros o Préstamos.

### Qué significa

- GET: consultar.
- POST: crear.
- PUT: actualizar.
- DELETE: eliminar o inactivar según regla de negocio.
- PATCH: modificación parcial o acción puntual.

### Posible pregunta

**¿Qué es un endpoint?**  
Es una URL de la API asociada a una operación concreta.

---

## 7. JPQL - 45 segundos

### Qué digo

> Para el CRUD básico uso JpaRepository, pero para consultas específicas utilicé JPQL. JPQL se parece a SQL, pero consulta entidades y atributos Java en lugar de escribir directamente sobre nombres físicos de tablas.

### Qué muestro

Abrir `UsuarioRepositorio.java` y señalar:

```java
@Query("select u from Usuario u where lower(u.correo) = lower(:correo) and u.estado = :estado")
```

Luego `LibroRepositorio.java`, búsqueda por título, autor o categoría.

### Qué significa

`@Param` enlaza valores recibidos con parámetros de la consulta.

### Posible pregunta

**¿Cuál es la diferencia entre SQL y JPQL?**  
SQL consulta directamente tablas y columnas; JPQL consulta entidades y propiedades mapeadas por JPA.

---

## 8. Login, BCrypt y sesión - 75 segundos

### Qué digo

> El login recibe correo y contraseña mediante un DTO. El backend busca un usuario activo, compara la contraseña con un hash BCrypt y, si coincide, crea un token de sesión UUID válido por ocho horas. El frontend guarda el token en sessionStorage y lo envía como Bearer Token en las siguientes peticiones.

### Qué muestro

1. `dto/LoginRequest.java`
2. `servicio/AuthServicio.java`
3. `security/SesionServicio.java`
4. Iniciar sesión en el frontend.

### Demo

Ingresar:

```text
admin@biblioweb.test
Admin123
```

### Qué significa

BCrypt es una función de hash para contraseñas. No permite recuperar la contraseña original: se compara la clave ingresada contra el hash.

El token identifica temporalmente la sesión sin volver a enviar la contraseña.

### Posible pregunta

**¿Usa JWT?**  
No. Para este proyecto académico implementé un token opaco UUID guardado en memoria porque es suficiente, más simple de explicar y evita introducir complejidad que no pide el curso.

**¿Qué pasa al reiniciar el backend?**  
Las sesiones temporales se pierden y el usuario debe iniciar sesión nuevamente. Los datos de la biblioteca no se pierden porque permanecen en MySQL.

---

## 9. Roles - 45 segundos

### Qué digo

> BiblioWeb tiene dos roles. El bibliotecario administra categorías, libros, lectores y préstamos. El lector únicamente consulta catálogo y sus propios préstamos. El frontend oculta opciones según el rol y el backend también valida las operaciones administrativas.

### Qué muestro

Panel como bibliotecario. Después, si hay tiempo, cerrar sesión y entrar como:

```text
lector@biblioweb.test
Lector123
```

Mostrar que solo aparecen Catálogo y Mis préstamos.

### Posible pregunta

**¿Ocultar el botón es suficiente seguridad?**  
No. Por eso las operaciones administrativas también verifican el rol en el backend.

---

## 10. Frontend y AJAX - 60 segundos

### Qué digo

> El frontend utiliza HTML, CSS y JavaScript genérico, como se trabajó en clase. Centralicé las peticiones en `request.js`. Allí uso `XMLHttpRequest` para enviar peticiones asíncronas, convertir objetos JavaScript a JSON y agregar el token de autenticación.

### Qué muestro

Abrir `frontend/js/request.js`.

Señalar:

```javascript
const request = new XMLHttpRequest();
request.open(method, API_BASE + endpoint, true);
request.setRequestHeader('Content-Type', 'application/json');
request.setRequestHeader('Authorization', 'Bearer ' + sesion.token);
```

### Posible pregunta

**¿Qué significa AJAX?**  
Es una forma de hacer peticiones al servidor desde JavaScript sin recargar completamente la página.

---

## 11. Demostración CRUD - 60 segundos

### Qué digo

> Voy a demostrar un CRUD real. Crearé un libro desde el frontend. La información viaja como JSON al backend, se valida, el servicio aplica las reglas y JPA lo guarda en MySQL.

### Qué muestro y dónde hago clic

1. Panel -> **Libros**.
2. Completar un libro de prueba.
3. Guardar.
4. Mostrarlo inmediatamente en la tabla.
5. Opcional: mostrar la fila en HeidiSQL.
6. Editarlo.
7. Retirarlo y mostrar estado `INACTIVO`.

### Posible pregunta

**¿DELETE borra físicamente el libro?**  
No en este caso. Para conservar historial, el libro se inactiva. Es una decisión basada en las reglas del Sprint 1.

---

## 12. Préstamo y devolución - 75 segundos

### Qué digo

> La operación más importante del negocio es el préstamo. El backend comprueba que el usuario sea LECTOR y esté ACTIVO, y que el libro esté DISPONIBLE. Al registrar el préstamo, el libro pasa automáticamente a PRESTADO. Al devolverlo, el préstamo pasa a DEVUELTO y el libro vuelve a DISPONIBLE.

### Qué muestro

1. Panel -> Préstamos.
2. Elegir lector activo.
3. Elegir libro disponible.
4. Registrar.
5. Mostrar estado ACTIVO y libro PRESTADO.
6. Pulsar **Registrar devolución**.
7. Mostrar DEVUELTO.
8. Ir al catálogo y mostrar libro DISPONIBLE.

### Posible pregunta

**¿Qué pasa si intenta prestar dos veces el mismo libro?**  
El servicio detecta el préstamo activo o el estado PRESTADO y responde con `409 Conflict`.

---

## 13. Validaciones y excepciones - 45 segundos

### Qué digo

> El proyecto utiliza Jakarta Validation para validar datos básicos y un `@RestControllerAdvice` para devolver errores JSON consistentes. Por ejemplo, un correo inválido genera 400, un recurso inexistente 404 y una regla de negocio como prestar un libro ya prestado genera 409.

### Qué muestro

Abrir `ManejadorGlobalExcepciones.java` o mostrar una prueba Postman.

### Posible pregunta

**¿Por qué no devuelve 500 cuando no existe un libro?**  
Porque no es un error interno del servidor: es un recurso que no existe, por eso corresponde 404.

---

## 14. Cierre - 30 segundos

### Qué digo

> Con BiblioWeb integré lo trabajado durante el curso: Spring Boot, Maven, JPA, MySQL, CRUD, API REST, JPQL, login, roles, validaciones, Swagger y un frontend JavaScript que consume el backend. Además mantuve la estructura y las reglas definidas desde el Sprint 1.

---

# B. Sustentación express (3-5 minutos)

> Mi proyecto se llama BiblioWeb y administra una biblioteca pequeña. Mantiene categorías, libros, lectores y préstamos.
>
> Está dividido en tres capas. El frontend usa HTML, CSS y JavaScript; se comunica mediante una API REST con Spring Boot; y el backend utiliza JPA/Hibernate para persistir en MySQL.
>
> En el backend tengo entidades, repositorios, servicios y controladores. JpaRepository resuelve el CRUD básico, mientras que el servicio aplica las reglas de negocio. También utilizo JPQL para consultas específicas, por ejemplo buscar el usuario activo por correo.
>
> Implementé login con correo y contraseña. La contraseña se guarda como hash BCrypt. Cuando las credenciales son correctas, el backend genera un token temporal y el frontend lo envía en las siguientes peticiones.
>
> Existen dos roles: BIBLIOTECARIO y LECTOR. El bibliotecario administra los datos y préstamos; el lector consulta catálogo y sus propios préstamos.
>
> Como demostración, voy a registrar un préstamo. El backend comprueba que el lector esté activo y el libro disponible. Al guardar, el préstamo queda ACTIVO y el libro PRESTADO. Al registrar la devolución, el préstamo queda DEVUELTO y el libro vuelve a DISPONIBLE.
>
> La API se documenta con Swagger y las validaciones devuelven códigos HTTP adecuados como 400, 404, 409, 401 y 403.

## Qué mostrar en versión express

1. Login.
2. Panel del bibliotecario.
3. Swagger durante 20 segundos.
4. Un archivo `LibroControlador -> LibroServicio -> LibroRepositorio`.
5. Registrar préstamo y devolución.
6. Cerrar sesión e ingresar como lector.

---

# C. Diccionario para defender el proyecto

| Concepto | Respuesta corta aplicada a BiblioWeb |
|---|---|
| Spring Boot | Framework que simplifica crear y ejecutar el backend Java y la API REST. |
| Maven | Herramienta que administra dependencias y compila/empaqueta el proyecto. |
| JPA | Especificación para mapear objetos Java con tablas relacionales. |
| Hibernate | Implementación de JPA utilizada para realizar la persistencia. |
| Entidad | Clase Java que representa una tabla, por ejemplo `Libro`. |
| Repositorio | Interfaz de acceso a datos. En BiblioWeb extiende `JpaRepository`. |
| JpaRepository | Proporciona operaciones como `findAll`, `findById`, `save` y `delete`. |
| Servicio | Capa donde están reglas como no prestar un libro ya prestado. |
| Controlador | Recibe peticiones HTTP y expone los endpoints REST. |
| REST | Estilo para trabajar recursos mediante HTTP. |
| API | Conjunto de operaciones que el backend ofrece al frontend. |
| Endpoint | URL de una operación, por ejemplo `POST /api/prestamos`. |
| HTTP | Protocolo usado entre navegador y backend. |
| GET | Consultar información. |
| POST | Crear un nuevo recurso. |
| PUT | Actualizar un recurso completo. |
| DELETE | Eliminar o retirar un recurso según la regla del sistema. |
| PATCH | Cambiar una parte o ejecutar una acción puntual, como devolver un préstamo. |
| JSON | Formato textual usado para intercambiar datos entre frontend y backend. |
| DTO | Objeto diseñado para transportar datos; `LoginRequest` lleva correo y contraseña. |
| JPQL | Lenguaje de consultas de JPA que consulta entidades y atributos Java. |
| @Query | Permite escribir una consulta JPQL personalizada en un repositorio. |
| @Param | Enlaza un argumento Java con un parámetro de JPQL. |
| CORS | Regla del navegador que controla peticiones entre orígenes distintos. |
| Frontend | Interfaz que usa el usuario en el navegador. |
| Backend | Servidor que contiene API, reglas y persistencia. |
| AJAX | Peticiones asíncronas desde JavaScript sin recargar toda la página. |
| XMLHttpRequest | Objeto JavaScript usado en `request.js` para llamar la API. |
| fetch | API moderna equivalente para peticiones HTTP; este proyecto usa XMLHttpRequest por alineación con clase. |
| DOM | Representación de la página HTML que JavaScript puede modificar. |
| Bootstrap | Framework CSS usado como apoyo visual mediante CDN. |
| MySQL | Motor relacional donde se almacenan los datos. |
| PK | Clave primaria que identifica cada fila. |
| FK | Clave foránea que relaciona dos tablas. |
| @ManyToOne | Relación en la que muchos registros apuntan a uno; varios libros a una categoría. |
| @OneToMany | Relación inversa: una categoría puede relacionarse con muchos libros. |
| @JoinColumn | Indica la columna FK usada por una relación JPA. |
| Validation | Reglas automáticas para validar datos recibidos. |
| @Valid | Activa las validaciones del objeto recibido en el controlador. |
| @RestControllerAdvice | Centraliza el manejo de excepciones REST. |
| Swagger | Interfaz visual para explorar y documentar la API. |
| OpenAPI | Especificación que describe formalmente la API. |
| BCrypt | Algoritmo de hash diseñado para contraseñas. |
| Hash | Resultado irreversible usado para comprobar una contraseña sin guardarla en texto plano. |
| Token | Identificador temporal de una sesión autenticada. |
| Sesión | Estado temporal que indica quién está autenticado. |
| Rol | Perfil que determina qué acciones puede realizar un usuario. |
| 200 | Operación correcta. |
| 201 | Recurso creado correctamente. |
| 400 | Datos enviados inválidos. |
| 401 | Falta autenticación o las credenciales/token no son válidos. |
| 403 | Está autenticado, pero no tiene permiso para esa acción. |
| 404 | Recurso no encontrado. |
| 409 | Conflicto con una regla de negocio o integridad. |
| 500 | Error interno no esperado del servidor. |

---

# D. Preguntas probables del profesor

1. **¿Por qué utilizó Spring Boot?**  
   Porque simplifica la configuración del backend Java y permite crear rápidamente una API REST con JPA y Maven.

2. **¿Qué hace JPA?**  
   Mapea objetos Java con tablas relacionales y permite persistirlos sin escribir SQL para cada CRUD.

3. **¿Qué es Hibernate?**  
   Es el proveedor que implementa JPA y ejecuta el mapeo y las operaciones de persistencia.

4. **¿Qué diferencia existe entre SQL y JPQL?**  
   SQL consulta tablas y columnas; JPQL consulta entidades Java y sus atributos.

5. **¿Qué es una API REST?**  
   Es una interfaz HTTP que expone recursos mediante endpoints usando métodos como GET, POST, PUT y DELETE.

6. **¿Qué hace `@Entity`?**  
   Marca una clase para que JPA la trate como entidad persistente.

7. **¿Qué hace `@ManyToOne`?**  
   Representa una relación muchos a uno; en BiblioWeb muchos libros pueden pertenecer a una categoría.

8. **¿Para qué sirve `@JoinColumn`?**  
   Indica qué columna de la tabla contiene la clave foránea de la relación.

9. **¿Por qué existe una capa de servicio?**  
   Porque allí se centralizan reglas de negocio, separándolas del controlador y del acceso a datos.

10. **¿Por qué no llama el repositorio directamente desde JavaScript?**  
    Porque el repositorio es código Java interno. El frontend solo se comunica con la API REST del controlador mediante HTTP.

11. **¿Cómo se conecta JavaScript con Spring Boot?**  
    Mediante peticiones HTTP desde `XMLHttpRequest`, enviando y recibiendo JSON.

12. **¿Qué es CORS?**  
    Es una política del navegador que debe autorizar al frontend cuando se ejecuta en un origen diferente al backend.

13. **¿Qué pasa si presta un libro ya prestado?**  
    El servicio lo bloquea y responde 409 Conflict.

14. **¿Dónde se valida que el lector esté activo?**  
    En `PrestamoServicio`, antes de guardar el préstamo.

15. **¿Cómo funciona el login?**  
    El frontend envía correo y contraseña; el backend busca al usuario activo, compara BCrypt y devuelve un token de sesión.

16. **¿Por qué no guarda contraseñas en texto plano?**  
    Porque si la base se expone, las contraseñas quedarían visibles. Se almacena un hash BCrypt.

17. **¿Qué es BCrypt?**  
    Es un algoritmo de hash lento y con salt diseñado para almacenar contraseñas de forma más segura.

18. **¿Dónde queda la sesión?**  
    El backend mantiene el token en memoria y el frontend guarda su copia en `sessionStorage`.

19. **¿Qué diferencia hay entre 401 y 403?**  
    401 significa que no está autenticado; 403 que sí está autenticado pero no tiene permiso.

20. **¿Cómo sabe el lector cuáles préstamos son suyos?**  
    El backend obtiene su id desde la sesión y consulta los préstamos asociados a ese usuario.

21. **¿Qué sucede al devolver un libro?**  
    El préstamo cambia a DEVUELTO, se registra la fecha actual y el libro vuelve a DISPONIBLE.

22. **¿Qué es un DTO?**  
    Es una clase creada para transportar datos entre capas o peticiones, sin representar necesariamente una tabla.

23. **¿Por qué usa JSON?**  
    Porque es un formato simple y estándar para intercambiar estructuras de datos entre JavaScript y la API REST.

24. **¿Qué hace Maven?**  
    Descarga dependencias, compila, ejecuta pruebas y empaqueta el proyecto.

25. **¿Dónde configura MySQL?**  
    En `backend/src/main/resources/application.properties`.

26. **¿Qué ocurre si MySQL está apagado?**  
    Spring Boot no podrá validar/conectar con la base y la aplicación no funcionará hasta iniciar MySQL.

27. **¿Cuál es la diferencia entre frontend y backend?**  
    El frontend presenta la interfaz; el backend procesa reglas y accede a los datos.

28. **¿Por qué usa un token UUID y no JWT?**  
    Porque para este proyecto académico un token opaco es suficiente y reduce complejidad.

29. **¿Es una seguridad de producción?**  
    No. Es un mecanismo académico básico que demuestra autenticación y autorización. En producción usaría Spring Security y un esquema de sesiones/JWT robusto según el caso.

30. **¿Por qué DELETE de usuario no borra la fila?**  
    Porque las reglas del proyecto exigen conservar el historial; por eso se cambia a INACTIVO.

31. **¿Qué evita que una categoría con libros sea eliminada?**  
    El servicio consulta si existen libros asociados y devuelve 409 si los hay; la FK también protege la integridad.

32. **¿Qué hace `@Valid`?**  
    Ejecuta automáticamente las anotaciones de validación antes de entrar a la lógica del controlador.

33. **¿Cómo maneja los errores?**  
    Con un `@RestControllerAdvice` que convierte excepciones en respuestas JSON con códigos HTTP adecuados.

34. **¿Qué muestra Swagger?**  
    Los endpoints, parámetros, modelos y operaciones disponibles en la API.

35. **¿Qué parte del proyecto usa JPQL?**  
    Por ejemplo la búsqueda de usuario activo por correo, la búsqueda del catálogo y los préstamos por usuario.

---

# E. Qué hacer si algo falla en la sustentación

## Backend no inicia

1. Mostrar el último `BUILD SUCCESS` si lo tiene.
2. Revisar consola.
3. Confirmar que `application.properties` tenga la contraseña local.
4. Si no hay tiempo, mostrar código + Swagger/evidencias capturadas previamente y explicar el flujo.

## Puerto 8094 ocupado

En CMD:

```bat
netstat -ano | findstr :8094
```

Cerrar el proceso o cambiar temporalmente `server.port`.

## MySQL desconectado

1. Abrir Servicios de Windows.
2. Iniciar `MySQL84`.
3. Comprobar HeidiSQL en puerto 3307.

## Frontend no conecta

1. Confirmar Spring Boot activo.
2. Probar `http://localhost:8094/swagger-ui/index.html`.
3. Verificar `API_BASE` en `frontend/js/request.js`.

## CORS

Si Swagger funciona pero el navegador bloquea peticiones, revisar `WebConfig.java` y confirmar que el frontend se esté abriendo con Live Server.

## Login falla

1. Confirmar que se ejecutó `database/BiblioWeb.sql`.
2. Usar exactamente:

```text
admin@biblioweb.test / Admin123
lector@biblioweb.test / Lector123
```

3. Confirmar que el usuario esté ACTIVO.

## Swagger no abre

1. Confirmar backend iniciado.
2. Probar:

```text
http://localhost:8094/swagger-ui/index.html
http://localhost:8094/swagger-ui.html
http://localhost:8094/api-docs
```

## Live Server falla

Puede usar cualquier servidor HTTP estático. Como salida de emergencia, mostrar Swagger/Postman para demostrar backend y abrir los HTML directamente para explicar la interfaz, aunque algunas peticiones puedan quedar bloqueadas por el navegador.

---

# F. Checklist 10 minutos antes de sustentar

- [ ] MySQL84 iniciado.
- [ ] Base `biblioweb` disponible.
- [ ] Contraseña local escrita en `application.properties`.
- [ ] Backend iniciado en 8094.
- [ ] Swagger abre.
- [ ] Live Server iniciado.
- [ ] Login admin funciona.
- [ ] Login lector funciona.
- [ ] Existe al menos un libro DISPONIBLE para demostrar préstamo.
- [ ] Existe al menos un lector ACTIVO.
- [ ] Tener Postman abierto como respaldo.
- [ ] Tener HeidiSQL abierto en la base `biblioweb`.
- [ ] No improvisar explicaciones que no corresponden al código.
