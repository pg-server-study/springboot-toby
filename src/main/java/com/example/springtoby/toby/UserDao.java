package com.example.springtoby.toby;

import com.example.springtoby.toby.exception.DuplicateUserIdException;

import java.util.List;

public interface UserDao {

    void deleteAll();
    void add(final User user) throws DuplicateUserIdException;
    User get(String id);
    List<User> getAll();
    void update(User user);

}
