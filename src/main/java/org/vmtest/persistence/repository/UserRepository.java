package org.vmtest.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.vmtest.persistence.entity.User;

/**
 * Created by victor on 27.10.15.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public User findByUserName(String userName);
}
