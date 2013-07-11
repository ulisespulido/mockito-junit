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

    @Before
    public void setUp() throws Exception {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        contact = new Contact();
        contactDAOJDBC = spy(new ContactDaoJdbc());
        doReturn(connection).when(contactDAOJDBC).getConnection();
    }

    @Test
    public void shouldStoreContactInDatabase() throws SQLException {

        contact.setName("Juan Perez");
        contact.setEmail("juan@perez.com");

        when(connection.prepareStatement(SQL)).thenReturn(preparedStatement);

        Result actual = contactDAOJDBC.save(contact);

        verify(preparedStatement).setString(1, "Juan Perez");
        verify(preparedStatement).setString(2, "juan@perez.com");
        verify(preparedStatement).execute();
        verify(preparedStatement).close();
        verify(connection).close();

        assertEquals(Result.SUCCESS, actual);

    }

    @Test
    public void shouldLogSqlException() throws SQLException {
        SQLException sqlException = mock(SQLException.class);
        Logger logger = mock(Logger.class);

        doReturn(logger).when(contactDAOJDBC).getLogger();
        when(sqlException.getMessage()).thenReturn("There was an error");
        when(connection.prepareStatement(SQL)).thenThrow(sqlException);

        Result actual = contactDAOJDBC.save(contact);

        verify(logger).error("Something weird happened", "There was an error");

        assertEquals(Result.FAILED, actual);

    }
}
