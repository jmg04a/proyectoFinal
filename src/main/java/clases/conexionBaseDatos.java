/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.sql.*;
import javax.swing.table.DefaultTableModel;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

public class conexionBaseDatos {

    private static final String DB_USER = "APLICACION";
    private static final String DB_PASSWORD = "22130828Jose";
    private static final String CONNECT_STRING = "(description= (retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=adb.mx-queretaro-1.oraclecloud.com))(connect_data=(service_name=gf58054ee61183e_tbdproyecto_high.adb.oraclecloud.com))(security=(ssl_server_dn_match=yes)))";
    private static final String CONN_FACTORY = "oracle.jdbc.pool.OracleDataSource";
    private PoolDataSource pool;

    public conexionBaseDatos() throws SQLException {
        pool = PoolDataSourceFactory.getPoolDataSource();
        pool.setConnectionFactoryClassName(CONN_FACTORY);
        pool.setURL("jdbc:oracle:thin:@" + CONNECT_STRING);
        pool.setUser(DB_USER);
        pool.setPassword(DB_PASSWORD);
        pool.setInitialPoolSize(1);
        pool.setMaxPoolSize(5);
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }
    
    public boolean probarConexion() {
        // Usamos try-with-resources para asegurar que la conexión se cierre 
        // y regrese al pool inmediatamente después de la prueba.
        try (Connection conn = pool.getConnection()) {
            
            // isValid(timeout) verifica si la conexión está viva.
            // Le damos 3 segundos para responder.
            System.out.println("Conexión funcional");
            return conn.isValid(3);
            
        } catch (SQLException e) {
            System.out.println("⚠️ Falló la prueba de conexión: " + e.getMessage());
            return false;
        }
    }

    public DefaultTableModel getTableModel(String query) {
        DefaultTableModel modelo = new DefaultTableModel();

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int numeroColumnas = metaData.getColumnCount();

            for (int i = 1; i <= numeroColumnas; i++) {
                modelo.addColumn(metaData.getColumnLabel(i));
            }

            while (rs.next()) {
                Object[] fila = new Object[numeroColumnas];
                for (int i = 0; i < numeroColumnas; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                modelo.addRow(fila);
            }
            // -----------------------------------------------------------

            return modelo;

        } catch (SQLException e) {
            System.out.println("Error al obtener datos: " + e.getMessage());
            e.printStackTrace(); // Esto ayuda a ver errores de Oracle específicos
            return new DefaultTableModel(); // Retorna tabla vacía en caso de error
        }
    }
}

