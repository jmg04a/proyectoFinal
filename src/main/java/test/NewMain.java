package test;

import static modelo.GestorPermisos.PERMISO_CREAR_CITA;
import static modelo.GestorPermisos.PERMISO_EDITAR_HISTORIAL;
import static modelo.GestorPermisos.PERMISO_VER_PACIENTE;
import static modelo.GestorPermisos.tienePermiso;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author jose
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // Ejemplo: El Doctor tiene valor Hex "5" (que es 1 + 4 en decimal)
        String rolDoctorHex = "ff"; 

        System.out.println("--- Probando Rol Doctor (Hex: 5) ---");

        // Prueba 1: ¿Puede ver paciente? (Permiso 1) -> Debería ser TRUE
        if (tienePermiso(rolDoctorHex, PERMISO_VER_PACIENTE)) {
            System.out.println("ACCESO CONCEDIDO: Puede ver pacientes.");
        } else {
            System.out.println("ACCESO DENEGADO: No puede ver pacientes.");
        }

        // Prueba 2: ¿Puede crear cita? (Permiso 2) -> Debería ser FALSE
        if (tienePermiso(rolDoctorHex, PERMISO_CREAR_CITA)) {
            System.out.println("ACCESO CONCEDIDO: Puede crear citas.");
        } else {
            System.out.println("ACCESO DENEGADO: No puede crear citas.");
        }
        
        // Prueba 3: ¿Puede editar historial? (Permiso 4) -> Debería ser TRUE
        if (tienePermiso(rolDoctorHex, PERMISO_EDITAR_HISTORIAL)) {
            System.out.println("ACCESO CONCEDIDO: Puede editar historial.");
        } else {
            System.out.println("ACCESO DENEGADO: No puede editar historial.");
        }
    }
    
}
