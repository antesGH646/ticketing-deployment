package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    //fetching a user using derived query method
    User findByUserName(String username);

    /**
     * If you are using the ddl(data definition language) operation
     * (create, alter, drop, truncate, rename, commit) you must use
     * the @Modifying annotation, but when you are using the  derived
     * queries methods must use the @Transactional
    */
    @Transactional
    void deleteByUserName(String username);

    List<User> findAllByRoleDescriptionIgnoreCase(String description);
}
