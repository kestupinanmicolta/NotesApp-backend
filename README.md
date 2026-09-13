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
├── config/           # Seguridad (SecurityConfig), CORS, JwtAuthFilter
├── controller/       # AuthController, NoteController
├── dto/              # AuthRequest, AuthResponse (incluye userId), NoteRequest, NoteResponse
├── model/            # User, Note (relación N:1 por usuario)
├── repository/       # UserRepository, NoteRepository (queries por usuario)
├── service/          # AuthService, NoteService
└── exception/        # GlobalExceptionHandler (mensajes en español)
```

## Endpoints

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Registro (username, password, email opcional*) | No |
| POST | `/api/auth/login` | Login (username + password, email opcional) | No |
| GET | `/api/notes` | Listar notas **del usuario autenticado** | Sí |
| GET | `/api/notes/{id}` | Obtener nota propia por ID | Sí |
| POST | `/api/notes` | Crear nota del usuario | Sí |
| PUT | `/api/notes/{id}` | Actualizar nota propia | Sí |
| DELETE | `/api/notes/{id}` | Eliminar nota propia | Sí |

\* El email solo es obligatorio en registro; el login usa username + password.

## Seguridad

- `formLogin` y `httpBasic` deshabilitados (API stateless).
- `/api/auth/**` público; todo lo demás requiere `Authorization: Bearer <token>`.
- JWT de 24 h (`jwt.expiration: 86400000`), secreto Base64 en `application.yml`.
- Passwords con BCrypt. CORS abierto (`*`) para desarrollo.
- `AuthResponse` devuelve `token` + `userId` (la app móvil lo usa para filtrar caché local).
- `GlobalExceptionHandler` con mensajes en español (400/401/404/500).

## Ejecución

Requisitos: Java 17+, Maven 3.6+, MySQL (XAMPP) con base `notesdb` (se crea sola con `createDatabaseIfNotExist=true`, usuario `root` sin password por defecto).

```bash
mvn spring-boot:run
```

Disponible en `http://localhost:8081` (en red local: `http://192.168.1.10:8081`).

`src/main/resources/application.yml` ya apunta a MySQL; `ddl-auto: update` genera el esquema.

## Pruebas con Bruno

Colección: `D:\Bruno\Pruebas para bruno\NotesApp Backend\` (7 requests + environment: register, login, CRUD de notas).
