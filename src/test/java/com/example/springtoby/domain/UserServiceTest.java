package com.example.springtoby.domain;

import com.example.springtoby.toby.User;
import com.example.springtoby.toby.UserDao;
import com.example.springtoby.toby.UserService;
import com.example.springtoby.toby.enums.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static com.example.springtoby.toby.UserService.MIN_LOG_COUNT_FOR_SILVER;
import static com.example.springtoby.toby.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    List<User> userList;

    @BeforeEach
    public void setUp() {
        userList = List.of(
                new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "p3", Level.SIlVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("madnite1", "이상호", "p4", Level.SIlVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오민규", "p5", Level.GOLD, 100, 100)
        );
    }

    @DisplayName("빈이 올바르게 주입되었는지 테스트한다.")
    @Test
    public void bean() {
        assertThat(this.userService).isNotNull();
    }

    @Test
    public void upgradeLevels() throws SQLException {

        userDao.deleteAll();

        for (User user : userList) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(userList.get(0), false);
        checkLevel(userList.get(1), true);
        checkLevel(userList.get(2), false);
        checkLevel(userList.get(3), true);
        checkLevel(userList.get(4), false);

    }

    @Test
    public void add() throws SQLException {
        userDao.deleteAll();

        User userWithLevel = userList.get(4); // GOLD LEVEL
        User userWithoutLevel = userList.get(0);
        userWithoutLevel.setLevel(null); // 비어있는 레벨 설정

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);

    }

    @Test
    public void upgradeAllOrNothing() throws SQLException {
        UserService testUserService = new TestUserService(userList.get(3).getId(), userDao, transactionManager);



        userDao.deleteAll();

        for (User user : userList) userDao.add(user);

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {

        }
        checkLevel(userList.get(1), false);
    }

    private void checkLevel(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());

        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().getNext());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }

    }

    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id, UserDao userDao, PlatformTransactionManager transactionManager) {
            super(userDao, transactionManager);
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }

}
