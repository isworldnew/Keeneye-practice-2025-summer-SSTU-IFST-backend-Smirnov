package ru.smirnov.keeneyepractice.backend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.smirnov.keeneyepractice.backend.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u JOIN FETCH u.passwordEntity WHERE u.username = :name")
    Optional<UserEntity> findByUsername(String name);
}