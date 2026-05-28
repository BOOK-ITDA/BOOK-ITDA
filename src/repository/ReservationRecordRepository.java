package repository;
import dto.LoanRecordDto;
import dto.ReservationRecordDto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ReservationRecordRepository {
    // 예약 기록 데이터 삽입 - 일반 예약
    int insertReservationRecord(Connection conn, ReservationRecordDto reservationRecordDto);
    List<ReservationRecordDto> getReservation() throws SQLException;
    void updateStatus(int ReservationId) throws SQLException;
}
