# Plan de Pruebas de Seguridad

## 1. Pruebas Manuales de Headers

### Test 1.1: Verificar CSP Header
```bash
curl -I https://tu-app.azurewebsites.net/

# Esperado:
Content-Security-Policy: default-src 'self'; script-src 'self' 'unsafe-inline'; ...
```

### Test 1.2: Verificar X-Frame-Options
```bash
curl -I https://tu-app.azurewebsites.net/ | grep "X-Frame-Options"

# Esperado: X-Frame-Options: DENY
```

### Test 1.3: Verificar HSTS
```bash
curl -I https://tu-app.azurewebsites.net/ | grep "Strict-Transport-Security"

# Esperado: Strict-Transport-Security: max-age=31536000; includeSubDomains; preload
```

### Test 1.4: Verificar X-Content-Type-Options
```bash
curl -I https://tu-app.azurewebsites.net/ | grep "X-Content-Type-Options"

# Esperado: X-Content-Type-Options: nosniff
```

---

## 2. Pruebas de XSS

### Test 2.1: XSS Reflected (GET)
```bash
curl "https://tu-app.azurewebsites.net/api/libros?search=<script>alert('xss')</script>"

# Esperado: HTTP 400 Bad Request (SecurityFilter lo detecta)
```

### Test 2.2: XSS Stored (POST)
```bash
curl -X POST https://tu-app.azurewebsites.net/api/libros/agregar \
  -H "Content-Type: application/json" \
  -d '{"titulo":"<img src=x onerror=alert(1)>","autor":"Test","isbn":"123"}'

# Esperado: HTTP 400 Bad Request
```

---

## 3. Pruebas de Path Traversal

### Test 3.1: Path Traversal Simple
```bash
curl "https://tu-app.azurewebsites.net/api/libros?id=../../etc/passwd"

# Esperado: HTTP 400 Bad Request (SecurityFilter lo detecta)
```

### Test 3.2: Path Traversal Encoded
```bash
curl "https://tu-app.azurewebsites.net/api/libros?id=..%2f..%2fetc%2fpasswd"

# Esperado: HTTP 400 Bad Request
```

---

## 4. Pruebas de CSRF

### Test 4.1: POST sin CSRF Token
```bash
curl -X POST https://tu-app.azurewebsites.net/api/libros/agregar \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Test","autor":"Test","isbn":"123"}'

# Esperado: HTTP 403 Forbidden (CSRF protection)
```

### Test 4.2: Validar Cookie CSRF
```bash
curl -v https://tu-app.azurewebsites.net/ 2>&1 | grep -i "csrf"

# Esperado: Ver cookie CSRF en response
```

---

## 5. Pruebas de Sesión

### Test 5.1: HttpOnly Cookie
```bash
curl -v https://tu-app.azurewebsites.net/ 2>&1 | grep -i "set-cookie"

# Esperado: HttpOnly; Secure; SameSite=Strict
```

### Test 5.2: Timeout de Sesión
```bash
# 1. Crear sesión
curl -c cookies.txt https://tu-app.azurewebsites.net/

# 2. Esperar más de 15 min
sleep 1000

# 3. Intentar usar sesión
curl -b cookies.txt https://tu-app.azurewebsites.net/api/libros

# Esperado: Sesión expirada (nuevo login requerido)
```

---

## 6. Pruebas de Manejo de Errores

### Test 6.1: Error 404 - Sin Stack Trace
```bash
curl https://tu-app.azurewebsites.net/api/inexistente

# Esperado: Mensaje genérico SIN:
# - at com.mcic...
# - Exception
# - Nombre de archivos
```

### Test 6.2: Error 500 - Sin Detalles Técnicos
```bash
# Forzar error (ej: null pointer)
curl "https://tu-app.azurewebsites.net/api/libros/null/incrementar"

# Esperado: Mensaje genérico "Se ha producido un error"
```

---

## 7. Pruebas de Inyección de Código

### Test 7.1: SQL Injection (prevenido por ORM/Prepared Statements)
```bash
curl -X POST https://tu-app.azurewebsites.net/api/libros/agregar \
  -H "Content-Type: application/json" \
  -d '{"titulo":"x\"; DROP TABLE libros; --","autor":"Test","isbn":"123"}'

# Esperado: HTTP 400 (SecurityFilter detecta patrones)
```

