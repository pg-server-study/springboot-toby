package com.example.springtoby.toby;

import com.example.springtoby.toby.context.JdbcContext;
import com.example.springtoby.toby.enums.Level;
import com.example.springtoby.toby.exception.DuplicateUserIdException;
import com.example.springtoby.toby.statement.DeleteAllStatement;
import com.example.springtoby.toby.statement.StatementStrategy;
import com.mysql.cj.exceptions.MysqlErrorNumbers;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@NoArgsConstructor
@Component
public class UserDao {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void deleteAll() throws SQLException {
        this.jdbcTemplate.update("delete from users");
    }

    public void add(final User user) throws DuplicateUserIdException {
        this.jdbcTemplate
                .update("insert into users(id, name, password, level, login, recommend) values(?,?,?,?,?,?)"
                        , user.getId(), user.getName(), user.getPassword(), user.getLevel().getValue(), user.getLogin(), user.getRecommend());
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setLevel(Level.valueOf(rs.getInt("level")));
                user.setLogin(rs.getInt("login"));
                user.setRecommend(rs.getInt("recommend"));
                return user;
            }
        });
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setLevel(Level.valueOf(rs.getInt("level")));
                user.setLogin(rs.getInt("login"));
                user.setRecommend(rs.getInt("recommend"));
                return user;
            }
        });
    }

    public void update(User user) {
        jdbcTemplate.update(
                "update users " +
                        "set name = ?, password = ?, level =?, login = ?, " +
                        "recommend = ? where id = ?",
                user.getName(), user.getPassword(),
                user.getLevel().getValue(), user.getLogin(),
                user.getRecommend(),
                user.getId()
        );
    }

}
