/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seguridad;
import modelo.Usuario;
/**
 *
 * @author Oskgab
 */
public class sesion {
    

    private static Usuario usuarioActual;
    private static String rolActual;

    // Iniciar sesión
    public static void iniciarSesion(Usuario usuario, String rol) {
        usuarioActual = usuario;
        rolActual = rol;
    }

    // Cerrar sesión
    public static void cerrarSesion() {
        usuarioActual = null;
        rolActual = null;
    }

    // Verificar si hay sesión activa
    public static boolean haySesion() {
        return usuarioActual != null;
    }

    // Getters
    public static Usuario getUsuario() {
        return usuarioActual;
    }

    public static String getRol() {
        return rolActual;
    }

    // Helpers por rol
    public static boolean esAdmin() {
        return "ADMIN".equalsIgnoreCase(rolActual);
    }

    public static boolean esDoctor() {
        return "DOCTOR".equalsIgnoreCase(rolActual);
    }

    public static boolean esRecepcionista() {
        return "RECEPCIONISTA".equalsIgnoreCase(rolActual);
    }
}


