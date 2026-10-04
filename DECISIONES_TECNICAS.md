# Decisiones Técnicas - NotesApp Backend

## 1. Framework: Spring Boot

**Por qué Spring Boot:**
- Ecosistema maduro y bien documentado
- Integración nativa con Spring Security y JPA
- Configuración por convención (convention over configuration)
- Soporte completo para REST APIs

**Implementación:**
- Spring Boot 3.2.0 con Java 17
- Spring Data JPA para persistencia
- Spring Security para autenticación
- Validación de request con Bean Validation

## 2. Autenticación JWT

**Por qué JWT:**
- Stateless (sin sesión en servidor)
- Escalable horizontalmente
- Estándar de la industria
- Fácil implementación en el cliente (Android)

**Configuración:**
- Tokens expiran en 24 horas
- Secret key configurable via variable de entorno
- Password hasheado con BCrypt

**Flujo:**
1. Login → servidor genera JWT
2. Cliente almacena token
3. Cada request incluye `Authorization: Bearer <token>`
4. `JwtAuthFilter` valida el token antes de cada request

**Seguridad:**
- Endpoints públicos: `/api/auth/*`
- Endpoints protegidos: `/api/notes/*`
- CORS abierto (`*`) para desarrollo

## 3. Persistencia: Spring Data JPA

**Por qué JPA:**
- ORM estándar para Java
- Integración nativa con Spring Boot
- Type-safe queries
- Múltiples implementaciones (Hibernate, EclipseLink)

**Configuración:**
- MySQL en `notesdb` por defecto (`application.yml`, usuario/clave configurables)
- Auto-generación de esquema (`spring.jpa.hibernate.ddl-auto=update`)
- H2 disponible como dependencia para pruebas locales

**Entidades:**
- `User`: ID, email (único, identidad de login), password, timestamp
- `Note`: ID, title, content, userId, latitude/longitude/locationName, timestamps

## 4. Base de Datos

### Desarrollo local (MySQL)
- Base `notesdb` (se crea sola con `createDatabaseIfNotExist=true`)
- Sin configuración adicional más allá de usuario/clave en `application.yml`

### Variables de entorno (ejemplo)
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/notesdb
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=secret
JWT_SECRET=tu-clave-en-base64
```

## 5. Manejo de Errores

**Excepciones globales:**
- `GlobalExceptionHandler` captura todas las excepciones
- Respuestas consistentes con formato JSON y mensajes en español
- Códigos HTTP apropiados (400, 401, 404, 409, 500)
- Sin fuga de detalles técnicos (SQL/stacktraces solo en el log del servidor)

**Validación:**
- Bean Validation en DTOs
- Mensajes de error descriptivos
- Validación a nivel de controller

## 6. Configuración

**Archivos:**
- `application.yml`: Configuración principal (MySQL, JWT, CORS)

**Variables de entorno para producción:**
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/notesdb
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET=your-secret-key
```