package dao;
import dto.UserDto;
import repository.UserRepository;
import database.DatabaseConnector;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements UserRepository {
    @Override
    public List<UserDto> findUserAll() throws SQLException {
        String sql = "SELECT u.user_id, u.name, u.birthdate, u.phone_number, u.address, u.loan_count " +
                "FROM USER u";

        List<UserDto> userList = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                LocalDate birthdate = rs.getDate("birthdate").toLocalDate();
                UserDto user = new UserDto(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        birthdate,
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getInt("loan_count")
                );
                userList.add(user);
            }
        }

        return userList;
    }
}