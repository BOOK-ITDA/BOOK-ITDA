package dao;
import dto.ReservationRecordDto;
import repository.ReservationRecordRepository;
import database.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<ReservationRecordDto> getReservation() throws SQLException {
        String sql = "SELECT rr.reserve_id, rr.user_id, rr.book_id, b.name, rr.library_id, rr.reserve_date, rr.status" +
            "FROM RESERVATION_RECORD rr" +
            "JOIN BOOK b ON rr.book_id = b.book_id";

        List<ReservationRecordDto> list = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ReservationRecordDto bt = new ReservationRecordDto(
                        rs.getInt("reserve_id"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id"),
                        rs.getString("book_name"),
                        rs.getInt("library_id"),
                        rs.getDate("reserve_date").toLocalDate(),
                        rs.getString("status")


                );
                list.add(bt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("분관대출 신청 목록 조회 중 오류 발생", e);
        }

        return list;
    }

    @Override
    public void updateStatus(int ReservationId) throws SQLException {
        String sql = "UPDATE RESERVATION_RECORD SET status = 'AVAILABLE' " +
                "WHERE reserve_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ReservationId);
            int rows = pstmt.executeUpdate();
            System.out.println("상태 변경 완료");
        }
    }




}
