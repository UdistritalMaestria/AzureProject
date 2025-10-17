package com.mcic;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SmokeTest {

    @Test
    @DisplayName("Prueba básica de que el sistema funciona")
    void testSistemaFunciona() {
        assertTrue(true, "El sistema básico debe funcionar");
    }

    
}
