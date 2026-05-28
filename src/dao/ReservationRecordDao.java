package dao;
import dto.ReservationRecordDto;
import repository.ReservationRecordRepository;
import database.DatabaseConnector;
import java.sql.*;

public class ReservationRecordDao implements ReservationRecordRepository {
    @Override // 예약 기록 삽입 (처리 상태는 데이터베이스 기본 값 적용)
    public int insertReservationRecord(Connection conn, ReservationRecordDto dto) {
        String sql = "INSERT INTO reservation_record (user_id, book_id, library_id, reserve_date) VALUES (?, ?, ?, ?)";
        try(PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setInt(1, dto.getUser_id());
            pstmt.setInt(2, dto.getBook_id());
            pstmt.setInt(3, dto.getLibrary_id());
            pstmt.setDate(4, java.sql.Date.valueOf(dto.getReserve_date()));

            pstmt.executeUpdate();
            try(ResultSet generatedKeys = pstmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    int reserve_id = generatedKeys.getInt(1);
                    dto.setReserve_id(reserve_id);
                    return reserve_id;
                } else throw new SQLException("생성된 ID를 가져올 수 없습니다.");
            }
        } catch(SQLException e){
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("예약 기록 생성 중 DB 오류 발생",e);
        }
    }
}
