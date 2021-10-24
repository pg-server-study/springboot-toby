package com.example.springtoby.toby.statement;

import com.example.springtoby.toby.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStatement implements StatementStrategy {

    private User user;

    public AddStatement(User user) {
        this.user = user;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into users(id, name, password) values(?,?,?)");

        preparedStatement.setString(1,user.getId());
        preparedStatement.setString(2,user.getName());
        preparedStatement.setString(3,user.getPassword());
        return preparedStatement;
    }
}
