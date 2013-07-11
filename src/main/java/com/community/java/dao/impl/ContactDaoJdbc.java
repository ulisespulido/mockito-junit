package com.community.java.dao.impl;

import com.community.java.beans.Contact;
import com.community.java.beans.Result;
import com.community.java.dao.ContactDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContactDaoJdbc implements ContactDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactDaoJdbc.class);

    @Override
    public Result save(Contact contact) {
        try {
            Connection connection = getConnection();
            String sql = "INSERT INTO contacts VALUES (nextval('contacts_sequence'), ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, contact.getName());
            preparedStatement.setString(2, contact.getEmail());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            getLogger().error("Something weird happened", e.getMessage());
            return Result.FAILED;
        }

        return Result.SUCCESS;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/agenda", "postgres", "postgres");
    }

    public Logger getLogger() {
        return LOGGER;
    }
}
