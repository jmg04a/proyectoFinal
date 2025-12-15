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

    private Connection conn;

    public UsuarioDAO() throws SQLException {
        conexionBaseDatos conexion = new conexionBaseDatos();
        this.conn = conexion.getConnection();
    }

    public Usuario login(String username, String passwordHash) {

        String sql = """
            SELECT u.id_usuario,
                   u.nombre,
                   u.username,
                   r.nombre_rol
            FROM aplicacion.usuario u
            JOIN aplicacion.usuario_rol ur ON u.id_usuario = ur.id_usuario
            JOIN aplicacion.rol r ON ur.id_rol = r.id_rol
            WHERE u.username = ?
              AND u.password_hash = ?
              AND u.estado = 'ACTIVO'
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, passwordHash);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setUsername(rs.getString("username"));
                u.setRol(rs.getString("nombre_rol"));
                return u;
            }

        } catch (SQLException e) {
            System.out.println("Error login: " + e.getMessage());
        }

        return null;
    }
}


