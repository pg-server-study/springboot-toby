package com.example.springtoby.domain;

import com.example.springtoby.toby.User;
import com.example.springtoby.toby.UserDao;
import com.example.springtoby.toby.UserDaoImpl;
import com.example.springtoby.toby.exception.DuplicateUserIdException;
import com.example.springtoby.toby.service.UserService;
import com.example.springtoby.toby.service.UserServiceImpl;
import com.example.springtoby.toby.enums.Level;
import com.example.springtoby.toby.service.UserServiceTx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static com.example.springtoby.toby.service.UserServiceImpl.MIN_LOG_COUNT_FOR_SILVER;
import static com.example.springtoby.toby.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Qualifier("userServiceTx")
    @Autowired
    UserService userService;

    @Autowired
    UserDaoImpl userDaoImpl;

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

        UserDao mockUserDao = mock(UserDao.class);

        when(mockUserDao.getAll()).thenReturn(this.userList); // getAll() 할때 userList 리턴하도록 설정

        UserServiceImpl userService = new UserServiceImpl(mockUserDao); // Service 에 Mock 객체 주입

        userDaoImpl.deleteAll();

        userService.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao, times(2)).update(any(User.class));

        verify(mockUserDao).update(userList.get(1));
        assertThat(userList.get(1).getLevel()).isEqualTo(Level.SIlVER);

        verify(mockUserDao).update(userList.get(3));
        assertThat(userList.get(3).getLevel()).isEqualTo(Level.GOLD);

    }
    // upgradeLevels 에서 사용할 테스트용 메소드
    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId()).isEqualTo(expectedId);
        assertThat(updated.getLevel()).isEqualTo(expectedLevel);
    }

    @Test
    public void add() throws SQLException {
        userDaoImpl.deleteAll();

        User userWithLevel = userList.get(4); // GOLD LEVEL
        User userWithoutLevel = userList.get(0);
        userWithoutLevel.setLevel(null); // 비어있는 레벨 설정

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDaoImpl.get(userWithLevel.getId());
        User userWithoutLevelRead = userDaoImpl.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);

    }

    @Test
    public void upgradeAllOrNothing() throws SQLException {
        UserServiceImpl testUserServiceImpl = new TestUserServiceImpl(userList.get(3).getId(), userDaoImpl);

        UserServiceTx userServiceTx = new UserServiceTx(testUserServiceImpl, transactionManager);

        userDaoImpl.deleteAll();

        for (User user : userList) userDaoImpl.add(user);

        try {
            userServiceTx.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {

        }
        checkLevel(userList.get(1), false);
    }

    private void checkLevel(User user, boolean upgraded) {
        User userUpdate = userDaoImpl.get(user.getId());

        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().getNext());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }

    }

    static class TestUserServiceImpl extends UserServiceImpl {
        private String id;

        private TestUserServiceImpl(String id, UserDaoImpl userDaoImpl) {
            super(userDaoImpl);
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

    // Mock
    static class MockUserDao implements UserDao {

        private List<User> users;
        private List<User> updated = new ArrayList<>();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public List<User> getAll() {
            return this.users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }

        //테스트에 사용안함
        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();

        }

        @Override
        public void add(User user) throws DuplicateUserIdException {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();

        }
    }

}
