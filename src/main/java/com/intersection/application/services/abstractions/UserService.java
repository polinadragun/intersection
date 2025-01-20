package com.intersection.application.services.abstractions;

import com.intersection.domain.entity.User;

import java.util.UUID;

public interface UserService {

    UUID registerUser(String username, String email, String password);

    String authenticateUser(String username, String password);
}