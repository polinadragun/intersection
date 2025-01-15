package com.intersection.infrastructure.dataaccess.repository;

import com.intersection.application.repositoryAbstractions.UserRepository;
import com.intersection.domain.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaUserRepository extends CrudRepository<User, Long>, UserRepository {

    @Override
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Override
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}
