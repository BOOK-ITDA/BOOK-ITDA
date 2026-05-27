package service;

import dao.UserDao;
import dto.UserDto;
import repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserRepository userDao = new UserDao(); // UserDao 주입

    // 회원 목록 조회(사서)
    public List<UserDto> findUserAll() throws SQLException {
        return userDao.findUserAll();
    }
}
