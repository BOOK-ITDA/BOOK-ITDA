package dao;
import dto.UserDto;
import repository.UserRepository;
import database.DatabaseConnector;
import java.sql.*;
public class UserDao implements UserRepository {
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
}
