package repository;

import dto.ReservationRecordDto;
import java.sql.Connection;
import java.util.List;
import java.sql.SQLException;

public interface ReservationRecordRepository {
    // 예약 기록 데이터 삽입 - 일반 예약
    int insertReservationRecord(Connection conn, ReservationRecordDto reservationRecordDto);

    // 예약 기록 조회 - 회원 대시보드용
    List<ReservationRecordDto> findByUserId(Connection conn, int user_id);

    List<ReservationRecordDto> getReservation() throws SQLException;
    void updateStatus(int ReservationId) throws SQLException;
}
