package service;

import repository.UserRepository;

import java.sql.SQLException;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int login(int inputId, String inputPw) throws SQLException {
        return userRepository.login(inputId, inputPw);
    }
}
