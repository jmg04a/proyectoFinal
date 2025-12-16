/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jose
 */
public class Permisos {
    // --- PACIENTES ---
    public static final int PACIENTE_VER       = 1;   // 2^0
    public static final int PACIENTE_EDITAR    = 2;   // 2^1 (Crear/Modificar datos personales)

    // --- CITAS ---
    public static final int CITA_VER           = 4;   // 2^2 (Ver calendario)
    public static final int CITA_GESTIONAR     = 8;   // 2^3 (Agendar/Cancelar/Reasignar)

    // --- HISTORIAL MÉDICO ---
    public static final int HISTORIAL_VER      = 16;  // 2^4 (Leer expedientes)
    public static final int HISTORIAL_EDITAR   = 32;  // 2^5 (Diagnosticar/Agregar notas)

    // --- ADMINISTRACIÓN ---
    public static final int USUARIO_GESTIONAR  = 64;  // 2^6 (Crear usuarios/roles)
    public static final int AUDITORIA_VER      = 128; // 2^7 (Ver logs de seguridad)
}
