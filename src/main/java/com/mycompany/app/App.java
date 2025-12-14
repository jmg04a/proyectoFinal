/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.app;

import clases.conexionBaseDatos;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jose
 */
public class App {

    public static void main(String[] args) throws SQLException {
        System.out.println("Hello World!");
        try {
            conexionBaseDatos test = new conexionBaseDatos();
            ResultSet rs=test.query("select * from paciente");
            System.out.println(rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
