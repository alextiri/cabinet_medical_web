package com.cabinet.server.repository.jpa;

import com.cabinet.server.domain.User;
import com.cabinet.server.domain.UserRepository;
import com.cabinet.server.repository.entity.UserEntity;
import com.cabinet.server.repository.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaUserRepository implements UserRepository {
    private final SpringDataUserRepository repository;

    public JpaUserRepository(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User findByUsername(String username) {
        UserEntity entity = repository.findByUsername(username);
        if (entity == null) {
            return null;
        }

        return UserMapper.toDomain(entity);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = repository.save(entity);

        return UserMapper.toDomain(saved);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public User findById(Integer id) {
        UserEntity entity = repository.findById(id)
                .orElse(null);

        if (entity == null) {
            return null;
        }

        return UserMapper.toDomain(entity);
    }

    @Override
    public void deleteUser(Integer id) {
        repository.deleteById(id);
    }
}