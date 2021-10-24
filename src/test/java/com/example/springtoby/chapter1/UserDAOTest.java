package com.example.springtoby.chapter1;

import com.example.springtoby.toby.User;
import com.example.springtoby.toby.UserDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserDAOTest {

    @Autowired
    UserDao userDao;

    @Test
    @DisplayName("데이터베이스에 유저 등록 및 조회 테스트")
    void addUserTest() throws ClassNotFoundException, SQLException {

        // given
        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        // when
        userDao.add(user); // 유저 등록 실행
        User savedUser = userDao.get(user.getId()); // 유저 조회 실행

        // then (저장하고자 했던 유저의 이름과 실제로 DB에 저장된 유저의 이름이 같은지 검증하라)
        assertThat(user.getName()).isEqualTo(savedUser.getName());

    }





}

