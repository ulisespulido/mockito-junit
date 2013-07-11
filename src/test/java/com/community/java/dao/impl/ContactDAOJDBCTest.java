package com.community.java.dao.impl;

import com.community.java.beans.Contact;
import com.community.java.beans.Result;
import com.community.java.dao.ContactDao;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ContactDaoJdbcTest {

    private Connection connection;
    public static final String SQL = "INSERT INTO contacts VALUES (nextval('contacts_sequence'), ?, ?)";
    private PreparedStatement preparedStatement;
    private Contact contact;
    private ContactDaoJdbc contactDAOJDBC;
    private SQLException sqlException;
    private Logger logger;

    @Before
    public void setUp() throws Exception {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        contact = new Contact();
        contactDAOJDBC = spy(new ContactDaoJdbc());
        sqlException = mock(SQLException.class);
        logger = mock(Logger.class);

        doReturn(connection).when(contactDAOJDBC).getConnection();
        doReturn(logger).when(contactDAOJDBC).getLogger();

        when(connection.prepareStatement(SQL)).thenReturn(preparedStatement);

    }

    @Test
    public void shouldStoreContactInDatabase() throws SQLException {

        contact.setName("Juan Perez");
        contact.setEmail("juan@perez.com");


        Result actual = contactDAOJDBC.save(contact);

        verify(preparedStatement).setString(1, "Juan Perez");
        verify(preparedStatement).setString(2, "juan@perez.com");
        verify(preparedStatement).execute();

        assertEquals(Result.SUCCESS, actual);

    }

    @Test
    public void shouldLogSqlException() throws SQLException {

        when(connection.prepareStatement(SQL)).thenThrow(sqlException);

        Result actual = contactDAOJDBC.save(contact);

        verify(logger).error("Something weird happened", sqlException);

        assertEquals(Result.FAILED, actual);
        verify(connection).close();

    }

    @Test
    public void shouldClosePreparedStatementInCaseAnErrorHappensInExecution() throws SQLException {
        when(preparedStatement.execute()).thenThrow(sqlException);
        Result actual = contactDAOJDBC.save(contact);
        assertEquals(Result.FAILED, actual);
        verify(connection).close();
        verify(preparedStatement).close();
    }

    @Test
    public void shouldNotTryToCloseWhenConnectionThrowsException () throws SQLException {

        doThrow(sqlException).when(contactDAOJDBC).getConnection();

        Result actual = contactDAOJDBC.save(contact);

        verify(connection, never()).close();
        verify(preparedStatement, never()).close();
        assertEquals(Result.FAILED, actual);

    }

    @Test
    public void shouldLogErrorIfClosingFails() throws SQLException {
        doThrow(sqlException).when(connection).close();
        doThrow(sqlException).when(preparedStatement).close();

        Result actual = contactDAOJDBC.save(contact);

        verify(logger).error("Error closing connection", sqlException);
        verify(logger).error("Error closing prepared statement", sqlException);

        assertEquals(Result.SUCCESS, actual);

    }
}
