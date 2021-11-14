package com.example.springtoby.domain;

import com.example.springtoby.toby.User;
import com.example.springtoby.toby.UserDaoImpl;
import com.example.springtoby.toby.enums.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserDAOTest {

    @Autowired
    UserDaoImpl userDaoImpl;

    User user1;
    User user2;
    User user3;

    @BeforeEach
    public void setUp() {
        this.user1 = new User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0);
        this.user2 = new User("leegw700", "이길원", "springno2", Level.SIlVER, 55, 10);
        this.user3 = new User("bumjin", "박범진", "springno3", Level.GOLD, 100, 40);

    }

    @Test
    @DisplayName("데이터베이스에 유저 등록 및 조회 테스트")
    void addUserTest() {

        // given
        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");
        user.setLevel(Level.SIlVER);
        user.setLogin(1);
        user.setRecommend(0);

        // when
        userDaoImpl.add(user); // 유저 등록 실행
        User savedUser = userDaoImpl.get(user.getId()); // 유저 조회 실행

        // then (저장하고자 했던 유저의 이름과 실제로 DB에 저장된 유저의 이름이 같은지 검증하라)
        assertThat(user.getName()).isEqualTo(savedUser.getName());

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    public void addAndGet() throws SQLException {


        userDaoImpl.add(user1);
        userDaoImpl.add(user2);

        User userGet1 = userDaoImpl.get(user1.getId());
        checkSameUser(userGet1, user1);

        User userGet2 = userDaoImpl.get(user2.getId());
        checkSameUser(userGet2, user2);

    }

    @Test
    public void update() throws SQLException {
        userDaoImpl.deleteAll();

        userDaoImpl.add(user1);
        userDaoImpl.add(user2); // 수정 하지 않을 사용자

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        userDaoImpl.update(user1);

        User user1Update = userDaoImpl.get(user1.getId());
        checkSameUser(user1, user1Update);

        User user2Update = userDaoImpl.get(user2.getId());
        checkSameUser(user2, user2Update);

    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }



}