### Test 7.2: Command Injection
```bash
curl "https://tu-app.azurewebsites.net/api/libros?id=; rm -rf /"

# Esperado: HTTP 400 (SecurityFilter)
```

---

## 8. Pruebas de Autenticación/Autorización

### Test 8.1: Acceso sin autenticación (permitido para demo)
```bash
curl https://tu-app.azurewebsites.net/api/libros

# Esperado: Funciona (según especificación)
```

### Test 8.2: Métodos HTTP no permitidos
```bash
curl -X TRACE https://tu-app.azurewebsites.net/

# Esperado: HTTP 405 Method Not Allowed o 400
```

---

## 9. Pruebas de Performance y DoS

### Test 9.1: Rate Limiting (si está implementado)
```bash
for i in {1..100}; do curl -s https://tu-app.azurewebsites.net/ > /dev/null; done

# Esperado: Algunas requests retornan 429 Too Many Requests
# (Si WAF está configurado)
```

### Test 9.2: Large Payload
```bash
curl -X POST https://tu-app.azurewebsites.net/api/libros/agregar \
  -H "Content-Type: application/json" \
  -d '{"titulo":"'$(printf 'A%.0s' {1..10000})'","autor":"Test","isbn":"123"}'

# Esperado: HTTP 413 Request Entity Too Large (configurable)
```

---

## 10. Pruebas Automatizadas con OWASP ZAP

### Instalación y Configuración
```bash
# Descargar OWASP ZAP desde:
https://www.zaproxy.org/download/

# O con Docker:
docker pull owasp/zap2docker-weekly
```

### Ejecutar Baseline Scan
```bash
docker run -t owasp/zap2docker-weekly zap-baseline.py \
  -t https://tu-app.azurewebsites.net \
  -r baseline-report.html
```

### Ejecutar Full Scan
```bash
docker run -t owasp/zap2docker-weekly zap-full-scan.py \
  -t https://tu-app.azurewebsites.net \
  -r full-report.html
```

### Parámetros útiles
```bash
# Solo ciertos tipos de ataques:
-X "SQL Injection" "Cross Site Scripting (XSS)"

# Tiempo de escaneo:
-m 60 (60 minutos)

# Configuración personalizada:
-c config.yaml
```

---

## 11. Pruebas de Conformidad

### Test 11.1: OWASP Top 10 A01 (Broken Access Control)
- ✅ CORS restrictivo
- ✅ Métodos HTTP validados

### Test 11.2: OWASP Top 10 A02 (Cryptographic Failures)
- ✅ HTTPS/HSTS habilitado
- ✅ Cookies secure habilitadas

### Test 11.3: OWASP Top 10 A05 (Security Misconfiguration)
- ✅ Headers de seguridad presentes
- ✅ Errores sin detalles técnicos

### Test 11.4: OWASP Top 10 A07 (XSS)
- ✅ CSP implementado
- ✅ SecurityFilter detecta patrones
- ✅ Input validation

---

## 12. Checklist de Validación

- [ ] Todos los 7 headers de seguridad presentes
- [ ] HTTPS funciona correctamente
- [ ] Cookies tienen HttpOnly + Secure + SameSite
- [ ] Errores no exponen información técnica
- [ ] XSS attempts son bloqueados
- [ ] Path traversal es prevenido
- [ ] CSRF protection activa
- [ ] CORS solo permite orígenes autorizados
- [ ] Métodos HTTP no permitidos retornan 405
- [ ] Rate limiting funciona (si implementado)
- [ ] Logs se generan correctamente
- [ ] Monitoreo en Azure está activo
- [ ] WAF en Azure está activo
- [ ] Escaneo DAST pasa con 0 vulnerabilidades críticas

---

## 13. Herramientas Recomendadas

### Escaneo de Seguridad
- OWASP ZAP (gratuito)
- Burp Suite (profesional)
- Qualys ASAP
- Acunetix

### Testing de Headers
- securityheaders.com
- Mozilla Observatory
- curl / Postman

### Análisis de Código
- SonarQube
- Checkmarx
- Black Duck
- Fortify

### Monitoreo en Azure
- Azure Security Center
- Azure Sentinel
- Application Insights
- Azure Advisor

---

**Generado**: 28 de Noviembre de 2025  
**Versión**: 1.0  
**Estado**: Listo para Testing
