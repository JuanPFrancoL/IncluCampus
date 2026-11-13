package org.Juan.util;

/** Valores fijos del proyecto. Se cambian aquí y afectan a todo. */
public final class Constantes {

    private Constantes() {
        // No se crean objetos de esta clase
    }

    // Cuánto "pesa" cada tipo de camino respecto a caminar en plano
    public static final double FACTOR_RAMPA = 1.5;
    public static final double FACTOR_ESCALERA = 3.0;
    public static final double ESPERA_ASCENSOR = 20.0;

    // Para el perfil visual: las escaleras pesan el doble de lo normal
    public static final double PENALIZACION_ESCALERA_VISUAL = 2.0;
}