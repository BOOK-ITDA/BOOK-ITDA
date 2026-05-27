package repository;
import dto.UserDto;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    //회원 조회(사서)
    List<UserDto> findUserAll() throws SQLException;
}
