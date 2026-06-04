package service;

import database.DatabaseConnector;
import dto.UserDto;
import repository.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int login(int inputId, String inputPw) throws SQLException {
        return userRepository.login(inputId, inputPw);
    }

    public int register(UserDto dto) {
        try(Connection conn = DatabaseConnector.getConnection()) {
            return userRepository.insertUser(conn, dto);
        } catch (SQLException e) {
            throw new RuntimeException("회원가입 처리 중 오류 발생", e);
        } catch (RuntimeException e) {
            if(e.getMessage().contains("이미 가입된 휴대폰 번호입니다.")){
                throw new IllegalStateException("이미 가입된 휴대폰 번호입니다.");
            }
            throw e;
        }
    }
}
