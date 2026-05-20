package com.cabinet.server.service;

import com.cabinet.server.domain.User;
import com.cabinet.server.dto.CreateUserRequest;
import com.cabinet.server.dto.UpdateUserRequest;
import com.cabinet.server.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(CreateUserRequest request) {
        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

    public User updateUser(Integer id, UpdateUserRequest request) {
        User user = userRepository.findById(id);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }

        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteUser(id);
    }
}