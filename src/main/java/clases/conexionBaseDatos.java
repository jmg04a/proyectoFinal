/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.sql.Connection;
import java.sql.SQLException;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

public class conexionBaseDatos {

    private static final String DB_USER = "APLICACION";
    private static final String DB_PASSWORD = "22130828Jose";
    private static final String CONNECT_STRING = "tbdproyecto_high";
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
}

