package com.example.springtoby.toby.service;

import com.example.springtoby.toby.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class UserServiceTx implements UserService {

    private final UserService userServiceImpl;
    private final PlatformTransactionManager transactionManager;

    public UserServiceTx(
            @Qualifier("UserServiceImpl") UserService userServiceImpl,
            PlatformTransactionManager transactionManager
    ) {
        this.userServiceImpl = userServiceImpl;
        this.transactionManager = transactionManager;
    }

    @Override
    public void add(User user) {
        userServiceImpl.add(user);
    }

    @Override
    public void upgradeLevels() {

        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            userServiceImpl.upgradeLevels();
            this.transactionManager.commit(status);
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }

    }
}
