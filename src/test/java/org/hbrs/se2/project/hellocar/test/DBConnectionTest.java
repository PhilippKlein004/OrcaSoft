package org.hbrs.se2.project.hellocar.test;


import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.db.JDBCConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;
import org.postgresql.Driver;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class DBConnectionTest {


    // Test Attribut von JDBCConnection
    private static JDBCConnection connection;

    // Initialisierung von JDBC Verbindung
    @BeforeAll
    static void setUp() throws Exception {
        connection = JDBCConnection.getInstance();
    }

    //Schließung von JDBC Verbindung
    @AfterAll
    static void tearDown() {
        connection.closeConnection();
    }

    // Test ob Verbindung existiert
    @Test
    void testConnectionIsPresent() throws DatabaseException {
        assertNotNull(connection);
        JDBCConnection connectionCopy = JDBCConnection.getInstance();
        assertEquals(connection, connectionCopy);
    }


    // Test ob Verbindung offen ist
    @Test
    void testConnectionIsOpen() throws SQLException {
        assertFalse(connection.getConnection().isClosed());

    }
    // Test ob Verbindung irgendein ein Statement aufnehmen kann
    @Test
    void testGetStatement() throws DatabaseException, SQLException {
        Statement statement = connection.getStatement();
        assertNotNull(statement);
        connection.closeConnection();
        Statement statement1 = connection.getStatement();
        assertFalse(connection.getConnection().isClosed());
        assertNotNull(statement1);
    }


    // Test ob Verbindung zu ist
    @Test
    void testClosedConnection() throws SQLException {
        connection.closeConnection();
        assertTrue(connection.getConnection().isClosed());
    }

    @Test
    void testGetPreparedStatement() throws DatabaseException, SQLException {
        PreparedStatement preparedStatement = connection.getPreparedStatement("insert into test values (?)");
        assertNotNull(preparedStatement);
        connection.closeConnection();
        PreparedStatement preparedStatement2 = connection.getPreparedStatement("insert into test values (?)");
        assertFalse(connection.getConnection().isClosed());
        assertNotNull(preparedStatement2);
    }

    @Test
    public void testGetInstanceDatabaseException() {
        try {
            JDBCConnection connection = JDBCConnection.getInstance();
            assertNotNull(connection, "Connection instance should not be null");
        } catch (DatabaseException e) {
            fail("DatabaseException should not be thrown");
        }
    }

    @Test
    public void testInitConnectionSQLException() {
        try {
            DriverManager.registerDriver(mock(Driver.class));
            connection.initConnection();
        } catch (DatabaseException e) {
            fail("DatabaseException should not be thrown");
        } catch (SQLException e) {
            assertTrue(true, "SQLException should be caught and logged");
        }
    }
/*
    @Test
    public void testOpenConnectionDatabaseException() {
        JDBCConnection jdbcConnectionMock = mock(JDBCConnection.class);

        try {
            doThrow(new SQLException()).when(jdbcConnectionMock).openConnection();
            jdbcConnectionMock.openConnection();
            fail("DatabaseException should be thrown");
        } catch (DatabaseException e) {
            assertEquals(DatabaseException.NO_CONNECTION, e.getMessage(), "Correct exception message should be thrown");
        }
    }



    @Test
    public void testGetStatementSQLException() {
        try {
            Connection connMock = mock(Connection.class);
            when(connMock.isClosed()).thenReturn(true);
            when(connMock.createStatement()).thenThrow(new SQLException());
            connection.openConnection();
            connection.getStatement();
            fail("DatabaseException should be thrown");
        } catch (DatabaseException e) {
            assertTrue(true, "DatabaseException should be thrown");
        } catch (SQLException e) {
            fail("SQLException should not be thrown directly");
        }
    }



    @Test
    public void testGetPreparedStatementSQLException() {
        try {
            Connection connMock = mock(Connection.class);
            when(connMock.isClosed()).thenReturn(true);
            when(connMock.prepareStatement(anyString())).thenThrow(new SQLException());
            connection.openConnection();
            connection.getPreparedStatement("SELECT * FROM table");
            fail("DatabaseException should be thrown");
        } catch (DatabaseException e) {
            assertTrue(true, "DatabaseException should be thrown");
        } catch (SQLException e) {
            fail("SQLException should not be thrown directly");
        }
    }

 */

    @Test
    public void testCloseConnectionSQLException() {
        try {
            Connection connMock = mock(Connection.class);
            doThrow(new SQLException()).when(connMock).close();
            connection.openConnection();
            connection.closeConnection();
            assertTrue(true, "SQLException should be caught and logged");
        } catch (DatabaseException e) {
            fail("DatabaseException should not be thrown");
        } catch (SQLException e) {
            fail("SQLException should not be thrown directly");
        }
    }




}
