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
- CORS configurado para desarrollo (localhost:3000, 10.0.2.2:8080)

## 3. Persistencia: Spring Data JPA

**Por qué JPA:**
- ORM estándar para Java
- Integración nativa con Spring Boot
- Type-safe queries
- Múltiples implementaciones (Hibernate, EclipseLink)

**Configuración:**
- H2 en memoria para desarrollo
- MySQL para producción
- Auto-generación de esquema (`spring.jpa.hibernate.ddl-auto=update`)

**Entidades:**
- `User`: ID, username, password, timestamp
- `Note`: ID, title, content, userId, timestamp, isPendingSync

## 4. Base de Datos

### Desarrollo (H2)
- Base de datos en memoria
- Consola web habilitada (`/h2-console`)
- Sin configuración adicional necesaria

### Producción (MySQL)
- Perfil `mysql` activado via `SPRING_PROFILES_ACTIVE`
- Configuración via variables de entorno
- Connection pooling con HikariCP

## 5. Manejo de Errores

**Excepciones globales:**
- `GlobalExceptionHandler` captura todas las excepciones
- Respuestas consistentes con formato JSON
- Códigos HTTP apropiados (400, 401, 404, 500)

**Validación:**
- Bean Validation en DTOs
- Mensajes de error descriptivos
- Validación a nivel de controller

## 6. Configuración

**Archivos:**
- `application.yml`: Configuración principal (H2, JWT, CORS)
- `application-mysql.yml`: Configuración MySQL (producción)

**Variables de entorno para producción:**
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/notesdb
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET=your-secret-key
```