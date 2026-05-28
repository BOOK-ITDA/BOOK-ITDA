package dao;
import database.DatabaseConnector;
import dto.UserDto;
import repository.UserRepository;
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

    @Override // 현재 대출 권수 가져오기
    public int getLoanCount(Connection conn, int user_id) {
        String sql = "SELECT loan_count FROM user WHERE user_id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, user_id);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    int loan_count = rs.getInt("loan_count");
                    return loan_count;
                }
                else {
                    throw new SQLException("해당 회원 정보를 찾을 수 없습니다."); // 회원 정보가 없을 경우 처리
                }
            }
            } catch(SQLException e){ // 데이터베이스 자체에 문제가 있을 경우 처리
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("대출 권수 읽는 중 DB 오류 발생", e);
        }
    }

    @Override // 현재 대출 권수 증가하도록 업데이트하기 (+1)
    public void increaseLoanCount(Connection conn, int user_id) {
        String sql = "UPDATE user SET loan_count = loan_count+1 WHERE user_id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, user_id);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0)
                throw new SQLException("해당 회원 정보를 찾을 수 없습니다."); // 회원 정보를 찾을 수 없을 경우 처리
        } catch(SQLException e){
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("대출 권수 업데이트 중 DB 오류 발생",e);
        }

    }

    @Override // 현재 대출 권수 감소하도록 업데이트하기 (-1)
    public void decreaseLoanCount(Connection conn, int user_id) {
        String sql = "UPDATE user SET loan_count = loan_count-1 WHERE user_id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, user_id);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0)
                throw new SQLException("해당 회원 정보를 찾을 수 없습니다."); // 회원 정보를 찾을 수 없을 경우 처리
        } catch(SQLException e){
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("대출 권수 업데이트 중 DB 오류 발생",e);
        }
    }

    @Override // 회원 데이터 삽입 (현재 대출 권수는 데이터베이스 기본값으로 지정)
    public int insertUser(Connection conn, UserDto dto) {
        String sql = "INSERT INTO user (name, birthdate, phone_number, address, password) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, dto.getName());
            pstmt.setDate(2, java.sql.Date.valueOf(dto.getBirthdate()));
            pstmt.setString(3, dto.getPhone_number());
            pstmt.setString(4, dto.getAddress());
            pstmt.setString(5, dto.getPassword());

            pstmt.executeUpdate();
            try(ResultSet generatedKeys = pstmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    int user_id = generatedKeys.getInt(1);
                    dto.setUser_id(user_id);
                    return user_id;
                } else throw new SQLException("생성된 ID를 가져올 수 없습니다.");
            }
        } catch(SQLException e){
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("회원 기록 생성 중 DB 오류 발생",e);
        }
    }
}
