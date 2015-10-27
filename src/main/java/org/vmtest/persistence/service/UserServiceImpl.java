package org.vmtest.persistence.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.vmtest.persistence.entity.User;
import org.vmtest.persistence.repository.UserRepository;

/**
 * Created by victor on 27.10.15.
 */
@Repository
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository repository;

    @Override
    public User findUserByLoginName(String userName) {
        return repository.findByUserName(userName);
    }

    @Override
    public void addNewUser(User newUser) {
        if (repository.findByUserName(newUser.getUserName()) != null) {
            throw new RuntimeException("User with name " + newUser.getUserName() + " already exist");
        }
        repository.save(newUser);
        logger.info("New user created :" + newUser.toString());
    }
}
