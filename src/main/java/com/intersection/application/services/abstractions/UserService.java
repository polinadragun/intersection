package com.intersection.application.services.abstractions;

public interface UserService {

    Long registerUser(String username, String email, String password);

    String authenticateUser(String username, String password);
}