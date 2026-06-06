package com.sbatec.authentserver.services.internals;

import com.sbatec.authentserver.dtos.User;
import com.sbatec.authentserver.exceptions.ErrorCatalog;
import com.sbatec.authentserver.exceptions.UserNotFoundException;
import com.sbatec.authentserver.mappers.UserMapper;
import com.sbatec.authentserver.models.UserEntity;
import com.sbatec.authentserver.repository.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserJpaRepository userJpaRepository;
    UserMapper userMapper;

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void deleteUserById(Long id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public List<User> findAllUsers() {
        return userMapper.toDtoList(userJpaRepository.findAll());
    }

    @Override
    public User findUserById(Long id) {
        UserEntity user = userJpaRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Not found"));
        return userMapper.toDto(user);
    }

    @Override
    public User findByUserName(String userName) {
        final String message = String.format("L'utilisateur %s est introuvable", userName);
        UserEntity userEntity = userJpaRepository.findByEmail(userName).orElseThrow(() -> new UserNotFoundException(ErrorCatalog.RESOURCE_NOT_FOUND, message));
        return userMapper.toDto(userEntity);
    }

    @Override
    public User findByUserNameAndPassword(String userName, String password) {
        return null;
    }
}
