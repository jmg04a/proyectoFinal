/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jose
 */
public class GestorPermisos {

    // 1. DEFINIR TUS PERMISOS (Potencias de 2)
    // Estos son los valores DECIMALES
    public static final int PERMISO_VER_PACIENTE    = 1;  // 2^0
    public static final int PERMISO_CREAR_CITA      = 2;  // 2^1
    public static final int PERMISO_EDITAR_HISTORIAL= 4;  // 2^2
    public static final int PERMISO_ELIMINAR_TODO   = 8;  // 2^3
    // Si necesitas mas: 16, 32, 64, 128...

    /**
     * Función solicitada por el profesor.
     * Verifica si un Rol (en Hexadecimal) tiene un Permiso específico (Decimal).
     * * @param rolHex  El valor que viene de la BD (ej: "F", "5", "1A")
     * @param permiso El permiso que quieres revisar (ej: 4)
     * @return true si tiene permiso, false si no.
     */
    
    public static boolean tienePermiso(String rolHex, int permiso) {
        try {
            // Paso A: Convertir el Hexadecimal (String) a Entero (Decimal)
            // El '16' indica que estamos parseando base hexadecimal
            int valorRol = Integer.parseInt(rolHex, 16);

            // Paso B: Operación Bitwise AND (&)
            // Si (Rol & Permiso) == Permiso, entonces el bit está encendido.
            return (valorRol & permiso) == permiso;

        } catch (NumberFormatException e) {
            System.err.println("Error: El rol no tiene un formato Hexadecimal válido.");
            return false;
        }
    }
}