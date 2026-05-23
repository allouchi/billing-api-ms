package com.sbatec.authentserver.services.internals;

import com.sbatec.authentserver.dtos.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    void deleteUser(User user);

    void deleteUserById(Long id);

    void deleteAll();

    void updateUser(User user);

    List<User> findAllUsers();

    User findUserById(Long id);

    User findByUserName(String userName);

    User findByUserNameAndPassword(String userName, String password);

}
