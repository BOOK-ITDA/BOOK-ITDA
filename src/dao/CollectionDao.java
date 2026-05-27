package dao;
import dto.CollectionDto;
import repository.CollectionRepository;
import java.sql.*;

public class CollectionDao implements CollectionRepository {
    @Override // 도서 상태 데이터 가져오기
    public CollectionDto.BookStatus getStatus(Connection conn, int book_id, int library_id) {
        String sql = "SELECT status FROM collection WHERE book_id = ? AND library_id = ? ";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, book_id);
            pstmt.setInt(2, library_id);

            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){
                    String status = rs.getString("status");
                    return CollectionDto.BookStatus.valueOf(status);
                } else {
                    throw new SQLException("해당 도서 정보를 찾을 수 없습니다."); // 도서 정보가 없을 경우 처리
                }
            }
        } catch (SQLException e) { // 데이터베이스 자체에 문제가 있을 경우 처리
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("도서 상태 업데이트 중 DB 오류 발생", e);
        }

    }

    @Override // 도서 상태 업데이트하기
    public void updateStatus(Connection conn, int book_id, int library_id, CollectionDto.BookStatus status) {
        String sql = "UPDATE collection SET status = ? WHERE book_id = ? AND library_id = ? ";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, status.name());
            pstmt.setInt(2, book_id);
            pstmt.setInt(3, library_id);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0 ){
                throw new SQLException("해당 도서 정보를 찾을 수 없습니다."); // 업데이트 대상이 없을 경우 처리
        }
        } catch (SQLException e) { // 데이터베이스 자체에 문제가 있을 경우 처리
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("도서 상태 업데이트 중 DB 오류 발생", e);
        }
    }
}
