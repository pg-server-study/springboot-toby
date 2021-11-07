package com.example.springtoby.domain;

import com.example.springtoby.toby.User;
import com.example.springtoby.toby.enums.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    User user;

    @BeforeEach
    public void setUp() {
        this.user = new User();
    }

    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();

        for(Level level : levels) {
            if (level.getNext() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.getNext());
        }
    }
}
