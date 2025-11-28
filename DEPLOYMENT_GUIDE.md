# Guía de Despliegue y Validación de Seguridad

## 1. Pre-Despliegue

### Verificar Compilación
```bash
mvn clean compile -DskipTests
mvn clean package -DskipTests
```

### Verificar Dependencias Actualizadas
```bash
mvn dependency:tree | grep security
```

---

## 2. Configuración en Azure

### Azure App Service Configuration

#### Variables de Entorno Requeridas:
```
PORT=8080
SPRING_PROFILES_ACTIVE=production
SERVER_SERVLET_SESSION_COOKIE_SECURE=true
SERVER_SERVLET_SESSION_COOKIE_HTTP_ONLY=true
```

#### Configurar HTTPS (Obligatorio)
```bash
# En Azure Portal:
1. App Service → TLS/SSL settings
2. Habilitar "HTTPS Only"
3. Requerir TLS 1.2 mínimo
4. Usar certificado SSL (gratuito o custom)
```

#### Configurar Headers en Azure
Si usas Azure Front Door, agregar headers:
```
Content-Security-Policy: default-src 'self'; ...
X-Frame-Options: DENY
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000; includeSubDomains
```

---

## 3. Validación Post-Despliegue

### Verificar Headers de Seguridad
```bash
curl -I https://tu-dominio.azurewebsites.net/

# Debe retornar:
# Content-Security-Policy: default-src 'self'; ...
# X-Frame-Options: DENY
# X-Content-Type-Options: nosniff
# Strict-Transport-Security: max-age=31536000; includeSubDomains; preload
# X-XSS-Protection: 1; mode=block
# Referrer-Policy: strict-origin-when-cross-origin
# Permissions-Policy: geolocation=(), microphone=(), ...
```

### Validar Respuestas de Error
```bash
# Intentar acceder a ruta no existente:
curl https://tu-dominio.azurewebsites.net/api/inexistente

# Debe retornar error genérico SIN stack trace
```

### Probar CSRF Protection
```bash
# Los cookies deben tener:
curl -I https://tu-dominio.azurewebsites.net/ -v

# Verificar Set-Cookie:
# - HttpOnly
# - Secure
# - SameSite=Strict
```

### Escanear con OWASP ZAP Nuevamente
```bash
# Configuración recomendada:
# - URL base: https://tu-dominio.azurewebsites.net
# - Scan type: Baseline Scan
# - Target scope: Solo tu dominio
# - Report format: HTML
```

---

## 4. Verificación en Código

### Files de Configuración Creados
- ✅ `SecurityHeadersConfig.java` - Headers HTTP seguros
- ✅ `SpringSecurityConfig.java` - Spring Security configuration
- ✅ `CorsConfig.java` - CORS configuration
- ✅ `GlobalExceptionHandler.java` - Exception handling
- ✅ `SecurityFilter.java` - Request filtering

### Dependencias Agregadas
- ✅ `spring-boot-starter-security`
- ✅ `spring-boot-starter-validation`
- ✅ `jakarta.servlet-api`

### Propiedades Actualizadas
- ✅ `application.properties` con configuraciones de seguridad

---

## 5. Testing Automatizado

### Unit Tests Recomendados
```java
@Test
void testSecurityHeaders() {
    mockMvc.perform(get("/"))
        .andExpect(header().exists("Content-Security-Policy"))
        .andExpect(header().exists("X-Frame-Options"))
        .andExpect(header().exists("X-Content-Type-Options"))
        .andExpect(header().exists("Strict-Transport-Security"));
}

@Test
void testXSSProtection() {
    mockMvc.perform(get("/api/libros?search=<script>alert('xss')</script>"))
        .andExpect(status().isBadRequest());
}

@Test
void testCSRFProtection() {
    mockMvc.perform(post("/api/libros/agregar"))
        .andExpect(status().isForbidden());
}
```

### Integration Tests
```java
@Test
void testErrorHandlingNoStackTrace() {
    mockMvc.perform(get("/api/inexistente"))
        .andExpect(content().string(not(containsString("at "))))
        .andExpect(content().string(not(containsString("Exception"))));
}
```

---

## 6. Monitoreo Continuo

### Azure Monitor - Alertas Recomendadas
```
1. HTTP Status 400+ > 10 en 5 min → Posible ataque
2. CPU > 80% → Performance degradation
3. Memory > 85% → Resource exhaustion
4. Failed requests > 5% → API issues
```

### Application Insights
```
- Track 4xx/5xx errors
- Monitor custom metrics
- Alertas para patrones sospechosos
```

### Logs a Revisar
```
- /var/log/webapp/security.log → SecurityFilter logs
- /var/log/webapp/error.log → GlobalExceptionHandler
- Application Insights → Trace telemetry
```

---

## 7. Checklist Final

- [ ] Todas las vulnerabilidades DAST resueltas
- [ ] Headers de seguridad verificados con curl
- [ ] HTTPS configurado y obligatorio
- [ ] Certificado SSL válido
- [ ] Error handling sin stack traces
- [ ] CSRF protection activa
- [ ] Sesiones seguras configuradas
- [ ] CORS restrictivo
- [ ] Logging de intentos sospechosos
- [ ] Azure Monitor configurado
- [ ] WAF en Azure Front Door
- [ ] Rate limiting implementado
- [ ] Nuevo escaneo DAST completado
- [ ] Resultados DAST mejorados

---

## 8. Referencias de Seguridad

### OWASP Top 10
- A01: Broken Access Control ✅ (Headers, CORS)
- A02: Cryptographic Failures ✅ (HTTPS, HSTS)
- A05: Security Misconfiguration ✅ (Headers, Error handling)
- A07: XSS ✅ (CSP, SecurityFilter)
- A08: Software and Data Integrity ✅ (CSRF)

### Estándares
- NIST Cybersecurity Framework
- CIS Top 20 Controls
- PCI DSS Requirements
- GDPR Security Principles

---

**Generado**: 28 de Noviembre de 2025
**Versión**: 1.0
**Estado**: Listo para Despliegue
