package com.example.springtoby.toby.service;

import com.example.springtoby.toby.User;
import org.springframework.stereotype.Service;

public interface UserService {

    void add(User user);
    void upgradeLevels();

}
