package com.escanor1986.tennis.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findOneWithRolesByLoginIgnoreCase(String login);
}
