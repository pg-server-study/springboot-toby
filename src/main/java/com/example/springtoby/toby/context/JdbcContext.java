package com.example.springtoby.toby.context;

import com.example.springtoby.toby.statement.StatementStrategy;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@NoArgsConstructor
@Component
public class JdbcContext {

    @Autowired
    private DataSource dataSource;

    public void executeSql(final String query) throws SQLException {
        workWithStatementStrategy(
                connection -> connection.prepareStatement(query)
        );
    }

    public void workWithStatementStrategy(StatementStrategy statementStrategy) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.dataSource.getConnection();

            preparedStatement = statementStrategy.makePreparedStatement(connection);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {

                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {

                }
            }
        }
    }
}
