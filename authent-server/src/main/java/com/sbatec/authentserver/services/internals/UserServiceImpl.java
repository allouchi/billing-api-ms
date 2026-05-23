package com.sbatec.authentserver.services.internals;

import com.sbatec.authentserver.dtos.User;
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
        return List.of();
    }

    @Override
    public User findUserById(Long id) {
        return null;
    }

    @Override
    public User findByUserName(String userName) {
        UserEntity userEntity = userJpaRepository.findByEmail(userName).orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));
        return userMapper.toDto(userEntity);
    }

    @Override
    public User findByUserNameAndPassword(String userName, String password) {
        return null;
    }
}
