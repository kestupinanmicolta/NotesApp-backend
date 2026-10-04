# NotesApp Backend

API REST para gestión de notas por usuario, con autenticación JWT y persistencia en MySQL.

## Stack técnico

- **Framework**: Spring Boot 3.2.0 · **Java 17** · **Build**: Maven
- **Seguridad**: Spring Security + JWT (jjwt 0.12.3, secreto en Base64)
- **Persistencia**: Spring Data JPA (Hibernate) + MySQL (`notesdb`)
- **Puerto**: `8081`

## Estructura del proyecto

```
src/main/java/com/notes/api/
├── config/           # SecurityConfig, JwtAuthFilter, CORS, GlobalExceptionHandler (errores en español)
├── controller/       # AuthController, NoteController
├── dto/              # AuthRequest, AuthResponse (incluye userId), NoteRequest, NoteResponse
├── model/            # User, Note (relación N:1 por usuario)
├── repository/       # UserRepository, NoteRepository (queries por usuario)
├── service/          # AuthService, JwtService, NoteService
bruno/                 # Colección Bruno para probar la API
```

## Endpoints

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Registro (email + password) | No |
| POST | `/api/auth/login` | Login (email + password) | No |
| GET | `/api/notes` | Listar notas **del usuario autenticado** | Sí |
| GET | `/api/notes/{id}` | Obtener nota propia por ID | Sí |
| POST | `/api/notes` | Crear nota (`title` requerido; `latitude`/`longitude`/`locationName` opcionales) | Sí |
| PUT | `/api/notes/{id}` | Actualizar nota propia (`location*` en null borra la ubicación) | Sí |
| DELETE | `/api/notes/{id}` | Eliminar nota propia | Sí |

La identidad del usuario es el **email**: registro y login solo usan `email` + `password`; el JWT lleva el email como subject.

## Seguridad

- `formLogin` y `httpBasic` deshabilitados (API stateless).
- `/api/auth/**` público; todo lo demás requiere `Authorization: Bearer <token>`.
- JWT de 24 h (`jwt.expiration: 86400000`), secreto Base64 en `application.yml`.
- Passwords con BCrypt. CORS abierto (`*`) para desarrollo.
- `AuthResponse` devuelve `token` + `userId` (la app móvil lo usa para filtrar caché local).
- `GlobalExceptionHandler` con mensajes en español (400/401/404/409/500) sin exponer detalles técnicos.

## Ejecución (cualquier PC)

Requisitos: Java 17+, Maven 3.6+ y MySQL 8 con base `notesdb`
(se crea sola con `createDatabaseIfNotExist=true`; usuario y clave en
`src/main/resources/application.yml`).

```bash
mvn spring-boot:run
```

Disponible en `http://localhost:8081`. `ddl-auto: update` genera/actualiza el esquema.

## Pruebas con Bruno

Colección incluida en `./bruno` (8 requests + entorno `local`):
register, login, CRUD de notas y limpieza de ubicación.
Ábrela en Bruno, selecciona el entorno `local` y ejecuta en orden.
