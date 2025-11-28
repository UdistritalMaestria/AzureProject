package com.mcic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Configuración de headers de seguridad HTTP
 * Mitiga vulnerabilidades DAST:
 * - Content Security Policy (CSP) Header Not Set
 * - Missing Anti-clickjacking Header (X-Frame-Options)
 * - Strict-Transport-Security Header Not Set
 * - X-Content-Type-Options Header Missing
 */
@Configuration
public class SecurityHeadersConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityHeadersInterceptor());
    }

    public static class SecurityHeadersInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, 
                                HttpServletResponse response, 
                                Object handler) {
            
            // Content Security Policy (CSP) Header
            // Permite solo recursos del mismo origen
            response.setHeader("Content-Security-Policy", 
                "default-src 'self'; " +
                "script-src 'self' 'unsafe-inline'; " +
                "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                "font-src 'self' https://fonts.gstatic.com; " +
                "img-src 'self' data: https:; " +
                "connect-src 'self'; " +
                "frame-ancestors 'none'; " +
                "base-uri 'self'; " +
                "form-action 'self'");

            // Strict-Transport-Security (HSTS)
            // Fuerza HTTPS en todos los navegadores durante 1 año
            response.setHeader("Strict-Transport-Security", 
                "max-age=31536000; includeSubDomains; preload");

            // X-Frame-Options - Anti-clickjacking
            // Impide que la página se cargue en iframes
            response.setHeader("X-Frame-Options", "DENY");

            // X-Content-Type-Options
            // Previene MIME type sniffing
            response.setHeader("X-Content-Type-Options", "nosniff");

            // X-XSS-Protection
            // Habilita protección contra XSS en navegadores antiguos
            response.setHeader("X-XSS-Protection", "1; mode=block");

            // Referrer-Policy
            // Controla información del referrer
            response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

            // Permissions-Policy (antes Feature-Policy)
            // Restricción de características del navegador
            response.setHeader("Permissions-Policy", 
                "geolocation=(), " +
                "microphone=(), " +
                "camera=(), " +
                "payment=(), " +
                "usb=(), " +
                "magnetometer=(), " +
                "gyroscope=(), " +
                "accelerometer=()");

            return true;
        }
    }
}
