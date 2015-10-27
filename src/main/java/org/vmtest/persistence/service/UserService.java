package org.vmtest.persistence.service;

import org.springframework.stereotype.Repository;
import org.vmtest.persistence.entity.User;

/**
 * Created by victor on 27.10.15.
 */
@Repository
public interface UserService {
    public User findUserByLoginName(String userName);

    public void addNewUser(User newUSer);
}
