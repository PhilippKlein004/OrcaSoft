package org.hbrs.se2.project.hellocar.db;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sascha
 */

public class JDBCConnection {

    private static JDBCConnection connection = null;

    private String url = "jdbc:postgresql://****";

    private Connection conn;

    // ToDo : Passwort und Nutzername sichern

    private String login = "****";

    private String password = "****";

    public static JDBCConnection getInstance() throws DatabaseException {

        if ( connection == null ) {
            connection = new JDBCConnection();
        }
        return connection;

    }

    private JDBCConnection() throws DatabaseException {
        this.initConnection();

    }

    public Connection getConnection() {
        return conn;
    }


    public void initConnection() throws DatabaseException {
        try {
            DriverManager.registerDriver( new org.postgresql.Driver() ); 
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.openConnection();

    }

    public void openConnection() throws DatabaseException {

        try {
            Properties props = new Properties();
            props.setProperty("user", "****" );
            props.setProperty("password", "****" );


            this.conn = DriverManager.getConnection(this.url, props);

        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException( DatabaseException.NO_CONNECTION );
        }
    }

    public Statement getStatement() throws DatabaseException {

        try {
            if ( this.conn.isClosed() ) {
                this.openConnection();
            }

            return this.conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public PreparedStatement getPreparedStatement( String sql  ) throws DatabaseException {
        try {
            if ( this.conn.isClosed() ) {
                this.openConnection();
            }

            return this.conn.prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public void closeConnection(){
        try {
            this.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}

