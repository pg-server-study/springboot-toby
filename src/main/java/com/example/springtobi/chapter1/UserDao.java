package com.example.springtobi.chapter1;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@NoArgsConstructor
@Component
public class UserDao {

    @Autowired
    private DataSource dataSource;

    // Insert
    public void add(User user) throws ClassNotFoundException {
        String sql = "insert into users (id, name, password) values(?,?,?)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1,user.getId());
            ps.setString(2,user.getName());
            ps.setString(3,user.getPassword());

            int result = ps.executeUpdate();

            if(result != 1) {
                throw new SQLException();
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public User get(String id) throws ClassNotFoundException {

        User user = new User();

        String sql = "select * from users where id = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));

            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return user;
    }

}
