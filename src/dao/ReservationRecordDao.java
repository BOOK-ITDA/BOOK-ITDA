package dao;
import dto.ReservationRecordDto;
import repository.ReservationRecordRepository;
import database.DatabaseConnector;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

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
    // 회원 예약 기록 전체 조회
    // RESERVATION_RECORD + BOOK + LIBRARY JOIN → 도서 제목, 도서관 이름 포함 반환
    public List<ReservationRecordDto> findByUserId(Connection conn, int user_id) {
        String sql = "SELECT r.reserve_id, b.name AS book_name, l.name AS library_name, " +
                     "       r.reserve_date, r.status " +
                     "FROM reservation_record r " +
                     "JOIN book b ON r.book_id = b.book_id " +
                     "JOIN library l ON r.library_id = l.library_id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.reserve_date DESC";

        List<ReservationRecordDto> list = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new ReservationRecordDto(
                            rs.getInt("reserve_id"),
                            rs.getString("book_name"),
                            rs.getString("library_name"),
                            rs.getDate("reserve_date").toLocalDate(),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("DAO 에러 발생 : " + e.getMessage());
            throw new RuntimeException("예약 기록 조회 중 DB 오류 발생", e);
        }

        return list; // 예약 기록 없으면 빈 리스트 반환
    }






}
