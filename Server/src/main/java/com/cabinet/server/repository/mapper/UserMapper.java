package com.cabinet.server.repository.mapper;

import com.cabinet.server.domain.User;
import com.cabinet.server.repository.entity.UserEntity;

public class UserMapper {
    public static User toDomain(UserEntity entity) {
        User user = new User();

        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setRole(entity.getRole());

        return user;
    }

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();

        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setRole(user.getRole());

        return entity;
    }
}