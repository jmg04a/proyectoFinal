/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import clases.conexionBaseDatos;
import modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // Necesitamos acceso al gestor de conexiones (puedes pasarlo en el constructor)
    private conexionBaseDatos dbManager;

    public UsuarioDAO() throws SQLException {
        // Opción A: Crear una instancia nueva (solo si conexionBaseDatos maneja el pool internamente como static)
        // Opción B (Mejor): Recibir el dbManager desde fuera para compartir el Pool.
        this.dbManager = new conexionBaseDatos(); 
    }

    public Usuario login(String username, String passwordHash) {
        
        // SQL: Agregué 'LIMIT 1' o 'FETCH FIRST' por seguridad si tiene múltiples roles
        // Nota: Oracle usa FETCH FIRST 1 ROWS ONLY
        String sql = """
            SELECT u.id_usuario, u.nombre, u.username, r.nombre_rol
            FROM aplicacion.usuario u
            JOIN aplicacion.usuario_rol ur ON u.id_usuario = ur.id_usuario
            JOIN aplicacion.rol r ON ur.id_rol = r.id_rol
            WHERE u.username = ? 
              AND u.password_hash = ? 
              AND u.estado = 'ACTIVO'
        """;
        // CORRECCIÓN CRÍTICA:
        // Pedimos la conexión AQUÍ, dentro del try.
        // Al terminar el bloque try, la conexión se cierra (regresa al pool) automáticamente.
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, passwordHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // --- AGREGA ESTO TEMPORALMENTE ---
                    System.out.println("--- DEBUG LOGIN ---");
                    System.out.println("Enviando Usuario: '" + username + "'");
                    System.out.println("Enviando Hash:    '" + passwordHash + "'");
                    // ---------------------------------
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setUsername(rs.getString("username"));
                    u.setRol(rs.getString("nombre_rol"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error grave en login: " + e.getMessage());
            e.printStackTrace(); // Imprime el error completo para ver detalles de Oracle
        }
        
        return null; // Retorna null si no existe, no tiene rol, o la pass está mal
    }
    // Agrega estos métodos dentro de tu clase UsuarioDAO

    // 1. Método para registrar la sesión en la BD
    public boolean registrarSesion(int idUsuario) {
        String sql = "INSERT INTO Sesion (id_sesion, id_usuario, fecha_inicio, estado) " +
                     "VALUES (seq_sesion.NEXTVAL, ?, SYSDATE, 'ACTIVA')";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            System.out.println("Sesión registrada exitosamente en BD para usuario ID: " + idUsuario);
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar sesión: " + e.getMessage());
            return false;
        }
    }

    // 2. Método para calcular permisos combinados (Soporta 1 o muchos roles)
    public String obtenerPermisosHexCombinados(int idUsuario) {
        int permisosTotales = 0;

        // Hacemos JOIN con Usuario_Rol para obtener TODOS los roles del usuario
        String sql = "SELECT r.valor_permisos_hex " +
                     "FROM APLICACION.Rol r " +
                     "JOIN APLICACION.Usuario_Rol ur ON r.id_rol = ur.id_rol " +
                     "WHERE ur.id_usuario = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String hex = rs.getString("valor_permisos_hex");
                    if (hex != null && !hex.isEmpty()) {
                        // Convertimos Hex a Entero y usamos OR bitwise (|) para sumar permisos
                        permisosTotales = permisosTotales | Integer.parseInt(hex, 16);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error calculando permisos: " + e.getMessage());
        }

        // Retornamos el total convertido nuevamente a Hexadecimal (String)
        return Integer.toHexString(permisosTotales).toUpperCase();
    }
}


