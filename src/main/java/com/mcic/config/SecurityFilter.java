package com.mcic.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filtro de seguridad HTTP
 * - Valida métodos HTTP permitidos
 * - Registra intentos sospechosos
 * - Previene ataques básicos de inyección
 */
@Component
public class SecurityFilter implements Filter {

    private static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"};
    private static final String[] FORBIDDEN_PATTERNS = {
        "..%2f", // Path traversal encoding
        "..\\", // Path traversal Windows
        "%00", // Null byte
        "script>", // XSS attempt
        "eval(", // Code injection
        "exec(", // Code injection
        "system(" // OS command injection
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Validar método HTTP
        String method = httpRequest.getMethod();
        boolean validMethod = false;
        for (String allowedMethod : ALLOWED_METHODS) {
            if (allowedMethod.equals(method)) {
                validMethod = true;
                break;
            }
        }

        if (!validMethod) {
            httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        // Validar patrones sospechosos en la URL
        String queryString = httpRequest.getQueryString();
        String requestURI = httpRequest.getRequestURI();
        
        if (containsForbiddenPattern(requestURI) || 
            (queryString != null && containsForbiddenPattern(queryString))) {
            
            // Log del intento sospechoso
            System.err.println("Suspicious request detected: " + requestURI + 
                             (queryString != null ? "?" + queryString : ""));
            
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }

    /**
     * Verifica si la cadena contiene patrones prohibidos
     */
    private boolean containsForbiddenPattern(String input) {
        if (input == null) return false;
        
        String lowercaseInput = input.toLowerCase();
        for (String pattern : FORBIDDEN_PATTERNS) {
            if (lowercaseInput.contains(pattern.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
