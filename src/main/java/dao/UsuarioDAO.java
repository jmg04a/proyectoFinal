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
}


