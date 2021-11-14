package com.example.springtoby.toby.service;

import com.example.springtoby.toby.User;
import com.example.springtoby.toby.UserDao;
import com.example.springtoby.toby.UserDaoImpl;
import com.example.springtoby.toby.enums.Level;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public static final int MIN_LOG_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    public void upgradeLevels() {

        List<User> userList = userDao.getAll();

        for(User user : userList) {

            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOG_COUNT_FOR_SILVER);
            case SIlVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level " + currentLevel);
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    public void add(User user) {
        if (user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }



}
